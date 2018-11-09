<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%--ext-all-neptune.css不支持IE6 --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/common/ext-4.2.1/resources/css/ext-all-neptune.css">
    <script src="${pageContext.request.contextPath}/common/ext-4.2.1/ext-all.js"></script>
    <script src="${pageContext.request.contextPath}/common/bootstrap/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/common/ext-4.2.1/locale/ext-lang-zh_CN.js"></script>

    <title>填写用户管理的数据</title>
    <script type="text/javascript">
//获取基本路径
var serverPath="${pageContext.request.contextPath}";
//到数据库查询数据
var doQueryUrl = serverPath + "/program.action?method=doQuery";
//保存数据
var doSaveUrl = serverPath + "/program.action?method=doSave";
//删除
var doDeleteUrl = serverPath + "/program.action?method=doDelete";
//表格的数据处理
var gridStore;
//窗口装载表单
var extWin;
//表单
var grid;


Ext.onReady(function() {
    /*Ext.BLANK_IMAGE_URL默认已经加载s.gif*/
    Ext.tip.QuickTipManager.init();

    var json = [{"productId":"test1","price":20},{"productId":"test2","price":25}];
    console.log(json[1].price);

    var root = {"result":[{"productId":"test1","price":20},{"productId":"test2","price":25}],"totalCount":100000};

    var storePageSize = 10;


    $("#edits").click(function(){
        alert("edit");
    });

    //填写产品数据的表单
    var form = new Ext.form.Panel({
        autoScroll: true,  //与layout: "fit",结合使用，在window内部显示滚动条
        frame: false,
        border: false,
        bodyPadding: 10,
        defaults: {
            width: 350
        },
        items: [{
            xtype: "hiddenfield",  //隐藏
            id: "pid",
            fieldLabel: "ID"
        }, {
            xtype: "textfield",
            id: "productId",
            fieldLabel: "产品ID<font color='red'>*</font>",
            allowBlank: false
        }, {
            xtype: "textarea",
            id: "productName",
            fieldLabel: "产品名称<font color='red'>*</font>",
            allowBlank: false,
            width: 350,
            height: 100
        }, {
            xtype: "numberfield",
            id: "price",
            fieldLabel: "低消金额（元）<font color='red'>*</font>",
            allowBlank: false
        }, {
            id: "upTime",
            xtype: "datefield",
            format: "Y-m-d",  //format: "Y-m-d H:i:s"
            fieldLabel: "上架时间<font color='red'>*</font>",
            allowBlank: false
        }, {
            id: "downTime",
            xtype: "datefield",
            format: "Y-m-d",  //format: "Y-m-d H:i:s"
            fieldLabel: "下架时间<font color='red'>*</font>",
            allowBlank: false
        }, {
            xtype: "textarea",
            id: "remark",
            fieldLabel: "说明",
            width: 350,
            height: 100
        }]
    });

    //装表单的窗口
    extWin = new Ext.window.Window({
        title: "方案套餐",
        width: 420,
        height: 450,
        closeAction: "hide",
        modal: true,  //遮罩 :就是让form表单以外不能编辑
        constrain: true,  //限制拖动范围
        resizable: true,  //可调整大小的; 可变尺寸的
        bodyPadding: 10,
        border:false,
        buttonAlign: "center",    //按钮显示位置
        layout: "fit",
        items: [form],  //装表单进来
        listeners: {
            "close": function() {
                //点击右上角的关闭按钮，就清空form
                form.getForm().reset();
            }
        },
        buttons: [{
            text: "保存",
            iconCls: "icon-save",
            handler: function() {
                doSave();
            }
        }, {
            text: "取消",
            iconCls: "icon-cancel",
            handler: function() {
                form.getForm().reset();
                extWin.hide();  //隐藏窗口
            }
        }]
    });

    function doSave() {
        if(form.getForm().isValid()) {
            Ext.Msg.confirm("提示", "是否保存？", function(btn) {
                if(btn == "yes") {
                    //加载中
                    //	layer.load(2);

                    $.ajax({
                        type : "POST",
                        url : doSaveUrl,
                        dataType : "json",  //返回 JSON 数据
                        cache : false,  //不缓存
                        async : true,
                        timeout : 30000,
                        data : {
                            //获取值
                            pid: Ext.getCmp("pid").getValue(),
                            productId: Ext.getCmp("productId").getValue(),
                            productName: Ext.getCmp("productName").getValue(),
                            price: Ext.getCmp("price").getValue(),
                            upTime: Ext.Date.format(new Date(Ext.getCmp("upTime").getValue()), 'Y-m-d'),  //格式化时间
                            downTime: Ext.Date.format(new Date(Ext.getCmp("downTime").getValue()), 'Y-m-d'),
                            remark: Ext.getCmp("remark").getValue()
                        },
                        success : function(data, textStatus) {
                            //关闭加载中
                            //	layer.closeAll('loading');

                            if(data.success == true || data.success == "true") {
                                form.getForm().reset();
                                extWin.hide();
                                doQuery();
                            }
                            //	layer.msg(data.msg);
                        },
                        error : function(XMLHttpRequest, textStatus, errorThrown) {
                            Ext.Msg.alert("提示","很遗憾出现错误");
                            //layer.closeAll('loading');
                        }
                    });
                }
            });
        }

    }

    gridStore = new Ext.data.Store({
        pageSize: storePageSize,  //设置分页大小
        fields: ["pid", "productId", "productName", "price", "downTime" , "upTime",
            "remark", "isValid"],
        proxy: {   //Proxy对象，用于访问数据对象。
            type: "ajax",
            url:  doQueryUrl,
            //data: json,
            actionMethods : {
                read: "POST"  //解决传中文参数乱码问题，默认为“GET”提交
            },
            reader: {   //处理数据对象的DataReader会返回一个Ext.data.Record对象的数组。其中的id属性会是一个缓冲了的键。
                type: "json",  //返回数据类型为json格式
                root:  "root.result",  //数据
                totalProperty: "root.totalCount"  //数据总条数
            }
        }
    });

    //为该元素添加事件处理函数（addListener的简写方式）。
    //on(eventName事件名称,handler处理函数)
    gridStore.on("beforeload", function(store, options) {
        options.page--;    //项目必须使用的分页参数
        var start = Ext.getCmp("txtUpTime").getValue();
        var end = Ext.getCmp("txtDownTime").getValue();
        if(start != null) {
            //日期格式化
            start = Ext.Date.format(new Date(start), 'Y-m-d');
        }
        if(end != null) {
            end = Ext.Date.format(new Date(end), 'Y-m-d');
        }

        var newParams = {
            size: storePageSize,  //项目必须使用的分页参数
            productId: Ext.getCmp("txtProductId").getValue(),
            productName: Ext.getCmp("txtProductName").getValue(),
            upTime: start,
            downTime: end
        };
        //用于实现把一个对象中的属性应用于另外一个对象中，相当于属性拷贝,
        //将会覆盖目标对象中的属性
        //该方法包含三个参数，第一个参数是要拷贝的目标对象，第二个参数是拷贝的源对象，第三个参数是可选的，表示给目标对象提供一个默认值。可以简单的理解成把第三个参数（如果有的话）及第二个参数中的属性拷贝给第一个参数对象
        Ext.apply(store.proxy.extraParams, newParams);
    });

    //创建多选
    var selModel = Ext.create("Ext.selection.CheckboxModel");

    grid = new Ext.grid.Panel({
        region: "center",
        selModel:selModel, // selModel:Ext.create('Ext.selection.CheckboxModel',{mode:"SIMPLE"}),
        disableSelection:false,//值为TRUE，表示禁止选择行
        title: "方案套餐列表",
        frame: true,
        border: false,
        columnLines: true,  //在列分隔处显示分隔符
        autoScroll: true,
        viewConfig: {
            forceFit: true  //列宽度自动充满空间,强制平均列宽度
        },
        store: gridStore,
        columns: [{
            xtype: "rownumberer"  //显示行号
        }, {
            header: "ID",
            hidden: true,  //隐藏
            dataIndex: "pid"
        },
            {
                header: "产品ID",
                dataIndex: "productId",
            },
            {
                header: "产品名称",
                dataIndex: "productName",
                width: 200
            },
            {
                header: "低消金额（元）",
                dataIndex: "price",
                width: 100
            },
            {
                header: "上架时间",
                dataIndex: "upTime"
            },
            {
                header: "下架时间",
                dataIndex: "downTime"
            },
            {
                header: "说明",
                dataIndex: "remark",
                width:200
            },
            {
                header: "是否有效",
                dataIndex: "isValid",
                /**
                 renderer可以格式化该列显示的数据格式或者按照你自定义的脚本显示最终数据样子
                 renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){ }
                 1.value是当前单元格的值
                 2.cellmeta里保存的是cellId单元格id，id不知道是干啥的，似乎是列号，css是这个单元格的css样式。
                 3.record是这行的所有数据，你想要什么，record.data["id"]这样就获得了。
                 4.rowIndex是行号，不是从头往下数的意思，而是计算了分页以后的结果。
                 5.columnIndex列号太简单了。
                 6.store，这个厉害，实际上这个是你构造表格时候传递的ds，也就是说表格里所有的数据，你都可以随便调用，唉，太厉害了。
                 */
                renderer: function(value, cellmeta, record, rowIndex, columnIndex, store) {
                    if(value == true || value == "true") {
                        return "是";
                    }
                    return "<font color='red'>否</font>";
                }
            },{
                header:"操作",
                dateIndex:"operation",
                width : 200,
                renderer : function(value, cellmeta, record, rowIndex, columnIndex, store){
                    return "<a href='javascript:void(0)'onclick='extWin.show()'>新增</a> |<a  href='javascript:void(0)' onclick='edits()'>编辑</a> |<a href='javascript:void(0)' onclick='deleteProdcut()'>删除</a>";
                }
            }],
        loadMask: {   //loadMask:True表示为当grid加载过程中，会有一个Ext.LoadMask的遮罩效果。默认为false。
            msg: "正在加载数据，请稍候......"
        },
        dockedItems: [{   //在底部显示，分页
            xtype: "pagingtoolbar",
            store: gridStore,
            dock: "bottom",	//extjs在容器中引入了dockedItems属性，需要停靠的组件应该放在这里。且停靠位置用dock属性指定。
            displayInfo: true
        }],
        tbar: [{
            text: "新增",
            iconCls: "icon-add",
            handler: function() {
                extWin.show();
            }
        },{
            text:"编辑",
            handler:function(){
                edits();
            }
        },{
            text:"删除",
            handler:function(){
                deleteProdcut();
            }
        },
            "-",//一条竖线，用于分隔
            "（提示：双击编辑!）"],
        listeners: {
            //双击事件
            /*视图：Ext.view.View
                    record：Ext.data.Model 属于item的记录  The record that belongs to the item
                    item：HTMLElement item的元素
                    index：Number item的索引
                    e：Ext.EventObject raw事件对象
                    eOpts：Object将options对象传递给Ext.util.Observable.addListener。*/
            "itemdblclick": function(grid, record, item, index, e) {
                editRow(record.data["pid"], record.data["productId"], record.data["productName"], record.data["price"], record.data["upTime"], record.data["downTime"], record.data["remark"]);
            }
        }
    });

    var panel = new Ext.Panel({
        region: "north",  //显示在顶部
        layout: {
            type: "table",
            columns: 6
        },
        height: 60,
        width: "100%",
        bodyPadding: 10,  //内边距
        frame: true,
        defaults: {
            width: 120,  //对 下面items内容宽度的设置
            margin: "0 10 10 0"
        },
        items: [{
            id: "txtProductId",
            xtype: "textfield",
            emptyText: "产品ID"
        }, {
            id: "txtProductName",
            xtype: "textfield",
            emptyText: "产品名称(模糊)"
        }, {
            id: "txtUpTime",
            xtype: "datefield",
            emptyText: "开始时间",
            format: "Y-m-d",  //format: "Y-m-d H:i:s"
            fieldLabel: "产品有效期",
            width: 220
        }, {
            id: "txtDownTime",
            xtype: "datefield",
            emptyText: "结束时间",
            format: "Y-m-d"
        }, {
            xtype: "button",
            text: "查询",
            width: 60,
            iconCls: "icon-search",
            handler: function() {
                doQuery();
            }
        }]
    });

    var viewport = new Ext.container.Viewport({
        layout: "border",
        items: [panel, grid]
    });

    doQuery();  //连接数据库才需要执行这一行
});

