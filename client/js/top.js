$(function(){
    $.ajax({
        url:"../commons/top.html",
        dataType:"text",
        type:"get",
        statusCode:{
           200:function(data){
               $("#top").html(data);
           }
        }
    });

});