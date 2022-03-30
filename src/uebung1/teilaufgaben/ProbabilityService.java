/*
Nehl Stefan
00935188
 */

package uebung1.teilaufgaben;

import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

public class ProbabilityService
{
    private double[] propArray;
    private double[] paschProbArray;

    public ProbabilityService()
    {
        var faces = 6;
        var numberOfDices = 2;
        var numberOfOutcomes = 36;

        var propMatrix = new SimpleMatrix(faces, faces);

        for(int c = 0; c < propMatrix.numCols(); c++)
        {
            for(int r = 0; r < propMatrix.numRows(); r++)
            {
                var valueToSet = r+1+c+1;
                propMatrix.set(r, c, valueToSet);
            }
        }

        //Added prob array for dice
        var matrixData = ((DMatrixRMaj)propMatrix.getMatrix()).getData();
        propArray = new double[numberOfDices * faces];
        for(int i = 0; i < propArray.length; i++)
        {
            var diceNumber = (double)i + 1;
            var count = 0;

            for(int j = 0; j < matrixData.length; j++)
            {
                if(matrixData[j] == diceNumber)
                    count++;
            }

            propArray[i] = (double) count / numberOfOutcomes;
        }

        //Added prob array for pasch
        paschProbArray = new double[numberOfDices * faces];
        for(int i = 0; i < paschProbArray.length; i++)
        {
            if((i + 1) % 2 == 0)
                paschProbArray[i] = (double) 1 / numberOfOutcomes;
        }
    }
    public double[] getDiceProbabilityArray()
    {
        return propArray;
    }

    public double getDiceProbabilityForNumber(int fieldNumber)
    {
        if(fieldNumber < 2 || fieldNumber > 12)
            return 0;

        var index = fieldNumber - 1;
        return propArray[index];
    }

    public double[] getPaschProbabilityArray()
    {
        return paschProbArray;
    }

    public double getPaschProbabilityForNumber(int fieldNumber)
    {
        if(fieldNumber < 2 || fieldNumber > 12)
            return 0;

        var index = fieldNumber - 1;
        return paschProbArray[index];
    }

    public double getPaschProbability()
    {
        return (1.0/6.0);
    }
}
