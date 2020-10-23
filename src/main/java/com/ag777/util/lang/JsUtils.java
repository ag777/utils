package com.ag777.util.lang;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.ag777.util.lang.collection.MapUtils;

/**
 * 有关 <code>js</code>工具类。
 * <p>
 * java调用JavaScript的工具类
 * </p>
 * 
 * @author ag777
 * @version create on 2018年09月21日,last modify at 2018年09月21日
 */
public class JsUtils {

	private static final String ENGINE_NAME_JS = "JavaScript";
	
	/**
	 * 预编译脚本
	 * @param formula formula
	 * @return
	 * @throws ScriptException ScriptException
	 */
	public static CompiledScript compile(String formula) throws ScriptException {
		ScriptEngine engine = getEngine();
		Compilable compEngine = (Compilable)engine;
		return compEngine.compile(formula);
	}
	
	public static ScriptEngine getEngine() {
		return new ScriptEngineManager().getEngineByName(ENGINE_NAME_JS);
	}
	
	/**
	 * 传入公式，调用js获取结果
	 * <p><pre>{@code
	 * 公式可以是这种形式var v1=false;var v2=true;(v1&&v2)||v2;
	 * 也可以是(v1&&v2)||v2;</pre>,这时变量键值对需要传输<pre class="code">{"v1": false,"v2":true};
	 * }</pre>
	 * 
	 * @param formula 公式
	 * @param params 变量键值对
	 * @return
	 * @throws ScriptException ScriptException
	 */
	public static <K, V>Object eval(String formula, Map<K, V> params) throws ScriptException {
	    return eval(formula, getBindings(params));
	}
	
	/**
	 * 传入公式，调用js获取结果
	 * @param formula formula
	 * @param bindings bindings
	 * @return
	 * @throws ScriptException ScriptException
	 */
	public static <K, V>Object eval(String formula, Bindings bindings) throws ScriptException {
		ScriptEngine engine = getEngine();
	    return eval(formula, bindings, engine);
	}
	
	/**
	 * 通过公式及一些初始变量计算出结果
	 * @param formula 公式
	 * @param bindings 变量,可以传一个SimpleBindings,可以为null
	 * @param engine 引擎
	 * @return
	 * @throws ScriptException ScriptException
	 */
	public static Object eval(String formula, Bindings bindings, ScriptEngine engine) throws ScriptException {
		if(engine instanceof Compilable) {	//预编译，其实这里(公式和变量都是一次性的)是没啥用的，只是举个例子
			Compilable compEngine = (Compilable)engine;
			CompiledScript script = compEngine.compile(formula);
			return eval(script, bindings);
		}
		return  engine.eval(formula, bindings);
	}
	
	/**
	 * 传入参数，执行预编译脚本，得到结果三连
	 * @param script 预编译好的js脚本
	 * @param params 变量键值对
	 * @return
	 * @throws ScriptException ScriptException
	 */
	public static <K, V>Object eval(CompiledScript script, Map<K, V> params) throws ScriptException {
		return eval(script, getBindings(params));
	}
	
	/**
	 * 传入参数，执行预编译脚本，得到结果三连
	 * @param script 预编译好的脚本
	 * @param bindings 构造好的变量键值对
	 * @return
	 * @throws ScriptException ScriptException
	 */
	public static Object eval(CompiledScript script, Bindings bindings) throws ScriptException {
		return script.eval(bindings);
	}

	/**
	 * 通过键值对构造bindings
	 * @param params params
	 * @param <K> K
	 * @param <V> V
	 * @return
	 */
	private static <K,V>Bindings getBindings(Map<K, V> params) {
		Bindings bindings = null;
		if(!MapUtils.isEmpty(params)) {
			bindings = new SimpleBindings(); 
			Iterator<K> itor = params.keySet().iterator();
			while(itor.hasNext()) {
				K key = itor.next();
				bindings.put(key.toString(), params.get(key));
			}
		}
		return bindings;
	}
	
	/**
	 * 控制台输出一些信息,测试用
	 */
	public static void info() {
		ScriptEngineManager manager = new ScriptEngineManager();
		// 得到所有的脚本引擎工厂
		List<ScriptEngineFactory> factories = manager.getEngineFactories();
		// 这是Java SE 5 和Java SE 6的新For语句语法
		for (ScriptEngineFactory factory : factories) {
			// 打印脚本信息
			System.out.printf(
					"Name: %s%n" + "Version: %s%n" + "Language name: %s%n" + "Language version: %s%n"
							+ "Extensions: %s%n" + "Mime types: %s%n" + "Names: %s%n",
					factory.getEngineName(), factory.getEngineVersion(), factory.getLanguageName(),
					factory.getLanguageVersion(), factory.getExtensions(), factory.getMimeTypes(), factory.getNames());
			// 得到当前的脚本引擎
//			ScriptEngine engine = factory.getScriptEngine();
		}
	}
	
	public static void main(String[] args) throws ScriptException {
		Object a = eval("v3<1", MapUtils.of("v3", "1"));
		System.out.println(a);
	}
}