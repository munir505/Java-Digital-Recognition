//This Class holds all the algorithms in
//the program, this class also gets the data
//from the CVS file
package opticalRecognition;

//Imports the necessary stuff
import java.util.Scanner;
import java.io.File;

public class Data {
	// Creating global variables that need to be used
	Scanner getData = new Scanner(System.in);
	// These two sets are for the data
	private int[][] classifyingSet = new int[2810][65];
	private int[][] classifiedSet = new int[2810][65];
	// These are for the majority of the algorithms
	private int[][] allDistances = new int[2810][2];
	private int[][][] allClusters = new int[10][2810][65];
	private int[][] allClustersCentre = new int[10][65];
	public int dimensionSize = 64;
	public int dataSize = 2810;
	// These globals are for the MLP
	public double learningRate = 0.1;
	public double[] inputWeights = new double[2560];
	public double[] hiddenLayerOneWeights = new double[400];
	public double[] hiddenLayerOneOutput = new double[40];
	public double[] outputLayerOutput = new double[10];
	public int weightIndex = 0;

	// This gets the data for the training set
	public void getclassifiedSet(String fileName) throws Exception {
		obtainData(fileName, classifiedSet);
	}

	// This gets the data for the testing set
	public void getclassifyingSet(String fileName) throws Exception {
		obtainData(fileName, classifyingSet);
	}

	// This method gets the data from the files
	private void obtainData(String fileName, int[][] dataArray) throws Exception {
		String[] dataLine = new String[dimensionSize + 1];
		File dataFile = new File(fileName);
		Scanner reader = new Scanner(dataFile);
		int nextLine = 0;
		// This while loop checks for next integer
		while (reader.hasNext()) {
			String nextVal = reader.nextLine();
			dataLine = nextVal.split(",");
			for (int dataIndex = 0; dataIndex < dimensionSize + 1; dataIndex++) {
				int dataVal = Integer.parseInt(dataLine[dataIndex]);
				dataArray[nextLine][dataIndex] = dataVal;
			}
			nextLine++;
		}
		reader.close();
	}

	// This function calculates the accuracy it requires a correct amount
	public float calculateAccuracy(float correct) {
		// Checks the accuracy out of 100
		float accuracy = correct / dataSize * 100;
		System.out.println("Accuracy Rate = " + accuracy + " %");
		int correctInt = (int) correct;
		System.out.println("Amount Correct = " + correctInt);
		// Returns the accuracy
		return accuracy;
	}

	// Gets the closest distance between a single point to the whole data set
	private int closestDistance(int[] classifyingData, int[][] classifiedData) {
		// Set the variables
		int closestDistance = Integer.MAX_VALUE;
		int closestArrayIndex = 0;
		for (int classifiedIndex = 0; classifiedIndex < dataSize; classifiedIndex++) {
			int totalDistance = 0;
			for (int coordinate = 0; coordinate < dimensionSize - 1; coordinate++) {
				int classifyingPoint = classifyingData[coordinate];
				int classifiedPoint = classifiedData[classifiedIndex][coordinate];
				int coordinateVal = classifyingPoint - classifiedPoint;
				int coordinateSqu = coordinateVal * coordinateVal;
				totalDistance = totalDistance + coordinateSqu;
			}
			// Checks the distance
			if (totalDistance < closestDistance) {
				closestDistance = totalDistance;
				closestArrayIndex = classifiedIndex;
			}
		}
		// Returns the closest distances index
		return closestArrayIndex;
	}

	// This is the Euclidean distance algorithm
	public float euclideanDistance() {
		float correct = 0;
		// Runs the other function which gets the distances
		for (int classifyingIndex = 0; classifyingIndex < dataSize; classifyingIndex++) {
			int closestIndex = closestDistance(classifyingSet[classifyingIndex], classifiedSet);
			if (classifiedSet[closestIndex][dimensionSize] == classifyingSet[classifyingIndex][dimensionSize]) {
				correct++;
			}
		}
		float accuracy = calculateAccuracy(correct);
		return accuracy;
	}