function doQuery() {
    gridStore.loadPage(1);
}

//设置值并显示在表单中
function editRow(pid, productId, productName, price, upTime, downTime, remark) {
    Ext.getCmp("pid").setValue(pid);
    Ext.getCmp("productId").setValue(productId);
    Ext.getCmp("productName").setValue(productName);
    Ext.getCmp("price").setValue(price);
    Ext.getCmp("upTime").setValue(upTime);
    Ext.getCmp("downTime").setValue(downTime);
    Ext.getCmp("remark").setValue(remark);

    extWin.show();
}




//编辑
function  edits(){
    var record = grid.getSelectionModel().getSelection();
    if(record.length == 0 || record.length > 1){
        Ext.MessageBox.show({
            title:"提示",
            msg:"请选择一行进行编辑！"
        })
        return;
    }else{
        var pid = record[0].get("pid");
        var productId = record[0].get("productId");
        var productName = record[0].get("productName");
        var price = record[0].get("price");
        var upTime = record[0].get("upTime");
        var downTime = record[0].get("downTime");
        var remark = record[0].get("remark");
        editRow(pid, productId, productName, price, upTime, downTime, remark);
    }
}


//根据pid批量删除
function deleteProdcut(){
    var record = grid.getSelectionModel().getSelection();
    if(record.length==0){
        Ext.MessageBox.show({
            title:"提示",
            msg:"请选择至少一行进行删除！"
        })
        return;
    }else{
        var ids = "";
        for(var i=0;i<record.length; i++){
            ids += record[i].get("pid")
            if(i<record.length-1){
                ids = ids +","
            }
        }

        $.ajax({
            type : "POST",
            url : doDeleteUrl,
            dataType : "JSON", //返回JSON数据
            cache : false, //不缓存
            async : true,
            timeout : 30000,
            data : {ids:ids},
            success : function(data,textStatus){
                if(data.success == true|| data.success == "true"){
                    doQuery();
                }
            }
        });
    }
}

</script>
</head>

<body>

</body>
</html>