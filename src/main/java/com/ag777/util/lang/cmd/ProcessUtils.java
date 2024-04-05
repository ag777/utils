package com.ag777.util.lang.cmd;

import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.StringUtils;
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
 * @version 2024/04/05 19:39
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
     * 读取命令行执行后的输出行。
     *
     * @param cmds       要执行的命令数组。
     * @param baseDir    命令执行的基目录。
     * @param redirectErrStream 是否将错误流重定向到输出流。
     * @return 包含命令输出的所有行的列表。
     * @throws IOException 如果读取输入流时发生错误。
     */
    public static List<String> readLines(String[] cmds, String baseDir, boolean redirectErrStream) throws IOException {
        return readLines(cmds, baseDir, redirectErrStream, charsetDefault);
    }

    /**
     * 读取命令行执行后的输出行。
     *
     * @param cmds               要执行的命令数组。
     * @param baseDir            命令执行的基目录。
     * @param redirectErrStream  是否将错误流重定向到输出流。
     * @param charset            用于读取输出的字符集。
     * @return 包含命令输出的所有行的列表。
     * @throws IOException 如果读取输入流时发生错误。
     */
    public static List<String> readLines(String[] cmds, String baseDir, boolean redirectErrStream, Charset charset) throws IOException {
        Process pro = newProcess(cmds, baseDir, true, redirectErrStream, false);
        try {
            try (InputStream in = pro.getInputStream()) {
                return IOUtils.readLines(in, charset);
            }
        } finally {
            destroy(pro);
        }
    }

    /**
     * 读取命令行执行后的完整输出文本。
     *
     * @param cmds       要执行的命令数组。
     * @param baseDir    命令执行的基目录。
     * @param redirectErrStream 是否将错误流重定向到输出流。
     * @return 命令输出的完整文本。
     * @throws IOException 如果读取输入流时发生错误。
     */
    public static String readText(String[] cmds, String baseDir, boolean redirectErrStream) throws IOException {
        return readText(cmds, baseDir, redirectErrStream, charsetDefault);
    }

    /**
     * 读取命令行执行后的完整输出文本。
     *
     * @param cmds               要执行的命令数组。
     * @param baseDir            命令执行的基目录。
     * @param redirectErrStream  是否将错误流重定向到输出流。
     * @param charset            用于读取输出的字符集。
     * @return 命令输出的完整文本。
     * @throws IOException 如果读取输入流时发生错误。
     */
    public static String readText(String[] cmds, String baseDir, boolean redirectErrStream, Charset charset) throws IOException {
        Process pro = newProcess(cmds, baseDir, true, redirectErrStream, false);
        try {
            try (InputStream in = pro.getInputStream()) {
                return new String(IOUtils.readBytes(in), charset);
            }
        } finally {
            destroy(pro);
        }
    }

    /**
     * 执行命令并等待其完成，返回命令执行是否成功。
     *
     * @param cmds       要执行的命令数组。
     * @param baseDir    命令执行的基目录。
     * @return 命令执行成功返回true，否则返回false。
     * @throws IOException 如果创建进程时发生错误。
     * @throws InterruptedException 如果等待进程完成时被中断。
     */
    public static boolean exec(String[] cmds, String baseDir) throws IOException, InterruptedException {
        Process pro = newProcess(cmds, baseDir, true, false, true);
        try {
            int exitValue = pro.waitFor();
            return exitValue == 0;
        } finally {
            destroy(pro);
        }
    }

    /**
     * 创建一个新的进程来执行指定的命令。
     *
     * @param cmds               要执行的命令数组。
     * @param baseDir            命令执行的基目录。
     * @param closeOutput        是否关闭进程的输出流。
     * @param redirectErrStream  是否将错误流重定向到输出流。
     * @param discardInput       是否丢弃进程的输入流。
     * @return 启动的进程实例。
     * @throws IOException 如果创建进程时发生错误。
     */
    public static Process newProcess(String[] cmds, String baseDir, boolean closeOutput, boolean redirectErrStream, boolean discardInput) throws IOException {
        ProcessBuilder builder = newProcessBuilder(cmds, baseDir);
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
     * 创建一个新的ProcessBuilder实例，用于执行外部命令。
     *
     * @param cmds 一个包含要执行的命令及其参数的字符串数组。
     * @param baseDir 执行命令时的基础目录路径，如果为空或null，则使用当前工作目录。
     * @return 一个配置好的ProcessBuilder实例，准备好启动外部过程。
     */
    public static ProcessBuilder newProcessBuilder(String[] cmds, String baseDir) {
        String[] command;
        if (SystemUtils.isWindows()) {
            // 为Windows系统准备命令，包括"cmd.exe"和"/c"参数
            command = new String[cmds.length + 2];
            command[0] = "cmd.exe";
            command[1] = "/c";
            System.arraycopy(cmds, 0, command, 2, cmds.length);
        } else {
            // 为非Windows系统准备命令，使用"/bin/sh"和"-c"参数，并合并所有命令为一个字符串
            // 对于非Windows系统，需要将cmds数组转换为单一字符串
            // 并且额外的2个空间用于"/bin/sh"和"-c"
            // 因为所有命令合并成一个字符串，所以长度是3
            command = new String[3];
            command[0] = "/bin/sh";
            command[1] = "-c";
            StringBuilder cmdBuilder = new StringBuilder();
            for (String cmd : cmds) {
                if (cmdBuilder.length() > 0) {
                    cmdBuilder.append(" ");
                }
                cmdBuilder.append(cmd);
            }
            command[2] = cmdBuilder.toString();
        }

        // 初始化ProcessBuilder并设置执行目录
        ProcessBuilder pb = new ProcessBuilder(command);
        if (!StringUtils.isEmpty(baseDir)) {
            pb.directory(new File(baseDir));
        }

        return pb;
    }

    /**
     * 获取进程的PID（已弃用）。
     *
     * @param pro 进程实例。
     * @return 进程的PID。
     * @throws IllegalArgumentException 如果传入的进程为null。
     * @throws IllegalAccessException 如果无法访问pid字段。
     * @throws NoSuchFieldException 如果找不到pid字段。
     * @throws SecurityException 如果访问控制不允许。
     * @deprecated 由于依赖于内部实现，此方法可能不稳定。
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
     * 销毁进程，关闭其输入输出流并请求其终止。
     *
     * @param process 要销毁的进程。
     */
    protected static void destroy(Process process) {
        if (process != null) {
            IOUtils.close(process.getOutputStream(), process.getInputStream());
            process.destroy();
        }
    }
}
