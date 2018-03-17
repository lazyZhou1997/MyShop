

function checkUser() {
    var username = $('input[name=username]').val();
    var userpsw = $('input[name=password]').val();
    if ("" == username) {
        alert("未输入用户名，请重试");
        return false;
    }
    if (!userpsw) {
        alert("未输入密码，请重试");
        return false;
    }
}