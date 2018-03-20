$(function () {
    $.getJSON("http://localhost:8080//getUserInfo",function(data){
        var username = data.data.userName;
        var userimg = data.data.headImg;
        var useremail = data.data.userId;
        var userdate = data.data.birthday;

        // 打印用户信息
        $('#user-info-name').attr('placeholder',username);
        $('#user-info-email').attr('placeholder',useremail);
        if(null != userdate ){
            $('#user-info-date').attr('placeholder',userdate);
        }
        $('#user-info-image').attr('src',userimg);
    })
});

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