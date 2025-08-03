package io.github.pikibanana;

public enum CustomModelDataFormats {

    UNDERSCORES {
        @Override
        public String format(float number) {
            return formatWithUnderscores(number);
        }
    },
    PLAIN {
        @Override
        public String format(float number) {
            return Float.toString(number);
        }
    };

    private static String formatWithUnderscores(float number) {
        String numberStr = Float.toString(number);
        StringBuilder formatted = new StringBuilder();
        int length = numberStr.length();
        for (int i = 0; i < length; i++) {
            formatted.append(numberStr.charAt(i));
            if ((length - i - 1) % 3 == 0 && i != length - 1) {
                formatted.append('_');
            }
        }
        return formatted.toString();
    }

    public abstract String format(float number);
}
