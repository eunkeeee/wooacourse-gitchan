package hellojpa;

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
            final long id = 3L;

            // 비영속 상태
            final Member member = new Member();
            member.setName("깃짱");
            member.setId(id);

            // 영속
            System.out.println("==BEFORE PERSIST==");
//            em.persist(member);
            final Member findMember1 = em.find(Member.class, id);
            final Member findMember2 = em.find(Member.class, id); // 두 번째 조회는 1차 캐시에서 조회하므로, 조회 쿼리가 날아가지 않음.
            System.out.println("findMember1 = " + findMember1);
            System.out.println("findMember2 = " + findMember2);

            System.out.println("result = " + (findMember1 == findMember2)); // true

            System.out.println("==AFTER PERSIST==");

            System.out.println("==BEFORE COMMIT==");
            tx.commit(); // 쿼리가 날아가는 시점
            System.out.println("==AFTER COMMIT==");
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
