package recognisers;

import algorithm.BackPropagationImpl;

import java.io.*;
import java.util.*;

public class FaceRecogniser {


    public static void main(String[] args) throws Exception {

        File f = new File("src/main/resources/straighteven_train.list.txt");
        File test1 = new File("src/main/resources/straighteven_test1.list.txt");
        File test2 = new File("src/main/resources/straighteven_test2.list.txt");
        String filePath = f.getAbsolutePath();
        FileInputStream fis = new FileInputStream(filePath);
        fis.close();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        int count1 = 0;


        //TRAINING DATA
        double[][] expectedOutput = new double[80][20];
        double[][] data = new double[80][960];

        for (int i = 0; i < 80; i++) {
            Arrays.fill(expectedOutput[i], 0);
        }
        BufferedReader br2 = new BufferedReader(new FileReader("src/main/resources/faces/.anonr"));
        String[] names = new String[20];
        int c1 = 0;
        while ((line = br2.readLine()) != null) {
            names[c1] = line;
            //System.out.println(names[c1]);
            c1++;
        }

        //going through every file mentioned in the list.
        while ((line = br.readLine()) != null) {

            //System.out.println(line);
            line = line.substring(line.lastIndexOf("ces/") + 4);
            //System.out.println(line);
            String fp = "src/main/resources/faces/" + line;
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/faces/" + line);
            DataInputStream dis = new DataInputStream(fileInputStream);
            for (int k = 0; k < 20; k++) {
                if (fp.contains(names[k])) {
                    expectedOutput[count1][k] = 1;
                    break;
                }
            }
            // look for 3 lines (i.e.: the header) and discard them
            int numnewlines = 3;
            while (numnewlines > 0) {
                char c;
                do {
                    c = (char) (dis.readUnsignedByte());
                } while (c != '\n');
                numnewlines--;
            }
            // read the image data

            int row = 0;
            //System.out.println(filePath.substring(filePath.lastIndexOf("_") + 1));

            while (dis.available() > 0) {

                data[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                //System.out.print(data[count1][row] + " ");
                row++;
            }
            //System.out.println("\nCOUNT: " + count);


            count1++;
        }


        //TESTING DATA 1------------------------------------------------------------------------------------------------
        String testPath = test1.getAbsolutePath();
        FileInputStream fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;


        double[][] testInput = new double[36][960];
        double expectedTest[][] = new double[36][20];


        //going through every file mentioned in the list.
        while ((line = br.readLine()) != null) {
            //System.out.println(line);
            line = line.substring(line.lastIndexOf("ces/") + 4);
            //System.out.println(line);
            String fp = "src/main/resources/faces/" + line;
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/faces/" + line);
            DataInputStream dis = new DataInputStream(fileInputStream);

            // look for 3 lines (i.e.: the header) and discard them
            int numnewlines = 3;
            while (numnewlines > 0) {
                char c;
                do {
                    c = (char) (dis.readUnsignedByte());
                } while (c != '\n');
                numnewlines--;
            }
            // read the image data


            int row = 0;

            while (dis.available() > 0) {

                testInput[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                //System.out.print(testInput[count1][row] + " ");
                row++;
            }
            //System.out.println("\nCOUNT: " + count);

            for (int k = 0; k < 20; k++) {
                if (fp.contains(names[k])) {
                    expectedTest[count1][k] = 1;
                    break;
                }
            }
            count1++;
        }

        int[] NumberOfNodes = new int[3];
        double LearnRate = 0.3;
        double Moment = 0.3;
        long MaxIter = 50000;
        double MinError = 0.01;
        NumberOfNodes[0] = 960;
        NumberOfNodes[1] = 20;
        NumberOfNodes[2] = 20;


        BackPropagationImpl backPropogator = new BackPropagationImpl(NumberOfNodes, data, expectedOutput, LearnRate, Moment, MinError, MaxIter);
        backPropogator.run();
        double[][] testOutput = new double[36][20];
        double Error = 0;
        double Accuracy;
        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 36; i++) {
            int index = 0;
            testOutput[i] = backPropogator.test(testInput[i]);
            double max = 0;
            int maxi = 0;
            for (int j = 0; j < 20; j++) {
                //System.out.println("  ----  " + i + ")" + "ANS: "+ testOutput[i][j]);
                if (testOutput[i][j] > max) {
                    maxi = j;
                    max = testOutput[i][j];
                }
                //counting  miss-classified data
                if (expectedTest[i][j] == 1) {
                    index = j;
                }

            }
            if (maxi != index) {
                System.out.println("test1" + " " + (i + 1) + " " + names[index]);
                Error++;
            }

        }


        //TESTING DATA 2------------------------------------------------------------------------------------------------------
        testPath = test2.getAbsolutePath();
        fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;


        double[][] test2Input = new double[40][960];
        double expectedTest2[][] = new double[40][20];


        //going through every file mentioned in the list.
        while ((line = br.readLine()) != null) {
            //System.out.println(line);
            line = line.substring(line.lastIndexOf("ces/") + 4);
            //System.out.println(line);
            String fp = "src/main/resources/faces/" + line;
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/faces/" + line);
            DataInputStream dis = new DataInputStream(fileInputStream);

            // look for 3 lines (i.e.: the header) and discard them
            int numnewlines = 3;
            while (numnewlines > 0) {
                char c;
                do {
                    c = (char) (dis.readUnsignedByte());
                } while (c != '\n');
                numnewlines--;
            }
            // read the image data


            int row = 0;

            while (dis.available() > 0) {

                test2Input[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                //System.out.print(testInput[count1][row] + " ");
                row++;
            }
            //System.out.println("\nCOUNT: " + count);

            for (int k = 0; k < 20; k++) {
                if (fp.contains(names[k])) {
                    expectedTest2[count1][k] = 1;
                    break;
                }
            }
            count1++;
        }

        backPropogator = new BackPropagationImpl(NumberOfNodes, data, expectedOutput, LearnRate, Moment, MinError, MaxIter);
        backPropogator.run();
        double[][] testOutput2 = new double[40][20];
        double Error2 = 0;
        double Accuracy2;
        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 40; i++) {
            int index = 0;
            testOutput2[i] = backPropogator.test(test2Input[i]);
            double max = 0;
            int maxi = 0;
            for (int j = 0; j < 20; j++) {
                //System.out.println("  ----  " + i + ")" + "ANS: "+ testOutput[i][j]);
                if (testOutput2[i][j] > max) {
                    maxi = j;
                    max = testOutput2[i][j];
                }
                //counting  miss-classified data
                if (expectedTest2[i][j] == 1) {
                    index = j;
                }

            }
            if (maxi != index) {
                System.out.println("test2" + " " + (i + 1) + " " + names[index]);
                Error2++;
            }

        }
        //System.out.println(Error);

//--------------------------------------------------------------------------------------------------------------------------				


        double error = backPropogator.get_error();
        System.out.println("TrainError: " + error);
        double accuracy = 100 - error;
        System.out.println("TrainAccuracy: " + accuracy);

        Error = Error / 36;
        System.out.println("TestingError1: " + " " + Error);
        Accuracy = 1 - Error;
        System.out.println("TestAccuracy1: " + Accuracy);

        Error2 = Error2 / 40;
        System.out.println("TestingError2: " + " " + Error2);
        Accuracy2 = 1 - Error2;
        System.out.println("TestAccuracy2: " + Accuracy2);


    }

}