	// This function gets the distances for the nearest N
	private void getAllDistances(int[] classifyingData, int[][] classifiedData) {
		for (int classifiedIndex = 0; classifiedIndex < dataSize; classifiedIndex++) {
			int totalDistance = 0;
			// This does nearest neighbour for the N closest
			for (int coordinate = 0; coordinate < dimensionSize; coordinate++) {
				int classifyingPoint = classifyingData[coordinate];
				int classifiedPoint = classifiedData[classifiedIndex][coordinate];
				int coordinateVal = classifyingPoint - classifiedPoint;
				int coordinateSqu = coordinateVal * coordinateVal;
				totalDistance = totalDistance + coordinateSqu;
			}
			// Adds the distances to the array
			allDistances[classifiedIndex][0] = totalDistance;
			allDistances[classifiedIndex][1] = classifiedData[classifiedIndex][64];
		}
	}

	// This sorts all the distances
	private void sortDistances(int[][] distances) {
		// THis goes through all the calculated distances and sorts them
		for (int current = 0; current < dataSize; current++) {
			for (int sortAgainst = 0; sortAgainst < dataSize; sortAgainst++) {
				// The sorting makes it easier to then get the N amount of
				// closest
				if (distances[current][0] < distances[sortAgainst][0]) {
					int tempDistance = distances[current][0];
					distances[current][0] = distances[sortAgainst][0];
					distances[sortAgainst][0] = tempDistance;
					int tempCategory = distances[current][1];
					distances[current][1] = distances[sortAgainst][1];
					distances[sortAgainst][1] = tempCategory;
				}
			}
		}
	}

	// This gets the all sorted distances
	private int majorityCategory(int runTime) {
		int[] allCategories = new int[10];
		// This sorts the categories
		for (int checkMajority = 0; checkMajority < runTime; checkMajority++) {
			if (allDistances[checkMajority][1] == 0) {
				allCategories[0] = allCategories[0] + 1;
			} else if (allDistances[checkMajority][1] == 1) {
				allCategories[1] = allCategories[1] + 1;
			} else if (allDistances[checkMajority][1] == 2) {
				allCategories[2] = allCategories[2] + 1;
			} else if (allDistances[checkMajority][1] == 3) {
				allCategories[3] = allCategories[3] + 1;
			} else if (allDistances[checkMajority][1] == 4) {
				allCategories[4] = allCategories[4] + 1;
			} else if (allDistances[checkMajority][1] == 5) {
				allCategories[5] = allCategories[5] + 1;
			} else if (allDistances[checkMajority][1] == 6) {
				allCategories[6] = allCategories[6] + 1;
			} else if (allDistances[checkMajority][1] == 7) {
				allCategories[7] = allCategories[7] + 1;
			} else if (allDistances[checkMajority][1] == 8) {
				allCategories[8] = allCategories[8] + 1;
			} else if (allDistances[checkMajority][1] == 9) {
				allCategories[9] = allCategories[9] + 1;
			}
		}
		// This finds the most popular category
		int mostCategory = 0;
		for (int getBiggest = 0; getBiggest < 10; getBiggest++) {
			if (allCategories[getBiggest] > mostCategory) {
				mostCategory = getBiggest;
			}
		}
		// Returns the most popular
		return mostCategory;
	}

	// This runs the nearest N algorithm
	public float nearestN(int checkAmount) {
		float correct = 0;
		// This gets all distances and runs the functions
		for (int classifyingIndex = 0; classifyingIndex < dataSize; classifyingIndex++) {
			getAllDistances(classifyingSet[classifyingIndex], classifiedSet);
			sortDistances(allDistances);
			int magCat = majorityCategory(checkAmount);
			// This checks how many are correct
			if (classifyingSet[classifyingIndex][dimensionSize] == magCat) {
				correct++;
			}
		}
		// Does the accuracy function
		float accuracy = calculateAccuracy(correct);
		return accuracy;
	}

