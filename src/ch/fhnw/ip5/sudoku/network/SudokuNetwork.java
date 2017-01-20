package ch.fhnw.ip5.sudoku.network;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;
import org.neuroph.util.data.norm.RangeNormalizer;

import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.Counter;
import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

//TODO JAVADOC
public class SudokuNetwork {

	private int inputNodes = 22;
	private int hiddenNodes = 10;
	private int outputNodes = 7;
	private int testSetPercentage = 80;
	private int maxIter = 500;
	private double learningRate = 0.01;
	private double maxError = 0.01;
	private DataSet fullSet;
	public MultiLayerPerceptron network;

	public int getInputNodes() {
		return inputNodes;
	}

	public void setInputNodes(int inputNodes) {
		this.inputNodes = inputNodes;
	}

	public int getHiddenNodes() {
		return hiddenNodes;
	}

	public void setHiddenNodes(int hiddenNodes) {
		this.hiddenNodes = hiddenNodes;
	}

	public int getOutputNodes() {
		return outputNodes;
	}

	public void setOutputNodes(int outputNodes) {
		this.outputNodes = outputNodes;
	}

	public int getTestSetPercentage() {
		return testSetPercentage;
	}

	public void setTestSetPercentage(int testSetPercentage) {
		this.testSetPercentage = testSetPercentage;
	}

	public int getMaxIter() {
		return maxIter;
	}

	public void setMaxIter(int maxIter) {
		this.maxIter = maxIter;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getMaxError() {
		return maxError;
	}

	public void setMaxError(double maxError) {
		this.maxError = maxError;
	}

	public SudokuNetwork(int in, int hid, int out, int testSet, int maxIt) {
		this.inputNodes = in;
		this.hiddenNodes = hid;
		this.outputNodes = out;
		this.testSetPercentage = testSet;
		this.maxIter = maxIt;
		this.network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, in, hid, out);
	}

	public SudokuNetwork(int in, int hid, int out, int testSet, int maxIt, double learningRate, double maxError) {
		this(in, hid, out, testSet, maxIt);
		this.learningRate = learningRate;
		this.maxError = maxError;
	}

