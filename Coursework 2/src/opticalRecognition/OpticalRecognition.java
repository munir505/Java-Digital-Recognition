//This does the folds tests and runs
//the algorithms
package opticalRecognition;

public class OpticalRecognition {
	// Creates globals that are needed
	public static final String fileOneName = "cw2DataSet1.csv";
	public static final String fileTwoName = "cw2DataSet2.csv";
	public static boolean foldTest = false;
	public static float accuracyOne = 0;
	public static float accuracyTwo = 0;

	// Runs the algorithms that takes input and runs accordingly
	public static void runAlgorithmFold(int algorithm, int checkAmount) {
		// Runs both folds and gets the average of the two folds
		System.out.println("Fold One");
		runAlgorithm(1, algorithm, checkAmount);
		System.out.println("Fold Two");
		runAlgorithm(2, algorithm, checkAmount);
		// This gets the average of both folds
		System.out.println("Getting Average.....");
		float averageTwoFold = (accuracyOne + accuracyTwo) / 2;
		System.out.println("Average of Two Folds = " + averageTwoFold + " %");
	}

	// This runs the according algorithm both times
	public static void runAlgorithm(int runFold, int algorithm, int checkAmount) {
		// Gets the data
		Data getData = new Data();
		String classified = null;
		String classifying = null;
		// Assigns the two different fold data sets
		if (runFold == 1) {
			classified = fileOneName;
			classifying = fileTwoName;
		} else if (runFold == 2) {
			classified = fileTwoName;
			classifying = fileOneName;
		}
		// Does a try catch exception
		try {
			// Gets the data from files
			getData.getclassifiedSet(classified);
			getData.getclassifyingSet(classifying);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		// Runs the according algorithm
		if (foldTest == false) {
			if (algorithm == 1) {
				accuracyOne = getData.euclideanDistance();
			} else if (algorithm == 2) {
				accuracyOne = getData.nearestN(checkAmount);
			} else if (algorithm == 3) {
				accuracyOne = getData.clusterNearest();
			} else if (algorithm == 4) {
				accuracyOne = getData.runMLP();
			}
			foldTest = true;
		} else {
			// Runs it for second fold
			if (algorithm == 1) {
				accuracyTwo = getData.euclideanDistance();
			} else if (algorithm == 2) {
				accuracyTwo = getData.nearestN(checkAmount);
			} else if (algorithm == 3) {
				accuracyTwo = getData.clusterNearest();
			} else if (algorithm == 4) {
				accuracyTwo = getData.runMLP();
			}
		}
	}

	// This is the main, this only calls
	public static void main(String[] args) {
		// This is for one of the algorithms
		// 1 is Euclidean, 2 is N Nearest, 3 is cluster and 4 is MLP
		for (int nAmount = 1; nAmount < 10; nAmount++) {
			// Does the N nearest neighbour
			foldTest = false;
			accuracyOne = 0;
			accuracyTwo = 0;
			// Uncomment bellow to run other algorithm
			// runAlgorithmFold(2, nAmount);
		}
		// This runs other Algorithms
		runAlgorithmFold(1, 0);
	}
}
