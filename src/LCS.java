import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.String;
import java.util.Random;


public class LCS {
    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
    static String ResultsFolderPath = "/home/matt/Results/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;
    static int numberOfTrials = 1;
    static String S1 = "e";
    static String S2 = "dkjfaddjfkjakdjfjaelksjdjfdkfasldkj";
    static String lcs;

    public static void main(String[] args) {

        program("lab7.txt");
    }

    static void program(String resultsFileName) {
        ThreadCpuStopWatch stopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials
        long elapsedTime = 0;
        System.gc();
        int i, trial;

        // To open a file to write to
        try {
            resultsFile = new FileWriter(ResultsFolderPath + "lab7.txt");
            resultsWriter = new PrintWriter(resultsFile);
        } catch(Exception e) {
            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+"lab6");
            return;
        }

        resultsWriter.println("#Trial  SubString           AvgTime"); // # marks a comment in gnuplot data
        resultsWriter.flush();

        System.out.println("String 1: " + S1);
        System.out.println("String 2: " + S2);


        for (trial = 0; trial < numberOfTrials; ++trial) {
            stopwatch.start(); // Start timer in nano secs

            for (i = 0; i < 10; ++i) {
                lcs = LcsBrute(S1, S2);
            }
            elapsedTime = stopwatch.elapsedTime();
            double averageTimePerTrialInBatch = (double) elapsedTime / (double)numberOfTrials;
            resultsWriter.printf("%-7d %-7S %20f \n", trial, lcs, averageTimePerTrialInBatch);
            resultsWriter.flush();
        }

        // Call function printString to display the lcs
        printString(lcs);
    }

    public static String LcsBrute(String S1, String S2) {
        // Declare variables
        int StartIndex = 0, MaxSoFar = 0;

        // Brute Force Loop
        for (int i = 0; i < S1.length(); ++i) {
            for (int j = 0; j < S2.length(); ++j) {
                int k = 0;
                // Do this loop while I find matches
                while (S1.charAt(i+k) == S2.charAt(j+k)) {
                    // Keep incrementing k if there is a match found
                    ++k;
                    if (((i+k) >= S1.length()) || ((j+k) >= S2.length())) break;  // Break out of loop at end of one of the strings
                }
                if (k > MaxSoFar) {
                    // Get the new max so far
                    MaxSoFar = k;
                    // Get my new start index
                    StartIndex = i;
                }
            }
        }

        // return part of the string that had most matches
        return S1.substring(StartIndex, (StartIndex + MaxSoFar));
    }

    // Print my string
    public static void printString (String subString) {
        System.out.println("SubString:");
        System.out.println(subString);
    }
}


