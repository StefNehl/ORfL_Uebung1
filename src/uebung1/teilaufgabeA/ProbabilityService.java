package uebung1.teilaufgabeA;

import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

public class ProbabilityService
{
    private double[] propArray;

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

    }
    public double[] getDiceProbabilityArray()
    {
        return propArray;
    }

    public double getDiceProbabilityForNumber(int number)
    {
        if(number < 2 || number > 12)
            return 0;

        var index = number - 1;
        return propArray[index];
    }
}
