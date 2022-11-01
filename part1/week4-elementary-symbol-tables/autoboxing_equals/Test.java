public class Test {
    private static void case1() {
        double a = 0.0;
        double b = -0.0;
        Double x = new Double(a);
        Double y = new Double(b);
        assert a == b && !x.equals(y);
    }

    private static void case2() {
        double a = Double.NaN;
        double b = Double.NaN;
        Double x = new Double(a);
        Double y = new Double(b);
        assert a != b && x.equals(y);
    }

    public static void main(String[] args) {
        case1();
        case2();
    }
}
