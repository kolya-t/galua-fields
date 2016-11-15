package eninja.com.galua.fields;

import java.util.BitSet;

/**
 * Класс простого поля. Поддерживает операции сложения,
 * умножения и деления полей.
 * Задавать поля можно с помощью строки, состоящей из нулей и единиц
 * (можно записывать без пробелов, с пробелами, с запятыми и т.д., как удобно,
 * конструктор удалит лишние символы, кроме нулей и единиц).
 * Также поддерживается запись как от младших разрядов к старшим, так и
 * наоборот, но стандартно принимается запись от "старших к младшим".
 */
public class Field {
    /**
     * Определяет, каким образом выводятся полиномы:
     * {@code true} - в полиномиальном виде
     * {@code false} - в бинарном виде
     */
    public static boolean POLYNOMIAL_TO_STRING = true;

    private static final int P = 2;
    private static final int M = 1;

    // элементы поля записываются по убыванию (desc)
    // (от старших разрядов к младшим)
    private BitSet items;

    // направление задания простого поля из двоичного вида
    public enum Direction {
        ASC, // по возрастанию
        DESC // по убыванию
    }

    private static final String regex = "[^01]";

    /**
     * Конструктр по умолчанию, создает полином с нулевым значением
     */
    public Field() {
        this.items = new BitSet();
    }

    /**
     * @param items строка с положением битов "от старших к младшим"
     */
    public Field(String items) {
        this(Direction.DESC, items);
    }

