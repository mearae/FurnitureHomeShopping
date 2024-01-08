# (6) 12.18 스크럼 및 회고

# **스크럼**

### HTML코드 작성

- 작성된 코드 확인을 위해 HTML코드 작성 후 자바코드 수정예정
- 각 맡은 파트 HTML코드 작성

배준혁 - `User`, `HomeComment`

이아현 - `Board`, `Option`

홍주형 - `Cart`, `Order`



---



## 회고

### 이슈

#### 1. `orderCheck` - `productComment` 연관관계 매핑 문제

`orderCheck`를 만들기 전에 `productComment`를 만들어서 수정을 아직 못함

`User`랑 `option`이랑 연결해서 삭제하고 `orderCheck`랑 연결해야함



#### 2. `orderCheck`로 변경하면서 발생되는 `orderStatus` 문제

`orderStatus`를 삭제하고 로직 수정

#### 3. 관리자 회원가입 문제

##### 솔루션

관리자 회원가입 삭제, SQL 에서 `id` 1번이 관리자로 지정

#### 4. `board`, `category` 연관관계 매핑

연관관계 매핑 했었으나 `QNA`와 `Notice`를 합치는것으로 해결하여 연관관계가 불필요해졌음

##### 솔루션

삭제로 해결

#### 5. `Option` 수량 개수 중복

##### 솔루션

`Option` 에 있는 `quantity`를 삭제하고 `item`의 `quantity` 사용

<br>

#### 6. HTML 상품페이지 기능 구현

 상품페이지 안에 별점의 평균값, 리뷰작성 및 리뷰이미지 등록 기능, 장바구니에 추가 기능 구현

### 솔루션

`function` 사용해서 해당 기능 ai의 도움을 받아 구현

<br>