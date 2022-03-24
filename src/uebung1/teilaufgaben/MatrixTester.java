package uebung1.teilaufgaben;

import org.ejml.simple.SimpleMatrix;

public class MatrixTester
{
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
