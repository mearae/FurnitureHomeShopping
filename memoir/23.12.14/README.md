# (4) 12.14 스크럼 및 회고

## 스크럼

  

### orderCheck

- 구원의 손길

  

### HomeController

> 페이지의 이동을 도와줄 컨트롤러

  

송한올 신규 작업배분

  

### 마무리

송한올 - `HomeController` 작업 -> `orderCheck` 지원

배준혁 - `Category` (CRUD) -> `User` 추가기능구현

이아현 - `Board` = `User` 연결 -> `orderCheck` 지원

홍주형 - `orderCheck` (C) -> `Cart` 검토

  

# 회고

## 이슈

### CartProduct 파일을 만들지?

> 몇개를 선택했는지 알 수가 없음



주석의 부재와 엔티티 클래스간의 동일된 변수명으로 인한 혼란

따라서 `Option` 의 `quantity` -> `storage` 변경



##### 솔루션

`cartItem` 클래스를 생성해서 선택 수량 관리

---

### @PreAuthorize 어노테이션?

`@PreAuthorize("hasRole('ROLE_ADMIN')")`

어드민 권한을 받을 수 있나?

- 검증 필요



### 주문확인을 order파일에 추가 할것 인지, 별도의 check파일 을 만들것인지



##### 솔루션

NicepayController 에서 로직 구현



### Board 안에서 Qna 와 Notice의 분리



##### 솔루션

- 자주묻는질문 (QNA)의 내용을 Notice 안으로 이동
- QNA 삭제



### 주문 상세 페이지 구현?



##### 솔루션

- 이미 RestController 에 구현되어있기에 그냥 html 파일로 이동만 해주면 된다.



### 홈컨트롤러의 카트서비스 구현

##### 솔루션

- 이미 RestController 에 구현되어있기에 그냥 html 파일로 이동만 해주면 된다.
