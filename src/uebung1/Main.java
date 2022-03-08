package uebung1;

import uebung1.monopoly.Board;
import uebung1.monopoly.Dice;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here

        var firstDice = new Dice(6);
        TestDice(firstDice, 100);

        var secondDice = new Dice(6);
        TestDice(secondDice, 100);

        var board = new Board(40, 11,
                new int[]{8, 18, 34},
                new int[]{23, 31});

        System.out.println(board);

    }

    private static void TestDice(Dice newDice, int numberOfTests)
    {
        for(int i = 0; i<numberOfTests; i++)
        {
            var result = newDice.rollDice();
            if(result > newDice.getSides())
            {
                System.out.println("Test Failed");
                return;
            }
        }

        System.out.println("Test Succeed");
    }
}
