package com.ag777.util.http.model;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * okhttp下载文件进度监听辅助类
 * 
 * @author ag777
 * @version last modify at 2018年04月04日
 */
public class ProgressResponseBody extends ResponseBody {

    //回调接口
    public interface ProgressListener{
        /**
         * @param curRead 已经读取的字节数
         * @param contentLength 响应总长度
         * @param bytesRead 当前读取字节数
         * @param done 是否读取完毕
         */
        void update(long curRead,long contentLength, long bytesRead, boolean done);
    }

    private final ResponseBody responseBody;
    private final ProgressListener progressListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody,ProgressListener progressListener){
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null){
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source){
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink,byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;   //不断统计当前下载好的数据
                long contentLength = responseBody.contentLength();
                
                //接口回调
                progressListener.update(totalBytesRead, contentLength, bytesRead != -1?bytesRead:0, bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
