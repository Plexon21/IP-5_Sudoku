package ch.fhnw.ip5.sudoku.network;

import java.io.File;
import java.util.ArrayList;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
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
public class MyNeuralNetwork implements LearningEventListener {

	int inputNodes = 22;
	int hiddenNodes = 10;
	int outputNodes = 7;
	int testSetPercentage = 80;
	int maxIter = 500;
	DataSet fullSet;
	MultiLayerPerceptron network;

	public MyNeuralNetwork(int in, int hid, int out, int testSet, int maxIt) {
		this.inputNodes = in;
		this.hiddenNodes = hid;
		this.outputNodes = out;
		this.testSetPercentage = testSet;
		this.maxIter = maxIt;
	}

	public static void main(String[] args) {
		MyNeuralNetwork network = new MyNeuralNetwork(22, 10, 7, 80, 1000);
		network.run();
	}

	public void parseAllFrom(String sourceFolder) {

		fullSet = new DataSet(inputNodes, outputNodes);

		File node = new File(sourceFolder);
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

	private void solve(File source) {
		try {

			ArrayList<Board> list = new ArrayList<>();

			list.addAll(SudokuReader.readFromFile(source));

			SolveMethod[] methods = new SolveMethod[] { new NakedSingleMethod(), new HiddenSingleMethod(),
					new NakedSubSetMethod((byte) 2), new HiddenSubSetMethod((byte) 2), new NakedSubSetMethod((byte) 3),
					new HiddenSubSetMethod((byte) 3), new NakedSubSetMethod((byte) 4), new HiddenSubSetMethod((byte) 4),
					new BlockLineInteractionMethod() };

			int numberOfSolvableWithGivenMethods = 0;
			int numberOfSudokus = list.size();

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

				if (b.isSolvedCorrectly()) {
					numberOfSolvableWithGivenMethods++;
				} else {
					Backtrack.solve(b);
					numberOfSolvableWithGivenMethods++;
					wasBacktracked = 1;
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

				double[] inputValues = new double[] { solveCounter[0], solveCounter[1], solveCounter[2],
						solveCounter[3], solveCounter[8], solveCounter[4], solveCounter[5], solveCounter[6],
						solveCounter[7], b.GIVENCOUNT, startPos[0], startPos[1], startPos[0], startPos[2], startPos[3],
						startPos[4], startPos[5], startPos[6], startPos[7], startPos[8], totalPossibilities,
						wasBacktracked };
				fullSet.addRow(inputValues, outputValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		parseAllFrom("C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\all_parsed");

		MultiLayerPerceptron network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputNodes, hiddenNodes,
				outputNodes);
		Normalizer n = new RangeNormalizer(0.0, 1.0);
		n.normalize(fullSet);

		DataSet[] sets = fullSet.createTrainingAndTestSubsets(testSetPercentage, 100 - testSetPercentage);
		DataSet trainingSet = sets[0];
		DataSet testSet = sets[1];

		network.setLearningRule(new MomentumBackpropagation());
		MomentumBackpropagation rule = (MomentumBackpropagation) network.getLearningRule();
		rule.setMaxIterations(maxIter);
		rule.setLearningRate(0.01);
		rule.setMaxError(0.01);

		rule.addListener(this);
		network.learn(trainingSet);
		testNetwork(network, testSet);
		DataSetRow row = fullSet.getRowAt(0);
		System.out.println(row.toCSV());
		fullSet.save("C:\\Programming\\IP-5_sudoku\\res\\fullset.txt");
		
	}

	public void trainNetwork(MultiLayerPerceptron network, DataSet trainingSet) {
		if (this.network == null) {
			network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputNodes, hiddenNodes,
				outputNodes);
		}
		Normalizer n = new RangeNormalizer(0.0, 1.0);
		network.setLearningRule(new MomentumBackpropagation());
		MomentumBackpropagation rule = (MomentumBackpropagation) network.getLearningRule();
		rule.setMaxIterations(maxIter);
		rule.setLearningRate(0.01);
		rule.setMaxError(0.01);

		rule.addListener(this);
		network.learn(trainingSet);
	}

	public void testNetwork(MultiLayerPerceptron network, DataSet testSet) {
		int[][] confMat = new int[7][7];

		for (DataSetRow row : testSet.getRows()) {
			network.setInput(row.getInput());
			network.calculate();

			double[] output = network.getOutput();
			int predictedDifficulty = getDifficulty(output);

			double[] shouldBe = row.getDesiredOutput();
			int targetDifficulty = getDifficulty(shouldBe);

			confMat[predictedDifficulty - 1][targetDifficulty - 1]++;
		}
		for (int i = 0; i < confMat.length; i++) {
			for (int j = 0; j < confMat[i].length; j++) {
				System.out.print(confMat[i][j] + "\t");
			}
			System.out.println();
		}
	}

	public int getDifficulty(double[] outputs) {
		int index = -1;
		double max = 0;
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] > max) {
				max = outputs[i];
				index = i + 1;
			}
		}
		return index;
	}

	@Override
	public void handleLearningEvent(LearningEvent event) {
		MomentumBackpropagation bp = (MomentumBackpropagation) event.getSource();
		/*
		 * if (event.getEventType() != LearningEvent.Type.LEARNING_STOPPED)
		 * System.out.println(bp.getCurrentIteration() + ". iteration : " +
		 * bp.getTotalNetworkError());
		 */

	}
}