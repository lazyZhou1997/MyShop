
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
            userdate = userdate.substring(0,10);
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

//order
$(function () {
    $.getJSON("/getUserOrders",function (data) {
        console.log(data);
        for(var i=0;i<data.length;i++){
            $.post("/getOrderItems",{
                orderID : data[i].orderId
            },function (DATA) {
                console.log(DATA);
            })
        }

        // for(var i = 0; i<data.length;i++){
        //     var tr = "<tr>";
        //     if(i == 0){
        //         tr += "<td rowspan='3'>" + "asdfg" + "</td>";
        //     }
        //     tr += "<td class = 'order-img'>" + "<a href=''><img alt='' src='" + "images/cart/one.png" + "'></a>";
        //     tr += "</td>" + "<td>";
        //     tr += "<h4><a href=''>" + "Testsd" + "</a></h4>";
        //     tr += "<span>Web ID:roysifan</span>";
        //     tr += "</td>";
        //     tr += "<td>" + "12" + "</td>";
        //     tr += "<td>" + "3" + "</td>";
        //     tr += "<td>" + "36" + "</td>";
        //     tr += "<td>" + "已付款"+ "</td>";
        //     tr += "<td>" + "<a class=\'glyphicon glyphicon-trash\'href=\'\'></a></td>";
        //     tr += "</tr>";
        //     $('#order-info').append(tr);
        // }
    })
})

//address
$(function () {
    $.getJSON("/getUserAddress",function (data) {
        console.log(data);
        for(var i=0;i<data.length;i++){
            var tr = "<tr>";
            tr += "<td>" + data[i].userId + "</td>";
            tr += "<td>" + data[i].addressInfo + "</td>";
            tr += "<td>" + data[i].phoneNumber + "</td>";
            tr += "<td>" + "<a href=\"javascript:void(0);\">修改</a>|<a href=\"javascript:void(0);\">删除</a>" + "</td>";
            if(data[i].isDefaultAddress == false) {
                tr += "<td>" + "</td>";
            }
            else {
                tr += "<td>" + "默认地址" +"</td>";
            }
                tr += "</tr>";
            $('#address-info').append(tr);
        }
    })

})


$(function () {
    $.getJSON("/getUserMessage", function (data) {
        var unread = 0;
        for (var i = 0; i < data.length; i++) {
            var div = "<div class=\"notif\">";
            if(data[i].isRead == false){
                div += "<div class=\"notif-status col-sm-1 col-md-1\">未读</div>";
                unread ++;
            }
            else{
                div += "<div class=\"notif-status col-sm-1 col-md-1\">已读</div>";
            }
            div += "<div class=\"col-sm-11 col-md-11\">\n" +
                "<div class=\"notif-head\">";
            div += "<span>" + data[i].senderId + "</span>";
            if(data[i].sendTime != null){
                div += "<span>" + data[i].sendTime + "</span>";
            }
            div += "</div>";
            div += "<div class=\"notif-body fa-border\">";
            div += "<p>" + data[i].messageContent + "</p>";
            div += "</div>";
            div += "</div>";
            div += "</div>";
            $('#user-notification').append(div);
        }
        $('.badge').text(unread);
    })
})

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