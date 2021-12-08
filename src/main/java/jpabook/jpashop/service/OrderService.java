package jpabook.jpashop.service;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import lombok.RequiredArgsConstructor;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문 데이터 저장
    // 필요한 데이터:
    // 1.회원 정보
    // 2.상품 정보
    // 3.주문 수량 ,단 주문한 상품은 한가지로 제한(so itemId 만 얻어오면 된다.)
    // 4.배송정보는 인자로 받지 않고 그냥 엔티티 생성해서 활용
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 멤버 찾기 => 멤버 엔티티 객체 생성
        Member member = memberRepository.findOne(memberId);
        // 상품 찾기 => 상품 엔티티 객체 생성
        Item item = itemRepository.findOne(itemId);

        // 배송 정보 생성 하기
        Delivery delivery = new Delivery();

        System.out.println("delivery :::::::::::::::::::::::::::::" + delivery);
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문 상품 엔티티 생성 하기
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // 주문 정보 생성 하기 (단 주문 상품 정보는 단 하나만 넘긴다.)
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 정보 영속화 하기
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 정보 찾기
        Order order = orderRepository.findOne(orderId);
        System.out.println("취소할 주문 "+ order);
        // 주문 취소
        order.cancel();
    }

}
