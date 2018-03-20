$(function () {
    $.getJSON("http://localhost:8080//getUserInfo",function(data){
        var username = data.data.userName;
        console.log(username);
        var userimg = data.data.headImg;
        console.log(userimg);
        var useremail = data.data.userId;
        console.log(useremail);
        var userdate = data.data.birthday;
        console.log(userdate);


    })
});