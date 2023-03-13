public class LambdaStudy {

    public static void main(String[] args) {
        Runnable r1 = () -> System.out.println("Hello World! 1");
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World! 2");
            }
        };

        r1.run(); // Hello World! 1
        r2.run(); // Hello World! 2

    }
}
