
$(function () {
    $.getJSON("http://localhost:8080/getUserInfo",function(data){
        var username = data.data.userName;
        var userimg = data.data.headImg;
        var useremail = data.data.userId;
        var userdate = data.data.birthday;
        console.log(username);
        // 打印用户信息
        $('#user-info-name').attr('placeholder',username);
        $('#user-info-email').attr('placeholder',useremail);
        if(null != userdate ){
            $('#user-info-date').attr('placeholder',userdate);
        }
        $('#user-info-image').attr('src',userimg);
    })
})

function check() {
 var username = $('#user-info-name').val();
 var userdate = $('#user-info-date').val();
 var userpsw1 = $('#user-info-psw1').val();
 var userpsw2 = $('#user-info-psw2').val();
 var DATE_FORMAT = /^[0-9]{4}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1}$/; //日期的正则表达式
 //对输入内容的判断
 //至少得输入一项
 if(!username && !userdate && !userpsw1 && !userpsw2) {
     swal("", "请至少修改一项内容", "error");
     return false;
 }
 //密码得一样
 else if(userpsw2 != userpsw1){
     swal("","两次密码输入不一致，请重试","error");
     return false;
 }
 //输入的格式不能错
 else if(userdate && !DATE_FORMAT.test(userdate)){
     swal("","输入的日期格式有误，请按照xxxx-xx-xx的格式输入","error");
     return false;
 }

}


//上传图片功能
$('#user-info-image').click(function () {
    $('#choose-image').trigger('click');
})
$('#choose-image').change(function () {
    $('#submit-image').submit();
    //     $('#submit-image').ajaxSubmit({
    //         url: "/fileUpload",
    //         success:function (data) {
    //             alert(data);
    //         }
    //     });
    //     return false;
        //     event.preventDefault();
        //     var form = $(this);
        //     console.log(form);
        //     // mulitipart form,如文件上传类
        //     var formData = new FormData(this);
        //     console.log(formData);
        //     $.ajax({
        //         type: form.attr('method'),
        //         url: form.attr('action'),
        //         data: formData,
        //         mimeType: "multipart/form-data",
        //         contentType: false,
        //         cache: false,
        //         processData: false
        //         }).success(function () {
        //             swal("","sd","success");
        //         }).fail(function (jqXHR, textStatus, errorThrown) {
        //             swal("","sd","error");
        //         });
        // });
    })


//订单信息
// <tr>
// <td>123456798</td>
// <td class="order-img">
//     <a href=""><img src="images/cart/one.png" alt=""></a>
//     </td>
//     <td>
//     <h4><a href="">Colorblock Scuba</a></h4>
// <span>Web ID: 1089772</span>
// </td>
// <td>$59</td>
// <td>1</td>
// <td>$59</td>
// <td>已完成</td>
// <td>
// <a class="glyphicon glyphicon-trash" href=""></a>
//     </td>
//     </tr>
$(function () {
    // $.getJSON("url of order ",function (data) {
    //     var order_id = data.order_id;
    //     var order_image  = data.image;
    //     var order_info = data.order_info;
    //     var order_price = data.order_price;
    //     var order_mount = data.order_mount;
    //     var order_status = data.order_status;
    //
    //     for(var i = 0; i<data.length;i++){
    //         var tr = "<tr>";
    //         tr += "<td>" + order_id + "</td>";
    //         tr += "<td class = 'order-img'>" + "<a href=''><img alt='' src='" + order_image + "'></a>";
    //         tr += "</td>" + "<td>";
    //         tr += "<h4><a href=''>" + order_info + "</a></h4>";
    //         tr += "<span>Web ID:roysifan</span>";
    //         tr += "</td>";
    //         tr += "<td>" + order_price + "</td>";
    //         tr += "<td>" + order_mount + "</td>";
    //         tr += "<td>" + order_price * order_mount + "</td>";
    //         tr += "<td>" + order_status + "</td>";
    //         tr += "<td>" + "<a class=\'glyphicon glyphicon-trash\'href=\'\'></a></td>";
    //         tr += "</tr>";
    //     }
    // })
    for(var i = 0; i<3;i++){
        var tr = "<tr>";
        if(i == 0){
             tr += "<td rowspan='3'>" + "asdfg" + "</td>";
        }
        tr += "<td class = 'order-img'>" + "<a href=''><img alt='' src='" + "images/cart/one.png" + "'></a>";
        tr += "</td>" + "<td>";
        tr += "<h4><a href=''>" + "Testsd" + "</a></h4>";
        tr += "<span>Web ID:roysifan</span>";
        tr += "</td>";
        tr += "<td>" + "12" + "</td>";
        tr += "<td>" + "3" + "</td>";
        tr += "<td>" + "36" + "</td>";
        tr += "<td>" + "已付款"+ "</td>";
        tr += "<td>" + "<a class=\'glyphicon glyphicon-trash\'href=\'\'></a></td>";
        tr += "</tr>";
        $('#order-info').append(tr);
    }
})

