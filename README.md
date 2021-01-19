# Paging 처리 최적화 관련 글
* https://jojoldu.tistory.com/528?category=637935
    - no offset 형식 -> more 형식의 페이징, 페이지인덱스 버튼 없이 운용할때 
    - 커버링 인덱스 select * from items as i JOIN (select i from items where~~ orderby~~offset ~ limit ~) as temp on temp.id = i.id

