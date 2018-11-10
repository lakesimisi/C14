$(function(){
    $.ajax({
        url:"../commons/bottom.html",
        dataType:"text",
        type:"get",
        statusCode:{
            200:function(data){
                $("#bottom").html(data);
            }
        }

    });

});