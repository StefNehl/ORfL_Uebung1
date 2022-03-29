package uebung1.teilaufgaben;

import org.ejml.simple.SimpleMatrix;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlottingService
{
    public static void plotBarChartForGame(Map<Integer, SimpleMatrix> moves, int boardSize, int jailField, int numbersUntilOutOfJail)
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
                tileArray.add("JB");
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

        for (Integer i : moves.keySet())
        {
            AddSeriesForData(chart, i, moves.get(i), tileArray);
        }

        new SwingWrapper<CategoryChart>(chart).displayChart();
    }

    private static void AddSeriesForData(CategoryChart chart, int numberOfMoves, SimpleMatrix matrix, List<String> tileArray)
    {
        String title = numberOfMoves + " Moves";

        if(numberOfMoves == 1)
            title = numberOfMoves + " Move";

        var firstMoveProbArray = getMovementDataFromStart(matrix);
        chart.addSeries(title,
                tileArray,
                firstMoveProbArray);
    }

    private static List<Double> getMovementDataFromStart(SimpleMatrix matrix)
    {
        var tiles = matrix.numCols();
        var result = new ArrayList<Double>();

        for(int i = 0; i < tiles; i++)
        {
            result.add(matrix.get(0, i));
        }

        return result;
    }
}
