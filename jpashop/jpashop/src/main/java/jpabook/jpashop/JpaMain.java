package jpabook.jpashop;

import jpabook.jpashop.domain.Address;
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
            Address address = new Address("homeCity", "street", "zipcode");

            Member member1 = new Member();
            member1.setName("member1");
            member1.setHomeAddress(address);

            member1.getFavoriteFoods().add("치킨");
            member1.getFavoriteFoods().add("족발");
            member1.getFavoriteFoods().add("피자");

            member1.getAddressHistory().add(new Address("old1", "street", "zipcode"));
            member1.getAddressHistory().add(new Address("old2", "street", "zipcode"));

            member1.setWorkPeriod(new Period());
            em.persist(member1);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
