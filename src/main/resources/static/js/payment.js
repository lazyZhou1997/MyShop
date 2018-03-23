// get user address
window.onload = function (ev) {
    $("#address-li-template").hide();
    $(".cart-tr-template").hide();
    $.get(
        "/getUserAddress",
        function (result) {
            showAddress(result);
        }
    );
    $.get(
        "/productInCart",
        function (result) {
            showCart(result);
        }
    )

    // show message count
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
};

function showAddress(data) {
    var addressCount = data.length;
    for (var i = 0; i < addressCount; i++) {
        var addressRow = $("#address-li-template").clone(true).appendTo(".address-list");
        addressRow.find(".user-addr").children("span").text(data[i].addressInfo);
        addressRow.find(".user-addr").children("em").text(data[i].phoneNumber);
        addressRow.attr("class", "address-wrap address-li");
        addressRow.find(".hidden-id").attr("value", data[i].addressId);
        if (data[i].isDefaultAddress) {
            addressRow.addClass("selected");
            addressRow.find("input").prop("checked", true);
            $(".confirmAddr-addr").children(".confirmAddr-addr-bd").text(data[i].addressInfo);
            $(".confirmAddr-addr-user").children(".confirmAddr-addr-bd").text(data[i].phoneNumber);
        }
        else {
            addressRow.find("input").prop("checked", false);
        }
        addressRow.show();
    }
    $("#address-li-template").remove();
}

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

$(".addressInput").click(function () {
    var addressInfo = $(this).parents("li").find(".user-addr").children("span").text();
    var phoneNumber = $(this).parents("li").find(".user-addr").children("em").text();
    $(".confirmAddr-addr").children(".confirmAddr-addr-bd").text(addressInfo);
    $(".confirmAddr-addr-user").children(".confirmAddr-addr-bd").text(phoneNumber);
});

// set the style of addresses
$(document).ready(function(){
    $(".addressInput").change(function(){
        $(".addressInput").each(function() {
            $(this).parents('.address-wrap').first().removeClass('selected');
        });
        $(this).parents('.address-wrap').first().addClass('selected');
    });
});

function showImage(productRow, productID) {
    $.get(
        "/productImageInCart",
        {productID: productID},
        function (result) {
            productRow.children(".cart_product").find("img").attr("src", result);
        }
    );
}

function reComputeAll() {
    var total = 0;
    $(".product-tr").each(function () {
        total += parseFloat($(this).children(".cart_total").find("p").text());
    });
    $(".realPay-price").text(total);

    if (total == 0) {
        window.location.href = "cart.html";
    }
}

// listen to delete <a>
$(".cart_quantity_delete").click(function () {
   $(this).parents("tr").remove();
   reComputeAll();
});