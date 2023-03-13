public class GenericsStudy<T> {

    private final T item;

    public GenericsStudy(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}

class GenericsStudyPlay {
    public static void main(String[] args) {
        GenericsStudy<String> stringGenericsStudy = new GenericsStudy<>("깃짱");
        GenericsStudy<Integer> integerGenericsStudy = new GenericsStudy<>(2000);
        GenericsStudy<Boolean> booleanGenericsStudy = new GenericsStudy<>(true);

        System.out.println(stringGenericsStudy.getItem()); // 깃짱
        System.out.println(integerGenericsStudy.getItem()); // 2000
        System.out.println(booleanGenericsStudy.getItem()); // true
    }
}
