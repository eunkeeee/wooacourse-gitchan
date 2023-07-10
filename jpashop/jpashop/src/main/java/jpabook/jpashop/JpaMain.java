package jpabook.jpashop;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.AddressEntity;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        final EntityManager em = emf.createEntityManager();

        final EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setName("member1");
            member.setAddress(new Address("homeCity", "street", "zipcode"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("old1", "street", "zipcode"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "zipcode"));

            member.setWorkPeriod(new Period());
            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("==========START==========");
            Member findMember = em.find(Member.class, member.getId());

            System.out.println("==========LAZY-LOADING==========");

            // 치킨 -> 냉면
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("냉면");

//            findMember.getAddressHistory().remove(new Address("old1", "street", "zipcode"));
//            findMember.getAddressHistory().add(new Address("newCity", "street", "10000"));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
