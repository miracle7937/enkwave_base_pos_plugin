package enums;

public enum IsoAccountType {
    DEFAULT_UNSPECIFIED("00"),
    SAVINGS("10"),
    CURRENT("20"),
    CREDIT("30"),
    UNIVERSAL_ACCOUNT("40"),
    INVESTMENT_ACCOUNT("50"),
    BONUS_ACCOUNT("91");

    private final String code;

    IsoAccountType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name().replace('_', ' ').toLowerCase().toUpperCase();
    }

    public static IsoAccountType parseStringAccountType(String acctType) {
        switch (acctType.toLowerCase()) {
            case "savings":
            case "saving":
                return IsoAccountType.SAVINGS;
            case "current":
                return IsoAccountType.CURRENT;
            case "credit":
                return IsoAccountType.CREDIT;
            case "universal":
                return IsoAccountType.UNIVERSAL_ACCOUNT;
            case "investment":
                return IsoAccountType.INVESTMENT_ACCOUNT;
            case "bonus":
                return IsoAccountType.BONUS_ACCOUNT;
            default:
                return IsoAccountType.DEFAULT_UNSPECIFIED;
        }
    }

    public static IsoAccountType parseIntAccountType(int type) {
        switch (type) {
            case 1:
            case 10:
                return IsoAccountType.SAVINGS;
            case 2:
            case 20:
                return IsoAccountType.CURRENT;
            case 3:
            case 30:
                return IsoAccountType.CREDIT;
            case 4:
            case 40:
                return IsoAccountType.UNIVERSAL_ACCOUNT;
            case 5:
            case 50:
                return IsoAccountType.INVESTMENT_ACCOUNT;
            case 9:
            case 91:
                return IsoAccountType.BONUS_ACCOUNT;
            default:
                return IsoAccountType.DEFAULT_UNSPECIFIED;
        }
    }
}
