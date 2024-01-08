# **스크럼**

### OrderCheck

- 나이스 페이먼츠를 확인해보면 `tId`라는 변수명 확인할 수 있음
- 따라서 `orderCheck`에서 `tid` 변수 생성 후 작업할 것
- `ordercheck`을 `order`패키지 밖에 생성할 것인지

배준혁 - `ProductComment`

이아현 - `Payment` → `orderCheck` 지원

홍주형 - `Order` 수정

---

# 회고

## **발생한 이슈**

### PaymentController 결제 성공 및 실패 로직에 어떤 것이 들어가야하는가?

- 옵션의 재고 수량 차감 서비스 기능 필요

**솔루션**

- `option` `StockQuantity` 추가
- `optionService` 에 결제성공 후 수량 차감 및 결제취소에 관한 수량 복구의 기능 추가

### 게시글 과 후기 수정의 문제

- `updateForm`기능의 부재
- `updateForm`에서 Model을 보내는데 RestController에선 사용하지 않음

**솔루션**

- `HomeController`에서 `updateForm`기능 작성

### OrderCheck의 연관관계 맵핑 이슈

- `product`와 일대다 맵핑이 옵션의 값(재고)을 받아오지 못함

**솔루션**

- `product`에서 `option`기능 작성

### ErrorController 작성 유무?

- 월요일 다시 회의예정
