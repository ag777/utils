package com.ag777.util.lang.cmd;

import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * ProcessUtils 提供了一组静态方法，用于在Java应用程序中执行和管理操作系统进程。
 * 它封装了ProcessBuilder API的使用，简化了进程的创建、执行、输出读取和销毁等操作。
 * 此外，它还提供了操作系统特定的功能，如根据操作系统类型确定空文件路径，
 *
 * 主要功能包括：
 * - 执行外部命令并等待其完成。
 * - 读取命令执行的输出（标准输出和错误输出）。
 * - 根据需要重定向错误输出到标准输出或丢弃。
 * - 获取进程的PID（已过时，如果是jdk9以上版本推荐使用ProcessHandle.pid()）。
 * - 创建进程时根据操作系统类型自动选择正确的命令解释器。
 *
 *
 * @author ag777 <837915770@vip.qq.com>
 * @version 2024/3/20 10:27
 */
public class ProcessUtils {

    /**
     * 根据操作系统类型确定空文件的路径。Windows系统使用"NUL"，其他系统使用"/dev/null"。
     */
    private static final File NULL_FILE = new File(
            (SystemUtils.isWindows() ? "NUL" : "/dev/null")
    );

    /**
     * 用于重定向进程输出到空文件，达到丢弃输出的目的。
     */
    public static final ProcessBuilder.Redirect DISCARD = ProcessBuilder.Redirect.to(NULL_FILE);

    private static Charset charsetDefault = SystemUtils.isWindows()?Charset.forName("GBK"):StandardCharsets.UTF_8;

    /**
     * 设置全局的默认字符集。建议最多只在服务启动时调用一次
     * @param charset 编码
     */
    public static void setDefaultCharset(Charset charset) {
        charsetDefault = charset;
    }

    /**
     * 读取命令执行的输出，使用系统默认字符集。
     * @param cmd 要执行的命令。
     * @param baseDir 命令执行的工作目录。
     * @param redirectErrStream 是否将错误输出重定向到标准输出。
     * @return 命令执行的输出内容，以行为单位。
     * @throws IOException 如果执行命令或读取输出时发生I/O错误。
     */
    public static List<String> readLines(String cmd, String baseDir, boolean redirectErrStream) throws IOException {
        return readLines(cmd, baseDir, redirectErrStream, charsetDefault);
    }

    /**
     * 读取命令执行的输出。
     * @param cmd 要执行的命令。
     * @param baseDir 命令执行的工作目录。
     * @param redirectErrStream 是否将错误输出重定向到标准输出。
     * @param charset 输出内容的字符集。
     * @return 命令执行的输出内容，以行为单位。
     * @throws IOException 如果执行命令或读取输出时发生I/O错误。
     */
    public static List<String> readLines(String cmd, String baseDir, boolean redirectErrStream, Charset charset) throws IOException {
        Process pro = newProcess(cmd, baseDir, true, redirectErrStream, false);
        try {
            // 确保进程的输入流被正确关闭
            try (InputStream in = pro.getInputStream()) {
                return IOUtils.readLines(in, charset);
            }
        } finally {
            destroy(pro);
        }
    }

    /**
     * 读取命令执行的输出文本，使用系统默认字符集。
     * @param cmd 要执行的命令。
     * @param baseDir 命令执行的工作目录。
     * @param redirectErrStream 是否将错误输出重定向到标准输出。
     * @return 命令执行的输出内容，作为一个字符串返回。
     * @throws IOException 如果执行命令或读取输出时发生I/O错误。
     */
    public static String readText(String cmd, String baseDir, boolean redirectErrStream) throws IOException {
        return readText(cmd, baseDir, redirectErrStream, charsetDefault);
    }

    /**
     * 读取命令执行的输出文本。
     * @param cmd 要执行的命令。
     * @param baseDir 命令执行的工作目录。
     * @param redirectErrStream 是否将错误输出重定向到标准输出。
     * @param charset 输出内容的字符集。
     * @return 命令执行的输出内容，作为一个字符串返回。
     * @throws IOException 如果执行命令或读取输出时发生I/O错误。
     */
    public static String readText(String cmd, String baseDir, boolean redirectErrStream, Charset charset) throws IOException {
        Process pro = newProcess(cmd, baseDir, true, redirectErrStream, false);
        try {
            // 确保进程的输入流被正确关闭
            try (InputStream in = pro.getInputStream()) {
                return new String(IOUtils.readBytes(in), charset);
            }
        } finally {
            destroy(pro);
        }
    }