	public void initWithSudokus(String path) {
		fullSet = new DataSet(inputNodes, outputNodes);

		File node = new File(path);
		String[] subNodes = node.list();
		for (String fileName : subNodes) {
			parse(new File(node, fileName));
		}
		initializeNetwork(fullSet);
		network.save("C:\\Programming\\IP-5_sudoku\\res\\network.txt");
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

	private void solve(File source) {
		try {

			ArrayList<Board> list = new ArrayList<>();

			list.addAll(SudokuReader.readFromFile(source));

			SolveMethod[] methods = new SolveMethod[] { new NakedSingleMethod(), new HiddenSingleMethod(),
					new NakedSubSetMethod((byte) 2), new HiddenSubSetMethod((byte) 2), new NakedSubSetMethod((byte) 3),
					new HiddenSubSetMethod((byte) 3), new NakedSubSetMethod((byte) 4), new HiddenSubSetMethod((byte) 4),
					new BlockLineInteractionMethod() };

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
					if (methods[solveMethod].apply(b)) {
						solveCounter[solveMethod]++;
						solveMethod = 0;
					} else
						solveMethod++;
				}

				if (!b.isSolvedCorrectly()) {
					Backtrack.solve(b);
					wasBacktracked = 1;
				} else {

				}
				double[] outputValues = new double[7];
				if (source.toString().contains("veryeasy") || source.toString().contains("S1"))
					outputValues[0] = 1;
				else if (source.toString().contains("easy") || source.toString().contains("S2"))
					outputValues[1] = 1;
				else if (source.toString().contains("medium") || source.toString().contains("S3"))
					outputValues[2] = 1;
				else if (source.toString().contains("S6"))
					outputValues[4] = 1;
				else if (source.toString().contains("very hard_expert") || source.toString().contains("S7"))
					outputValues[5] = 1;
				else if (source.toString().contains("hard") || source.toString().contains("S5"))
					outputValues[3] = 1;
				else if (source.toString().contains("evil") || source.toString().contains("exotic"))
					outputValues[6] = 1;

				double toSolveCount = b.SIZE * b.SIZE - b.GIVENCOUNT;
				double[] inputValues = new double[] { ((double) solveCounter[0] / toSolveCount) * 100,
						((double) solveCounter[1] / toSolveCount) * 100, solveCounter[2], solveCounter[3],
						solveCounter[8], solveCounter[4], solveCounter[5], solveCounter[6], solveCounter[7],
						b.GIVENCOUNT, startPos[0], startPos[1], startPos[0], startPos[2], startPos[3], startPos[4],
						startPos[5], startPos[6], startPos[7], startPos[8], totalPossibilities, wasBacktracked };
				fullSet.addRow(inputValues, outputValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initializeNetwork(DataSet fullSet) {
		DataSet[] sets = fullSet.createTrainingAndTestSubsets(testSetPercentage, 100 - testSetPercentage);
		DataSet trainingSet = sets[0];
		DataSet testSet = sets[1];
		trainNetwork(trainingSet);
		testNetwork(testSet);
	}

	public void trainNetwork(DataSet trainingSet) {
		network.randomizeWeights();
//		Normalizer n = new MaxNormalizer();
//		n.normalize(trainingSet);

		BackPropagation rule = network.getLearningRule();
		rule.setMaxIterations(maxIter);
		rule.setLearningRate(learningRate);
		rule.setMaxError(maxError);

		trainingSet.saveAsTxt("C:\\Programming\\IP-5_sudoku\\res\\trainingSet.txt", ",");

		network.learn(trainingSet);
	}

	public void testNetwork(DataSet testSet) {
//		Normalizer n = new MaxNormalizer();
//		n.normalize(testSet);

		int[][] confMat = new int[outputNodes][outputNodes];

		for (DataSetRow row : testSet.getRows()) {
			network.setInput(row.getInput());
			network.calculate();

			double[] output = network.getOutput();
			int predictedDifficulty = chooseDifficulty(output);

			double[] shouldBe = row.getDesiredOutput();
			int targetDifficulty = chooseDifficulty(shouldBe);

			confMat[predictedDifficulty - 1][targetDifficulty - 1]++;
		}
		testSet.saveAsTxt("C:\\Programming\\IP-5_sudoku\\res\\testSet.txt", ",");
		printConfMat(confMat);
	}

	public int getSudokuDifficulty(Board b) {

		DataSet set = new DataSet(inputNodes);
		set.addRow(boardToInputRow(b));

		return difficultiesFromDataSet(set)[0];
	}

	public int[] getMultipleSudokuDifficulty(Board[] boards) {

		DataSet set = new DataSet(inputNodes);

		for (Board b : boards) {
			set.addRow(boardToInputRow(b));
		}
		return difficultiesFromDataSet(set);
	}

	private int[] difficultiesFromDataSet(DataSet set) {
		List<Integer> difficulties = new ArrayList<Integer>();

		for (DataSetRow row : set.getRows()) {
			network.setInput(row.getInput());
			network.calculate();

			double[] output = network.getOutput();
			int predictedDifficulty = chooseDifficulty(output);
			difficulties.add(predictedDifficulty);
		}
		return convertIntegers(difficulties);
	}

	public DataSetRow boardToInputRow(Board b) {
		SolveMethod[] methods = new SolveMethod[] { new NakedSingleMethod(), new HiddenSingleMethod(),
				new NakedSubSetMethod((byte) 2), new HiddenSubSetMethod((byte) 2), new NakedSubSetMethod((byte) 3),
				new HiddenSubSetMethod((byte) 3), new NakedSubSetMethod((byte) 4), new HiddenSubSetMethod((byte) 4),
				new BlockLineInteractionMethod() };

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
			if (methods[solveMethod].apply(b)) {
				solveCounter[solveMethod]++;
				solveMethod = 0;
			} else
				solveMethod++;
		}

		if (b.isSolvedCorrectly()) {
		} else {
			Backtrack.solve(b);
			wasBacktracked = 1;
		}

		double toSolveCount = b.SIZE * b.SIZE - b.GIVENCOUNT;
		double[] inputValues = new double[] { ((double) solveCounter[0] / toSolveCount) * 100,
				((double) solveCounter[1] / toSolveCount) * 100, solveCounter[2], solveCounter[3], solveCounter[8],
				solveCounter[4], solveCounter[5], solveCounter[6], solveCounter[7], b.GIVENCOUNT, startPos[0],
				startPos[1], startPos[0], startPos[2], startPos[3], startPos[4], startPos[5], startPos[6], startPos[7],
				startPos[8], totalPossibilities, wasBacktracked };

		return new DataSetRow(inputValues);
	}

	public int chooseDifficulty(double[] outputs) {
		int index = 0;
		double max = 0;
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] > max) {
				max = outputs[i];
				index =7-i;
			}
		}
		return index;
	}

	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		Iterator<Integer> iterator = integers.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next().intValue();
		}
		return ret;
	}

	public void printConfMat(int[][] confMat) {
		for (int i = 0; i < confMat.length; i++) {
			for (int j = 0; j < confMat[i].length; j++) {
				System.out.print(confMat[i][j] + "\t");
			}
			System.out.println();
		}
	}
}