//地址管理
// <tr>
// <td>张三</td>
// <td>四川省成都市双流县西航港街道</td>
// <td>四川大学江安校区</td>
// <td>621107</td>
// <td>12345678901</td>
// <td><a href="">修改</a>|<a href="">删除</a></td>
// <td>默认地址</td>
// </tr>
$(function () {
    $.getJSON("/getUserAddress",function (data) {
        console.log(data);
        for(var i=0;i<data.length;i++){
            var tr = "<tr>";
            tr += "<td>" + "name" + "</td>";
            tr += "<td>" + "address" + "</td>";
            tr += "<td>" + "address in detail" + "</td>";
            tr += "<td>" + "youbian" + "</td>";
            tr += "<td>" + "phone" + "</td>";
            tr += "<td>" + "<a href=\"\">修改</a>|<a href=\"\">删除</a>" + "</td>";
            tr += "<td>" + "么人地址" + "</td>";
            tr += "</tr>";
            $('#address-info').append(tr);
        }
    })

})

//消息通知
// <div class="notif">
//     <div class="notif-status col-sm-1 col-md-1">未读</div>
//     <div class="col-sm-11 col-md-11">
//     <div class="notif-head">
//     <span>管理员001，</span>
// <span>一小时前</span>
// </div>
// <div class="notif-body fa-border">
//     <p>前言：近年来,随着留学市场的成熟,人们在考虑留学时越来越理性,不再是人云亦云随大流。许多欧洲小语种国家开始受到青睐,这些国家成本较低，大学免收学费及承认中国的学位而成为近年留学的热门国家。伴随互联网的迅猛发展，欧洲留学凭借它低成本、高回报的特…</p>
// </div>
// </div>
// </div>
$(function () {
    $.getJSON("/getUserMessage", function (data) {
        console.log(data);
        for (var i = 0; i < data.length; i++) {
            var div = "<div class=\"notif\">";
            div += "<div class=\"notif-status col-sm-1 col-md-1\">未读</div>";
            div += "<div class=\"col-sm-11 col-md-11\">\n" +
                "<div class=\"notif-head\">";
            div += "<span>" + "name of guider" + "</span>";
            div += "<span>" + "time" + "</span>";
            div += "</div>";
            div += "<div class=\"notif-body fa-border\">";
            div += "<p>" + "main info sadksaljdhewlwqdlsandalhdkjj2elksdasjldjadlkasjdljasd" + "</p>";
            div += "</div>";
            div += "</div>";
            div += "</div>";
            $('#user-notification').append(div);
        }
    })
})

// //js实现标签栏跳转至对应的窗口
//     $(function () {
//         $('#shop-menu > li').click(function () {
//             //     $('#shop-menu2 > li .active').removeClass('active');
//             //     console.log($('li .active'));
//             $('#shop-menu2 > li').each(function () {
//                 if($(this).hasClass('active')){
//                     $(this).removeClass('active');
//                 }
//             })
//             $('.tab-content > div').each(function () {
//                 if($(this).hasClass('active')){
//                     $(this).removeClass('active');
//                 }
//             })
//
//             var id = $(this).children('a').attr('href');
//                 $('#shop-menu2 > li > a').each(function () {
//                     if ($(this).attr('href') == id) {
//                         $(this).parent().addClass("active");
//                         $(id).addClass("active");
//                     }
//                 })
//             // })
//         })
//     })

//js实现标签栏跳转至对应的窗口
$(function () {
    var url = decodeURI(window.location.href);
    var argsIndex = url.split("?kw=");
    var arg = argsIndex[1];
    console.log(arg);
    // $('#shop-menu > li').click(function () {
    //     $('#shop-menu2 > li .active').removeClass('active');
    //     console.log($('li .active'));
    $('#shop-menu2 > li').each(function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        }
    })
    $('.tab-content > div').each(function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        }
    })
    var id = "#" + arg;
    $('#shop-menu2 > li > a').each(function () {
        if ($(this).attr('href') == id) {
            $(this).parent().addClass("active");
            $(id).addClass("active");
        }
    })
})