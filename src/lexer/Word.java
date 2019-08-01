package lexer;

public class Word extends Token {

    public String value;
    public static final Word
            and     = new Word("&&",    Tag.AND    ),
            or      = new Word("||",    Tag.OR     ),
            equal   = new Word("==",    Tag.EQUAL  ),
            unequal = new Word("!=",    Tag.UNEQUAL),
            le      = new Word("<=",    Tag.LE     ),
            ge      = new Word(">=",    Tag.GE     ),
            minus   = new Word("minus", Tag.MINUS  ),
            True    = new Word("true",  Tag.TRUE   ),
            False   = new Word("false", Tag.FALSE  ),
            temp    = new Word("temp",  Tag.TEMP   );

    Word(String value, String tag) {
        super(tag);
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public void show(String end) {
        if (end == null) end = "";
        System.out.print(value + end);
    }
}
