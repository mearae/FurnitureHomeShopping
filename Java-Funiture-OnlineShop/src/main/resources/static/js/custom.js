window.addEventListener('DOMContentLoaded', () => {
    let scrollPos = 0;
    const mainNav = document.getElementById('mainNav');
    const headerHeight = mainNav.clientHeight;
    window.addEventListener('scroll', function() {
        const currentTop = document.body.getBoundingClientRect().top * -1;
        if ( currentTop < scrollPos) {
            // Scrolling Up
            if (currentTop > 0 && mainNav.classList.contains('is-fixed')) {
                mainNav.classList.add('is-visible');
            } else {
                console.log(123);
                mainNav.classList.remove('is-visible', 'is-fixed');
            }
        } else {
            // Scrolling Down
            mainNav.classList.remove(['is-visible']);
            if (currentTop > headerHeight && !mainNav.classList.contains('is-fixed')) {
                mainNav.classList.add('is-fixed');
            }
        }
        scrollPos = currentTop;
    });
})
//------------------- 카트페이지--------------------------------

// 페이지가 로드되면 장바구니 정보를 가져와서 표시합니다.
window.onload = function() {
    axios.get('/carts')
        .then(function(response) {
            var cart = response.data;
            var tableBody = document.getElementById('cartTable').getElementsByTagName('tbody')[0];
            var totalPrice = 0;

            // 장바구니의 각 아이템을 테이블에 표시합니다.
            cart.items.forEach(function(item, index) {
                var row = tableBody.insertRow();
                row.insertCell(0).innerText = index + 1;
                row.insertCell(1).innerText = item.option.name;
                row.insertCell(2).innerText = item.option.price;
                row.insertCell(3).innerText = item.quantity;
                row.insertCell(4).innerText = item.option.price * item.quantity;
                var deleteButton = document.createElement('button');
                deleteButton.innerText = '삭제';
                deleteButton.className = 'btn btn-danger';
                deleteButton.onclick = function() {
                    axios.delete('/cart/remove/' + item.option.id)
                        .then(function() {
                            row.remove();
                        });
                };
                row.insertCell(5).appendChild(deleteButton);

                totalPrice += item.option.price * item.quantity;
            });

            // 총 금액을 표시합니다.
            document.getElementById('totalPrice').innerText = totalPrice;
        });
};

// 결제하기 버튼을 누르면 결제 페이지로 이동합니다.
document.getElementById('checkoutButton').onclick = function() {
    location.href = '/checkout';
};

//------------------- 오더페이지--------------------------------


// 오더페이지 함수
document.addEventListener("DOMContentLoaded", function() {
    // URL에서 파라미터를 읽어와서 선택한 상품들의 정보를 가져옴
    var params = new URLSearchParams(window.location.search);
    var productList = document.querySelector("#order-details ul");

    // URL 파라미터에서 productId에 해당하는 값들을 가져와서 리스트에 추가
    params.getAll("productId").forEach(function(productId) {
        // 서버에서 상품 정보를 가져와서 표시
        fetch("/products/" + productId)
            .then(function(response) {
                return response.json();
            })
            .then(function(product) {
                var listItem = document.createElement("li");
                listItem.textContent = "상품명: " + product.name + " / 가격: " + product.price + "원";
                productList.appendChild(listItem);
            })
            .catch(function(error) {
                console.error("상품 정보를 가져오는 중 오류가 발생했습니다:", error);
            });
    });
});