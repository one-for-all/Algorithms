public class Test {
    private static void case1() {
        double a = 0.0;
        double b = -0.0;
        Double x = new Double(a);
        Double y = new Double(b);
        assert a == b && x != y;
    }

    private static void case2() {
        double a = Double.NaN;
        Double x = new Double(a);
        Double y = x;
        double b = y;
        assert a != b && x == y;
    }

    public static void main(String[] args) {
        case1();
        case2();
    }
}
