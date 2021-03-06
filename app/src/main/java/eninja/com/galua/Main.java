package eninja.com.galua;

import java.util.Arrays;
import eninja.com.galua.fields.Field;


public class Main {
    public static void main(String[] args) {
        Field a = new Field("1101100");
        Field b = new Field("101");
        System.out.printf("%-20s %-20s\n", "a:",              a);
        System.out.printf("%-20s %-20s\n", "b:",              b);
        System.out.printf("%-20s %-20s\n", "empty set:",      new Field());
        System.out.printf("%-20s %-20s\n", "ascending:",      new Field(Field.Direction.ASC, "0011011"));
        System.out.printf("%-20s %-20s\n", "descending:",     new Field(Field.Direction.DESC, "1101100"));
        System.out.printf("%-20s %-20s\n", "with brackets:",  new Field("[1,1 ,0,1, 1 0, 0]"));

        Field.POLYNOMIAL_TO_STRING = false;

        System.out.printf("%-20s %-20s\n", "a + b:", a.add(b));

        System.out.printf("%-20s %-20s\n", "in decimal:", new Field("[100 0000 0000]").toInt());
        System.out.printf("%-20s %-20s\n", "from string:", Field.toInt("[100 0000 0010]"));
        System.out.printf("%-20s %-20s\n", "shift a left by 3:", a.shl(3));
        System.out.printf("%-20s %-20s\n", "shift a right by 3:", a.shr(3));
        System.out.printf("%-20s %-20s\n", "a * b:", a.mul(b));

        System.out.println(a.compareTo(b) == 1 ? "a > b" : "a <= b");
        System.out.println(Arrays.toString(a.div(b)));
        System.out.println(Arrays.toString(new Field("110101011").div(new Field("1101"))));
    }
}
