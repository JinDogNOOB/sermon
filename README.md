# sermon backend
* 개발환경
    - visual studio code on windows10
    - WSL ubuntu18.04 -> mysql
    - spring boot framework
* 사용라이브러리
    - lombok
    - querydsl
    - jpa hibernate
    - json web token
    - spring security

# 기타
* TDD
    - TDD 개발을 맛보기위해서 테스트 코드를 작성 -> 테스트 코드 실행 -> 통과 식으로 서비스 레이어를 테스팅하면서 개발을 진행함
* controller <-> service 사이 데이터 전달 객체
    - DTO를 사용했음
    - Entity는 service, repository에서만 사용했음
    - controller에서 로딩이 안된 필드를 불러오는 실수 등을 방지하기 위해서 함
* 해쉬태그 
    - 해쉬태그 기능과 해쉬태그 검색 기능을 추가했음
    - 해쉬태그에 weight는 넣지 않았음
* service interface
    - 후에 mybatis 등으로도 구현될 수 있는 부분은 인터페이스를 두고 구현하는 식으로 개발을 진행했음
* db
    - 개발 초기에는 h2를 사용해서 테스트 했으나 개인적으로 heidiSQL툴을 사용해서 데이터를 직관적으로 보는 것이 편해서 mysql를 사용하는 것으로 변경함
* github
    - pull request 등을 연습하기 위해서 일부로 자기 자신에 요청함
    - 본래 master 브랜치 외에 develop 브랜치를 만들어서 진행하려고 했으나 개인적으로 진행하는 것이고
    - 개발기간을 상당히 짧게 잡았기 때문에 중요한 기능에 경우에는 브랜치로 작업하고 대부분 master에서 작업함 
* oauth2.0 
    - 본래 oauth2.0 네이버, 구글 을 적용하려고 했으나 기간이 짧아서 넣지는 못했음
* hibernate 복합키
    - 최대한 복합키 사용을 자제했음

# 스크린샷
<div>
 <img width="900" src="https://user-images.githubusercontent.com/49367014/106103933-09ecf180-6185-11eb-8078-6613cfe6a63f.PNG">
 <img width="900" src="https://user-images.githubusercontent.com/49367014/106103936-0a858800-6185-11eb-8903-725d9f42e7c1.PNG">
 <img width="900" src="https://user-images.githubusercontent.com/49367014/106103939-0bb6b500-6185-11eb-8ce8-8d0141bcbb4e.PNG">
</div>

# API
* SNR : security not required
* /user/login : 로그인 SNR
    - email
    - password
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
    - String hashTags ex) #adf #ff 
* /board/question/more GET : 질문 리스트 조회 more (lastIndex기반) SNR
    - long lastIndex : 요구사항변경으로 추가 
    - String hashTags ex) #adf #ff 
* /board/question POST : 질문 등록
    - String title
    - String content
    - String hashTags ex) #adf #ff 

* /board/question/{questionId} GET : 질문 상세 조회 SNR
* /board/question/{questionId} POST : 답변 채택
    - long answerId
* /board/question/{questionId} PUT : 질문 수정
    - String title
    - String content
* /board/question/{questionId} DELETE : 질문 삭제

* /board/question/{questionId}/answer GET : 답변 리스트 조회 SNR
* /board/question/{questionId}/answer POST : 답변 등록
    - String title
    - String content

* /board/question/{questionId}/answer/{answerId} GET : 답변 상세 조회 SNR
* /board/question/{questionId}/answer/{answerId} POST : 답변 추천 SNR
* /board/question/{questionId}/answer/{answerId} PUT : 답변 수정
    - String title
    - String content
* /board/question/{questionId}/answer/{answerId} DELETE : 답변 삭제

* /board/hashtag GET : 해쉬태그 추천 SNR
    - String letter

# 이슈
* 페이징 처리 최적화
    - https://jojoldu.tistory.com/528?category=637935
    - no offset 형식 -> more 형식의 페이징, 페이지인덱스 버튼 없이 운용할때 
    - 커버링 인덱스 select * from items as i JOIN (select i from items where~~ orderby~~offset ~ limit ~) as temp on temp.id = i.id



