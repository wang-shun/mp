var commonQuestions = [
   	{
	    "code": "1",
	    "question": "您的姓名",
	    "type": "4",
	    "selMax": 0,
	    "required": "1"
	},
	{
	    "code": "2",
	    "question": "您的性别",
	    "type": "1",
	    "selMax": 0,
	    "required": "1",
	    "suQustionOptions": [
	        {
	            "option": "男",
	            "customInput": "0",
	            "sequ": 1
	        },
	        {
	            "option": "女",
	            "customInput": "0",
	            "sequ": 2
	        }
	    ]
	},
	{
	    "code": "3",
	    "question": "您的年龄段",
	    "type": "1",
	    "selMax": 0,
	    "required": "1",
	    "suQustionOptions": [
	        {
	            "option": "18岁以下",
	            "customInput": "0",
	            "sequ": 1
	        },
	        {
	            "option": "18~25",
	            "customInput": "0",
	            "sequ": 2
	        },
	        {
	            "option": "26~30",
	            "customInput": "0",
	            "sequ": 3
	        },
	        {
	            "option": "31~40",
	            "customInput": "0",
	            "sequ": 4
	        },
	        {
	            "option": "41~50",
	            "customInput": "0",
	            "sequ": 5
	        },
	        {
	            "option": "51~60",
	            "customInput": "0",
	            "sequ": 6
	        },
	        {
	            "option": "60岁以上",
	            "customInput": "0",
	            "sequ": 7
	        }
	    ]
	},
	{
	    "code": "4",
	    "question": "请输入您的手机号码",
	    "type": "4",
	    "selMax": 0,
	    "required": "1"
	},
	{
	    "code": "5",
	    "question": "请输入您常用的E-MAIL地址",
	    "type": "4",
	    "selMax": 0,
	    "required": "1"
	},
	{
	    "code": "6",
	    "question": "您目前从事的职业",
	    "type": "2",
	    "selMax": 0,
	    "required": "1",
	    "suQustionOptions": [
	        {
	            "option": "全日制学生",
	            "customInput": "0",
	            "sequ": 1
	        },
	        {
	            "option": "生产人员",
	            "customInput": "0",
	            "sequ": 2
	        },
	        {
	            "option": "销售人员",
	            "customInput": "0",
	            "sequ": 3
	        },
	        {
	            "option": "市场/公关人员",
	            "customInput": "0",
	            "sequ": 4
	        },
	        {
	            "option": "客服人员",
	            "customInput": "0",
	            "sequ": 5
	        },
	        {
	            "option": "行政/后勤人员",
	            "customInput": "0",
	            "sequ": 6
	        },
	        {
	            "option": "人力资源",
	            "customInput": "0",
	            "sequ": 7
	        },
	        {
	            "option": "财务/审计人员",
	            "customInput": "0",
	            "sequ": 8
	        },
	        {
	            "option": "文职/办事人员",
	            "customInput": "0",
	            "sequ": 9
	        },
	        {
	            "option": "技术/研发人员",
	            "customInput": "0",
	            "sequ": 10
	        },
	        {
	            "option": "管理人员",
	            "customInput": "0",
	            "sequ": 11
	        },
	        {
	            "option": "教师",
	            "customInput": "0",
	            "sequ": 12
	        },
	        {
	            "option": "顾问/咨询",
	            "customInput": "0",
	            "sequ": 13
	        },
	        {
	            "option": "专业人士(如会计师、律师、建筑师等)",
	            "customInput": "0",
	            "sequ": 14
	        },
	        {
	            "option": "其他",
	            "customInput": "0",
	            "sequ": 15
	        }
	    ]
	},
	{
	    "code": "7",
	    "question": "您目前从事的行业",
	    "type": "2",
	    "selMax": 0,
	    "required": "1",
	    "suQustionOptions": [
	        {
	            "option": "IT/软硬件服务/电子商务/因特网运营",
	            "customInput": "0",
	            "sequ": 1
	        },
	        {
	            "option": "快速消费品(食品/饮料/化妆品)",
	            "customInput": "0",
	            "sequ": 2
	        },
	        {
	            "option": "批发/零售",
	            "customInput": "0",
	            "sequ": 3
	        },
	        {
	            "option": "服装/纺织/皮革",
	            "customInput": "0",
	            "sequ": 4
	        },
	        {
	            "option": "家具/工艺品/玩具",
	            "customInput": "0",
	            "sequ": 5
	        },
	        {
	            "option": "教育/培训/科研/院校",
	            "customInput": "0",
	            "sequ": 6
	        },
	        {
	            "option": "家电",
	            "customInput": "0",
	            "sequ": 7
	        },
	        {
	            "option": "通信/电信运营/网络设备/增值服务",
	            "customInput": "0",
	            "sequ": 8
	        },
	        {
	            "option": "制造业",
	            "customInput": "0",
	            "sequ": 9
	        },
	        {
	            "option": "汽车及零配件",
	            "customInput": "0",
	            "sequ": 10
	        },
	        {
	            "option": "餐饮/娱乐/旅游/酒店/生活服务",
	            "customInput": "0",
	            "sequ": 11
	        },
	        {
	            "option": "办公用品及设备",
	            "customInput": "0",
	            "sequ": 12
	        },
	        {
	            "option": "会计/审计",
	            "customInput": "0",
	            "sequ": 13
	        },
	        {
	            "option": "法律",
	            "customInput": "0",
	            "sequ": 14
	        },
	        {
	            "option": "银行/保险/证券/投资银行/风险基金",
	            "customInput": "0",
	            "sequ": 15
	        },
	        {
	            "option": "电子技术/半导体/集成电路",
	            "customInput": "0",
	            "sequ": 16
	        },
	        {
	            "option": "仪器仪表/工业自动化",
	            "customInput": "0",
	            "sequ": 17
	        },
	        {
	            "option": "贸易/进出口",
	            "customInput": "0",
	            "sequ": 18
	        },
	        {
	            "option": "机械/设备/重工",
	            "customInput": "0",
	            "sequ": 19
	        },
	        {
	            "option": "制药/生物工程/医疗设备/器械",
	            "customInput": "0",
	            "sequ": 20
	        },
	        {
	            "option": "医疗/护理/保健/卫生",
	            "customInput": "0",
	            "sequ": 21
	        },
	        {
	            "option": "广告/公关/媒体/艺术",
	            "customInput": "0",
	            "sequ": 22
	        },
	        {
	            "option": "出版/印刷/包装",
	            "customInput": "0",
	            "sequ": 23
	        },
	        {
	            "option": "房地产开发/建筑工程/装潢/设计",
	            "customInput": "0",
	            "sequ": 24
	        },
	        {
	            "option": "物业管理/商业中心",
	            "customInput": "0",
	            "sequ": 25
	        },
	        {
	            "option": "中介/咨询/猎头/认证",
	            "customInput": "0",
	            "sequ": 26
	        },
	        {
	            "option": "交通/运输/物流",
	            "customInput": "0",
	            "sequ": 27
	        },
	        {
	            "option": "航天/航空/能源/化工",
	            "customInput": "0",
	            "sequ": 28
	        },
	        {
	            "option": "农业/渔业/林业",
	            "customInput": "0",
	            "sequ": 29
	        },
	        {
	            "option": "其他行业",
	            "customInput": "0",
	            "sequ": 30
	        }
	    ]
	}
]