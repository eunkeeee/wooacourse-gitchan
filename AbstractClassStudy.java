import java.util.ArrayList;
import java.util.List;

public class AbstractClassStudy {
    public static void main(String[] args) {
        List<Father> fatherList = new ArrayList<>();
        Father daughter = new Daughter();
        fatherList.add(daughter);
        Father son = new Son();
        fatherList.add(son);

        Son convertFatherToSon = (Son) fatherList.get(0);
    }
}

abstract class Father {
    abstract int abstractMethod();
}

class Daughter extends Father {

    @Override
    int abstractMethod() {
        return 1;
    }
}

class Son extends Father {
    @Override
    int abstractMethod() {
        return 2;
    }
}
