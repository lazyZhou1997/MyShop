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

        // var id = $(this).children('a').attr('href');

        // })
        // })
        // })
    })