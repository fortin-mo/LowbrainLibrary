package lowbrain.library;

import org.jetbrains.annotations.Contract;

public enum FunctionType {
    LINEAR,
    CUBIC,
    SQUARE;

    @Contract(pure = true)
    public static FunctionType get(int val) {
        switch (val) {
            case 0:
                return FunctionType.LINEAR;
            default:
            case 1:
                return FunctionType.CUBIC;
            case 2:
                return FunctionType.SQUARE;
        }
    }
}
