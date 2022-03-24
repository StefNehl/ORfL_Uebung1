package uebung1.teilaufgabeA;

import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

public class PlottingService
{
    public static void plotBarChartForGame(SimpleMatrix firstMove, SimpleMatrix nMove, int numberOfMoves)
    {
        // Create Chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Game Probability")
                .xAxisTitle("Field")
                .yAxisTitle("Probability")
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);


        var firstMoveTileArray = new double[firstMove.numRows()];
        for(int i = 0; i < firstMoveTileArray.length; i++)
        {
            firstMoveTileArray[i] = i;
        }

        var firstMoveProbArray = getMovementDataFromStart(firstMove);
        chart.addSeries("First Move",
                firstMoveTileArray,
                firstMoveProbArray);

        var nMoveProbArray = getMovementDataFromStart(nMove);
        chart.addSeries(numberOfMoves + " Move",
                firstMoveTileArray,
                nMoveProbArray);

        new SwingWrapper<CategoryChart>(chart).displayChart();
    }

    private static double[] getMovementDataFromStart(SimpleMatrix matrix)
    {
        var matrixData = ((DMatrixRMaj)matrix.getMatrix()).getData();
        var tiles = matrix.numRows();

        var result = new double[tiles];

        for(int i = 0; i < result.length; i++)
        {
            result[i] = matrixData[i];
        }

        return result;
    }
}
