package com.ag777.util.http.model;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

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
     * @return SSLSocketFactory
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
     * @return SSLSocketFactory
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
            } catch (IOException ignored) {
            }
        }
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext.getSocketFactory();
	}

    //获取TrustManager  
    public static TrustManager[] getTrustManager() {
        return new TrustManager[]{
                getX509TrustManager()
        };
    }  
  
    //获取HostnameVerifier  
    public static HostnameVerifier getHostnameVerifier() {
        return (s, sslSession) -> true;
    }

    private static X509TrustManager getX509TrustManager() {
        return new X509TrustManager() {
            //检查客户端的证书是否可信
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {

            }
            //检查服务器端的证书是否可信
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }
}