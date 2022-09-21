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

            em.flush();
            em.clear();

            String query = "select t from Team t join t.members m";
            List<Team> list = em.createQuery(query, Team.class)
                    .getResultList();
            System.out.println("list size: " + list.size());
            list.forEach(item -> {
                System.out.println(item.toString());
                for(Member item2 : item.getMembers()) {
                    System.out.println("\t> "+item2.toString());
                }
            });

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
