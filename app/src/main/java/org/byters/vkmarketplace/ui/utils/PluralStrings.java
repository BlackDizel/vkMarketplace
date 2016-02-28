package org.byters.vkmarketplace.ui.utils;

public class PluralStrings {

    public static final int PLURAL_ONE = 0;
    public static final int PLURAL_FEW = 1;
    public static final int PLURAL_MANY = 2;

    public static int getPluralGroup(final int num) {
        final int last = num % 10;
        final int lasttwo = num % 100;

        if (last == 1) {
            if (lasttwo == 11)
                return PLURAL_MANY;
            else
                return PLURAL_ONE;
        } else if (last >= 2 && last <= 4) {
            if (lasttwo >= 12 && lasttwo <= 14)
                return PLURAL_MANY;
            else
                return PLURAL_FEW;
        }

        return PLURAL_MANY;
    }

    public static String getStringForm(int value, String one, String few, String many) {
        switch (getPluralGroup(value)) {
            case PLURAL_ONE:
                return one;
            case PLURAL_FEW:
                return few;
            default:
                return many;
        }
    }

    public static String combineString(int value, String one, String few, String many) {
        switch (getPluralGroup(value)) {
            case PLURAL_ONE:
                return String.valueOf(value) + " " + one;
            case PLURAL_FEW:
                return String.valueOf(value) + " " + few;
            default:
                return String.valueOf(value) + " " + many;
        }
    }

}