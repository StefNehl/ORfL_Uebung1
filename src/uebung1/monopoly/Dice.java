package uebung1.monopoly;

import java.util.Random;

public class Dice
{

    private Random _random;
    private int _sides;

    public Dice(int sides)
    {
        _sides = sides;
        _random = new Random();
    }

    public int rollDice()
    {
        return (_random.nextInt(_sides)) + 1;
    }

    public int getSides()
    {
        return _sides;
    }
}
