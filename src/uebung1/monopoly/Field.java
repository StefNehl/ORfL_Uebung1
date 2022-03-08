package uebung1.monopoly;

public class Field
{
    private int _fieldNumber;
    private FieldType _fieldType;

    public Field(int fieldNumber, FieldType fieldType)
    {
        _fieldNumber = fieldNumber;
        _fieldType = fieldType;
    }

    public int getFieldNumber()
    {
        return _fieldNumber;
    }

    public FieldType getFieldType()
    {
        return _fieldType;
    }

    @Override
    public String toString()
    {
        return "FN: " + _fieldNumber + " Type: " + _fieldType;
    }
}
