package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Fail.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // 엔티티 객체 설정
        Member member = new Member();
        member.setName("kim");

        // 회원 가입
        Long saveId = memberService.join(member);

        // 회원 가입 정보가 담긴 엔티티와 회원 아이디로 조회한 엔티티를 비교
        assertEquals(member, memberRepository.findOne(saveId));
    }

    // 이름이 hyun인 두 멤버 객체 생성후 둘다 회원 가입 함수에 사용
    @Test
    public void 중복회원가입방지() throws Exception {
        Member member1 = new Member();
        member1.setName("hyun");
        Member member2 = new Member();
        member2.setName("hyun");

        memberService.join(member1);

        try {
            memberService.join(member2); // 여기에서 에러가 발생해야 함
        } catch (IllegalStateException e) {
            // 예외가 발생할 경우 메세지 출력
            System.out.println("중복 회원 가입 에러 발생!!");
            return;
        }
        // 예외가 발생하지 않았으면 test 실패
        fail("예외가 발생해야 한다.");
    }

}
