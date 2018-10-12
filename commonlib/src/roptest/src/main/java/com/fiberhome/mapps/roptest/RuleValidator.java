package com.fiberhome.mapps.roptest;

import java.io.InputStreamReader;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleValidator {
	private static Logger LOG = LoggerFactory.getLogger(RuleValidator.class);
	
	ScriptEngine engine;

	public RuleValidator(Map context) {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		this.engine = engineManager.getEngineByExtension("js");

		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.putAll(context);

		// 装载function.js
		try {
			engine.eval(new InputStreamReader(
					Thread.currentThread().getContextClassLoader().getResourceAsStream("js/assert.js"), "UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean validate(AssertRule rule) {
		StringBuilder exp = new StringBuilder();
		Object result = rule.getResult();
		
		String ch = (result instanceof String) ?"'" : "";
		exp.append(rule.getOperate()).
			append("(").append(rule.getExpresion()).append(", ").
			append(ch).append(result).append(ch).
			append(")");
		try {
			Object ret = engine.eval(exp.toString());
			if (Boolean.TRUE.equals(ret)) {
				System.out.println("Rule: " + exp + ", Test result: " + ret);
			} else {
				System.err.println("Rule: " + exp + ", Test result: " + ret);
			}
			return Boolean.TRUE == ret;
		} catch (ScriptException e) {
			System.err.println("Rule: " + exp + ", Test result: failure!");
			LOG.error("Rule: {}， Test result：failure", e, exp);
		}
		return false;
	}
}
