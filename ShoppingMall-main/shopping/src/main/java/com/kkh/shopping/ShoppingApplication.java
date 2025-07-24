package com.kkh.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ShoppingApplication {

	void 함수이름() { // 함수는 타입과 값을 맞춰서 작성

	};

	public static void main(String[] args) {
		SpringApplication.run(ShoppingApplication.class, args);

//		final String lover = "강예원";
//		var age = 25;
//		final int wifeCount = 1;

//		System.out.println(wifeCount);
//
//		System.out.println(lover);


//		new friend("kim", 25);
//		new friend("park", 26);
//
//		var test = new friend("park", 25);
//		System.out.println(test.name);

	}

}

//	class Test { // class는 변수 함수를 담아두는 통!
//		String name = "Kim";
//		void hello() {
//			System.out.println("안녕");
//		}
//	}
//
//	class friend {
//		String name = "jang";
//		int age = 29;
//		friend(String 가변, int 가변2) {
//			this.name = 가변; // this - 새로 생성될 오브젝트
//		}
//	}

// type 적기 귀찮으면 var
// final은 변수명 앞에, 바뀌지 않는 변수에 사용
// class의 변수 함수 쓰려면 new클래스()로 복사해서 사용

// 클래스는 관련있는 변수, 함수를 모아두기 위해 사용
// 중요한 변수, 함수 원본을 지킬 수 있음

// 복사안해도 쓸 수 있는, new로 복사 안되는 변수, 함수 제작가능 ex) static, private 등
// 함수안에 있는 변수는 필드, 애트리뷰트, 메소드로 불림