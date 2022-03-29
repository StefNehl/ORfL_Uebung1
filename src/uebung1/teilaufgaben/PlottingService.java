package uebung1.teilaufgaben;

import org.ejml.simple.SimpleMatrix;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlottingService
{
    public static void plotBarChartForGame(Map<String, SimpleMatrix> moves, int boardSize, int jailField, int numbersUntilOutOfJail)
    {
        // Create Chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Game Probability")
                .xAxisTitle("Tile")
                .yAxisTitle("Probability")
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);

        var jailIndex = jailField - 1;
        var lastJailIndex = jailIndex + numbersUntilOutOfJail;
        var jailCount = 1;

        var tileArray = new ArrayList<String>();
        for(int i = 0; i < boardSize; i++)
        {
            if(i == jailIndex)
            {
                tileArray.add("JV");
                continue;
            }
            if(jailCount >= 0 && i > jailIndex && i <= lastJailIndex)
            {
                tileArray.add("J" + jailCount);
                jailCount++;
                continue;
            }

            if(i == 0)
            {
                tileArray.add("S");
                continue;
            }

            tileArray.add(i + 1 + "");
        }

        for (String i : moves.keySet())
        {
            AddSeriesForData(chart, i, moves.get(i), tileArray);
        }

        new SwingWrapper<CategoryChart>(chart).displayChart();
    }

    private static void AddSeriesForData(CategoryChart chart, String nameOfMovement, SimpleMatrix matrix, List<String> tileArray)
    {
        var firstMoveProbArray = getMovementDataFromStart(matrix);
        chart.addSeries(nameOfMovement,
                tileArray,
                firstMoveProbArray);
    }

    private static List<Double> getMovementDataFromStart(SimpleMatrix matrix)
    {
        var tiles = matrix.numCols();
        var result = new ArrayList<Double>();

        //plot PI Matrix
        if(matrix.numCols() == 1)
        {
            tiles = matrix.numRows();
            for(int i = 0; i < tiles; i++)
            {
                result.add(matrix.get(i, 0));
            }
            return result;
        }

        for(int i = 0; i < tiles; i++)
        {
            result.add(matrix.get(0, i));
        }

        return result;
    }
}
