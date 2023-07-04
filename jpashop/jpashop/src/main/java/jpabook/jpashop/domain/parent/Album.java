package jpabook.jpashop.domain.parent;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class Album extends ParentItem {

    private String artist;
}