	// This stores the clusters
	private void storeCluster(int clusterCategory, int[] coordinate) {
		boolean emptyCoordinate = false;
		int checkEmpty = 0;
		// Checks if the cluster is empty
		while (emptyCoordinate == false) {
			if (allClusters[clusterCategory][checkEmpty][0] == -1) {
				emptyCoordinate = true;
			}
			// Adds to the according array
			if (emptyCoordinate == true) {
				allClusters[clusterCategory][checkEmpty] = coordinate;
			}
			checkEmpty++;
		}
	}

	// This gets the centre of a cluster
	private void getCentre(int[][] cluster) {
		int containsAmount = 0;
		for (int checkEmpty = 0; checkEmpty < dataSize; checkEmpty++) {
			// This checks if the cluster has empty values
			if (cluster[checkEmpty][0] != -1) {
				containsAmount++;
			}
		}
		// This gets the centre
		for (int elements = 0; elements < dimensionSize; elements++) {
			int lowestValue = Integer.MAX_VALUE;
			int highestValue = 0;
			// Gets the lowest value
			for (int allCoordinates = 0; allCoordinates < containsAmount; allCoordinates++) {
				if (cluster[allCoordinates][elements] < lowestValue) {
					lowestValue = cluster[allCoordinates][elements];
				}
				// This gets the highest Value
				if (cluster[allCoordinates][elements] > highestValue) {
					highestValue = cluster[allCoordinates][elements];
				}
			}
			// This gets the middle from the lowest and highest
			int middleValue = (highestValue + lowestValue) / 2;
			allClustersCentre[cluster[0][dimensionSize]][elements] = middleValue;
			allClustersCentre[cluster[0][dimensionSize]][dimensionSize] = cluster[0][dimensionSize];
		}
	}

	// This checks the clusters against the test data
	public float clusterNearest() {
		createClusters();
		for (int getAllCentre = 0; getAllCentre < 10; getAllCentre++) {
			getCentre(allClusters[getAllCentre]);
		}
		// This checks how many are correct
		float correct = 0;
		for (int checkClosest = 0; checkClosest < dataSize; checkClosest++) {
			int classifiedCategory = getClosestCluster(classifyingSet[checkClosest], allClustersCentre);
			if (classifyingSet[checkClosest][dimensionSize] == classifiedCategory) {
				correct++;
			}
		}
		// Runs the function to calculate the amount correct
		float accuracy = calculateAccuracy(correct);
		return accuracy;
	}

	// This gets the closest cluster to a coordinate
	public int getClosestCluster(int[] classifyingData, int[][] clusterData) {
		int closestDistance = Integer.MAX_VALUE;
		int closestArrayIndex = 0;
		// This check how far a coordinate is from the cluster coordinate
		for (int classifiedIndex = 0; classifiedIndex < 10; classifiedIndex++) {
			int totalDistance = 0;
			// Gets the distances
			for (int coordinate = 0; coordinate < dimensionSize; coordinate++) {
				int classifyingPoint = classifyingData[coordinate];
				int classifiedPoint = clusterData[classifiedIndex][coordinate];
				int coordinateVal = classifyingPoint - classifiedPoint;
				int coordinateSqu = coordinateVal * coordinateVal;
				totalDistance = totalDistance + coordinateSqu;
			}
			// Checks the closest and saves it
			if (totalDistance < closestDistance) {
				closestDistance = totalDistance;
				closestArrayIndex = classifiedIndex;
			}
		}
		// This returns the closest distance categorys
		int closestCategory = clusterData[closestArrayIndex][dimensionSize];
		return closestCategory;
	}

	// This separates all clusters
	public void createClusters() {
		// Sets the values to -1 to check for empty slots
		for (int clusterNumber = 0; clusterNumber < 10; clusterNumber++) {
			for (int coordinate = 0; coordinate < dataSize; coordinate++) {
				for (int element = 0; element < dimensionSize + 1; element++) {
					allClusters[clusterNumber][coordinate][element] = -1;
				}
			}
		}
		// Adds the according coordinates to the empty slots
		for (int coordinate = 0; coordinate < dataSize; coordinate++) {
			switch (classifiedSet[coordinate][dimensionSize]) {
			case 0:
				storeCluster(0, classifiedSet[coordinate]);
				break;
			case 1:
				storeCluster(1, classifiedSet[coordinate]);
				break;
			case 2:
				storeCluster(2, classifiedSet[coordinate]);
				break;
			case 3:
				storeCluster(3, classifiedSet[coordinate]);
				break;
			case 4:
				storeCluster(4, classifiedSet[coordinate]);
				break;
			case 5:
				storeCluster(5, classifiedSet[coordinate]);
				break;
			case 6:
				storeCluster(6, classifiedSet[coordinate]);
				break;
			case 7:
				storeCluster(7, classifiedSet[coordinate]);
				break;
			case 8:
				storeCluster(8, classifiedSet[coordinate]);
				break;
			case 9:
				storeCluster(9, classifiedSet[coordinate]);
				break;
			}
		}
	}

