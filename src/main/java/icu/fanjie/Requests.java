package icu.fanjie;

import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Requests {

    public static Response request(String method, String url, Params params, Data data, Headers headers, int timeout, boolean allow_redirects, Proxies proxies) {
        if (params != null && params.size() > 0) {
            url = url + params.toString();
        }
        OkHttpClient client = null;
        if (proxies != null && proxies.size() > 0) {
            String[] proxy = proxies.randomProxy();
            if (proxy[0].equals("auth")) {
                client = initClient(proxy[1], proxy[2], proxy[3], proxy[4], timeout, allow_redirects);
            } else {
                client = initClient(null, null, proxy[1], proxy[2], timeout, allow_redirects);
            }
        } else {
            client = initClient(null, null, null, null, timeout, allow_redirects);
        }

        if (method.equalsIgnoreCase("get")) {

            return new Response();
        }
        if (method.equalsIgnoreCase("post")) {
            return new Response();
        }
        try {
            throw new Exception("method error");
        } catch (Exception e) {
            System.out.println("method error");
        }
        return null;
    }

    public static Response get(String url, Params params, Headers headers, int timeout, boolean allow_redirects, Proxies proxies) {
        return request("get", url, params, null, headers, timeout, allow_redirects, proxies);
    }

    public static Response post(String url, Params params, Data data, Headers headers, int timeout, boolean allow_redirects, Proxies proxies) {
        return request("post", url, params, data, headers, timeout, allow_redirects, proxies);
    }

    private static OkHttpClient initClient(String proxy_user, String proxy_pwd, String proxy_host, String proxy_port, int timeout, boolean allow_redirects) {
        List<ConnectionSpec> connectionSpecList = Util.randomSSL();
        if (proxy_user != null) {
            return new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .connectionSpecs(connectionSpecList)
                    .followRedirects(allow_redirects)
                    .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2, Protocol.QUIC))
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_host, Integer.parseInt(proxy_port))))
                    .proxyAuthenticator((route, response) -> {
                        String credential = Credentials.basic(proxy_user, proxy_pwd);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    })
                    .build();
        } else if (proxy_host != null) {
            return new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .connectionSpecs(connectionSpecList)
                    .followRedirects(allow_redirects)
                    .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2, Protocol.QUIC))
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_host, Integer.parseInt(proxy_port))))
                    .build();

        } else {
            return new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .connectionSpecs(connectionSpecList)
                    .followRedirects(allow_redirects)
                    .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2, Protocol.QUIC))
                    .build();
        }

    }
}