    /**
     * @param direction DESC - от старших к младшим битам,
     *                  ASC - от младших к старшим
     * @param items
     */
    public Field(Direction direction, String items) {
        // удаляем лишние символы из входящей строки
        String string = items.replaceAll(regex, "");

        this.items = new BitSet(string.length());

        // если подана строка в виде "от старших битов к младшим"
        if (direction == Direction.DESC) {
            string = new StringBuilder(string).reverse().toString();
        }

        // перевод строки в BitSet
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '1') {
                this.items.set(i);
            }
        }
    }

    /**
     * Приватный конструктор на основе BitSet
     *
     * @param set сет, на основе которого нужно создать полином
     */
    private Field(BitSet set) {
        items = set;
    }

    /**
     * Операция сложения двух полиномов
     *
     * @param field полином, с которым нужно сложить исходный полином
     * @return результат сложения полиномов
     */
    public Field add(Field field) {
        BitSet bits = (BitSet) items.clone();
        bits.xor(field.items);
        return new Field(bits);
    }

    /**
     * Операция умножения двух полиномов
     *
     * @param field полином, с которым нужно перемножить исходный полином
     * @return результат умножения полиномов
     */
    public Field mul(Field field) {
        BitSet resultSet = new BitSet();

        int setBitIdx = field.items.nextSetBit(0);
        while (setBitIdx != -1) {
            resultSet.xor(shl(setBitIdx).items);
            setBitIdx = field.items.nextSetBit(setBitIdx + 1);
        }

        return new Field(resultSet);
    }

    /**
     * Деление полиномов в двоичном поле
     *
     * @param divider делитель
     * @return массив из двухэлементов: частное от деления полиномов
     * и остаток от деления.
     */
    public Field[] div(Field divider) {
        Field dividend = new Field(items); // делимое
        Field result = new Field();
        Field currentResult;

        while (dividend.compareTo(divider) != -1) {
            currentResult = new Field("1").shl(dividend.length() - divider.length());
            result = result.add(currentResult);
            dividend = dividend.add(currentResult.mul(divider));
        }
        return new Field[]{result, dividend};
    }

    /**
     * Сравнивает численные значения полиномов (toInt())
     *
     * @param anotherField полином, с которым нужно сравнить исходный полином
     * @return значение 0 если x == y,
     * значение больше нуля, если x > y
     * и значение меньше нуля если x < y.
     */
    public int compareTo(Field anotherField) {
        return compare(this, anotherField);
    }

    /**
     * Сравнивает численные значения полиномов (toInt())
     *
     * @param x первый полином
     * @param y второй полином
     * @return значение 0 если x == y,
     * значение больше нуля, если x > y
     * и значение меньше нуля если x < y.
     */
    public static int compare(Field x, Field y) {
        return compare(x.items, y.items);
    }

    private static int compare(BitSet x, BitSet y) {
        return x.equals(y) ? 0 : Integer.compare(new Field(x).toInt(), new Field(y).toInt());
    }

    /**
     * Нециклический сдвиг влево. Операция не изменяет исходный полином.
     * Если bitCount отрицательна, то сдвиг происходит на -bitCount в другую
     * сторону.
     *
     * @param bitCount количество бит, на сколько нужно сдивнуть исходную
     *                 последовательность битов
     * @return полином, последовательность бит которого сдвинута влево на
     * bitCount битов
     */
    public Field shl(int bitCount) {
        if (bitCount == 0) {
            return this;
        }
        if (bitCount < 0) {
            return shr(-bitCount);
        }

        BitSet bitSet = new BitSet(length() + bitCount);
        for (int i = 0; i < length(); i++) {
            if (items.get(i)) {
                bitSet.set(i + bitCount);
            }
        }
        return new Field(bitSet);
    }

    /**
     * Нециклический сдвиг влево. Операция не изменяет исходный полином.
     * Если bitCount отрицательна, то сдвиг происходит на -bitCount в другую
     * сторону.
     *
     * @param bitCount количество бит, на сколько нужно сдивнуть исходную
     *                 последовательность битов
     * @return полином, последовательность бит которого сдвинута вправо на
     * bitCount битов
     */
    public Field shr(int bitCount) {
        if (bitCount == 0) {
            return this;
        }
        if (bitCount < 0) {
            return shl(-bitCount);
        }

        BitSet bitSet = items.get(bitCount, length());
        return new Field(bitSet);
    }

    /**
     * @return количество используемых бит
     */
    public int length() {
        return items.length();
    }

    /**
     * @return строка вида [1 0 0 1 1]
     */
    public String toBinaryString() {
        if (items == null)
            return "null";

        int iMax = length() - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(items.get(iMax - i) ? '1' : '0');
            if (i == iMax)
                return b.append(']').toString();
            b.append(' '); // b.append(", ");
        }
    }

    /**
     * @return строка вида x^3 + x + 1
     */
    public String toPolynomialString() {
        if (items == null)
            return "null";

        int iMax = length() - 1;
        if (iMax == -1)
            return "0";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            // если встретилась единица
            if (items.get(iMax - i)) {
                switch (iMax - i) {
                    // если одночлен x^0
                    case 0:
                        b.append('1');
                        break;
                    // если одночлен x^1
                    case 1:
                        b.append('x');
                        break;
                    // если степень одночлена > 1
                    default:
                        b.append("x^").append(iMax - i);
                        break;
                }

                if (i != iMax) {
                    b.append(" + ");
                }
            }
            // если это последний одночлен этого полинома
            if (i == iMax) {
                String string = b.toString();
                if (string.endsWith(" + ")) {
                    string = string.substring(0, string.length() - 3);
                }
                return string;
            }
        }
    }

    @Override
    public String toString() {
        return POLYNOMIAL_TO_STRING ? toPolynomialString() : toBinaryString();
    }

    /**
     * Статический метод перевода полинома в десятичное число
     *
     * @param field исходный полином
     * @return полином в десятичном виде
     */
    public static int toInt(Field field) {
        String s = field.toBinaryString().replaceAll(regex, "");
        return s.equals("") ? 0 : Integer.parseInt(s, 2);
    }

    /**
     * Статический метод перевода полинома в десятичное число
     *
     * @param field исходный полином в виде строки (в свободном стиле)
     * @return полином в десятичном виде
     */
    public static int toInt(String field) {
        return toInt(new Field(field));
    }

    /**
     * Метод перевода полинома в десятичное число
     *
     * @return полином в десятичном виде
     */
    public int toInt() {
        return toInt(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        return items != null ? items.equals(field.items) : field.items == null;

    }

    @Override
    public int hashCode() {
        return items != null ? items.hashCode() : 0;
    }
}
