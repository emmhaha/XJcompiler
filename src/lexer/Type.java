package lexer;

public class Type extends Word {

    public int width;
    public static final Type
            Int = new Type("int", Tag.BASIC_TYPE, 4),
            Float = new Type("float", Tag.BASIC_TYPE, 8),
            Bool = new Type("bool", Tag.BASIC_TYPE, 1);

    Type(String value, String tag, int width) {
        super(value, tag);
        this.width = width;
    }

}
