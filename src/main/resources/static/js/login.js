

function checkUser() {
    var username = $('input[name=username]').val();
    var userpsw = $('input[name=password]').val();
    if ("" == username) {
        swal("未输入用户名，请重试");
        return false;
    }
    else if (!userpsw) {
        swal("未输入密码,请重试");
        return false;
    }
}