    /**
     * 执行命令并等待其完成。
     * @param cmd 要执行的命令。
     * @param baseDir 命令执行的工作目录。
     * @return 如果命令成功执行（退出值为0），则返回true；否则返回false。
     * @throws IOException 如果执行命令时发生I/O错误。
     * @throws InterruptedException 如果等待进程结束时被中断。
     */
    public static boolean exec(String cmd, String baseDir) throws IOException, InterruptedException {
        Process pro = newProcess(cmd, baseDir, true, false, true);
        try {
            int exitValue = pro.waitFor();
            return exitValue == 0;
        } finally {
            destroy(pro);
        }
    }

    /**
     * 创建一个新的进程。
     * @param cmd 要执行的命令。
     * @param baseDir 命令执行的工作目录。
     * @param closeOutput 是否关闭标准输出。
     * @param redirectErrStream 是否将错误输出重定向到标准输出
     * @param discardInput 是否丢弃标准输入。
     * @return 创建的进程。
     * @throws IOException 如果启动进程时发生I/O错误。
     */
    public static Process newProcess(String cmd, String baseDir, boolean closeOutput, boolean redirectErrStream, boolean discardInput) throws IOException {
        ProcessBuilder builder = newProcessBuilder(cmd, baseDir);
        if (redirectErrStream) {
            builder.redirectErrorStream(true);
        } else {
            builder.redirectError(DISCARD);
        }
        if (discardInput) {
            builder.redirectInput(NULL_FILE);
        }
        Process pro = builder.start();
        if (closeOutput) {
            IOUtils.close(pro.getOutputStream());
        }
        if (discardInput) {
            IOUtils.close(pro.getInputStream());
        }
        return pro;
    }

    /**
     * 根据命令和工作目录创建一个新的ProcessBuilder。
     * @param cmd 要执行的命令。
     * @param baseDir 命令执行的工作目录。
     * @return 配置好的ProcessBuilder。
     */
    public static ProcessBuilder newProcessBuilder(String cmd, String baseDir) {
        ProcessBuilder builder;
        if (SystemUtils.isWindows()) {
            builder = new ProcessBuilder("cmd.exe", "/c", cmd);
        } else {
            builder = new ProcessBuilder("/bin/sh", "-c", cmd);
        }

        if(baseDir != null) {
            builder.directory(new File(baseDir));
        }
        return builder;
    }

    /**
     * 获取进程的PID（进程ID）。
     * <p>
     * 通过反射机制访问进程实例的{@code pid}字段来获取其PID值。这种方法依赖于JVM实现的内部细节，
     * 并且可能在不同版本或不同JVM实现中不可用。
     * </p>
     * @param pro 要获取PID的进程实例。
     * @return 进程的PID。
     * @throws IllegalArgumentException 如果反射调用的方法参数不合法。
     * @throws IllegalAccessException 如果反射调用试图访问不可访问的字段。
     * @throws NoSuchFieldException 如果在进程类中找不到{@code pid}字段。
     * @throws SecurityException 如果安全管理器存在并且阻止了反射操作的执行。
     */
    @Deprecated
    public int getPid(Process pro) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Class<?> cProcessImpl = pro.getClass();
        Field fPid = cProcessImpl.getDeclaredField("pid");
        if (!fPid.isAccessible()) {
            fPid.setAccessible(true);
        }
        return fPid.getInt(pro);
    }

    /**
     * 销毁给定的进程。
     * @param process 要销毁的进程。
     */
    protected static void destroy(Process process) {
        if(process != null) {
            // 调用process.destroy();确实会请求终止进程，但它不会自动关闭与该进程关联的输入/输出流。这意味着您需要手动管理这些流的关闭，以避免潜在的资源泄露。
            IOUtils.close(process.getOutputStream(), process.getInputStream());
            process.destroy();
        }
    }
}
