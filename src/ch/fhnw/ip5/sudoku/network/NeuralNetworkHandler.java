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

//TODO JAVADOC
public class NeuralNetworkHandler implements LearningEventListener {

	public DataSet fullSet;
	public int[] countAll = new int[8];
	public int[] countCorrect = new int[8];
	int unpredicted = 0;
	boolean trained = false;

	int[][] confMat = new int[7][7];
	MultiLayerPerceptron network;

	public void trainNetwork() {
		trainNetwork("C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\old_parsed");
	}

	@Override
	public void handleLearningEvent(LearningEvent event) {
		BackPropagation bp = (BackPropagation) event.getSource();

		if (event.getEventType() != LearningEvent.Type.LEARNING_STOPPED)
			System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());

	}

	public void trainNetwork(String path) {
		network = new MultiLayerPerceptron(22, 50, 7);
		network.setLearningRule(new MomentumBackpropagation());
		;
		MomentumBackpropagation rule = (MomentumBackpropagation) network.getLearningRule();
		rule.setMaxIterations(2000);
		rule.setMomentum(0.7);
		rule.setLearningRate(0.1);
		fullSet = new DataSet(22, 7);
		sudokusToFile(path);

		int trainingSetPercentage = 70;

		DataSet[] sets = fullSet.createTrainingAndTestSubsets(trainingSetPercentage, 100 - trainingSetPercentage);
		DataSet training = sets[0];
		DataSet test = sets[1];
		network.learn(training);

		trained = true;

		testNeuralNetwork(network, test);
	}

	public int predictBoard(Board b) {
		if (!trained) {
			trainNetwork();
		}

		DataSetRow row = getFeaturesAsRow(b);
		double[] input = row.getInput();
		network.setInput(input);
		network.calculate();

		double[] output = network.getOutput();

		return maxOutput(output);

	}

	public static void main(String[] args) {
		NeuralNetworkHandler myNet = new NeuralNetworkHandler();
		myNet.network = new MultiLayerPerceptron(22, 50, 7);
		myNet.network.setLearningRule(new MomentumBackpropagation());
		MomentumBackpropagation rule = (MomentumBackpropagation) myNet.network.getLearningRule();
		rule.setMaxIterations(5000);
		rule.addListener(myNet);
		rule.setLearningRate(0.05);
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

	private void sudokusToFile(String filePath) {

		File node = new File(filePath);
		String[] subNodes = node.list();
		for (String fileName : subNodes) {
			parse(new File(node, fileName));
		}
	}

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

	private DataSetRow getFeaturesAsRow(Board ori) {
		Board b = new Board(ori);

		int[] features = Solver.solve(b, true);
		double[] inputValues = normalizeFeaturesLog(features);
		return new DataSetRow(inputValues);
	}

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
				"Incorrectly predicted cases: " + (this.countAll[7] - this.countCorrect[7] - unpredicted) + ". ");
		double percentTotal = (double) this.countCorrect[7] * 100 / (double) this.countAll[7];
		System.out.println("Predicted correctly: " + percentTotal + "%. ");

		unpredicted = 0;
	}

	private void printConfMat() {
		for (int i = 0; i < confMat.length; i++) {
			for (int j = 0; j < confMat[i].length; j++) {
				System.out.print(confMat[i][j] + "\t");
			}
			System.out.println();
		}
	}

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

	private void keepScore(int prediction, int ideal) {
		addToConfMat(prediction, ideal);
		countAll[ideal]++;
		countAll[7]++;

		if (prediction == ideal) {
			countCorrect[ideal]++;
			countCorrect[7]++;
		}
	}

	private void addToConfMat(int prediction, int ideal) {
		if (prediction >= 0)
			confMat[prediction][ideal]++;

	}
}
