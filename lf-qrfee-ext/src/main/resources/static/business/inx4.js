Ext.require('Ext.tab.*');

Ext.onReady(function () {

    //#######Tab1病人待缴费信息查询Panel#######
    var feeQueryFrom = new Ext.form.Panel({
        region: "north",  //显示在顶部
        layout: {
            type: "form",
            columns: 2
        },
        height: 60,
        width: "100%",
        bodyPadding: 10,  //内边距
        frame: true,
        defaults: {
            width: 120,
            margin: "0 10 10 0"
        },
        items: [{
            xtype: "textfield",
            fieldLabel: "病人ID",
            allowBlank: false,
            emptyText: "请刷卡或手工输入病人就诊卡号"
        },{
            xtype: "button",
            text: "查询",
            width: 60,
            iconCls: "Magnifier",
            handler: function() {
                //doQuery();
            }
        }]
    });

    var sss = new  Ext.form.FieldSet({
        region: "north",  //显示在顶部
        title: "费用查询",
        height: 60,
        layout:'column',
        fieldDefaults: {
            labelAlign: 'right',
            labelWidth: 60,
            margin:"8px"
        },
        items :[{
            xtype: "textfield",
            fieldLabel: "病人ID",
            allowBlank: false,
            columnWidth: 0.5,
            emptyText: "请刷卡或手工输入病人就诊卡号"
        },{
            xtype: "button",
            text: "查询",
            width: 60,
            columnWidth: 0.15,
            iconCls: "Magnifier",
            handler: function() {
                //doQuery();
            }
        }]
    });

    var feeQueryStore = new Ext.data.Store({
        pageSize: 10,  //设置分页大小
        fields: ["pid", "productId", "productName", "price", "downTime" , "upTime",
            "remark", "isValid"],
        proxy: {   //Proxy对象，用于访问数据对象。
            type: "ajax",
            url:  "",
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

    //创建多选
    var selModel = Ext.create("Ext.selection.CheckboxModel");

    var feeQueryGrid = new Ext.grid.Panel({
        region: "center",
        selModel:selModel, // selModel:Ext.create('Ext.selection.CheckboxModel',{mode:"SIMPLE"}),
        disableSelection:false,//值为TRUE，表示禁止选择行
        //title: "方案套餐列表",
        frame: true,
        border: false,
        columnLines: true,  //在列分隔处显示分隔符
        autoScroll: true,
        viewConfig: {
            forceFit: true  //列宽度自动充满空间,强制平均列宽度
        },
        store: feeQueryStore,
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
            store: feeQueryStore,
            dock: "bottom",	//extjs在容器中引入了dockedItems属性，需要停靠的组件应该放在这里。且停靠位置用dock属性指定。
            displayInfo: true
        }],
        tbar: [{
            text: "新增",
            iconCls: "Magnifier",
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
            "itemdblclick": function(grid, record, item, index, e) {
                editRow(record.data["pid"], record.data["productId"], record.data["productName"], record.data["price"], record.data["upTime"], record.data["downTime"], record.data["remark"]);
            }
        }
    });

    var feePanel = new Ext.Panel({
        title: '门诊缴费',
        layout: "border",
        items: [sss, feeQueryGrid]
    });

    var tabs2 = Ext.widget('tabpanel', {
        renderTo: document.body,
        activeTab: 0,
        width: 480,
        height: 360,
        plain: true,
        defaults: {
            autoScroll: true,
            bodyPadding: 10
        },
        items: [feePanel, {
            title: 'Ajax Tab 1',
            loader: {
                url: 'ajax1.htm',
                contentType: 'html',
                loadMask: true
            },
            listeners: {
                activate: function (tab) {
                    tab.loader.load();
                }
            }
        }
        ]
    });
});