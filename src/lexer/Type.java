package lexer;

public class Type extends Word {

    public int width;
    public static final Type
            Char = new Type("char", Tag.BASIC_TYPE, 1),
            Int = new Type("int", Tag.BASIC_TYPE, 4),
            Float = new Type("float", Tag.BASIC_TYPE, 8),
            Bool = new Type("bool", Tag.BASIC_TYPE, 1);

    Type(String value, String tag, int width) {
        super(value, tag);
        this.width = width;
    }

    public static boolean isNumeric(Type type) {
        return type == Type.Float || type == Type.Int || type == Type.Char;
    }

    public static Type max(Type type1, Type type2) {
        if (!isNumeric(type1) || !isNumeric(type2)) return null;
        else if (type1 == Type.Float || type2 == Type.Float) return Type.Float;
        else if (type1 == Type.Int || type2 == Type.Int) return Type.Int;
        else return Type.Char;
    }

}
