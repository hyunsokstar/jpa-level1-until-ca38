package jpabook.jpashop.repository;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    // 멤버 엔티티 정보를 받아서 저장 하기
    public void save(Member member) {
        em.persist(member);
    }

    // id를 이용해 멤버 정보 찾기
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // 모든 멤버 정보 찾기
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // 멤버 이름으로 찾기
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
