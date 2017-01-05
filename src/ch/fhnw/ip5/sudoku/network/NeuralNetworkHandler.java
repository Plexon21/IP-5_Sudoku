package ch.fhnw.ip5.sudoku.network;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.Counter;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class NeuralNetworkHandler {

	public DataSet fullSet;
	public int[] countAll = new int[8];
	public int[] countCorrect = new int[8];
	int unpredicted = 0;
	boolean trained = false;

	int[][] confMat = new int[7][7];
	MultiLayerPerceptron network;

	public void trainNetwork() {
		trainNetwork("C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\all_parsed");
	}

	public void trainNetwork(String path) {
		NeuralNetworkHandler myNet = new NeuralNetworkHandler();
		network = new MultiLayerPerceptron(21, 50, 7);
		BackPropagation rule = network.getLearningRule();
		rule.setMaxIterations(500);
		myNet.fullSet = new DataSet(21, 7);
		myNet.sudokusToFile(path);

		Normalizer norm = new MaxNormalizer();
		norm.normalize(myNet.fullSet);

		int trainingSetPercentage = 70;

		DataSet[] sets = myNet.fullSet.createTrainingAndTestSubsets(trainingSetPercentage, 100 - trainingSetPercentage);
		DataSet training = sets[0];
		DataSet test = sets[1];
		network.learn(training);

		trained = true;

		myNet.testNeuralNetwork(network, test);
	}

	public int predictBoard(Board b) {
		if (!trained) {
			trainNetwork();
		}
		DataSetRow row = addBoard(b);
		network.setInput(row.getInput());
		network.calculate();

		double[] output = network.getOutput();
		return maxOutput(output);

	}

	public static void main(String[] args) {
		NeuralNetworkHandler myNet = new NeuralNetworkHandler();
		MultiLayerPerceptron network = new MultiLayerPerceptron(21, 50, 7);
		BackPropagation rule = network.getLearningRule();
		rule.setMaxIterations(500);
		myNet.fullSet = new DataSet(21, 7);
		myNet.sudokusToFile("C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\KTI_parsed");

		Normalizer norm = new MaxNormalizer();
		norm.normalize(myNet.fullSet);

		int trainingSetPercentage = 70;

		DataSet[] sets = myNet.fullSet.createTrainingAndTestSubsets(trainingSetPercentage, 100 - trainingSetPercentage);
		DataSet training = sets[0];
		DataSet test = sets[1];
		network.learn(training);

		myNet.testNeuralNetwork(network, test);

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

	private DataSetRow addBoard(Board b) {
		SolveMethod[] methods = new SolveMethod[] { new NakedSingleMethod(), new HiddenSingleMethod(),
				new NakedSubSetMethod((byte) 2), new HiddenSubSetMethod((byte) 2), new BlockLineInteractionMethod(),
				new NakedSubSetMethod((byte) 3), new HiddenSubSetMethod((byte) 3), new NakedSubSetMethod((byte) 4),
				new HiddenSubSetMethod((byte) 4) };

		b.setupBoard();

		int[] startPos = Counter.check(b);
		int total = 0;
		for (int i = 1; i <= b.SIZE; i++) {
			total += startPos[i - 1];
		}

		int possibilities[] = Counter.countPossibilities(b);
		int totalPossibilities = 0;
		for (int i = 1; i <= b.SIZE; i++) {
			totalPossibilities += possibilities[i - 1];
		}

		boolean solving = true;
		int[] solveCounter = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int wasBacktracked = 0;

		int solveMethod = 0;
		while (solving) {
			if (solveMethod >= methods.length) {
				solving = false;
				solveMethod = 0;
			}
			if (methods[solveMethod].solve(b)) {
				solveCounter[solveMethod]++;
				solveMethod = 0;
			} else
				solveMethod++;
		}

		if (!b.isSolvedCorrectly()) {
			Backtrack.solve(b);
			wasBacktracked = 1;
		}

		double toSolveCount = b.SIZE * b.SIZE - b.GIVENCOUNT;
		double[] inputValues = new double[] { (double) solveCounter[0] / toSolveCount,
				(double) solveCounter[1] / toSolveCount, solveCounter[2], solveCounter[3], solveCounter[8],
				solveCounter[4], solveCounter[5], solveCounter[6], solveCounter[7], b.GIVENCOUNT, startPos[0],
				startPos[1], startPos[2], startPos[3], startPos[4], startPos[5], startPos[6], startPos[7], startPos[8],
				totalPossibilities, wasBacktracked };
		return new DataSetRow(inputValues);

	}

	private void solve(File source) {
		try {
			ArrayList<Board> list = new ArrayList<>();

			list.addAll(SudokuReader.readFromFile(source));
			SolveMethod[] methods = new SolveMethod[] { new NakedSingleMethod(), new HiddenSingleMethod(),
					new NakedSubSetMethod((byte) 2), new HiddenSubSetMethod((byte) 2), new BlockLineInteractionMethod(),
					new NakedSubSetMethod((byte) 3), new HiddenSubSetMethod((byte) 3), new NakedSubSetMethod((byte) 4),
					new HiddenSubSetMethod((byte) 4) };

			for (Board b : list) {
				b.setupBoard();

				int[] startPos = Counter.check(b);
				int total = 0;
				for (int i = 1; i <= b.SIZE; i++) {
					total += startPos[i - 1];
				}

				int possibilities[] = Counter.countPossibilities(b);
				int totalPossibilities = 0;
				for (int i = 1; i <= b.SIZE; i++) {
					totalPossibilities += possibilities[i - 1];
				}

				boolean solving = true;
				int[] solveCounter = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				int wasBacktracked = 0;

				int solveMethod = 0;
				while (solving) {
					if (solveMethod >= methods.length) {
						solving = false;
						solveMethod = 0;
					}
					if (methods[solveMethod].solve(b)) {
						solveCounter[solveMethod]++;
						solveMethod = 0;
					} else
						solveMethod++;
				}

				if (!b.isSolvedCorrectly()) {
					Backtrack.solve(b);
					wasBacktracked = 1;
				}
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

				double toSolveCount = b.SIZE * b.SIZE - b.GIVENCOUNT;
				double[] inputValues = new double[] { (double) solveCounter[0] / toSolveCount,
						(double) solveCounter[1] / toSolveCount, solveCounter[2], solveCounter[3], solveCounter[8],
						solveCounter[4], solveCounter[5], solveCounter[6], solveCounter[7], b.GIVENCOUNT, startPos[0],
						startPos[1], startPos[2], startPos[3], startPos[4], startPos[5], startPos[6], startPos[7],
						startPos[8], totalPossibilities, wasBacktracked };
				double[] outputValues = new double[] { difficulty[0], difficulty[1], difficulty[2], difficulty[3],
						difficulty[4], difficulty[5], difficulty[6], };
				fullSet.addRow(inputValues, outputValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testNeuralNetwork(MultiLayerPerceptron neuralNet, DataSet testSet) {

		System.out.println("**************************************************");
		System.out.println("**********************RESULT**********************");
		System.out.println("**************************************************");
		for (DataSetRow testSetRow : testSet.getRows()) {
			neuralNet.setInput(testSetRow.getInput());
			neuralNet.calculate();

			// Finding network output
			double[] networkOutput = neuralNet.getOutput();
			int predicted = maxOutput(networkOutput);

			// Finding actual output
			double[] networkDesiredOutput = testSetRow.getDesiredOutput();
			int ideal = maxOutput(networkDesiredOutput);

			// Colecting data for network evaluation
			keepScore(predicted, ideal);
		}
		printConfMat();

		System.out.println("Total cases: " + this.countAll[7] + ". ");
		System.out.println("Correctly predicted cases: " + this.countCorrect[7] + ". ");
		System.out.println(
				"Incorrectly predicted cases: " + (this.countAll[7] - this.countCorrect[7] - unpredicted) + ". ");
		System.out.println("Unrecognized cases: " + unpredicted + ". ");
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
		double second = Double.NEGATIVE_INFINITY;
		int index = 0;

		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				index = i;
				second = max;
				max = array[i];
			}
		}
		// If maximum is less than 0.5, that prediction will not count.
		if (max - second < 0.2) {
			return -1;
		}
		return index;
	}

	// Colecting data to evaluate network.
	private void keepScore(int prediction, int ideal) {
		addToConfMat(prediction, ideal);
		countAll[ideal]++;
		countAll[7]++;

		if (prediction == ideal) {
			countCorrect[ideal]++;
			countCorrect[7]++;
		}
		if (prediction == -1) {
			unpredicted++;
		}
	}

	private void addToConfMat(int prediction, int ideal) {
		if (prediction >= 0)
			confMat[prediction][ideal]++;

	}
}
