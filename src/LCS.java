import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.String;
import java.util.Random;
import java.nio.file.*;



public class LCS {
    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
    static String ResultsFolderPath = "/home/matt/Results/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;
    static int numberOfTrials = 3;
    static String S1 = "aaaaaaaaaaaaaaaaaaaa";
    static String S2 = "aaaaaaaaaaaaaaaaaaaa";
    static String lcs;

    public static void main(String[] args) {
        // run the whole experiment at least twice, and expect to throw away the data from the earlier runs, before java has fully optimized
        System.out.println("Running first full experiment...");
        program("lab7-1.txt");
        System.out.println("Running second full experiment...");
        program("lab7-2.txt");
        System.out.println("Running third full experiment...");
        program("lab7-3.txt");
    }

    static void program(String resultsFileName) {
        ThreadCpuStopWatch stopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials
        int maxInput = 1000;
        int i, trial;
        String str = "";

        // To open a file to write to
        try {
            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);
        } catch(Exception e) {
            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+"lab6");
            return;
        }

        resultsWriter.println("#InputSize       AvgTime"); // # marks a comment in gnuplot data
        resultsWriter.flush();

        try {
            S1 = readFile("/home/matt/Results/book1.txt");
            S2 = readFile("/home/matt/Results/book2.txt");
        } catch(Exception e) {
            System.out.println(" Error!! ");
            return;
        }

        String S3 = concatenate(S1);
        String S4 = concatenate(S2);


        // Create my random strings
        //S1 = randomString(str);
        //S2 = randomString(str);

        //System.out.println("String 1: " + S3);
        //System.out.println("String 2: " + S4);


        for (int inputSize = 1; inputSize < maxInput; inputSize*=2) {
            long elapsedTime = 0;
            System.gc();

            stopwatch.start(); // Start timer in nano secs

            for (i = 0; i < 10; ++i) {

                S1 = ranStartIndex(S3, inputSize);
                S2 = ranStartIndex(S4, inputSize);
                System.out.println("String 1: " + S1);
                System.out.println("String 2: " + S2);
                lcs = LcsBrute(S1, S2);
                //lcs = Lcs2D(S1, S2);

                // Call function printString to display the lcs
                printString(lcs);
            }
            elapsedTime = stopwatch.elapsedTime();
            double averageTimePerTrialInBatch = (double) elapsedTime / (double)numberOfTrials;
            resultsWriter.printf("%-7d %20f \n", inputSize, averageTimePerTrialInBatch);
            resultsWriter.flush();
        }


    }

    public static String ranStartIndex (String str, int N) {
        int length = str.length();
        Random random = new Random();
        int ranStart = random.nextInt(length-1);

        return str.substring(ranStart, (ranStart + N));
    }

    public static String concatenate (String str) {
        int length = str.length();
        int N = 30;
        char[] strArray = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strArray.length; ++i) {
            if ((strArray[i] != ' ') && (strArray[i] != '\t' && (strArray[i] != '\n'))) {
                stringBuffer.append(strArray[i]);
            }
        }
        String noSpaceStr2 = stringBuffer.toString();

        return noSpaceStr2;

        //Random random = new Random();
        //int ranStart = random.nextInt(length-1);

        //return noSpaceStr2.substring(ranStart, (ranStart + N));
    }

    public static String readFile (String file)throws Exception {
        file = new String (Files.readAllBytes(Paths.get(file)));
        return file;
    }

    public static String randomString (String Str) {
        int a = 97, z = 122;
        int length = 20;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            int getInt = a + (int) (random.nextFloat() * (z - a + 1));
            buffer.append((char) getInt);
        }
        String myRandomString = buffer.toString();

        return myRandomString;
    }

    public static String Lcs2D (String S1, String S2) {
        int StartIndex = 0, MaxSoFar = 0;

        // Need the length of our strings
        int len1 = S1.length();
        int len2 = S2.length();

        // Create lcsArray
        int[][] lcsArray = new int[len1][len2];

        // Need to keep track of the last index
        int k = 0;

        for (int i = 0; i < len1; ++i) {
            for (int j = 0; j < len2; ++j) {
                // Put matching characters from the string and mark them
                if (S1.charAt(i) == S2.charAt(j)) {
                    if (i == 0 || j == 0) {
                        // Mark those elements
                        lcsArray[i][j] = 1;
                    }
                    else {
                        lcsArray[i][j] = lcsArray[i-1][j-1]+1;
                    }
                    // Find where the last matching index is and make that k
                    if (MaxSoFar < lcsArray[i][j]) {
                        MaxSoFar = lcsArray[i][j];
                        k = i;
                    }
                }
            }
        }

        // The longest substring up to k
        return S1.substring(k-MaxSoFar+1, k+1);
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


