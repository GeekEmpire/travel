$(function () {
    var keyNow;
    var keyNowIndex;
    var shengheData = [
        {id:"1658979496",name:"张三",zhiwei:"普通职员",createTime:"2018-1-31",needTime:"2018-2-1-10:00~2018-2-1-12:00",build:"5号",phoneNumber:"18390763586",others:"无",state:""},
        {id:"2",name:"张三丰",zhiwei:"领班",createTime:"2018-1-31",needTime:"2018-2-1-10:00~2018-2-1-12:00",build:"25号",phoneNumber:"18390763586",others:"需要投影",state:""},
    ];
    var shenghedData=[
        {id:"1658979496",name:"李二狗子",zhiwei:"普通职员",createTime:"2018-1-31",needTime:"2018-2-1-10:00~2018-2-1-12:00",build:"4号",phoneNumber:"18390763586",others:"无",state:"批准"},
        {id:"1658979496",name:"张三",zhiwei:"普通职员",createTime:"2018-1-31",needTime:"2018-2-1-10:00~2018-2-1-12:00",build:"6号",phoneNumber:"18390763586",others:"无",state:"拒绝"},
    ]
    function showShenghed() {
        // $.ajax({
        //     type : "GET",url : "ajaxGet", data : {
        //         method : "query"
        //     }, dataType : "json",//返回的数据类型
        //     success : function(data) {
        //     },
        //     error : function() {
        //         alert("查询失败！");
        //     }
        // });
        var html='<tr><th>#</th><th>ID</th><th>姓名</th><th>职位</th><th >提交时间</th><th >' +
            '申请时间段</th><th>申请会议室</th><th>联系方式</th><th>其他</th><th>状态</th></tr>';
        for(var i=0;i<shenghedData.length;i++){
            html=html+'<tr>';
            html = html + '<td>' + (i+1) + '</td>';
            html = html + '<td>' + shenghedData[i].id + '</td>';
            html = html + '<td>' + shenghedData[i].name + '</td>';
            html = html + '<td>' + shenghedData[i].zhiwei + '</td>';
            html = html + '<td>' + shenghedData[i].createTime + '</td>';
            html = html + '<td>' + shenghedData[i].needTime + '</td>';
            html = html + '<td>' + shenghedData[i].build + '</td>';
            html = html + '<td>' + shenghedData[i].phoneNumber + '</td>';
            html = html + '<td>' + shenghedData[i].others + '</td>';
            html = html + '<td>' + shenghedData[i].state + '</td>';
            html = html + '</tr>';
        }
        $('#mz_shenghed_table').html(html);
    }

    function showShenghe() {
        // for(var i=0;i<shengheData.length;i++){
        //     var trHtml = "<tr><td>"+(i+2)+"</td><td>"+shengheData[i].id+"</td><td>"+shengheData[i].name+"</td><td>"
        //         +shengheData[i].zhiwei+"</td><td>"+shengheData[i].createTime+"</td><td>"+shengheData[i].needTime+"</td><td>"+
        //         shengheData[i].build+"</td><td>"+shengheData[i].phoneNumber+"</td><td>"+shengheData[i].others+
        //         "</td><td><div class='mz_shenghe_handle'><button class=\"mz_permit\">批准</button>" +
        //         "<button class=\"mz_reject\">拒绝</button></div></td></tr>";
        //     console.log($("#mz_shenghe_table tr"));
        //     $("#mz_shenghe_table tr").last();
        // }
        var html='<tr><th>#</th><th>ID</th><th>姓名</th><th>职位</th><th >提交时间</th><th >' +
            '申请时间段</th><th>申请会议室</th><th>联系方式</th><th>其他</th><th>操作</th></tr>';
        for(var i=0;i<shengheData.length;i++){
            html=html+'<tr>';
            html = html + '<td>' + (i+1) + '</td>';
            html = html + '<td>' + shengheData[i].id + '</td>';
            html = html + '<td>' + shengheData[i].name + '</td>';
            html = html + '<td>' + shengheData[i].zhiwei + '</td>';
            html = html + '<td>' + shengheData[i].createTime + '</td>';
            html = html + '<td>' + shengheData[i].needTime + '</td>';
            html = html + '<td>' + shengheData[i].build + '</td>';
            html = html + '<td>' + shengheData[i].phoneNumber + '</td>';
            html = html + '<td>' + shengheData[i].others + '</td>';
            html = html +"<td><div class='mz_shenghe_handle'><button class='mz_permit'>批准</button>" +
                    "<button class='mz_reject'>拒绝</button></div></td>";
            html = html + '</tr>';
        }
        $('#mz_shenghe_table').html(html);
    }
    showShenghe();
    showShenghed();

    $("#mz_shenghe_table").on("click",".mz_permit",function () {
        keyNow = $(this).parents("tr");
        console.log(keyNow.children()[0].innerText);
        keyNowIndex = keyNow.children()[0].innerText;
        $('#tableModal').modal();
    });

    $("#tableModal-yes").on("click",function () {
        if (keyNow != null){
            shengheData[keyNowIndex-1].state="批准";
            shenghedData.push(shengheData[keyNowIndex-1]);
            shengheData.splice(keyNowIndex-1,1);
            console.log(shenghedData);
            showShenghe();
            showShenghed();
        }

    });

});