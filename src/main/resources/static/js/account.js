$(function () {
    $.getJSON("http://localhost:8080//getUserInfo",function(data){
        var username = data.data.userName;
        var userimg = data.data.headImg;
        var useremail = data.data.userId;
        var userdate = data.data.birthday;

        $('#user-info-name').attr('placeholder',username);
        $('#user-info-email').attr('placeholder',useremail);
        if(null != userdate ){
            $('#user-info-date').attr('placeholder',userdate);
        }
        $('#user-info-image').attr('src',userimg);
    })
});