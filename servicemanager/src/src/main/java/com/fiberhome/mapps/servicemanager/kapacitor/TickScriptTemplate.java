package com.fiberhome.mapps.servicemanager.kapacitor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.servicemanager.entity.McAlertCondition;
import com.fiberhome.mapps.servicemanager.entity.McAlertMethod;
import com.fiberhome.mapps.servicemanager.entity.McAlertRule;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TickScriptTemplate {

	public static Map<String, String> template(McAlertRule alertRule, List<McAlertCondition> alertConditionList,
			List<McAlertMethod> alertMethodList, String smsPushKey, String osName) {
		Configuration cfg = new Configuration();
		String dir = "src/main/resources/tickscript";
		Map<String, String> scriptList = new HashMap<String, String>();
		try {
			// 从哪里加载模板文件
			cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), "tickscript");
			// cfg.setDirectoryForTemplateLoading(new File(dir));

			// 定义模版的位置，从类路径中，相对于FreemarkerManager所在的路径加载模版
			// cfg.setTemplateLoader(new
			// ClassTemplateLoader(FreemarkerManager.class, "templates"))

			// 设置对象包装器
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			// 设置异常处理器
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

			String groupBy = "";
			if (!"".equals(alertRule.getGroupBy())) {
				groupBy = ".groupBy(" + alertRule.getGroupBy() + ")";
			}

			String emailRoot = ".details('''" + "\n<h1>{{ .ID }}</h1>" + "\n<b>{{ .Message }}</b>\n        ''')";
			String emailStr = "";
			String smsStr = "";
			for (McAlertMethod mam : alertMethodList) {
				if ("email".equals(mam.getAlertMethod())) {
					if ("".equals(emailStr)) {
						emailStr += emailRoot + "\n        .email('" + mam.getConfig() + "')";
					} else {
						emailStr += "\n          .to('" + mam.getConfig() + "')";
					}
				} else if ("sms".equals(mam.getAlertMethod())) {
					if ("".equals(smsStr)) {
						smsStr += "" + mam.getConfig() + "";
					} else {
						smsStr += "," + mam.getConfig() + "";
					}
				}
			}
			// 处理因email标签引起的格式问题
			if (!"".equals(emailStr)) {
				emailStr += "\n        ";
			}
			// 构建调用mplus第三方接入短信服务请求参数,通过调用脚本执行curl语言完成
			// 不使用exec标签 ,该方法废弃
			// String execcurl = "";
			// if (!"".equals(smsStr)) {
			// String curlParams =
			// "FHuma025appKey=mos&content=testtest&format=json&locale=zh_CN&method=mobileark.smspush&phoneNumbers="
			// + smsStr + "&v=1.0FHuma025";
			// String sign = DigestUtils.sha1Hex(curlParams.replaceAll("&",
			// "").replaceAll("=", ""));
			// //curlParams += "&sign=" + sign;
			// execcurl +=
			// ".exec('http://localhost:8761/smspush?phoneNumbers="+smsStr.replaceAll(",",
			// "and")+"')";
			// //,'mos','contextTemplate','"+smsStr.replaceAll(",",
			// "#")+"','"+sign.toUpperCase()+"')";
			//
			// }

			smsStr = ".post('http://sm.mapps.ip:8761/smspush?phoneNumbers=" + smsStr.replaceAll(",", "and") + "&key="
					+ smsPushKey + "')";

			for (McAlertCondition mac : alertConditionList) {
				// 定义数据模型
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("email", emailStr);
				root.put("sms", smsStr);
				root.put("smsPushKey", smsPushKey);
				root.put("alertRule", alertRule);
				root.put("groupBy", groupBy);
				root.put("level", mac.getAlertLevel());
				String alertMessage = "";
				StringBuilder lambda = new StringBuilder("");
				if ("threshold".equals(mac.getRuleType())) {
					alertMessage += alertRule.getFunc() + " is " + mac.getSetting1() + " " + mac.getSetting2();
					if ("in".equals(mac.getSetting1())) {
						String range = mac.getSetting2().replaceAll("\\(", "").replaceAll("\\)", "");
						lambda.append("\"" + alertRule.getFunc() + "\" > " + range.split(",")[0] + " AND \""
								+ alertRule.getFunc() + "\" < " + range.split(",")[1]);
					} else if ("out".equals(mac.getSetting1())) {
						String range = mac.getSetting2().replaceAll("\\(", "").replaceAll("\\)", "");
						lambda.append("\"" + alertRule.getFunc() + "\" < " + range.split(",")[0] + " OR \""
								+ alertRule.getFunc() + "\" > " + range.split(",")[1]);
					} else {
						lambda.append("\"" + alertRule.getFunc() + "\" " + mac.getSetting1() + " " + mac.getSetting2());
					}
				} else if ("relative".equals(mac.getRuleType())) {
					alertMessage += "Compared to " + alertRule.getFunc() + " over " + mac.getSetting1() + ","
							+ mac.getSetting2() + " is " + mac.getSetting3() + " " + mac.getSetting4();
					root.put("shift", mac.getSetting1());
					if ("in".equals(mac.getSetting3())) {
						String range = mac.getSetting4().replaceAll("\\(", "").replaceAll("\\)", "");
						lambda.append("\"value\" > " + range.split(",")[0] + " AND \"value\" < " + range.split(",")[1]);
					} else if ("out".equals(mac.getSetting3())) {
						String range = mac.getSetting4().replaceAll("\\(", "").replaceAll("\\)", "");
						lambda.append("\"value\" < " + range.split(",")[0] + " OR \"value\" > " + range.split(",")[1]);
					} else {
						lambda.append("\"value\" " + mac.getSetting3() + " " + mac.getSetting4());
					}
					String changeLambda = "";
					if ("ratio".equals(mac.getSetting2())) {
						changeLambda = "float(\"current.value\" - \"past.value\")";
					} else if ("percent".equals(mac.getSetting2())) {
						changeLambda = "abs(float(\"current.value\" - \"past.value\")) / float(\"past.value\") * 100.0";
					}
					root.put("changeLambda", changeLambda);
				} else if ("deadman".equals(mac.getRuleType())) {
					alertMessage += "There is no data of " + alertRule.getFunc() + " recieved over " + mac.getSetting1();
					root.put("deadtime", mac.getSetting1());
				}
				alertMessage += " : "+alertRule.getMessage();
				root.put("alertMessage", alertMessage);
				root.put("lambda", lambda.toString());

				if (osName.toLowerCase().indexOf("windows") > -1) {
					root.put("logpath", "D:/kapacitorAlerts.log");
				} else {
					root.put("logpath", "/tmp/kapacitorAlerts.log");
				}

				// 通过freemarker解释模板，首先需要获得Template对象
				Template template = cfg.getTemplate(mac.getRuleType() + ".ftl");

				// 定义模板解释完成之后的输出
				StringWriter sw = new StringWriter();
				try {
					// 解释模板
					template.process(root, sw);

					scriptList.put(mac.getId(), sw.toString());
					// scriptList.add(sw.toString());
				} catch (TemplateException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scriptList;

	}
}
