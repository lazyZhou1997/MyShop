

function checkUser() {
    var username = $('input[name=username]').val();
    var userpsw = $('input[name=password]').val();
    if ("" == username) {
        swal("未输入用户名，请重试");
        return false;
    }
    else if (!userpsw) {
        swal("","未输入密码,请重试","error");
        return false;
    }else {
        $.ajax({
            type: 'POST',
            url:'/check',
            data:{
                username:username,
                password:userpsw
        },
            success:function (data,status) {
                if(data=="false"){
                    alert("worng");
                }
                else {
                    if(data=="wrong"){
                        alert("worong")
                    }
                    if(data=="success"){
                        window.location.href = "/index";
                    }
                }
            }

        })
    }
}