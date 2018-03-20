function checkUser() {
    var username = $('input[name=userName]').val();
    var useremail = $('input[name=userId]').val();
    var userpsw = $('input[name=userPassword]').val();
    var userpsw2 = $('input[name=userPassword2]').val();

    if ("" == username) {
        swal("未输入用户名，请重试");
        return false;
    }
    if ("" == useremail) {
        swal("未输入邮箱，请重试");
        return false;
    }
    if (!userpsw) {
        swal("未输入密码，请重试");
        return false;
Y    if(userpsw!=userpsw2){
        swal("密码不一致","您两次输入的密码似乎不一样，请重新输入","error");
        return false;
    }

}