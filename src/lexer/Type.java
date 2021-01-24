package lexer;

import java.util.Objects;

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

    Type(Type type) {
        super(type.value, type.tag);
        this.width = type.width;
    }

    public static boolean isNumeric(Type type) {
        return Objects.equals(type.value, Type.Float.value) || Objects.equals(type.value, Type.Int.value) || Objects.equals(type.value, Type.Char.value);
    }

    public static boolean isInteger(Type type) {
        return Objects.equals(type.value, Type.Int.value) || Objects.equals(type.value, Type.Char.value);
    }

    public static Type max(Type type1, Type type2) {
        if (!isNumeric(type1) || !isNumeric(type2)) return null;
        else if (Objects.equals(type1.value, Type.Float.value) || Objects.equals(type2.value, Type.Float.value)) return Type.Float;
        else if (Objects.equals(type1.value, Type.Int.value) || Objects.equals(type2.value, Type.Int.value)) return Type.Int;
        else return Type.Char;
    }

}
