package app.model;

public enum Color {
    YELLOW,
    RED;

    public Color opposite() {
        if (this == YELLOW) {
            return RED;
        } else {
            return YELLOW;
        }
    }

    static public String colorString(Color color) {
        if (color == null)
            return "#ffffff";
        return switch (color) {
            case YELLOW -> "#fcd822";
            case RED -> "#c93939";
        };
    }


    @Override
    public String toString() {
        return switch (this) {
            case YELLOW -> "ŻÓŁTY";
            case RED -> "CZERWONY";
        };
    }
}
