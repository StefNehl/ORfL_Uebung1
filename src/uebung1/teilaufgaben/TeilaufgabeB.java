package uebung1.teilaufgaben;

import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;

public class TeilaufgabeB
{
    public static void main(String[] args)
    {
        final int MAX_DICE_NUMBER = 12;
        final int BOARD_SIZE = 40;

        int startField = 0;

        int jailField = 10;
        int inJailFieldStart = jailField + 1;
        int turnsToGetOutOfJail = 3;
        int inJailFieldEnd = inJailFieldStart + turnsToGetOutOfJail - 1;
        int numberOfJailFields = turnsToGetOutOfJail;

        int numberOfStates = BOARD_SIZE + numberOfJailFields;

        var probService = new ProbabilityService();
        var probabilityMatrix = new SimpleMatrix(numberOfStates, numberOfStates);

        for(int r = 0; r < probabilityMatrix.numRows(); r++)
        {
            if(r >= inJailFieldStart && r < inJailFieldEnd )
                continue;

            int columnToStart = r + 1;
            for(int c = columnToStart; c < columnToStart + MAX_DICE_NUMBER; c++)
            {
                var diceNumber = c - r ;
                var numberProb = probService.getDiceProbabilityForNumber(diceNumber);

                if(c >= numberOfStates)
                {
                    probabilityMatrix.set(r , c - numberOfStates, numberProb);
                    continue;
                }

                probabilityMatrix.set(r, c, numberProb);
            }
        }

        //Add BackToStart
        //If tile 8 => back to 1
        //For tile 8, 18, 34 (-1 for index)
        for(int i = 0; i < probabilityMatrix.numCols(); i++)
        {
            if(i == startField)
            {
                probabilityMatrix.set(7, i, 1);
                probabilityMatrix.set(17, i, 1);
                probabilityMatrix.set(33, i, 1);
                continue;
            }
            probabilityMatrix.set(7, i, 0);
            probabilityMatrix.set(17, i, 0);
            probabilityMatrix.set(33, i, 0);
        }

        //To Jail
        //if tile 23 => back to 12
        //For tile 23, 31 (-1 for index)
        for(int i = 0; i < probabilityMatrix.numCols(); i++)
        {
            if(i == inJailFieldStart)
            {
                probabilityMatrix.set(22, i, 1);
                probabilityMatrix.set(30, i, 1);
                continue;
            }
            probabilityMatrix.set(22, i, 0);
            probabilityMatrix.set(30, i, 0);
        }

        //Get out of Jail
        //If Pasch => get out of jail
        //For tile 11 = Jail visit
        //For tile 12 - 14 Jail => get out of Jail rounds
        int tries = 0;
        for(int r = inJailFieldStart; r < inJailFieldEnd; r++)
        {
            var columnToStart = inJailFieldStart + tries;
            for(int c = columnToStart; c < inJailFieldEnd + MAX_DICE_NUMBER + tries; c++)
            {
                var diceNumber = c - (inJailFieldStart + tries);
                var numberProb = probService.getPaschProbabilityForNumber(diceNumber);

                probabilityMatrix.set(r, c, numberProb);
            }

            probabilityMatrix.set(r, columnToStart, 1 - probService.getPaschProbability());
            tries++;
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

            if(i == maxIterations-1)
                movements.put((i + 1) + " Moves", copyOfProbMatrix);
        }

        //Calculate Gleichgewichtsverteilung

        var pMatrix = probabilityMatrix.copy();
        var aHomogenous = pMatrix.transpose().minus(SimpleMatrix.identity(pMatrix.numCols()));
        var aMatrix = aHomogenous.copy();

        for (int j = 0; j < aMatrix.numCols(); j++)
        {
            aMatrix.set(aMatrix.numRows() - 1, j, 1);
        }

        var bMatrix = new SimpleMatrix(aMatrix.numRows(), 1);
        bMatrix.set(bMatrix.numRows() - 1, 0, 1);

        SimpleMatrix piMatrix = aMatrix.solve(bMatrix);
        System.out.println(piMatrix);
        System.out.println();

        movements.put("PI", piMatrix);

        //Plot results

        var resultArray = new double[BOARD_SIZE];
        int resultArrayCount = 0;

        System.out.println("Field Probability: ");
        for(int i = 0; i < copyOfProbMatrix.numCols(); i++)
        {
            if(i >= inJailFieldStart && i <= inJailFieldStart + numberOfJailFields - 1)
                continue;

            System.out.println("Field " + (resultArrayCount + 1) + ": " + piMatrix.get(resultArrayCount, 0));
            resultArray[resultArrayCount] = piMatrix.get(resultArrayCount, 0);
            resultArrayCount++;
        }
        System.out.println();
        System.out.println("Highest Probability (not jail or start): ");

        movements.get(maxIterations);
        PlottingService.plotBarChartForGame(movements, numberOfStates, jailField +1, numberOfJailFields);
    }
}
