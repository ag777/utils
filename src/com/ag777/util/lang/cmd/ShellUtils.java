package com.ag777.util.lang.cmd;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.ag777.util.lang.model.Charsets;

/**
 * 执行shell命令的工具类
 * 
 * @author ag777
 * @version create on 2018年07月04日,last modify at 2018年07月04日
 */
public class ShellUtils extends AbstractCmdUtils {

	public static ShellUtils mInstance;
	
	public static ShellUtils getInstance() {
		if(mInstance == null) {
			synchronized (ShellUtils.class) {
				if(mInstance == null) {
					mInstance = new ShellUtils();
				}
			}
		}
		return mInstance;
	}
	
	public ShellUtils() {
		super(Charsets.UTF_8);
	}

	public ShellUtils(Charset charset) {
		super(charset);
	}

	@Override
	public Process getProcess(String cmd, String baseDir) throws IOException {
		Process pro = null;
		String[] cmdA = { "/bin/sh", "-c", cmd };
		if(baseDir == null) {
			pro = Runtime.getRuntime().exec(cmdA);
		} else {
			pro = Runtime.getRuntime().exec(cmdA, null, new File(baseDir));
		}
		return pro;
	}

}
