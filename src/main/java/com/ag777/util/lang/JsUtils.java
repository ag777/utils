package com.ag777.util.lang;

import com.ag777.util.lang.collection.MapUtils;

import javax.script.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 有关 <code>js</code>工具类。
 * <p>
 * java调用JavaScript的工具类
 * </p>
 * 
 * @author ag777
 * @version create on 2018年09月21日,last modify at 2023年09月27日
 */
public class JsUtils {
	// ScriptEngineManager用于管理和创建ScriptEngine实例，它负责为不同的脚本语言提供相应的引擎。
	private static volatile ScriptEngineManager scriptEngineManager;

	private static final String ENGINE_NAME_JS = "JavaScript";

	/**
	 * 使用ThreadLocal存储ScriptEngine实例，确保线程安全
	 * <p>
	 *     ThreadLocal.withInitial是Java 8中引入的一个便捷方法，用于创建一个带有初始值的ThreadLocal实例。它接受一个Supplier类型的参数，该参数是一个无参数的函数，用于生成初始值。
	 *     在这里,ThreadLocal.withInitial用于创建一个ThreadLocal<ScriptEngine>实例，每个线程在首次访问时，都会调用Supplier提供的函数来创建一个新的ScriptEngine实例。这样，每个线程都会拥有自己的ScriptEngine实例，从而实现线程安全。
	 * </p>
	 */
	private static final ThreadLocal<ScriptEngine> SCRIPT_ENGINE_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
		if (scriptEngineManager == null) {
			synchronized (JsUtils.class) {
				if (scriptEngineManager == null) {
					scriptEngineManager = new ScriptEngineManager();
				}
			}
		}
		return scriptEngineManager.getEngineByName(ENGINE_NAME_JS);
	});

	// 获取ScriptEngine实例
	public static ScriptEngine getEngine() {
		return SCRIPT_ENGINE_THREAD_LOCAL.get();
	}

	/**
	 * 预编译脚本
	 *
	 * @param formula 公式
	 * @return 预编译后的脚本
	 * @throws ScriptException 脚本异常
	 */
	public static CompiledScript compile(String formula) throws ScriptException {
		ScriptEngine engine = getEngine();
		Compilable compEngine = (Compilable) engine;
		return compEngine.compile(formula);
	}

	/**
	 * 传入公式和参数，调用JavaScript引擎获取结果
	 * <p>
	 *     公式可以是这种形式var v1=false;var v2=true;(v1&&v2)||v2;
	 *     也可以是(v1&&v2)||v2;</pre>,这时变量键值对需要传输<pre class="code">{"v1": false,"v2":true};
	 * </p>
	 * <pre>
	 * {@code
	 * Object result = eval("a + b", MapUtils.of("a", 1, "b", 2));
	 * // result: 3
	 * }
	 * </pre>
	 * @param formula 公式
	 * @param params  参数键值对
	 * @return 计算结果
	 * @throws ScriptException 脚本异常
	 */
	public static <K, V> Object eval(String formula, Map<K, V> params) throws ScriptException {
		return eval(formula, getBindings(params));
	}

	/**
	 * 传入公式和Bindings，调用JavaScript引擎获取结果
	 *
	 * @param formula  公式
	 * @param bindings 绑定的参数
	 * @return 计算结果
	 * @throws ScriptException 脚本异常
	 */
	public static Object eval(String formula, Bindings bindings) throws ScriptException {
		ScriptEngine engine = getEngine();
		return eval(formula, bindings, engine);
	}

	/**
	 * 通过公式和初始变量计算结果
	 *
	 * @param formula  公式
	 * @param bindings 变量，可以传一个SimpleBindings，也可以为null
	 * @param engine   引擎
	 * @return 计算结果
	 * @throws ScriptException 脚本异常
	 */
	public static Object eval(String formula, Bindings bindings, ScriptEngine engine) throws ScriptException {
		if (engine instanceof Compilable) {
			Compilable compEngine = (Compilable) engine;
			CompiledScript script = compEngine.compile(formula);
			return eval(script, bindings);
		}
		return engine.eval(formula, bindings);
	}

	/**
	 * 传入参数，执行预编译脚本，得到结果
	 *
	 * @param script 预编译好的js脚本
	 * @param params 变量键值对
	 * @return 计算结果
	 * @throws ScriptException 脚本异常
	 */
	public static <K, V> Object eval(CompiledScript script, Map<K, V> params) throws ScriptException {
		return eval(script, getBindings(params));
	}

	/**
	 * 传入参数，执行预编译脚本，得到结果
	 *
	 * @param script   预编译好的脚本
	 * @param bindings 构造好的变量键值对
	 * @return 计算结果
	 * @throws ScriptException 脚本异常
	 */
	public static Object eval(CompiledScript script, Bindings bindings) throws ScriptException {
		return script.eval(bindings);
	}

	/**
	 * 通过键值对构造Bindings
	 *
	 * @param params 参数键值对
	 * @param <K>    键类型
	 * @param <V>    值类型
	 * @return 构造好的Bindings
	 */
	private static <K, V> Bindings getBindings(Map<K, V> params) {
		Bindings bindings = null;
		if (!MapUtils.isEmpty(params)) {
			bindings = new SimpleBindings();
            for (K key : params.keySet()) {
                bindings.put(key.toString(), params.get(key));
            }
		}
		return bindings;
	}

	/**
	 * 返回一些信息,测试用
	 */
	public static String info() {
		ScriptEngineManager manager = new ScriptEngineManager();
		// 得到所有的脚本引擎工厂
		List<ScriptEngineFactory> factories = manager.getEngineFactories();
		List<String> infos = new ArrayList<>();
		// 这是Java SE 5 和Java SE 6的新For语句语法
		for (ScriptEngineFactory factory : factories) {
			// 拼接信息
			infos.add(String.format(
					"Name: %s%n" + "Version: %s%n" + "Language name: %s%n" + "Language version: %s%n"
							+ "Extensions: %s%n" + "Mime types: %s%n" + "Names: %s%n",
					factory.getEngineName(), factory.getEngineVersion(), factory.getLanguageName(),
					factory.getLanguageVersion(), factory.getExtensions(), factory.getMimeTypes(), factory.getNames()));
		}
		return String.join("\n", infos);
	}
	
	public static void main(String[] args) throws ScriptException {
		Object result = eval("v3 < 1", MapUtils.of("v3", "1"));
		System.out.println(result);
	}
}
