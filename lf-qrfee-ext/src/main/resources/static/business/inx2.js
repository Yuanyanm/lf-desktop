Ext.require('Ext.tab.*');
Ext.onReady(function () {
    Ext.widget('tabpanel', {
        renderTo: document.body,
        activeTab: 0,
        width: 600,
        height: 250,
        plain: true,
        defaults: {
            autoScroll: true,
            iconCls: 'tabs',
            bodyPadding: 10
        },
        items: [{
            title: '门诊缴费',
            xtype:'pan',
            items:[{
                xtype:'fieldset',
                checkboxToggle:true,
                title: 'User Information',
                defaultType: 'textfield',
                collapsed: true,
                layout: 'anchor',
                defaults: {
                    anchor: '100%'
                },
                items :[{
                    fieldLabel: 'First Name',
                    afterLabelTextTpl: required,
                    name: 'first',
                    allowBlank:false
                },{
                    fieldLabel: 'Last Name',
                    afterLabelTextTpl: required,
                    name: 'last'
                },{
                    fieldLabel: 'Company',
                    name: 'company'
                }, {
                    fieldLabel: 'Email',
                    afterLabelTextTpl: required,
                    name: 'email',
                    vtype:'email'
                }]
            }]
        }, {
            title: '缴费历史',
            listeners: {
                activate: function (tab) {
                    setTimeout(function () {
                        //alert(tab.title + ' was activated.');
                    }, 1);
                }
            },
            html: "I am tab 4's content. I also have an event listener attached."
        }
        ]
    });
});