$(function(){
    $.ajax({
        url:"../commons/left.html",
        dataType:"text",
        type:"get",
        statusCode:{
            200:function(data){
                $("#left").html(data);
            }
        }

    });

});