
# 중고 거래 플랫폼 멋사마켓 V2🚀
테킷 백엔드 스쿨 5기 프로젝트 1
<br>
<br>

## 🛠 프로젝트 환경
- Java 17
- Spring Boot 3.1.1
- MySQL
<br>
<br>

## 🎯 프로젝트 목표
- 이 프로젝트는 사용자들이 물품을 등록하고, 이에 대한 제안을 등록하며, 그 제안을 관리하는 웹 API를 제공하는 것을 목표로 합니다.
<br>
<br>

## 💡 기능 요약
- 유저는 물품을 등록 및 수정 삭제할 수 있습니다.
- 다른 유저는 물품에 대한 댓글과 제안을 작성 및 수정, 삭제할 수 있습니다.
- 물품 등록자는 해당 댓글에 대한 답변을 작성할 수 있으며, 제안에 대한 수락 및 거절을 할 수 있습니다.
<br>
<br>

## 🛠 ERD
![ERD](https://github.com/likelion-backend-5th/Project_1_KimDoyoung/blob/main/docs/miniprojectERD.png)   


## 📘 기능 요약

1. **유저 User**
    - 회원가입 POST /auth/signup
        유저는 회원가입을 할 수 있습니다. 이 때 아이디와 비밀번호는 필수 요소입니다.
    - 로그인 GET /auth/login : 아이디와 비밀번호 입력 시, 토큰 정보를 반환받을 수 있습니다.
    - 로그아웃 GET /auth/logout
    - 프로필 조회 GET /users/profile (토큰) : 현재 로그인 된 유저의 프로필을 조회합니다.
    - 프로필 수정 PUT /users/profile (토큰) : 현재 로그인 된 유저의 정보를 수정합니다. (비밀번호, email, phone, address)

<br>

2. **아이템 SalesItem**
    - 아이템 등록 POST /items (토큰) : 현재 로그인된 유저는 아이템을 등록할 수 있습니다. 이미지를 함께 업로드할 수 있으며, 별도의 파일 업로드가 없다면 기본 이미지(/static/defaultImage.png)가 등록됩니다. 동시에 해당 물품은 '판매중' 상태가 됩니다. ( 아이템 상태 = {'판매중', '판매 완료'} )
    - 아이템 수정 PUT /items/{itemId} (토큰) : 등록된 아이템의 정보를 수정할 수 있습니다.
    - 아이템 이미지 등록 PUT /items/{itemsId}/image (토큰) : 등록된 아이템에 이미지를 등록 및 수정을 할 수 있습니다.
    - 아이템 조회 GET /items : 현재 등록된 전체 아이템을 페이지별로 조회할 수 있습니다.
    - 아이템 단일 조회 GET /items/{itemId} : 등록된 아이템을 단일 조회할 수 있습니다.
    - 아이템 삭제 DELETE /items/{itemId} (토큰) : 현재 로그인된 유저가 등록한 아이템을 삭제할 수 있습니다.

<br>

3. **제안 Negotiation**
    - 제안 등록 POST /items/{itemId}/proposals (토큰) : 등록된 아이템에 대한 가격 제안을 등록할 수 있습니다. 동시에 해당 제안은 '제안' 상태가 됩니다. ( 제안 상태 = {'제안', '수락', '거절', '확정'} )
    - 제안 조회 GET /items/{itemId}/proposals (토큰) : 아이템을 조회한 등록자는 모든 제안을, 그 외 유저는 본인이 직접 등록한 제안만을 조회할 수 있습니다.
    - 제안 수정 PUT /items/{itemId}/proposals/{proposalId} (토큰) : 제안을 등록한 작성자는 해당 제안을 수정할 수 있습니다.
    - 제안 상태 변경 PUT /items/{itemId}/proposals/{proposalId} (토큰) : 아이템을 등록한 유저는 해당 아이템에 대한 제안의 상태를 '수락' 또는 '거절' 상태로 변경할 수 있습니다.
    - 제안 상태 변경 (확정) PUT /items/{itemId}/proposals/{proposalId} (토큰) : 아이템을 등록한 유저는 해당 아이템에 대한 제안의 상태가 이미 '수락' 상태일 시, 이를 '확정' 상태로 변경할 수 있습니다. 해당 제안이 '확정' 상태가 된다면, 이 외 제안들은 모두 '거절' 상태가 됩니다. 동시에 해당 물품의 상태는 '판매 완료'가 됩니다.
    - 제안 삭제 DELETE /items/{itemId}/proposals/{proposalId} (토큰) : 제안을 등록한 유저는 해당 제안을 삭제할 수 있습니다.

<br>

4. **댓글 Comment**
    - 댓글 등록 POST /items/{itemId}/comments (토큰) : 로그인된 유저는 아이템에 댓글을 등록할 수 있습니다.
    - 댓글 수정 PUT /items/{itemId}/comments/{commentId} (토큰) : 댓글을 작성한 유저는 해당 댓글의 내용을 수정할 수 있습니다.
    - 댓글 삭제 DELETE /items/{itemId}/comments/{commentId} (토큰) : 댓글을 작성한 유저는 해당 댓글을 삭제할 수 있습니다.
    - 답글 등록 PUT /items/{itemId}/comments/{commentId}/reply (토큰) : 아이템을 등록한 유저는 해당 아이템에 작성된 댓글에 대한 답글을 등록할 수 있습니다.

<br>

## ※ 주의 사항
- **/api/v1/~** 으로 시작하는 경로는 기존 미니프로젝트와 관련된 요청입니다. (유저 관리가 이루어지지 않습니다. -> 모든 아이템과 댓글 등의 대부분의 기능이 개별 등록 ID와 비밀번호를 요구합니다.)
- **일반적인 실행은 모든 요청 URL이 /api/v2/~ 으로 시작**합니다. 포스트맨에서의 실행 또한 v2 폴더를 이용하시면 됩니다.
- 해당 프로젝트는 MySql로 작성되었으며, **로컬 환경에 맞게 .yml에서 별도의 DB 연결 설정이 필요**할 수 있습니다.
- 전반적인 실행을 위해 우선 더미 데이터 등록이 필요합니다.


#### Postman App
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/20423736-6c720510-3e35-4783-bc44-5e177210e3a9?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D20423736-6c720510-3e35-4783-bc44-5e177210e3a9%26entityType%3Dcollection%26workspaceId%3D21690344-85e0-491e-a567-dca00482effc)
<br>
#### Postman Browser
[![Run in Postman](https://run.pstmn.io/button.svg)](https://www.postman.com/satellite-technologist-21539520/workspace/marcket-likelion/collection/20423736-6c720510-3e35-4783-bc44-5e177210e3a9?action=share&creator=20423736)   

### Postman JSON
[postman_collection_json](https://github.com/likelion-backend-5th/Project_1_KimDoyoung/blob/main/docs/market_likelion.postman_collection.json)   

<br>
<br>

