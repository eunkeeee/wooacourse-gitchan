package jpabook.jpashop.domain;

import javax.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    // 기간
    @Embedded
    private Period workPeriod;

    // 주소
    @Embedded
    private Address homdAddress;

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomdAddress() {
        return homdAddress;
    }

    public void setHomdAddress(Address homdAddress) {
        this.homdAddress = homdAddress;
    }
}
