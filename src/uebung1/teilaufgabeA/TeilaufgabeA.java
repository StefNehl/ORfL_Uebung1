package uebung1.teilaufgabeA;

import org.ejml.simple.SimpleMatrix;

public class TeilaufgabeA
{
    public static void main(String[] args)
    {
        testProbService();
        final int NUMBER_OF_MOVES = 30;
        final int MAX_DICE_NUMBER = 12;
        final int BOARD_SIZE = 20;

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
        checkMarkovMatrix(probabilityMatrix);

        var result = probabilityMatrix.copy();

        for (int i = 0; i < NUMBER_OF_MOVES ; i++)
        {
            result = result.mult(probabilityMatrix);
        }

        System.out.println(result);
        checkMarkovMatrix(result);

        PlottingService.plotBarChartForGame(probabilityMatrix, result, NUMBER_OF_MOVES);


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

    public static void checkMarkovMatrix(SimpleMatrix matrix)
    {
        for(int r = 0; r < matrix.numRows(); r++)
        {
            var columnSum = 0.0;
            for(int c = 0; c < matrix.numCols(); c++)
            {
                columnSum += matrix.get(r, c);
            }
            double roundedSum = (double) Math.round(columnSum * 100.0)/100.0;
            if(roundedSum != 1.0)
                System.out.println("Error in r: " + r + " Sum was: " + roundedSum);
        }
    }

}
