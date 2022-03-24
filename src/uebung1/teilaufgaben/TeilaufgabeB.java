package uebung1.teilaufgaben;

import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;

public class TeilaufgabeB
{
    public static void main(String[] args)
    {
        final int MAX_DICE_NUMBER = 12;
        final int BOARD_SIZE = 40;

        var probService = new ProbabilityService();

        var probabilityMatrix = new SimpleMatrix(BOARD_SIZE, BOARD_SIZE);

        for(int c = 0; c < probabilityMatrix.numCols(); c++)
        {
            int rowToStart = c + 1;

            for(int r = rowToStart; r < rowToStart + MAX_DICE_NUMBER; r++)
            {
                var diceNumber = r - c;
                var numberProb = probService.getDiceProbabilityForNumber(diceNumber);

                if(r >= BOARD_SIZE)
                {
                    probabilityMatrix.set(r - BOARD_SIZE, c, numberProb);
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

        int maxIterations = 30;

        for (int i = 0; i < maxIterations; i++)
        {
            copyOfProbMatrix = copyOfProbMatrix.mult(copyOfProbMatrix);
            MatrixTester.checkMarkovMatrix(copyOfProbMatrix);

            if(i == 1 || i == 2 || i == maxIterations-1)
                movements.put(i + 1, copyOfProbMatrix);
        }

        PlottingService.plotBarChartForGame(movements, BOARD_SIZE);


    }
}
