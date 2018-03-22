// TODO: 删除商品后刷新
// TODO: 自动删除商品后刷新
// TODO: 商品数量区间提示
// TODO: 输入框


window.onload = function (ev) {
    $(".product-tr-template").hide(); // hide template
    $.get(
        "/productInCart",
        function (result) {
            showProducts(result);
        }
    )
}

function showProducts(data) {
    var productCount = data.length;
    var htmlCode = "";
    if (productCount == 0) {
        alert("nothing");
        htmlCode += "nothing";// 在这里新增空样式
        return;
    }
    for (var i = 0; i < productCount; i++) {
        var productRow = $(".product-tr-template").clone(true).prependTo("#product-tbody");
        productRow.children(".cart_product").find("img").attr("alt", data[i].productId);
        productRow.children(".cart_description").find("a").text(data[i].productName);
        productRow.children(".cart_description").find("p").text(data[i].productDescription);
        productRow.children(".cart_price").find("p").text(data[i].productPrice);
        productRow.children(".cart_quantity").find("input").attr("value", data[i].productLeftTotals);
        productRow.children(".cart_total").find("p").text(data[i].productPrice * data[i].productLeftTotals);
        productRow.find(".cart_quantity_delete").attr("href", "/deleteProduct?productID="+data[i].productId);
        productRow.attr("class", "product-tr"); // change id to divide from template
        productRow.show();
        showImage(productRow, data[i].productId);
    }
    $(".product-tr-template").remove();
}

function showImage(productRow, productID) {
    $.get(
        "/productImageInCart",
        {productID: productID},
        function (result) {
            productRow.children(".cart_product").find("img").attr("src", result);
        }
    );
}

// listen to item row
$(".check").change(function () {
    reComputeAll();
});

function reComputeAll() {
    var totalCount = 0;
    var totalPrice = 0;
    $(".product-tr").each(function () {
        var isChecked = $(this).find(".check").prop("checked");
        if (isChecked) {
            var itemCount = parseInt($(this).find(".cart_quantity_input").val());
            var itemTotalPrice = parseFloat($(this).find(".cart_total_price").text());
            totalCount += itemCount;
            totalPrice += itemTotalPrice;
        }
        else {
            // unselect "SelectAll" box
            $("#cart_purchase_bar").find("input").prop("checked", false);
        }
    });
    $(".cart_quantity").children("em").text(totalCount);
    $("#total_price").html(totalPrice);
}

// listen to select all
$("#cart_purchase_bar").find("input").change(function () {
    var isChecked = $(this).prop("checked");
    $(".product-tr").each(function () {
        $(this).find(".check").prop("checked", isChecked);
    });
    reComputeAll();
});

// listen to increase product
$(".cart_quantity_up").click(function () {
    var thisObj = $(this);
    var rowObj = $(this).parents("tr");
    $.post(
        "/modifyProductNumber",
        {
            productID: $(this).parents(".product-tr").find("img").attr("alt"),
            productNumber: parseInt($(this).parents(".product-tr").find(".cart_quantity_input").val()) + 1,
        },
        function (result) {
            if (result == null || result == "") {
                var itemCount = parseInt(thisObj.siblings("input").val());
                thisObj.siblings(".cart_quantity_input").attr("value", itemCount + 1);

                // update row
                var producePrice = parseFloat(rowObj.children(".cart_price").find("p").text());
                var productCount = parseInt(rowObj.children(".cart_quantity").find("input").val());
                rowObj.children(".cart_total").find("p").text(producePrice * productCount);

                reComputeAll();
            }
            else {
                swal("",result, "error");
            }
        }
    );
});

// listen to decrease product
$(".cart_quantity_down").click(function () {
    var thisObj = $(this);
    var rowObj = $(this).parents("tr");
    $.post(
        "/modifyProductNumber",
        {
            productID: $(this).parents(".product-tr").find("img").attr("alt"),
            productNumber: parseInt($(this).parents(".product-tr").find(".cart_quantity_input").val()) - 1,
        },
        function (result) {
            if (result == null || result == "") {
                var itemCount = parseInt(thisObj.siblings("input").val());
                thisObj.siblings(".cart_quantity_input").attr("value", itemCount - 1);

                // update row
                var producePrice = parseFloat(rowObj.children(".cart_price").find("p").text());
                var productCount = parseInt(rowObj.children(".cart_quantity").find("input").val());
                rowObj.children(".cart_total").find("p").text(producePrice * productCount);

                reComputeAll();
            }
            else {
                swal("",result, "error");
            }
        }
    );
});

// listen to input label
// remember old value
$(".cart_quantity_input").focus(function () {
    $(this).attr("oldValue", $(this).val());
});
// check new value
$(".cart_quantity_input").blur(function () {
    var thisObj = $(this);
    var rowObj = $(this).parents("tr");
    $.post(
        "/modifyProductNumber",
        {
            productID: $(this).parents(".product-tr").find("img").attr("alt"),
            productNumber: parseInt($(this).parents(".product-tr").find(".cart_quantity_input").val()),
        },
        function (result) {
            if (result == null || result == "") {
                var itemCount = parseInt(thisObj.siblings("input").val());
                thisObj.siblings(".cart_quantity_input").attr("value", itemCount);

                // update row
                var producePrice = parseFloat(rowObj.children(".cart_price").find("p").text());
                var productCount = parseInt(rowObj.children(".cart_quantity").find("input").val());
                rowObj.children(".cart_total").find("p").text(producePrice * productCount);

                reComputeAll();
            }
            else {
                swal("",result, "error");
                thisObj.val(thisObj.attr("oldValue"));
            }
        }
    );
});

// modify submit data
$('#settle-account').submit(function (e) {
    var form = $(this);
    $(".product-tr").each(function () {
        var isChecked = $(this).find(".check").prop("checked");
        if (isChecked) {
            var productID = $(this).find("img").attr("alt");
            $("<input />").attr("type", "hidden").attr("name", "productID").attr("value", productID).appendTo(form);
        }
        else {
            $(this).find(".cart_quantity_input").attr("disabled", "disabled");
        }
    });
    return true;
});