# Paging 처리 최적화 관련 글
* https://jojoldu.tistory.com/528?category=637935
    - no offset 형식 -> more 형식의 페이징, 페이지인덱스 버튼 없이 운용할때 
    - 커버링 인덱스 select * from items as i JOIN (select i from items where~~ orderby~~offset ~ limit ~) as temp on temp.id = i.id

# api 
* SNR : security not required

* /user/signup POST : 회원가입 SNR
    - String email
    - String password
    - String nickname

* /user/auth GET : 토큰유효성검사용
* /user/auth PUT : 패스워드 변경
    - String currentPassword
    - String newPassword
* /user/auth DELETE : 회원탈퇴
    - String password

* /board/question GET : 질문 리스트 조회 SNR
* /board/question POST : 질문 등록
    - String title
    - String content

* /board/question/{questionId} GET : 질문 상세 조회 SNR
* /board/question/{questionId} POST : 답변 채택
    - long answerId
* /board/question/{questionId} PUT : 질문 수정
    - String title
    - String content
* /board/question/{questionId} DELETE : 질문 삭제

* /question/{questionId}/answer GET : 답변 리스트 조회 SNR
* /question/{questionId}/answer POST : 답변 등록
    - String title
    - String content

* /question/{questionId}/answer/{answerId} GET : 답변 상세 조회 SNR
* /question/{questionId}/answer/{answerId} POST : 답변 추천 SNR
* /question/{questionId}/answer/{answerId} PUT : 답변 수정
    - String title
    - String content
* /question/{questionId}/answer/{answerId} DELETE : 답변 삭제




