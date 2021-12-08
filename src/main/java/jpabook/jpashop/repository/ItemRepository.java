package jpabook.jpashop.repository;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    // 아이템 등록 함수
    public void save(Item item) {
        // 미등록 아이템일 경우 등록
        if(item.getId() == null) {
            em.persist(item);
            // 이미 등록된 아이템일 경우 수정
        } else {
            em.merge(item);
        }
    }

    // 아이템 하나 찾기
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 모든 아이템 목록 조회
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
