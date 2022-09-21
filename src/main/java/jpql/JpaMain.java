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

            Team team = new Team();
            team.setName("팀A");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Member member = new Member();
            member.setUsername("회원1");
            member.changeTeam(team);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(team);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원2");
            member3.changeTeam(team2);
            em.persist(member3);

//            em.flush();
//            em.clear();

            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();
            System.out.println("resultCount = " + resultCount);

            // 벌크 연산은 영속성 컨텍스트를 무시한다 getAge = 0
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("member.getAge() = " + findMember.getAge());

            List<Member> resultList = em.createQuery("select m from Member m where m.id = :id", Member.class)
                    .setParameter("id", member.getId())
                    .getResultList();
            System.out.println("resultList.get(0).getAge() = " + resultList.get(0).getAge());

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
