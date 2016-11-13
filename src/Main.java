import fields.Field;

public class Main {
    public static void main(String[] args) {
        Field a = new Field("1101100");
        Field b = new Field("101");
        System.out.printf("%-20s %-20s\n", "a:",              a);
        System.out.printf("%-20s %-20s\n", "b:",              b);
        System.out.printf("%-20s %-20s\n", "empty set:",      new Field(""));
        System.out.printf("%-20s %-20s\n", "ascending:",      new Field(Field.Direction.ASC, "0011011"));
        System.out.printf("%-20s %-20s\n", "descending:",     new Field(Field.Direction.DESC, "1101100"));
        System.out.printf("%-20s %-20s\n", "with brackets:",  new Field("[1,1 ,0,1, 1 0, 0]"));
    }
}
