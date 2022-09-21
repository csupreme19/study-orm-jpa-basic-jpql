package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            // Query
            List resultList = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();
            Object[] result = (Object[]) resultList.get(0);
            for(Object o : result) {
                System.out.println(o);
            }

            // TypedQuery
            List<Object[]> resultList2 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();
            for(Object[] result2 : resultList2) {
                for(Object o : result2) {
                    System.out.println(o);
                }
            }

            // new로 조회
            List<MemberDTO> resultList3 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            for (MemberDTO memberDTO : resultList3) {
                System.out.println(memberDTO.getUsername());
                System.out.println(memberDTO.getAge());
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
