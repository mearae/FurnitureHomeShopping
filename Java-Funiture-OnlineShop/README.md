REST API 설계 <br>
=============
👨‍💼 회원<br>
---------
### 1. 회원가입 <br>
\- 회원의 정보를 받아 데이터베이스에 등록 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식|
|-|-|-|
|POST|8080/user/join|-|

\- 헤더 <br>

|이름|설명|필수|
|-|-|-|
|Content-type|application/json<br>요청 데이터 타입|O|

\- 본문 <br>

|이름|타입|설명|필수|
|-|-|-|-|
|email|String|이메일(아이디)|O
|password|String|비밀번호(영문자, 특수문자, 숫자를 각각 반드시 포함하는 8~20자)|O
|username|String|이름|O
|phoneNumber|String|전화번호(10~11자)|O
|address|String|배송주소|O

--------------

### 2. 로그인 <br>
\- 아이디(e\-mail), 비밀번호를 받아 JWT 토큰을 반환, 쿠키에 등록 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식|
|-|-|-|
|POST|8080/user/login|-|

\- 헤더 <br>

|이름|설명|필수|
|-|-|-|
|Content-type|application/json<br>요청 데이터 타입|O|

\- 본문 <br>

|이름|타입|설명|필수|
|-|-|-|-|
|email|String|이메일(아이디)|O|
|password|String|비밀번호(영문자, 특수문자, 숫자를 각각 반드시 포함하는 8~20자)|O
<br>

#### 응답 <br>
\- 헤더<br>

|이름|타입|설명|
|-|-|-|
|Authorization|String|JWT 토큰|

------------------

### 3. 로그아웃 <br>
\- 로그인 시 받은 토큰을 무효화 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식|
|-|-|-|
|POST|8080/user/logout|엑세스 토큰|

\- 헤더 <br>

|이름|설명|필수|
|-|-|-|
|Content-type|application/json<br>요청 데이터 타입|O|
|Authorization|Authorization: Baer ${access_token}<br>사용자 인증 수단, 엑세스 토큰 값|O

----------------------

### 4. 회원정보 확인 <br>
\- 회원가입 시 넣은 회원 정보를 반환 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-|
|GET|8080/user/info|엑세스 토큰

\- 헤더 <br>

|이름|설명|필수|
|-|-|-|
|Content-type|application/json<br>요청 데이터 타입|O|
|Authorization|Authorization: Baer ${access_token}<br>사용자 인증 수단, 엑세스 토큰 값|O
<br>

#### 응답 <br>

\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|email|String|이메일(아이디)|O
|password|String|비밀번호(영문자, 특수문자, 숫자를 각각 반드시 포함하는 8~20자)|O
|username|String|이름|O
|phoneNumber|String|전화번호(10~11자)|O
|address|String|배송주소|O
|roles|List\<String>|권한|O

-------------------------

### 5. 회원탈퇴 <br>
\- 등록된 회원을 데이터베이스에서 제거 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-|
|POST|8080/user/delete|엑세스 토큰

\- 헤더 <br>

|이름|설명|필수
|-|-|-|
|Content-type|application/json<br>요청 데이터 타입|O
|Authorization|Authorization: Baer ${access_token}<br>사용자 인증 수단, 엑세스 토큰 값|O

-------------------

💬 카테고리 <br>
---------------
### 1. 카테고리 저장 <br>
\- 최상위 혹은 하위 카테고리로 저장 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|POST|8080/category/save|-

\- 헤더 <br>

|이름|설명|필수|
|-|-|-|
|Content-type|application/json<br>요청 데이터 타입|O|

\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|superCategory_id|Long|상위 카테고리 id|X
|categoryName|String|카테고리 명|O

------------------

### 2. 최상위 카테고리 모두 보기 <br>
\- 등록된 최상위 카테고리를 전부 탐색하여 반환 <br><br>
#### 요청 <br>
\- 기본 정보 <br>


|메서드|URL|인증 방식
|-|-|-
|GET|8080/category/super|-

\- 헤더 <br>

|이름|설명|필수|
|-|-|-|
|Content-type|application/json<br>요청 데이터 타입|O|
<br>

#### 응답 <br>
\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|categories|List\<FindAllDto>|모든 최상위 카테고리 엔티티의 Dto 리스트|O

\- FindAllDto <br>

|이름|타입|설명|필수
|-|-|-|-
|id|Long|카테고리 id|O
|categoryName|String|카테고리 명|O
|superCategory_id|Long|상위 카테고리 id|X

-------------------

### 3. 한 카테고리와 그 상하위 카테고리 <br>
\- 요청받은 카테고리와 그 상하위 카테고리에 대한 정보를 반환 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|GET|8080/category/{id}|-

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
<br>

#### 응답 <br>
\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|id|Long|카테고리 id|O
|categoryName|String|카테고리 명|O
|superCategory|CategoryDto|상위 카테고리|X
|subCatgories|List\<FindByIdDto>|하위 카테고리 집합|X

\- FindByIdDto <br>

|이름|타입|설명|필수
|-|-|-|-
|id|Long|카테고리 id|O
|categoryName|String|카테고리 명|O

------------------

### 4. 한 카테고리의 모든 하위 카테고리 <br>
\- 요청받은 카테고리의 모든 하위 카테고리를 탐색하여 반환 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|GET|8080/category/son/{id}|-

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
<br>

#### 응답 <br>
\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|categories|List\<FindAllDto>|모든 하위 카테고리 엔티티의 Dto 리스트|O

\- FindAllDto <br>

|이름|타입|설명|필수
|-|-|-|-
|id|Long|카테고리 id|O
|categoryName|String|카테고리 명|O
|superCategory_id|Long|상위 카테고리 id|X

