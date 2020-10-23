package com.ag777.util.http.model;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 用于连接https
 * 
 * @author ag777
 * @version create on 2017年06月06日,last modify at 2018年08月02日
 */
public class SSLSocketClient {  
	
	private SSLSocketClient() {}
	
	/**
     * 绕过https
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory() {  
        try {  
            SSLContext sslContext = SSLContext.getInstance("SSL");  
            sslContext.init(null, getTrustManager(), new SecureRandom());  
            return sslContext.getSocketFactory();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
    /**
     * 导入ssl证书
     * @param certificates certificates
     * @return
     * @throws KeyStoreException KeyStoreException
     * @throws CertificateException CertificateException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws IOException IOException
     * @throws KeyManagementException KeyManagementException
     */
    public static SSLSocketFactory getSSLSocketFactory(InputStream... certificates) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        int index = 0;
        for (InputStream certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

            try {
                if (certificate != null)
                    certificate.close();
            } catch (IOException e) {
            }
        }
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext.getSocketFactory();
	}
  
    //获取TrustManager  
    private static TrustManager[] getTrustManager() {  
        TrustManager[] trustAllCerts = new TrustManager[]{  
                new X509TrustManager() {

                	@Override  
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {  
                    }  
  
                    @Override  
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {  
                    }  
  
                    @Override  
                    public X509Certificate[] getAcceptedIssuers() {  	//okhttp3.0之前返回的是null,参考资料:https://blog.csdn.net/u014752325/article/details/73185351
                        return new X509Certificate[]{};  
                    }  
                    
                }  
        };  
        return trustAllCerts;  
    }  
  
    //获取HostnameVerifier  
    public static HostnameVerifier getHostnameVerifier() {  
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {  
            @Override  
            public boolean verify(String s, SSLSession sslSession) {  
                return true;  
            }
            
        };  
        return hostnameVerifier;  
    }  
}