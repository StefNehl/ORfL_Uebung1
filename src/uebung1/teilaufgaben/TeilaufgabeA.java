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

        var movements = new HashMap<Integer, SimpleMatrix>();
        movements.put(1, probabilityMatrix);

        var copyOfProbMatrix = probabilityMatrix.copy();

        int maxIterations = 1000;

        for (int i = 0; i < maxIterations; i++)
        {
            copyOfProbMatrix = probabilityMatrix.mult(copyOfProbMatrix);
            MatrixTester.checkMarkovMatrix(copyOfProbMatrix);

            if(i == 1 || i == 2 || i == 3 || i == maxIterations-1)
                movements.put(i + 1, copyOfProbMatrix);
        }

        PlottingService.plotBarChartForGame(movements, BOARD_SIZE);


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
