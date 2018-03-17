function checkUser() {
    var username = $('input[name=userName]').val();
    var useremail = $('input[name=userId]').val();
    var userpsw = $('input[name=userPassword]').val();

    console.log("test");
    if ("" == username) {
        alert("未输入用户名，请重试");
        return false;
    }
    if ("" == useremail) {
        alert("未输入邮箱，请重试");
        return false;
    }
    if (!userpsw) {
        alert("未输入密码，请重试");
        return false;
    }

}

function test() {
    alert("test if no");

}