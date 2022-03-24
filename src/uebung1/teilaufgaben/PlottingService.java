package uebung1.teilaufgaben;

import org.ejml.simple.SimpleMatrix;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.util.Map;

public class PlottingService
{
    public static void plotBarChartForGame(Map<Integer, SimpleMatrix> moves, int boardSize)
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


        var tileArray = new double[boardSize];
        for(int i = 0; i < tileArray.length; i++)
        {
            tileArray[i] = i;
        }

        for (Integer i : moves.keySet())
        {
            AddSeriesForData(chart, i, moves.get(i), tileArray);
        }

        new SwingWrapper<CategoryChart>(chart).displayChart();
    }

    private static void AddSeriesForData(CategoryChart chart, int numberOfMoves, SimpleMatrix matrix, double[] tileArray)
    {
        String title = numberOfMoves + " Moves";

        if(numberOfMoves == 1)
            title = numberOfMoves + "Move";

        var firstMoveProbArray = getMovementDataFromStart(matrix);
        chart.addSeries(title,
                tileArray,
                firstMoveProbArray);
    }

    private static double[] getMovementDataFromStart(SimpleMatrix matrix)
    {
        var tiles = matrix.numRows();
        var result = new double[tiles];

        for(int i = 0; i < result.length; i++)
        {
            result[i] = matrix.get(i, 0);
        }

        return result;
    }
}
