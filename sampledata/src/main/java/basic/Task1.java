package basic;

import java.util.Arrays;
import java.util.Random;

/**
 * @author： fulisha
 * @date： 2023/4/4 14:41
 * @description：学生的成绩存放于一个矩阵，其中行表示学生，列表示科目。如：第 0 行表示第 0 个学生的数学、语文、英语成绩。
 * 要求：进行学生成绩的随机生成, 区间为 [50, 100].找出成绩最好、最差的同学。但有挂科的同学不参加评比.
 */
public class Task1 {
    public static void main(String[] args) {
        task1();
    }

    public static void task1(){
        //step1:Generate the data with n students and m courses.
        int n = 10;
        int m = 3;
        int lowerBound = 50;
        int upperBound = 100;
        int threshold = 60;

        // Here we have to use an object to generate random numbers.
        Random tempRandom = new Random();
        int[][] data = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] = lowerBound + tempRandom.nextInt(upperBound - lowerBound);
            }
        }

        System.out.println("The data is:\r\n" + Arrays.deepToString(data));

        // Step 2. Compute the total score of each student.
        int[] totalScores = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (data[i][j] < threshold) {
                    totalScores[i] = 0;
                    break;
                }

                totalScores[i] += data[i][j];
            }
        }

        System.out.println("The total scores are:\r\n" + Arrays.toString(totalScores));

        // Step 3. Find the best and worst student.
        // Typical initialization for index: invalid value.
        int tempBestIndex = -1;
        int tempWorstIndex = -1;
        // Typical initialization for best and worst values.
        // They must be replaced by valid values.
        int tempBestScore = 0;
        int tempWorstScore = m * upperBound + 1;
        for (int i = 0; i < n; i++) {
            // Do not consider failed students.
            if (totalScores[i] == 0) {
                continue;
            }

            if (tempBestScore < totalScores[i]) {
                tempBestScore = totalScores[i];
                tempBestIndex = i;
            }

            // Attention: This if statement cannot be combined with the last one using "else if", because a student can be both the best and the
            // worst. I found this bug while setting upperBound = 65.
            if (tempWorstScore > totalScores[i]) {
                tempWorstScore = totalScores[i];
                tempWorstIndex = i;
            }
        }

        // Step 4. Output the student number and score.
        if (tempBestIndex == -1) {
            System.out.println("Cannot find best student. All students have failed.");
        } else {
            System.out.println("The best student is No." + tempBestIndex + " with scores: "
                    + Arrays.toString(data[tempBestIndex]));
        }

        if (tempWorstIndex == -1) {
            System.out.println("Cannot find worst student. All students have failed.");
        } else {
            System.out.println("The worst student is No." + tempWorstIndex + " with scores: "
                    + Arrays.toString(data[tempWorstIndex]));
        }
    }

    public static int[][] getStudentScore (int[][] studentScore) {
        // tempResult：一行代表学生具体成绩索引和总成绩
        int[][] tempResult = new int[2][2];
        int standard = 60;
        int tempMaxIndex = -1; //记录学生索引
        int tempMinIndex = -1;
        int tempMaxScore = -1; //学生最好成绩
        int tempMinScore = 900; //学生最差成绩


        for (int i = 0; i < studentScore.length; i++) {
            if (studentScore[i][0] < standard || studentScore[i][1] < standard || studentScore[i][2] < standard) {
                continue;
            }
            int totalScore = studentScore[i][0] + studentScore[i][1] + studentScore[i][2];
            if (totalScore < tempMinScore) {
                tempMinScore = totalScore;
                tempMinIndex = i;
            }
            if (totalScore > tempMaxScore) {
                tempMaxScore = totalScore;
                tempMaxIndex = i;
            }
        }

        tempResult[0][0] = tempMaxIndex;
        tempResult[0][1] = tempMaxScore;
        tempResult[1][0] = tempMinIndex;
        tempResult[1][1] = tempMinScore;
        return tempResult;
    }
}