	// This runs the MLP
	public float runMLP() {
		// This creates random weights for both layers
		createRandomWeights();
		double[] inputs = new double[dimensionSize];
		for (int learnAmount = 0; learnAmount < 1000; learnAmount++) {
			// This does all function for all coordinates
			for (int doCoordinate = 0; doCoordinate < dataSize; doCoordinate++) {
				// Changes an int to a double for compatibility
				inputs = toSingle(doCoordinate);
				// Runs the layers needed
				runLayer(inputs, dimensionSize, hiddenLayerOneOutput.length, "InputLayer");
				runLayer(hiddenLayerOneOutput, hiddenLayerOneOutput.length, 10, "FirstHiddenLayer");
				// Makes a prediction
				int predictedAnswer = getPredicted();
				int expectedAnswer = classifiedSet[doCoordinate][dimensionSize];
				// Checks if it is correct
				if (predictedAnswer == expectedAnswer) {
					;
				} else {
					// Runs the back propagation
					double errorSignal = error(predictedAnswer, expectedAnswer);
					errorBackPropagation(errorSignal);
				}
			}
		}
		// Checks the amount correct
		float correct = 0;
		// Adds the weights to the testing set and check how much correct
		for (int checkCorrect = 0; checkCorrect < dataSize; checkCorrect++) {
			inputs = toSingle(checkCorrect);
			runLayer(inputs, dimensionSize, hiddenLayerOneOutput.length, "InputLayer");
			runLayer(hiddenLayerOneOutput, hiddenLayerOneOutput.length, 10, "FirstHiddenLayer");
			int predictedAnswer = getPredicted();
			int expectedAnswer = classifyingSet[checkCorrect][dimensionSize];
			// Calculates how many correct
			if (predictedAnswer == expectedAnswer) {
				correct++;
			}
		}
		// Runs the accuracy function to get correct amount
		float accuracy = calculateAccuracy(correct);
		return accuracy;
	}

	// This check the error signal for the back propagation
	public double error(int predictedAnswer, int expectedAnswer) {
		double errorSignal = 0;
		double actual = outputLayerOutput[expectedAnswer];
		double predictedVal = outputLayerOutput[predictedAnswer];
		int startChange = predictedAnswer * hiddenLayerOneOutput.length;
		int endChange = startChange + hiddenLayerOneOutput.length;
		// Does the weight change to all weights
		for (int previousVal = 0, currentVal = 0; previousVal < hiddenLayerOneOutput.length * 10; previousVal++) {
			if (previousVal >= startChange && previousVal < endChange) {
				errorSignal = (actual - predictedVal) * predictedVal * (1 - predictedVal);
				// Calculates the weight change
				double weightChange = learningRate * errorSignal * hiddenLayerOneOutput[currentVal];
				hiddenLayerOneWeights[previousVal] = hiddenLayerOneWeights[previousVal] + weightChange;
				currentVal++;
			}
		}
		return errorSignal;
	}

