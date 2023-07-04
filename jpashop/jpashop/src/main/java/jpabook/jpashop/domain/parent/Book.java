package jpabook.jpashop.domain.parent;

import jpabook.jpashop.domain.Item;

import javax.persistence.Entity;

@Entity
public class Book extends ParentItem {

    private String author;
    private String isbn;
}
