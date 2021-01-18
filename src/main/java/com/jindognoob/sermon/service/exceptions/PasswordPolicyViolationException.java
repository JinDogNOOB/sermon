package com.jindognoob.sermon.service.exceptions;

/*
Transactonal 안에서 내부Transaction메소드 안에서 RuntimeException을 던지면은
try catch 가 그 바깥 호출자에 쌓여져있어도 
전역설정이 Exception 발생시 무조건 롤백이다
https://woowabros.github.io/experience/2019/01/29/exception-in-transaction.html
*/
/*
RuntimeException : 실행할때 에러 발생 주로 프로그래머 실수 배열크기 침점, null참조변수멤버호출, 0나누기
Exception : 사용자 실수 반드시 try catch 로 잡아야한다
*/

public class PasswordPolicyViolationException extends Exception{
    public PasswordPolicyViolationException() {}
    public PasswordPolicyViolationException(String message){super(message);}
}
