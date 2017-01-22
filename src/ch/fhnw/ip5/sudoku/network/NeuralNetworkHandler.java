package ch.fhnw.ip5.sudoku.network;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.Counter;
import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.solver.Solver;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.XWingMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

/**
 * Neural Network to classify Sudokus 
 *
 */
public class NeuralNetworkHandler implements LearningEventListener {

	/**
	 * contains the full Dataset for Training
	 */
	public DataSet fullSet;
	
	/** 
	 * number of all predicted sudokus for every difficulty plus one couter for all sudokus
	 */
	public int[] countAll = new int[8];
	
	/**
	 * number of correctly predicted sudokus for every difficulty plus one counter for all sudokus
	 */
	public int[] countCorrect = new int[8];
	
	/**
	 * Expresses if the network was already trained
	 */
	boolean trained = false;

	/** 
	 * Contains the confusion Matrix of the Testset
	 */
	int[][] confMat = new int[7][7];
	
	/***
	 * The actual neural Network
	 */
	MultiLayerPerceptron network;

	/***
	 * Wrapper with default path of training set
	 */
	public void trainNetwork() {
		trainNetwork("C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\old_parsed");
	}

	@Override
	/**
	 * Listener to display learning status
	 * @param event the event containing the data to display
	 */
	public void handleLearningEvent(LearningEvent event) {
		BackPropagation bp = (BackPropagation) event.getSource();

		if (event.getEventType() != LearningEvent.Type.LEARNING_STOPPED)
			System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());

	}

	/***
	 * Train the network with 22 inputs, 1 hidden Layer containing 50 Neurons and 7 outputs.
	 * Learning stops if a MSE of 0.01 or lower is reached or if 5000 iterations passed.
	 * 80% of the Dataset are used for training, the rest for testing the network.
	 * MomentumBackpropagation with a Momentum of 0.7 and a Learningrate of 0.1 is used as LearningRule.
	 * @param path Path pointing to the trainingset
	 */
	public void trainNetwork(String path) {
		network = new MultiLayerPerceptron(22, 50, 7);
		network.setLearningRule(new MomentumBackpropagation());
		;
		MomentumBackpropagation rule = (MomentumBackpropagation) network.getLearningRule();
		rule.setMaxIterations(5000);
		rule.setMomentum(0.7);
		rule.setLearningRate(0.1);
		fullSet = new DataSet(22, 7);
		sudokusToFile(path);

		int trainingSetPercentage = 80;

		DataSet[] sets = fullSet.createTrainingAndTestSubsets(trainingSetPercentage, 100 - trainingSetPercentage);
		DataSet training = sets[0];
		DataSet test = sets[1];
		network.learn(training);

		trained = true;

		testNeuralNetwork(network, test);
	}
	
	/***
	 * Predicts the difficulty of a single sudoku board
	 * @param b Board to be classified
	 * @return difficulty of the board
	 */
	public int predictBoard(Board b) {
		if (!trained)
			trainNetwork();		

		DataSetRow row = getFeaturesAsRow(b);
		double[] input = row.getInput();
		network.setInput(input);
		network.calculate();

		double[] output = network.getOutput();

		return maxOutput(output);

	}

	/***
	 * Trains the network with default values
	 * @param args
	 */
	public static void main(String[] args) {
		NeuralNetworkHandler myNet = new NeuralNetworkHandler();
		myNet.network = new MultiLayerPerceptron(22, 50, 7);
		myNet.network.setLearningRule(new MomentumBackpropagation());
		MomentumBackpropagation rule = (MomentumBackpropagation) myNet.network.getLearningRule();
		
		rule.setMaxIterations(5000);
		rule.addListener(myNet);
		rule.setLearningRate(0.1);
		rule.setMomentum(0.7);
		
		myNet.fullSet = new DataSet(22, 7);
		myNet.sudokusToFile("C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\old_parsed");

		int trainingSetPercentage = 80;

		DataSet[] sets = myNet.fullSet.createTrainingAndTestSubsets(trainingSetPercentage, 100 - trainingSetPercentage);
		DataSet training = sets[0];
		DataSet test = sets[1];
		myNet.network.learn(training);

		myNet.testNeuralNetwork(myNet.network, test);

	}

	/***
	 * Parses every subNode of given Path
	 * @param filePath Path to start parsing
	 */
	private void sudokusToFile(String filePath) {

		File node = new File(filePath);
		String[] subNodes = node.list();
		for (String fileName : subNodes) {
			parse(new File(node, fileName));
		}
	}

	/***
	 * Recursivly start solving every sudoku in given Directory
	 * @param source current position
	 */
	private void parse(File source) {
		if (source.isDirectory()) {
			String[] subNode = source.list();

			for (String fileName : subNode) {
				parse(new File(source, fileName));
			}
		} else {
			if (source.getName().substring(source.getName().length() - 6).equalsIgnoreCase("sudoku")) {
				solve(source);
			}
		}
	}

	/***
	 * Solves the board and returns a normalized feature vector
	 * @param ori the board to get the feature vector from
	 * @return return DataSetRow to be estimated by the network
	 */
	private DataSetRow getFeaturesAsRow(Board ori) {
		Board b = new Board(ori);

		int[] features = Solver.solve(b, true);
		double[] inputValues = normalizeFeaturesLog(features);
		return new DataSetRow(inputValues);
	}

	/**
	 * Creates features from statistics array (hidden/naked singles to PercentageHidden/Naked)
	 * Logarithmus over all features
	 * @param features features of sudoku
	 * @return normalized feature vector 
	 */
	public double[] normalizeFeaturesLog(int[] features) {

		double[] results = new double[features.length - 2];
		for (int i = 2; i < features.length - 2; i++) {
			results[i] = Math.log(features[i + 2] + 1);
		}

		double percentageNaked = (double) features[2] / (double) features[12];
		double percentageHidden = (double) features[2] / (double) features[12];
		results[0] = percentageNaked;
		results[1] = percentageHidden;

		return results;
	}

	/**
	 * Solves every board in source file and adds its normalized feature vector to fullSet.
	 * @param source .sudoku File
	 */
	private void solve(File source) {
		try {
			ArrayList<Board> list = new ArrayList<>();

			list.addAll(SudokuReader.readFromFile(source));

			for (Board b : list) {
				int[] features = Solver.solve(b, true);

				int[] difficulty = new int[7];
				if (source.toString().contains("veryeasy") || source.toString().contains("S1"))
					difficulty[0] = 1;
				else if (source.toString().contains("easy") || source.toString().contains("S2"))
					difficulty[1] = 1;
				else if (source.toString().contains("medium") || source.toString().contains("S3"))
					difficulty[2] = 1;
				else if (source.toString().contains("S6"))
					difficulty[4] = 1;
				else if (source.toString().contains("very hard_expert") || source.toString().contains("S7"))
					difficulty[5] = 1;
				else if (source.toString().contains("hard") || source.toString().contains("S5"))
					difficulty[3] = 1;
				else if (source.toString().contains("evil") || source.toString().contains("exotic"))
					difficulty[6] = 1;

				double[] inputValues = normalizeFeaturesLog(features);
				double[] outputValues = new double[] { difficulty[0], difficulty[1], difficulty[2], difficulty[3],
						difficulty[4], difficulty[5], difficulty[6], };
				fullSet.addRow(inputValues, outputValues);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test the accuracy of the neural network.
	 * Prints confusion Matrix.
	 * @param neuralNet neural Network to test
	 * @param testSet Set to use for Test
	 */
	public void testNeuralNetwork(MultiLayerPerceptron neuralNet, DataSet testSet) {

		System.out.println("Result:");
		for (DataSetRow testSetRow : testSet.getRows()) {
			neuralNet.setInput(testSetRow.getInput());
			neuralNet.calculate();

			double[] networkOutput = neuralNet.getOutput();
			int predicted = maxOutput(networkOutput);

			double[] networkDesiredOutput = testSetRow.getDesiredOutput();
			int ideal = maxOutput(networkDesiredOutput);

			keepScore(predicted, ideal);
		}
		printConfMat();

		System.out.println("Total cases: " + this.countAll[7] + ". ");
		System.out.println("Correctly predicted cases: " + this.countCorrect[7] + ". ");
		System.out.println(
				"Incorrectly predicted cases: " + (this.countAll[7] - this.countCorrect[7]) + ". ");
		double percentTotal = (double) this.countCorrect[7] * 100 / (double) this.countAll[7];
		System.out.println("Predicted correctly: " + percentTotal + "%. ");
	}
	/**
	 * Print data from confMat array as Confusion Matrix.
	 */
	private void printConfMat() {
		for (int i = 0; i < confMat.length; i++) {
			for (int j = 0; j < confMat[i].length; j++) {
				System.out.print(confMat[i][j] + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * chooses max value of given array
	 * @param array Contains values assigned by neural network
	 * @return actual difficulty value (0-based)
	 */
	private static int maxOutput(double[] array) {
		double max = array[0];
		int index = 0;

		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				index = i;
				max = array[i];
			}
		}
		return index;
	}

	/**
	 * add predictions to the global arrays
	 * @param prediction prediction of certain board
	 * @param ideal actual difficulty value of the board
	 */
	private void keepScore(int prediction, int ideal) {
		addToConfMat(prediction, ideal);
		countAll[ideal]++;
		countAll[7]++;

		if (prediction == ideal) {
			countCorrect[ideal]++;
			countCorrect[7]++;
		}
	}

	/**
	 * add current prediction to confusionmatrix.
	 * @param prediction Predicted difficulty of board.
	 * @param ideal Actual difficulty of predicted sudoku.
	 */
	private void addToConfMat(int prediction, int ideal) {
		if (prediction >= 0)
			confMat[prediction][ideal]++;

	}
}
