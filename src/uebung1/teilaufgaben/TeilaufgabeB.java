package uebung1.teilaufgaben;

import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;

public class TeilaufgabeB
{
    public static void main(String[] args)
    {
        final int MAX_DICE_NUMBER = 12;
        final int BOARD_SIZE = 40;

        int startField = 0; //Index = field - 1
        int jailField = 10;

        int inJailFieldStart = jailField + 1;
        int turnsToGetOutOfJail = 3;
        int numberOfJailFields = turnsToGetOutOfJail;
        int inJailFieldEnd = inJailFieldStart + numberOfJailFields - 1;

        int numberOfStates = BOARD_SIZE + numberOfJailFields;

        var probService = new ProbabilityService();
        var probabilityMatrix = new SimpleMatrix(numberOfStates, numberOfStates);

        for(int r = 0; r < probabilityMatrix.numRows(); r++)
        {
            if(r >= inJailFieldStart && r <= inJailFieldEnd )
                continue;

            int columnToStart = r + 1;
            for(int c = columnToStart; c < columnToStart + MAX_DICE_NUMBER + numberOfJailFields; c++)
            {
                //Jump over Jail Fields
                if(c >= inJailFieldStart && c <= inJailFieldEnd)
                {
                    continue;
                }
                else if(c > inJailFieldEnd)
                {
                    var diceNumber = c - r - numberOfJailFields;
                    var numberProb = probService.getDiceProbabilityForNumber(diceNumber);

                    if(c >= numberOfStates)
                        probabilityMatrix.set(r , c - numberOfStates, numberProb);
                    else
                        probabilityMatrix.set(r, c, numberProb);
                }
                else
                {
                    var diceNumber = c - r;
                    var numberProb = probService.getDiceProbabilityForNumber(diceNumber);

                    if(c >= numberOfStates)
                        probabilityMatrix.set(r , c - numberOfStates, numberProb);
                    else
                        probabilityMatrix.set(r, c, numberProb);
                }
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
        //For index 10 = Jail visit
        //For index 11 - 13 Jail => get out of Jail rounds
        //For index 14 => start after jail
        int tries = 0;
        for(int r = inJailFieldStart; r <= inJailFieldEnd; r++)
        {
            for(int c = inJailFieldEnd; c <= inJailFieldEnd + MAX_DICE_NUMBER; c++)
            {
                var diceNumber = c - inJailFieldEnd;
                var numberProb = probService.getPaschProbabilityForNumber(diceNumber);

                if(r == inJailFieldEnd)
                    numberProb = probService.getDiceProbabilityForNumber(diceNumber);

                probabilityMatrix.set(r, c, numberProb);
            }

            //+1 to get to the next state
            if(r < inJailFieldEnd)
                probabilityMatrix.set(r, inJailFieldStart + tries + 1, 1 - probService.getPaschProbability());
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

            if(i == 1 || i == maxIterations - 1)
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

        double maxValue = Double.MIN_VALUE;
        int maxValueIndex = -1;
        double minValue = Double.MAX_VALUE;
        int minValueIndex = -1;

        for(int i = 0; i < piMatrix.numRows(); i++)
        {
            if(i == 0 || (i >= inJailFieldStart && i <= inJailFieldEnd))
                continue;

            double matrixValue = piMatrix.get(i, 0);

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

        int maxValueField = -1;
        int minValueField = -1;

        if(maxValueIndex < inJailFieldEnd)
            maxValueField = maxValueIndex + 1;
        else
            maxValueField = maxValueIndex + 1 - numberOfJailFields;

        if(minValueIndex < inJailFieldEnd)
            minValueField = minValueIndex + 1;
        else
            minValueField = minValueIndex + 1 - numberOfJailFields;


        //Plot results
        System.out.println("Field Probability: ");
        for(int i = 0; i < copyOfProbMatrix.numCols(); i++)
        {
            System.out.println("Field " + (i + 1) + ": " + piMatrix.get(i, 0));
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
        PlottingService.plotBarChartForGame(movements, numberOfStates, jailField +1, numberOfJailFields);
    }
}
