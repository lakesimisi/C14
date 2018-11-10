$(function(){
    $("#btn1").click(function(){

        if($("#username").val() == ""){
            alert("用户名不能为空！");
            return ;
        }
        if($("#password").val()==""){
            alert("密码不能为空！");
            return ;
        }
        var URL="http://localhost:8080/acc/login";
        var b=JSON.stringify($("#form1").serializeObject());
        $.ajax({
            contentType:"application/json",
            url:URL,
            type:"post",
            data:b,
            statusCode:{
                200:function(data){
                   // alert(data);
                    window.location="../shop/main.html";
                },
                404:function(){
                    alert("登录失败");
                    window.location="../shop/login.html";
                }
            }

        });

    });
});
