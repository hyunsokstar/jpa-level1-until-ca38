package jpabook.jpashop;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JpashopApplication {
    public static void main(String[] args) {
        // 1. Hello 객체 생성
        // 2. 셋겟 메서드로 data 가져온뒤 출력 test
        //  Hello hello = new Hello();
        //  hello.setData("hello");
        //  String data = hello.getData();
       //  System.out.println("data : " +data);
        SpringApplication.run(JpashopApplication.class, args);
    }
}
