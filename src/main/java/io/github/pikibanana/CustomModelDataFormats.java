package io.github.pikibanana;

public enum CustomModelDataFormats {

    UNDERSCORES {
        @Override
        public String format(int number) {
            return formatWithUnderscores(number);
        }
    },
    PLAIN {
        @Override
        public String format(int number) {
            return Integer.toString(number);
        }
    };

    private static String formatWithUnderscores(int number) {
        String numberStr = Integer.toString(number);
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

    public abstract String format(int number);
}