------------------

### 5. 카테고리 수정 <br>
\- 카테고리 명을 수정 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|POST|8080/category/update|엑세스 토큰

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
|Authorization|Authorization: Baer ${access_token}<br>사용자 인증 수단, 엑세스 토큰 값|O

\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|id|Long|카테고리 id|O
|categoryName|String|카테고리 명|O

----------------------

### 6. 카테고리 삭제 <br>
\- 한 카테고리와 그 하위 카테고리들을 전부 삭제 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|POST|8080/category/delete/{id}|엑세스 토큰

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
|Authorization|Authorization: Baer ${access_token}<br>사용자 인증 수단, 엑세스 토큰 값|O

----------------

💭 상품 후기
-------------
### 1. 상품 후기 저장 <br>
\- 주문한 상품에 대한 후기(별점, 내용, 이미지)를 저장 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|POST|8080/product_comment/save|엑세스 토큰

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
|Authorization|Authorization: Baer ${access_token}<br>사용자 인증 수단, 엑세스 토큰 값|O

\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|star|int|별점(1~5)|O
|contents|Long|내용|O
|createTime|Date|작성일|X
|updateTime|Date|수정일|X
|orderCheckId|Long|주문 내역 id|O
|files|MultipartFile[]|첨부파일들(이미지)|X
<br>

#### 응답 <br>
\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|star|int|별점(1~5)|O
|contents|Long|내용|O
|createTime|Date|작성일|X
|updateTime|Date|수정일|X
|orderCheckId|Long|주문 내역 id|O

--------------

### 2. 상품 후기 탐색 <br>
\- 자신(로그인한 회원)이 작성한 모든 상품 후기를 탐색하여 반환 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|GET|8080/product_comment/comments/{id}|-

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
<br>

#### 응답 <br>
\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|commentDtos|List\<CommentDto>|상품 후기 목록|X

\- CommentDto <br>

|이름|타입|설명|필수
|-|-|-|-
|star|int|별점(1~5)|O
|contents|String|내용|O
|optionName|String|옵션명|O
|createTime|Date|작성일|X
|updateTime|Date|수정일|X
|orderCheckId|Long|주문 내역 id|O
|optionId|Long|옵션 id|O
|productId|Long|상품 id|O
|userId|Long|작성자 id|O
|files|List\<CommentFileDto>|첨부 사진들의 정보 Dto|X

\- CommentFileDto <br>

|이름|타입|설명|필수
|-|-|-|-
|id|Long|첨부 파일 id|O
|filePath|String|파일 경로|O
|fileName|String|파일 명|O
|fileType|String|파일 형식|X
|uuid|String|랜덤 키|X
|fileSize|Long|파일 크기|O

------------------

### 3. 상품 후기 삭제 <br>
\- 자신(로그인한 회원)이 작성한 상품 후기를 삭제 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|POST|8080/product_comment/delete/{id}|엑세스 토큰

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
|Authorization|Authorization: Baer ${access_token}<br>사용자 인증 수단, 엑세스 토큰 값|O

------------------

### 4. 상품 후기 수정 <br>
\- 자신(로그인한 회원)이 작성한 상품 후기의 별점, 내용, 이미지(이전 이미지 모두 삭제) 수정 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|POST|8080/product_comment/delete/{id}|엑세스 토큰

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
|Authorization|Authorization: Baer ${access_token}<br>사용자 인증 수단, 엑세스 토큰 값|O

\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|id|Long|상품 후기 id|O
|star|int|별점(1~5)|O
|contents|Long|내용|O
|files|MultipartFile[]|첨부파일들(이미지)|X
<br>

#### 응답 <br>
\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|id|Long|상품 후기 id|O
|star|int|별점(1~5)|O
|contents|Long|내용|O

--------------------

### 5. 주문 내역 탐색 <br>
\- 자신(로그인한 회원)의 모든 결제 정보를 탐색하여 반환 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|GET|8080/product_comment/orderCheck|엑세스 토큰

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
|Authorization|Authorization: Baer ${access_token}<br>사용자 인증 수단, 엑세스 토큰 값|O
<br>

#### 응답 <br>
\- 본문 <br>

|이름|타입|설명|필수
|-|-|-|-
|orderCheckDtos|List\<OrderCheckDto>|상품 후기 목록|X

\- OrderCheckDto <br>

|이름|타입|설명|필수
|-|-|-|-
|id|Long|주문 내역 id|O
|tid|String|거래 id|O
|orderId|String|거래 상점 주문 id|O
|quantity|Long|수량|O
|productName|String|상품명|X
|optionName|String|옵션명|O
|price|Long|가격|O
|orderDate|Date|주문 일자|O
|optionId|Long|옵션 id|O
|userIdㅣLong|회원 id|O
|commentId|상품 후기 id|X

----------------------

### 6. 상품 후기 사진 이미지화 <br>
\- 상품 후기의 사진을 바이트 코드화 하여 <img>의 th:src="/product_comment/image/{id}"로 이미지가 뷰에 나타나도록 하는 기능 <br><br>
#### 요청 <br>
\- 기본 정보 <br>

|메서드|URL|인증 방식
|-|-|-
|GET|8080/product_comment/image/{id}|-

\- 헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|application/json<br>요청 데이터 타입|O
<br>

#### 응답 <br>
\-헤더 <br>

|이름|설명|필수
|-|-|-
|Content-type|/image/*<br>이미지가 응답임을 명시|O

\- 본문 <br>

|타입|설명|필수
|-|-|-
|byte[]|상품 후기 이미지 파일의 바이트|O
