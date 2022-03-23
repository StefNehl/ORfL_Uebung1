package uebung1.teilaufgabeA;

import org.ejml.dense.fixed.CommonOps_DDF5;
import org.ejml.simple.SimpleMatrix;

public class TeilaufgabeA
{
    public static void main(String[] args)
    {
        testProbService();
        final int NUMBER_OF_ITERATIONS = 30;
        final double WIN_PROBABILITY = 0.1;

        var probService = new ProbabilityService();

        var probabilityMatrix = new SimpleMatrix(10, 10);

        for(int c = 0; c < probabilityMatrix.numCols(); c++)
        {
            for(int r = 0; r < probabilityMatrix.numRows(); r++)
            {
                if(r <= c)
                {

                }
                else
                {
                    var diceNumber = r - c;
                    var numberProb = probService.getDiceProbabilityForNumber(diceNumber);
                    probabilityMatrix.set(r, c, numberProb);
                }
            }
        }

        System.out.println(probabilityMatrix);
        checkMarkovMatrix(probabilityMatrix);

        var result = probabilityMatrix.copy();

        for (int i = 0; i < 30 ; i++)
        {
            result = result.mult(probabilityMatrix);
        }

        System.out.println(result);
    }

    public static void testProbService()
    {
        var service = new ProbabilityService();
        var probArray = service.getDiceProbabilityArray();

        System.out.println("Prob Array");
        System.out.print("[ ");
        for(int i = 0; i < probArray.length; i++)
        {
            System.out.print(probArray[i] + ", ");
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
            if(columnSum != 1.0)
                System.out.println("Error in r: " + r + " Sum was: " + columnSum);
        }
    }

}
