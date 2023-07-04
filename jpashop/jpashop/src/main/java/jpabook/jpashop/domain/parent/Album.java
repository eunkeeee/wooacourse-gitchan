package jpabook.jpashop.domain.parent;

import jpabook.jpashop.domain.Item;

import javax.persistence.Entity;

@Entity
public class Album extends ParentItem {

    private String artist;
}
