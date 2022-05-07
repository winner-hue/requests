package icu.fanjie;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Requests {

    public static Response request(String method, String url, Params params, Data data, Headers headers, int timeout, Boolean allow_redirects, Proxies proxies, List<ConnectionSpec> connectionSpecList, Boolean verify) throws IOException {
        if (params != null && params.size() > 0) {
            url = url + params.toString();
        }
        if (verify == null) {
            verify = true;
        }
        if (allow_redirects == null) {
            allow_redirects = true;
        }
        OkHttpClient client = null;
        if (proxies != null && proxies.size() > 0) {
            String[] proxy = proxies.randomProxy();
            if (proxy[0].equals("auth")) {
                client = initClient(proxy[1], proxy[2], proxy[3], proxy[4], proxies.getType(), timeout, allow_redirects, connectionSpecList, verify);
            } else {
                client = initClient(null, null, proxy[1], proxy[2], proxies.getType(), timeout, allow_redirects, connectionSpecList, verify);
            }
        } else {
            client = initClient(null, null, null, null, null, timeout, allow_redirects, connectionSpecList, verify);
        }
        Request request = null;

        if (method.equalsIgnoreCase("get")) {
            request = new Request.Builder().url(url).headers(headers.getHeaders()).get()
                    .build();

        } else {
            if (data.getPostType() == null) {
                request = new Request.Builder().url(url).headers(headers.getHeaders())
                        .post(RequestBody.create(data.getData(), MediaType.parse("application/json; charset=utf-8")))
                        .build();
            } else {
                request = new Request.Builder().url(url).headers(headers.getHeaders())
                        .post(data.getFormData())
                        .build();
            }
        }
        Call call = client.newCall(request);
        okhttp3.Response response = call.execute();
        Response resp = new Response();
        resp.setResponse(response);
        resp.setRequest(request);
        resp.setHtml(Objects.requireNonNull(response.body()).string());
        resp.setStatusCode(response.code());
        List<String> headerList = response.headers("set-cookie");
        List<String> cookies = new ArrayList<>();
        for (String s : headerList) {
            String cookie = s.split(";")[0];
            cookies.add(cookie);
        }
        resp.setCookie(StringUtils.join(cookies, "; "));
        client.connectionPool().evictAll();
        return resp;

    }

    public static Response get(String url, Params params, Headers headers, int timeout, boolean allow_redirects, Proxies proxies, List<ConnectionSpec> connectionSpecList, Boolean verify) throws IOException {
        return request("get", url, params, null, headers, timeout, allow_redirects, proxies, connectionSpecList, verify);
    }

    public static Response post(String url, Params params, Data data, Headers headers, int timeout, boolean allow_redirects, Proxies proxies, List<ConnectionSpec> connectionSpecList, Boolean verify) throws IOException {
        return request("post", url, params, data, headers, timeout, allow_redirects, proxies, connectionSpecList, verify);
    }


    private static OkHttpClient initClient(String proxy_user, String proxy_pwd, String proxy_host, String proxy_port, Proxy.Type type, int timeout, boolean allow_redirects, List<ConnectionSpec> connectionSpecList, boolean verify) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        builder.followRedirects(allow_redirects);
        builder.protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2, Protocol.QUIC));
        if (!verify) {
            builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager());
        } else {
            if (connectionSpecList != null) {
                builder.connectionSpecs(connectionSpecList);
            }
        }
        if (proxy_user != null) {
            builder
                    .followRedirects(allow_redirects)
                    .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2, Protocol.QUIC))
                    .proxy(new Proxy(type, new InetSocketAddress(proxy_host, Integer.parseInt(proxy_port))))
                    .proxyAuthenticator((route, response) -> {
                        String credential = Credentials.basic(proxy_user, proxy_pwd);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    })
                    .build();
        } else if (proxy_host != null) {
            builder
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .followRedirects(allow_redirects)
                    .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2, Protocol.QUIC))
                    .proxy(new Proxy(type, new InetSocketAddress(proxy_host, Integer.parseInt(proxy_port))))
                    .build();

        } else {
            builder
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .followRedirects(allow_redirects)
                    .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2, Protocol.QUIC))
                    .build();
        }
        return builder.build();
    }
}
