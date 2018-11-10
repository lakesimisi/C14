$(function(){
    var url=window.location.href;
    var str=url.substring(url.lastIndexOf('/')+1);
    var p=str.split("?")[0];
    // alert(p)
    if(p.indexOf("product")!=-1){
        var str1=str.substring(str.indexOf("?")+1);
        var category=str1.split("=")[1];
        queryProductByCategory(category);

    }
    else if(p.indexOf("items")!=-1){
        var str1 = str.substring(str.lastIndexOf('?') + 1);
        var pro=str1.split("=")[1];
        queryItemsByPro(pro);
    }
    else if(p.indexOf("item")!=-1){
        var str1 = str.substring(str.lastIndexOf('?') + 1);
        var item=str1.split("&")[0];
        var iid=item.split("=")[1];
        var pro=str1.split("&")[1];

        var pid=pro.split("=")[1];
        queryItemByPro(iid,pid);

    }
});
function queryProductByCategory(cate){
    var URL="http://localhost:8080/pet/qProduct/"+cate;
    $.ajax({
        url:URL,
        type:"get",
        statusCode:{
            200:function(data){
                showProduct(data)
            },
            404:function(){
                alert("查询产品失败")
            }
        }
    })
}
function showProduct(data){
    $("#msg>tbody").empty();
    var d=$("#msg>thead>tr:last").attr("id");
    var str="";
    $(data).each(function(index,value){
        str+="<tr id='"+(++d)+"'bgcolor=\"#FFFFFF\">"+
            "<td>"+"<b><a href=\"items.html?pid="+value.productid+"\">"+
            value.productno+"</a></b>"+"</td>"+

            "<td>" +
            "<b>" +value.name+
            "</b>" +
            "</td>" +

            "<td>" +
            "<b>" +value.descn+
            "</b>" +
            "</td>" +

            "<td>" +
            "<image src='../images/"+value.pic+"'/>"+
            "</td>"

    })
    $("#msg>tbody").append(str)

}
function queryItemsByPro(pro){
    var URL="http://localhost:8080/pet/qItems/"+pro;
    $.ajax({
        url:URL,
        type:"get",
        statusCode:{
            200:function(data){
                showItems(data)
            },
            404:function(){
                alert("查询具体项目失败")
            }
        }
    })
}
function showItems(data){
    $("#msg>tbody").empty()
    var d=$("#msg>thead>tr:last").attr("id");
    var str="";
    $(data).each(function(index,value){
        str+="<tr id='"+(++d)+"'bgcolor=\"#FFFFFF\">"+
            "<td>"+"<b><a href=\"item.html?iid="+value.itemid+"&pid="+value.product.productid+"\">"+value.itemno+"</a>"
            +"</b></td>"+

            "<td>" +
            "<b>" +value.product.productno+
            "</b>" +
            "</td>" +

            "<td>" +
            "<b>" +value.product.name+
            "</b>" +
            "</td>" +

            "<td>" +
            "<b>" +value.listprice+
            "</b>" +
            "</td>" +

            "<td>" +
            "<img src='../images/"+value.product.pic+"'/>" +
            "</td>" +

            "<td>" +
            "<b>" +value.product.descn+"-"+value.attr1+
            "</b>" +
            "</td>" +

            "<td><b>"+
            "<input onclick=\"addCartForItems("+value.itemid+",1)\" type='image' src=\"../images/button_add_to_cart.gif\"/>"+
            "</b></td>"+

            "</tr>"
    })
    $("#msg>tbody").append(str);
}
function  queryItemByPro(iid,pid){
    var URL="http://localhost:8080/pet/qItem/"+iid+"/"+pid;
    $.ajax({
        url:URL,
        type:"get",
        statusCode:{
            200:function(data){
                showItem(data);
            },
            404:function(data){
                alert("查询物品具体信息失败")
            }

        }
    })
}
function showItem(data){
    $(data).each(function(index,value){
        $("#pic").attr("src","../images/"+value.product.pic);

        $("#productno").html(value.product.productno);
        $("#itemno").html(value.itemno);
        $("#itemid").val(value.itemid);
        $("#productid").val(value.product.productid);
        $("#price").html(value.listprice);
        $("#descn").html(value.product.descn);

    })




}