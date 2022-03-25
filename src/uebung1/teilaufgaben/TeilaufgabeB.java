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
        int turnsToGetOutOfJail = 3;
        int numberOfJailFields = turnsToGetOutOfJail - 1; //one field is already included

        var probService = new ProbabilityService();
        var probabilityMatrix = new SimpleMatrix(BOARD_SIZE + numberOfJailFields, BOARD_SIZE + numberOfJailFields);

        for(int r = 0; r < probabilityMatrix.numCols(); r++)
        {
            if(r > jailField && r <= jailField + numberOfJailFields)
                continue;

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
        //if tile 23 => back to 11
        //For tile 23, 31 (-1 for index)
        for(int i = 0; i < probabilityMatrix.numCols(); i++)
        {
            if(i == jailField)
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
        //For tile 11 (-1 for index)
        for(int r = jailField; r <= jailField + numberOfJailFields; r++)
        {
            int columnToStart = jailField + 1;
            if(r == numberOfJailFields + jailField)
            {
                for(int c = columnToStart; c < columnToStart + MAX_DICE_NUMBER; c++)
                {
                    var diceNumber = c - 10 ;
                    var numberProb = probService.getDiceProbabilityForNumber(diceNumber);

                    probabilityMatrix.set(r, c, numberProb);
                }
                continue;
            }

            double sumOfProbabilityToGetOutOfJail = 0;
            for(int c = columnToStart; c < columnToStart + MAX_DICE_NUMBER; c++)
            {
                var diceNumber = c - 10 ;
                var numberProb = probService.getDiceProbabilityForNumber(diceNumber) *
                        probService.getPaschProbability();

                sumOfProbabilityToGetOutOfJail += numberProb;
                probabilityMatrix.set(r, c, numberProb);
            }

            probabilityMatrix.set(r, jailField, 1 - sumOfProbabilityToGetOutOfJail);
        }


        //Stay in jail



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

        PlottingService.plotBarChartForGame(movements, BOARD_SIZE + numberOfJailFields);


    }
}
