package uebung1.teilaufgaben;

import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;

public class TeilaufgabeA
{
    public static void main(String[] args)
    {
        testProbService();
        final int MAX_DICE_NUMBER = 12;
        final int BOARD_SIZE = 40;

        var probService = new ProbabilityService();

        var probabilityMatrix = new SimpleMatrix(BOARD_SIZE, BOARD_SIZE);

        for(int r = 0; r < probabilityMatrix.numCols(); r++)
        {
            int columnToStart = r + 1;

            for(int c = columnToStart; c < columnToStart + MAX_DICE_NUMBER; c++)
            {
                var diceNumber = c - r ;
                var numberProb = probService.getDiceProbabilityForNumber(diceNumber);

                if(c >= BOARD_SIZE)
                {
                    probabilityMatrix.set(r , c- BOARD_SIZE, numberProb);
                    continue;
                }

                probabilityMatrix.set(r, c, numberProb);
            }
        }

        System.out.println(probabilityMatrix);
        MatrixTester.checkMarkovMatrix(probabilityMatrix);

        var movements = new HashMap<String, SimpleMatrix>();
        movements.put("1 Move", probabilityMatrix);

        var copyOfProbMatrix = probabilityMatrix.copy();

        int maxIterations = 1000;

        for (int i = 0; i < maxIterations; i++)
        {
            copyOfProbMatrix = probabilityMatrix.mult(copyOfProbMatrix);
            MatrixTester.checkMarkovMatrix(copyOfProbMatrix);

            if(i == 1 || i == 2 || i == 3 || i == maxIterations-1)
                movements.put((i + 1) + " Moves", copyOfProbMatrix);
        }

        double maxValue = Double.MIN_VALUE;
        int maxValueIndex = -1;
        double minValue = Double.MAX_VALUE;
        int minValueIndex = -1;

        for(int i = 0; i < copyOfProbMatrix.numCols(); i++)
        {
            if(i == 0)
                continue;

            double matrixValue = copyOfProbMatrix.get(0, i);

            if(matrixValue > maxValue)
            {
                maxValue = matrixValue;
                maxValueIndex = i;
            }

            if(matrixValue < minValue)
            {
                minValue = matrixValue;
                minValueIndex = i;
            }
        }

        int maxValueField = maxValueIndex + 1;
        int minValueField = minValueIndex + 1;

        //Plot results
        System.out.println("Field Probability: ");
        for(int i = 0; i < copyOfProbMatrix.numCols(); i++)
        {
            System.out.println("Field " + (i + 1) + ": " + copyOfProbMatrix.get(i, 0));
        }
        System.out.println();
        System.out.println("Highest Probability (not jail or start): ");
        var valueString = String.format(" Prob: %.4f", (maxValue * 100));
        System.out.println("Field: " + maxValueField + valueString + "%");


        System.out.println();
        System.out.println("Lowest Probability (not jail or start): ");
        valueString = String.format(" Prob: %.4f", (minValue * 100));
        System.out.println("Field: " + minValueField + valueString + "%");

        movements.get(maxIterations);
        PlottingService.plotBarChartForGame(movements, BOARD_SIZE, -1, 0);


    }

    public static void testProbService()
    {
        var service = new ProbabilityService();
        var probArray = service.getDiceProbabilityArray();

        System.out.println("Prob Array");
        System.out.print("[ ");
        for (double v : probArray) {
            System.out.print(v + ", ");
        }
        System.out.println(" ]");

        System.out.println(service.getDiceProbabilityForNumber(7));
        System.out.println(service.getDiceProbabilityForNumber(1));
        System.out.println(service.getDiceProbabilityForNumber(13));
    }

}
