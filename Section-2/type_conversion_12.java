public class type_conversion_12 {
    public static void main(String[] args) {
        
        // byte b = 125;
        /// This is the type conversion
        int a = 12;
        byte k = (byte) a;

        float f = 5.6f;
        // When you specify something that means its explicit conversion
        int t = (int) f; 


        /// This is the type promotion
        
        byte b = 12;
        byte c = 15;
        int result = b * c;

        System.out.println(result);
        System.out.println(a);
        System.out.println(t);
    }
}
