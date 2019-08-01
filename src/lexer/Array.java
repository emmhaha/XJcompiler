package lexer;

public class Array extends Type {
    public Type valueType;
    public int size = 0;

    public Array(int size, Type type) {
        super("[]", Tag.INDEX, size * type.width);
        this.size = size;
        this.valueType = type;
    }
}
