package com.kkh.shopping.Item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.InvalidClassException;

@ControllerAdvice
public class MyExceptionHandler { // 해당 패키지의 모든 컨트롤러 에러 처리

//    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // 해당클래스의 에러 익셉션 통합, Exception을 수정해서 여러 종류의 에러 처리
//    public ResponseEntity<String> handler1() {
//        return ResponseEntity.status(400).body("에러");
//    }
//
//    @ExceptionHandler(Exception.class) // 해당클래스의 에러 익셉션 통합
//    public ResponseEntity<String> handler() {
//        return ResponseEntity.status(400).body("에러");
//    }

//    @ExceptionHandler(Exception.class) // 해당클래스의 에러 익셉션 통합
//    public ResponseEntity<String> handler2() {
//        return ResponseEntity.status(500).body("에러");
//    }
}
