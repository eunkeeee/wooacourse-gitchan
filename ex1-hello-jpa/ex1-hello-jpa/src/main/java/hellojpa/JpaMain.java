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
            final Member memberA = new Member(4L, "깃짱");
            final Member memberB = new Member(5L, "훈짱");

            // 영속
            em.persist(memberA);
            em.persist(memberB);
            System.out.println("=================");

            tx.commit(); // 여기서 두 번의 쿼리가 날아감
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
