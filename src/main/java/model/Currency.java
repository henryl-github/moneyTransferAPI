package model;

public enum Currency {
    GBP,
    USD,
    EUR;
    public static Currency fromInteger(int x) {
        switch(x) {
            case 0:
                return GBP;
            case 1:
                return USD;
            case 2:
                return EUR;
        }
        return null;
    }
}
