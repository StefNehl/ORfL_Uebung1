package uebung1.monopoly;

import java.util.Arrays;

/**
 * Defines the Board in an int array
 * 0 is the start field
 * index is from 0 - (numbersOfFields - 1)
 */
public class Board
{
    private Field[] _board;

    public Board(int numberOfFields, int numberForJailField,
                 int[] numbersForGoToStartFields, int[] numbersForGoToJailFields) throws Exception
    {
        _board = new Field[numberOfFields];

        for(int i = 0; i < _board.length; i++)
            _board[i] = new Field(i + 1, FieldType.Normal);

        _board[0] = new Field(1, FieldType.Start);

        var indexForJailField = numberForJailField - 1;
        if(indexForJailField >= numberOfFields)
            throw new Exception("Incorrect Number for Jail Field");

        _board[indexForJailField] = new Field(numberForJailField, FieldType.Jail);




        for(int i = 0; i < numbersForGoToJailFields.length; i++)
        {
            var indexForGoToJail = numbersForGoToJailFields[i] - 1;
            if(indexForGoToJail >= numberOfFields)
                throw new Exception("Incorrect Number for GoTo Jail Field");

            _board[indexForGoToJail] = new Field(indexForGoToJail + 1, FieldType.GoToJail);
        }

        for(int i = 0; i < numbersForGoToStartFields.length; i++)
        {
            var indexForGoToStart = numbersForGoToStartFields[i] - 1;
            if(indexForGoToStart >= numberOfFields)
                throw new Exception("Incorrect Number for GoTo Start Field");

            _board[indexForGoToStart] = new Field(indexForGoToStart + 1, FieldType.GoToStart);
        }
    }

    public Field[] getBoard()
    {
        return _board;
    }

    @Override
    public String toString()
    {
        return Arrays.toString(_board);
    }
}
