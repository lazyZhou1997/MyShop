$(function () {
    $.getJSON("http://localhost:8080/getUserInfo",function (data) {
        var user_name  = data.data.userName;
        console.log(user_name);
        $('#user-name').text(user_name);
    })
})