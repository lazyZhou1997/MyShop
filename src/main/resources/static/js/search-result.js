// <div class="col-sm-2">
//     <div class="product-image-wrapper">
//     <div class="single-products">
//     <div class="productinfo text-center">
//     <a href="#"><img src="images/home/gallery3.jpg" alt="" /></a>
//     <h2>$56</h2>
//     <p>Easy Polo Black Edition</p>
// <a href="#" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</a>
// </div>
//
//
// </div>
// </div>
// </div>

$(function () {
    $.getJSON("url of search result",function (data) {
        var product_image = data.image;
        var product_description = data.description;
        var product_price = data.price;

        for(var i = 0; i<data.length;i++){
            var div = "<div class=\"col-sm-2\">";
            div += "<div class=\"product-image-wrapper\">";
            div += " <div class=\"single-products\">";
            div += "<div class=\"productinfo text-center\">";
            div += "<a href=\"#\"><img alt = '' src='" + product_image + "'/></a>";
            div += "<h2>" + product_price + "</h2>";
            div += "<p>" + product_description + "</p>";
            div += "<a href=\"#\" class=\"btn btn-default add-to-cart\"><i class=\"fa fa-shopping-cart\"></i>Add to cart</a>";
            div += "</div>";
            $('.tab-content').append(div);
        }

    })
    for(var i = 0; i<4;i++){
        var div = "<div class=\"col-sm-2\">";
        div += "<div class=\"product-image-wrapper\">";
        div += " <div class=\"single-products\">";
        div += "<div class=\"productinfo text-center\">";
        div += "<a href=\"#\"><img src=\"images/home/gallery3.jpg\" alt=\"\" /></a>";
        div += "<h2>" + "213"+ "</h2>";
        div += "<p>" + "nonono + "</p>";
        div += "<a href=\"#\" class=\"btn btn-default add-to-cart\"><i class=\"fa fa-shopping-cart\"></i>Add to cart</a>";
        div += "</div>";
        $('.tab-content').append(div);
    }
})