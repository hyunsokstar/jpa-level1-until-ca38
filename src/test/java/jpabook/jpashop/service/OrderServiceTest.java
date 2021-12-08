package jpabook.jpashop.service;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import javax.transaction.Transactional;
import static org.junit.Assert.*;


// 테스트 관련 어노테이션 추가
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    // 필요한 객체들 생성
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;


    // 상품 주문 테스트 추가해 보기
    @Test
    public void  상품주문() throws Exception {
        // 임의의 멤버 정보 저장 => 엔티티 생성
        Member member = createMember();
        // 임의의 상품 엔티티 데이터 저장 => 엔티티 생성
        Item item = createBook("잭과 콩나물", 1000, 10);
        // 임의의 주문 수량 설정
        int orderCount = 2;

        // 주문 생성 (멤버 아이디, 상품 아이디, 수량)
        /// 리턴값은 주문 아이디
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        System.out.println("주문 정보 생성후 리턴 받은 주문 아이디: "+ orderId);

        // 리턴 받은 아이디로 주문 정보 다시 조회 (주문 정보가 제대로 생성되었으면 조회도 잘되야 한다.)
        Order getOrder = orderRepository.findOne(orderId);
        System.out.println("db에서 조회한 주문 정보 : " + getOrder);

        // test1: 생성한 주문 정보의 주문 상태가 ORDER 이어야 한다.
        assertEquals("생성한 주문 정보의 주문 상태가 ORDER 이어야 한다.", OrderStatus.ORDER , getOrder.getStatus());
        // test2: 생성한 주문 정보에서 주문 상품의 개수는 1개이다.
        assertEquals("생성한 주문 정보이 주문 상품 개수는 1개 이다.", 1, getOrder.getOrderItems().size());
        // test3: 생성한 주문 정보에서 주문 상품들의 가격 합계는 2000이다.
        assertEquals("생성한 주문 정보에서 주문한 상품 가격들의 합계는 1000원자리 책 2권 즉 2000원 이다 ", 2000, getOrder.getTotalPrice());
        // tesst4: 주문 수량만큼 상품의 재고가 줄어야 한다.
        assertEquals("주문 수량만큼 상품의 재고가 줄어야 한다." , 8, item.getStockQuantity());

    }


    // 공통 함수
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    public void 주문취소() throws Exception {
        Member member = createMember();
        Item item = createBook("잭과 콩나물", 1000, 10);
        int orderCount = 2;

        // 주문 하기
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // 주문 취소 하기
        orderService.cancelOrder(orderId);
        // 주문 데이터 조회 하기
        Order getOrder = orderRepository.findOne(orderId);
        // 조회한 주문 데이터의 상태가 CANCEL 인지 확인 하기
        assertEquals("주문 취소한 주문 데이터의 주문 상태는 CANCEL 이어야 한다.", OrderStatus.CANCEL, getOrder.getStatus());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 초과수량주문테스트(){
        Member member = createMember();
        Item item = createBook("잭과 콩나물", 1000, 10);
        int orderCount = 11;
        orderService.order(member.getId(), item.getId(), orderCount);
        fail("주문 수량이 재고 수량보다 큰데 에러가 발생 안했음");
    }



}

