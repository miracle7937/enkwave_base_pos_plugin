package enums;

public enum TransactionRoute {
    NIBSS("nibss", 1),
    INTERSWITCH("interswitch", 2),
    ALL("ALL", 3);

    private final String routeName;
    private final int routeCode;

    TransactionRoute(String routeName, int routeCode) {
        this.routeName = routeName;
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public int getRouteCode() {
        return routeCode;
    }
}
