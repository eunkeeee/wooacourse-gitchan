package parent;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//@Entity
@DiscriminatorValue("B")
public class Book extends ParentItem {

    private String author;
    private String isbn;
}
