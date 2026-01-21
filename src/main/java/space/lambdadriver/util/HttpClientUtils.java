package space.lambdadriver.util;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;

public class HttpClientUtils {
    private static SSLContext initSslContext() throws NoSuchAlgorithmException, KeyManagementException {
        // 创建信任所有证书的 TrustManager
        TrustManager[] trustManagers = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // 创建 SSLContext 并初始化
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new java.security.SecureRandom());

        return sslContext;
    }

    private static SSLParameters initSslParams() {
        // 创建自定义 SSLParameters 以支持所有 TLS 版本和密码套件
        SSLParameters sslParams = new SSLParameters();
        sslParams.setProtocols(new String[]{"TLSv1.2", "TLSv1.3"});
        return sslParams;
    }

    public static java.net.http.HttpClient create() throws NoSuchAlgorithmException, KeyManagementException {
        return java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .sslContext(initSslContext())
                .sslParameters(initSslParams())
                .build();
    }
}
