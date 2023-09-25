import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Calculator {
    public static void main(String[] args) throws Exception {
        Scanner reader = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение: ");
        String[] input_line = reader.nextLine().split(" ");
        if (input_line.length!=3){
            throw new Exception("Некорретный ввод строки. Пример корретной: \n 1 + 2 \n VI / III" +
                    "\n Или формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
        // Операция == */-+
        String resolve_operations = "+-*/";
        String op = input_line[1];
        if (!resolve_operations.contains(op)) {
            throw new Exception("\nВведен неверный тип операции. \n Разрешенные операции + - * /");
        }
        // Проверка на ввод одной строки?
        // ?
        // Введенные числа одного типа? (арабские или римские)
        int num1 = 0;
        int num2 = 0;
        boolean isRomanNumbers = false;
        // пробуем считать арабские цифры
        try {
            num1 = Integer.parseInt(input_line[0]);
            num2 = Integer.parseInt(input_line[2]);
        } catch (Exception e1) {
            try {// Пробуем считать римские цифры
                num1 = romanToArabic(input_line[0]);
                num2 = romanToArabic(input_line[2]);
                isRomanNumbers = true;
            } catch (Exception e2) {
                throw new Exception("Необходимо вводить либо два арабских либо два римских числа");
            }
        }

        // Введенные числа больше или равны 1 и меньше либо равны 10
        if (num1 < 1 || num1 > 10 || num2 < 1 || num2 > 10) {
            throw new Exception("Введенные числа выходят за разрешенный диапазон от 1 до 10");
        }
        // Введенные числа целые
        if (num1 % 1 != 0 || num2 % 1 != 0) {
            throw new Exception("Введенные числа должны быть целые");
        }
        int result = 0;
        switch (op) {
            case ("+"):
                result = num1 + num2;
                break;
            case ("-"):
                result = num1 - num2;
                break;
            case ("*"):
                result = num1 * num2;
                break;
            case ("/"):
                if (num2 == 0) {
                    throw new Exception("На ноль делить нельзя");
                }
                // Отбрасываем дробную часть при делении
                result = (int) (num1 / num2);
                break;
        }

        // Печатаем результат
        if (isRomanNumbers) {
            if (result < 1) {
                throw new Exception("Результатом работы калькулятора с римскими числами могут быть только положительные числа");
            }
            System.out.println(arabicToRoman(result));
        } else {
            System.out.println(result);
        }

    }

    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(input + " cannot be converted to a Roman Numeral");
        }

        return result;
    }

    public static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " is not in range (0,4000]");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }
}

enum RomanNumeral {
    I(1), IV(4), V(5), IX(9), X(10),
    XL(40), L(50), XC(90), C(100),
    CD(400), D(500), CM(900), M(1000);

    private int value;

    RomanNumeral(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static List<RomanNumeral> getReverseSortedValues() {
        return Arrays.stream(values())
                .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                .collect(Collectors.toList());
    }
}

