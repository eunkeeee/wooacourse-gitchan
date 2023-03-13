import java.util.stream.IntStream;

public class StreamStudy {
    public static void main(String[] args) {
        int sum = IntStream.rangeClosed(1, 5).reduce(0, (number1, number2) -> number1 + number2);
        int count = IntStream.rangeClosed(1, 5).reduce(0, (number1, number2) -> number1 + 1);

        System.out.println(sum); // 15
        System.out.println(count); // 5
    }
}