	// Calculates the error for the hidden layer weights
	public void errorBackPropagation(double errorSignal) {
		double error = 0;
		double sigma = 0;
		double weightChange = 0;
		// Does this for all weights in the input layer
		for (int checkChange = 0; checkChange < hiddenLayerOneOutput.length; checkChange++) {
			for (int currentWeight = 0; currentWeight < dimensionSize; currentWeight++) {
				error = hiddenLayerOneOutput[checkChange] * (1 - hiddenLayerOneOutput[checkChange]);
				sigma = errorSignal;
				int start = checkChange * 10;
				// Does sigma
				for (int sumSigma = 0; sumSigma < start; sumSigma++) {
					sigma = sigma + hiddenLayerOneWeights[sumSigma];
				}
				// Does weight change
				weightChange = error * sigma;
				int startChange = checkChange * hiddenLayerOneOutput.length;
				inputWeights[startChange] = weightChange;
				startChange++;
			}
		}
	}

	// Gets the predicted value
	public int getPredicted() {
		double getHighest = 0;
		int predicted = 0;
		// Gets the highest and predicted it as answer
		for (int checkPredicted = 0; checkPredicted < 10; checkPredicted++) {
			if (outputLayerOutput[checkPredicted] > getHighest) {
				// Saves current answer
				getHighest = outputLayerOutput[checkPredicted];
				predicted = checkPredicted;
			}
		}
		// returns the answer that it predicted
		return predicted;
	}

	// Converts a double to int and double to single array
	public double[] toSingle(int index) {
		// Creates array to save in
		double[] inputs = new double[dimensionSize];
		// Gets the integer and saves it as double
		for (int changeToSingle = 0; changeToSingle < dimensionSize; changeToSingle++) {
			inputs[changeToSingle] = classifyingSet[index][changeToSingle];
		}
		// Returns the array that was converted
		return inputs;
	}

	// This runs the layer, it takes different parameters and runs correct layer
	public void runLayer(double[] inputs, int inputAmount, int outputAmount, String layer) {
		weightIndex = 0;
		double[] weightedValues = new double[inputAmount];
		// Runs the perceptron and weights the values
		for (int runPerceptron = 0; runPerceptron < outputAmount; runPerceptron++) {
			weightedValues = weightValues(inputs, inputAmount, layer);
			perceptron(weightedValues, runPerceptron, layer);
		}
	}

	// This weights the values that are needed
	public double[] weightValues(double[] inputs, int inputAmount, String layer) {
		double[] weightedValues = new double[dimensionSize];
		// Checks what layers need to be run
		if (layer.equals("InputLayer")) {
			for (int currentWeight = 0; currentWeight < inputAmount; currentWeight++) {
				// Weights the inputs
				weightedValues[currentWeight] = inputs[currentWeight] * inputWeights[weightIndex];
				weightIndex++;
			}
		} else if (layer.equals("FirstHiddenLayer")) {
			for (int currentWeight = 0; currentWeight < inputAmount; currentWeight++) {
				// weights the inputs
				weightedValues[currentWeight] = inputs[currentWeight] * hiddenLayerOneWeights[weightIndex];
				weightIndex++;
			}
		}
		// Return the weighted inputs
		return weightedValues;
	}

	// This create random weights for the two layers
	public void createRandomWeights() {
		// For the input weights
		for (int currentWeight = 0; currentWeight < inputWeights.length; currentWeight++) {
			double createdRanNum = Math.random() * 2 - 1;
			inputWeights[currentWeight] = createdRanNum;
		}
		// for the hidden layer weights
		for (int currentWeight = 0; currentWeight < hiddenLayerOneOutput.length * 10; currentWeight++) {
			double createdRanNum = Math.random() * 2 - 1;
			hiddenLayerOneWeights[currentWeight] = createdRanNum;
		}
	}

	// This is the perceptron and gets the sum and sigmoid
	public void perceptron(double[] weightedInputs, int index, String layer) {
		double weightedSum = 0;
		// This gets the sum of the answers and adds together
		for (int onWeight = 0; onWeight < dimensionSize; onWeight++) {
			weightedSum = weightedSum + weightedInputs[onWeight];
		}
		// This does the sigmoid function to the weighted sum
		double sigmoidSum = 1 / (1 + Math.pow(Math.E, -weightedSum));
		// Does to the according layer
		if (layer.equals("InputLayer")) {
			hiddenLayerOneOutput[index] = sigmoidSum;
		} else if (layer.equals("FirstHiddenLayer")) {
			outputLayerOutput[index] = sigmoidSum;
		}
	}
}