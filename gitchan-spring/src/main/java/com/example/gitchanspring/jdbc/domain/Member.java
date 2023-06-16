package com.example.gitchanspring.jdbc.domain;

import lombok.Data;

@Data
public class Member {

    private String memberId;
    private int money;

    public Member() {
    }

    public Member(final String memberId, final int money) {
        this.memberId = memberId;
        this.money = money;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(final String memberId) {
        this.memberId = memberId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(final int money) {
        this.money = money;
    }
}
