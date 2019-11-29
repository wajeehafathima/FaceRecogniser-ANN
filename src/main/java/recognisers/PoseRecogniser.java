package recognisers;

import algorithm.BackPropagationImpl;

import java.io.*;

public class PoseRecogniser {


    public static void main(String[] args) throws Exception {

        File f = new File("src/main/resources/all_train.list.txt");
        File test1 = new File("src/main/resources/all_test1.list.txt");
        File test2 = new File("src/main/resources/all_test2.list.txt");
        String filePath = f.getAbsolutePath();
        FileInputStream fis = new FileInputStream(filePath);
        fis.close();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        int count1 = 0;

        //TRAINIG DATA:
        double[][] expectedOutput = new double[277][4];
        double[][] data = new double[277][960];

        //going through every file mentioned in the list.
        while ((line = br.readLine()) != null) {
            int count = 0;
            //System.out.println(line);
            line = line.substring(line.lastIndexOf("ces/") + 4);
            //System.out.println(line);
            String fp = "src/main/resources/faces/" + line;
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/faces/" + line);
            DataInputStream dis = new DataInputStream(fileInputStream);

            //storing expected output from file name
            if (fp.contains("left")) {
                expectedOutput[count1][0] = 1;
                expectedOutput[count1][1] = 0;
                expectedOutput[count1][2] = 0;
                expectedOutput[count1][3] = 0;
            } else if (fp.contains("right")) {
                expectedOutput[count1][0] = 0;
                expectedOutput[count1][1] = 1;
                expectedOutput[count1][2] = 0;
                expectedOutput[count1][3] = 0;
            } else if (fp.contains("up")) {
                expectedOutput[count1][0] = 0;
                expectedOutput[count1][1] = 0;
                expectedOutput[count1][2] = 1;
                expectedOutput[count1][3] = 0;
            } else if (fp.contains("straight")) {
                expectedOutput[count1][0] = 0;
                expectedOutput[count1][1] = 0;
                expectedOutput[count1][2] = 0;
                expectedOutput[count1][3] = 1;
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

            count = 0;
            int row = 0;

            while (dis.available() > 0) {
                count++;
                data[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                // System.out.print(data[count1][row] + " ");
                row++;
            }
            //System.out.println("\nCOUNT: " + count);


            count1++;
        }


        //TESTING DATA1-----------------------------------------------------------------------------------------------------
        String testPath = test1.getAbsolutePath();
        FileInputStream fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;


        double[][] testInput = new double[139][960];
        double[][] expectedTest = new double[139][4];


        //going through every file mentioned in the list.
        while ((line = br.readLine()) != null) {
            int count = 0;
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

            count = 0;
            int row = 0;


            while (dis.available() > 0) {
                count++;
                testInput[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                //System.out.print(testInput[count1][row] + " ");
                row++;
            }
            //System.out.println("\nCOUNT: " + count);

            //Expected out of test data from file name
            if (fp.contains("left")) {
                expectedTest[count1][0] = 1;
                expectedTest[count1][1] = 0;
                expectedTest[count1][2] = 0;
                expectedTest[count1][3] = 0;
            } else if (fp.contains("right")) {
                expectedTest[count1][0] = 0;
                expectedTest[count1][1] = 1;
                expectedTest[count1][2] = 0;
                expectedTest[count1][3] = 0;
            } else if (fp.contains("up")) {
                expectedTest[count1][0] = 0;
                expectedTest[count1][1] = 0;
                expectedTest[count1][2] = 1;
                expectedTest[count1][3] = 0;
            } else if (fp.contains("straight")) {
                expectedTest[count1][0] = 0;
                expectedTest[count1][1] = 0;
                expectedTest[count1][2] = 0;
                expectedTest[count1][3] = 1;
            }

            count1++;
        }


        int[] NumberOfNodes = new int[3];
        double LearnRate = 0.3;
        double Moment = 0.3;
        long MaxIter = 1000;
        double MinError = 0.01;
        NumberOfNodes[0] = 960;
        NumberOfNodes[1] = 6;
        NumberOfNodes[2] = 4;


        BackPropagationImpl backPropogator = new BackPropagationImpl(NumberOfNodes, data, expectedOutput, LearnRate, Moment, MinError, MaxIter);
        backPropogator.run();
        double[][] testOutput = new double[139][4];
        String[] poses = new String[4];
        int index = 0;
        double Error = 0, Accuracy;
        poses[0] = "left";
        poses[1] = "right";
        poses[2] = "up";
        poses[3] = "straight";

        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 139; i++) {
            index = 0;
            testOutput[i] = backPropogator.test(testInput[i]);
            double max = 0;
            int maxi = 0;
            for (int j = 0; j < 4; j++) {


                if (testOutput[i][j] > max) {
                    maxi = j;
                    max = testOutput[i][j];
                }
                if (expectedTest[i][j] == 1) {
                    index = j;
                }

            }
            //counting data miss-classified
            if (maxi != index) {
                //System.out.println("test1"+" "+(i+1)+" "+poses[index]);
                Error++;
            }
            //System.out.println(Error);
        }
        //System.out.println(Error);


        //TESTING DATA2-----------------------------------------------------------------------------------------------------
        testPath = test2.getAbsolutePath();
        fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;


        double[][] testInput2 = new double[208][960];
        double[][] expectedTest2 = new double[208][4];


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

                testInput2[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                //System.out.print(testInput[count1][row] + " ");
                row++;
            }
            //System.out.println("\nCOUNT: " + count);

            //Expected out of test data from file name
            if (fp.contains("left")) {
                expectedTest2[count1][0] = 1;
                expectedTest2[count1][1] = 0;
                expectedTest2[count1][2] = 0;
                expectedTest2[count1][3] = 0;
            } else if (fp.contains("right")) {
                expectedTest2[count1][0] = 0;
                expectedTest2[count1][1] = 1;
                expectedTest2[count1][2] = 0;
                expectedTest2[count1][3] = 0;
            } else if (fp.contains("up")) {
                expectedTest2[count1][0] = 0;
                expectedTest2[count1][1] = 0;
                expectedTest2[count1][2] = 1;
                expectedTest2[count1][3] = 0;
            } else if (fp.contains("straight")) {
                expectedTest2[count1][0] = 0;
                expectedTest2[count1][1] = 0;
                expectedTest2[count1][2] = 0;
                expectedTest2[count1][3] = 1;
            }

            count1++;
        }


        backPropogator = new BackPropagationImpl(NumberOfNodes, data, expectedOutput, LearnRate, Moment, MinError, MaxIter);
        backPropogator.run();
        double[][] testOutput2 = new double[208][4];
        poses = new String[4];
        index = 0;
        double Error2 = 0, Accuracy2;
        poses[0] = "left";
        poses[1] = "right";
        poses[2] = "up";
        poses[3] = "straight";

        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 208; i++) {
            index = 0;
            testOutput2[i] = backPropogator.test(testInput2[i]);
            double max = 0;
            int maxi = 0;
            for (int j = 0; j < 4; j++) {


                if (testOutput2[i][j] > max) {
                    maxi = j;
                    max = testOutput2[i][j];
                }
                if (expectedTest2[i][j] == 1) {
                    index = j;
                }

            }
            //counting data miss-classified
            if (maxi != index) {
                //System.out.println("test2"+" "+(i+1)+" "+poses[index]);
                Error2++;
            }
            //System.out.println(Error2);
        }
        //System.out.println(Error);


        double error = backPropogator.get_error();
        System.out.println("Error: " + error);
        double accuracy = 100 - error;
        System.out.println("Accuracy: " + accuracy);

        //System.out.println("t1"+Error);
        Error = Error / 139;
        System.out.println("TestingError1: " + Error);
        Accuracy = 1 - Error;
        System.out.println("TestingAccuracy1: " + Accuracy);


        Error2 = Error2 / 208;
        System.out.println("TestingError2: " + Error2);
        Accuracy2 = 1 - Error2;
        System.out.println("TestingAccuracy2: " + Accuracy2);


    }

}
