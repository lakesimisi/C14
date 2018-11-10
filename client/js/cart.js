$(function(){
    $("#add").click(function(){
        var URL="http://localhost:8080/acc/verify";
        $.ajax({
            url:URL,
            type:"get",
            dataType:"text",
            statusCode:{
                200:function(data){
                    var itemid=$("#itemid").val();
                    var quantity=$("#quantity").val();
                    add(data.toString().split(":")[0],itemid,quantity);
                },404:function(data){
                    alert("账户认证失败")
                }
            }

        })
    })
    $("#checkout").click(function(){
        var URL="http://localhost:8080/acc/verify";
        $.ajax({
            url:URL,
            type:"get",
            dataType:"text",
            statusCode:{
                200:function(data){
                   var orders={
                       "userid":data.toString().split(":")[0],
                       "orderid":$("input[name='oid']").val(),
                       "totalprice":$("#total").text()
                   }
                   var d=JSON.stringify(orders);
                   var URL2="http://localhost:8080/cart/checkout";
                   $.ajax({
                       url:URL2,
                       type:"post",
                       contentType:"application/json",
                       data:d,
                       statusCode:{
                           200:function(data){
                               window.location="../shop/main.html";
                           },404:function(data){
                               alert("结算失败")
                           }

                       }


                   })


                },404:function(data){
                    alert("账户认证失败")
                }
            }

        })
    })
});
function addCartForItems(itemid,quantity){//为items页面写的
    var URL="http://localhost:8080/acc/verify";
    $.ajax({
        url:URL,
        type:"get",
        dataType:"text",
        statusCode:{
            200:function(data){
                add(data.toString().split(":")[0],itemid,quantity);
            },404:function(data){
                alert("账户认证失败")
            }
        }

    })
}
function add(userid,itemid,quantity){
   var URL="http://localhost:8080/cart/add"
   var cart={
       "userid":userid,
       "itemid":itemid,
       "quantity":quantity
       // "item":{
       //     "productid":$("#productid").val()
       // }
   };
   var s=JSON.stringify(cart);
   $.ajax({
       contentType:"application/json",
       url:URL,
       data:s,
       type:"post",
       dataType:"text json",
       statusCode:{
           200:function(data){
               window.location="../shop/cart.html"
           },404:function(data){
               $("#loginname").html("");
               window.location="../shop/login.html";
           }
       }
   })

}
function showCart(userid){
    var URL="http://localhost:8080/cart/show/"+userid;
    $.ajax({
        url:URL,
        type:"get",
        dataType:"text json",
        statusCode:{
            200:function(data){
                $("#msg>tbody").empty();
                var d=$("#msg>thead>tr:last").attr("id");
                var str="";
                var total=0;
                $(data).each(function(index,value){
                    total+=value.quantity*value.item.listprice;
                    str+="<tr id='"+(++d)+"'bgcolor=\"#FFFFFF\">"+
                        "<td>" +
                        "<b><a href=\"item.html?iid="+value.itemid+"&pid="+value.item.product.productid+"\">" +value.item.itemno+
                        "</a>" +
                        "</b>" +
                        "</td>" +

                        "<td>" +
                        "<b>" +value.item.product.productno+
                        "</b>" +
                        "</td>" +

                        "<td>" +
                        "<b>" +value.item.product.descn+
                        "</b>" +
                        "</td>" +

                        "<td>" +
                        "<b>" +value.item.attr1+
                        "</b>" +
                        "</td>" +

                        "<td>" +
                        "<img src='../images/"+value.item.product.pic+"'/>" +
                         "<input type='hidden' value='"+value.orderid+"' name='oid'>"+
                        "</td>" +

                        "<td>" +value.item.listprice+
                        "</td>" +

                        "<td><b>"+
                        "<input type='number' maxlength='2' onchange=\"update(this.value,"+value.itemid+","+value.orderid+")\"  id=\"qty"+value.itemid+"\" value=\""+value.quantity+"\"/>"+
                        "</b>" +
                        +"</td>"

                        +"<td>" +(value.quantity* value.item.listprice)+
                        "</td>" +

                        "<td>" +
                        "<b>"+"<input onclick=\"del("+value.orderid+","+value.itemid+")\" type='image' src=\"../images/button_remove.gif\"/>"+
                        "</b>" +
                        "</td></tr>"
                })
                $("#msg>tbody").append(str);
                $("#total").html(total)

            },404:function(data){
                $("#loginname").html("");
                window.location="../shop/login.html";
            }
        }
    })
}
// function del(orderid,itemid){
//     var URL="http://localhost:8080/acc/verify";
//     $.ajax({
//         url:URL,
//         type:"get",
//         dataType:"text",
//         statusCode:{
//             200:function(data){
//                 alert("odfijsk")
//                 realDel(data.toString().split(":")[0],orderid,itemid);
//             },
//             404:function(data){
//                 $("#loginname").html("");
//                 window.location="../shop/login.html";
//             }
//         }
//     });
// }
function del(orderid,itemid){
    var URL="http://localhost:8080/acc/verify";
    $.ajax({
        url:URL,
        type:"get",
        dataType:"text",
        statusCode:{
            200:function(data){

                realDel(data.toString().split(":")[0],orderid,itemid);
            },
            404:function(data){
                $("#loginname").html("");
                window.location="../shop/login.html";
            }
        }
    });


}

function realDel(userid,orderid,itemid){//删除
    var URL="http://localhost:8080/cart/del";

    var cart={
        "userid":userid,
        "itemid": itemid,
        "orderid":orderid
    }
    var d=JSON.stringify(cart);
    $.ajax({
        contentType:"application/json",
        url:URL,
        data:d,
         type:"post",
        statusCode:{
            200:function(data){
                window.location="../shop/cart.html";
            },
            404:function(data){


            }
        }
    });

}

function update(quantity,itemid,orderid){
    var URL="http://localhost:8080/acc/verify";
    $.ajax({
        url:URL,
        type:"get",
        dataType:"text",
        statusCode:{
            200:function(data){

                realUpdate(quantity,data.toString().split(":")[0],orderid,itemid);
            },
            404:function(data){
                $("#loginname").html("");
                window.location="../shop/login.html";
            }
        }
    });
}
function realUpdate(quantity,userid,orderid,itemid) {//真正修改
    var URL="http://localhost:8080/cart/update";

    var cart={
        "userid":userid,
        "itemid": itemid,
        "orderid":orderid,
        "quantity":quantity

    }
    var d=JSON.stringify(cart);
    $.ajax({
        contentType:"application/json",
        url:URL,
        data:d,
        type:"post",
        statusCode:{
            200:function(data){
                window.location="../shop/cart.html";
            },
            404:function(data){
                alert("增加数量失败")
            }
        }
    });

}