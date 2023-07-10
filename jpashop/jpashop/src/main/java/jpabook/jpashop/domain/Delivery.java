package jpabook.jpashop.domain;

import baseEntity.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

//    @OneToOne(mappedBy = "delivery", fetch = LAZY)
//    private Order order;

    public Delivery() {
    }
}
