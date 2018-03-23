$(function () {
    $.getJSON("/getUserInfo",function (data) {
        var user_name  = data.data.userName;
        console.log(user_name);
        $('#user-name').text(user_name);
    })

    if (!window.EventSource) {
        swal("", "您的浏览器不支持实时消息通知", "error");
    }
    var source = new EventSource('serverMessage');
    var str = "";
    source.addEventListener('message', function (e) {
        var messageList = JSON.parse(e.data);
        var messageLength = messageList.length;
        $("#notif-no").text(messageLength);
    });
})
//购物车
$(function (ev) {
    $(".cart-tr-template").hide();
    $.get(
        "/productInCart",
        function (result) {
            showCart(result);
        }
    )
})
function showCart(data) {
    var productCount = data.length;
    $(".cart-logo").find("span").text(productCount);
    if (data.length == 0) {
        $("#none-tips").show();
        $("#have-info").hide();
        return;
    }
    for (var i = 0; i < productCount; i++) {
        var productRow = $(".cart-tr-template").clone(true).appendTo("#cart-tbody");
        productRow.find(".cart-inshort-des").children("span").text(data[i].productName);
        productRow.find(".cart-inshort-pri").children("span:eq('0')").text("￥" + data[i].productPrice);
        productRow.find(".cart-inshort-pri").children("span:eq('1')").text(data[i].productLeftTotals);
        productRow.find(".cart-inshort-pic").find("img").attr("alt", data[i].productId);
        productRow.attr("class", "cart-tr");
        productRow.show();
        showImageInCart(productRow, data[i].productId);
    }
    $(".cart-tr-template").remove();
}

function showImageInCart(productRow, productID) {
    $.get(
        "/productImageInCart",
        {productID: productID},
        function (result) {
            productRow.children(".cart-inshort-pic").find("img").attr("src", result);
            //TODO: product page productRow.children(".cart-inshort-pic").find("a").attr("href", result);
        }
    );
}


$('#search-area').keydown(function (e) {
    if (13 == e.which) {
        console.log("test");
        var url = "search-results.html?kw=" + $('#search-area').val();
        window.location.href = url;
    }
})