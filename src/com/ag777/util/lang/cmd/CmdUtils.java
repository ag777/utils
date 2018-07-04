package com.ag777.util.lang.cmd;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.ag777.util.lang.model.Charsets;

/**
 * 执行Cmd命令的工具类
 * 
 * @author ag777
 * @version create on 2018年07月04日,last modify at 2018年07月04日
 */
public class CmdUtils extends AbstractCmdUtils {

	public static CmdUtils mInstance;
	
	public static CmdUtils getInstance() {
		if(mInstance == null) {
			synchronized (CmdUtils.class) {
				if(mInstance == null) {
					mInstance = new CmdUtils();
				}
			}
		}
		return mInstance;
	}
	
	public CmdUtils() {
		super(Charsets.UTF_8);
	}

	/**
	 * windows系统请设置编码为GBK
	 * @param charset
	 */
	public CmdUtils(Charset charset) {
		super(charset);
	}

	@Override
	public Process getProcess(String cmd, String baseDir) throws IOException {
		Process pro = null;
		if(baseDir == null) {
			pro = Runtime.getRuntime().exec(cmd);
		} else {
			pro = Runtime.getRuntime().exec(cmd, null, new File(baseDir));// 执行删除默认路由命令
		}
		return pro;
	}
	
}
