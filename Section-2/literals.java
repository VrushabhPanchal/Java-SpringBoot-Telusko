public class literals {
    public static void main(String[] args) {
        // Literals

        int num1 = 10;
        int num2 = 0b1010; // Binary literal
        int num3 = 0x1A; // Hexadecimal literal
        int num4 = 10_00_00_0000;

        double d = 1e13;

        char c = 'a';
        c++;

        System.out.println(c  + " <========== Character after increment");
        System.out.println(num1 + " <========== Integer");
        System.out.println(num2 + " <========== Integer with binary literal");
        System.out.println(num3 + " <========== Integer with hexadecimal literal");
        System.out.println(num4);
        System.out.println(d);
    }

}
