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

    public static Response request(String method, String url, Params params, Data data, Headers headers, int timeout, boolean allow_redirects, Proxies proxies, List<ConnectionSpec> connectionSpecList) throws IOException {
        if (params != null && params.size() > 0) {
            url = url + params.toString();
        }
        OkHttpClient client = null;
        if (proxies != null && proxies.size() > 0) {
            String[] proxy = proxies.randomProxy();
            if (proxy[0].equals("auth")) {
                client = initClient(proxy[1], proxy[2], proxy[3], proxy[4], proxies.getType(), timeout, allow_redirects, connectionSpecList);
            } else {
                client = initClient(null, null, proxy[1], proxy[2], proxies.getType(), timeout, allow_redirects, connectionSpecList);
            }
        } else {
            client = initClient(null, null, null, null, null, timeout, allow_redirects, connectionSpecList);
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

    public static Response get(String url, Params params, Headers headers, int timeout, boolean allow_redirects, Proxies proxies, List<ConnectionSpec> connectionSpecList) throws IOException {
        return request("get", url, params, null, headers, timeout, allow_redirects, proxies, connectionSpecList);
    }

    public static Response post(String url, Params params, Data data, Headers headers, int timeout, boolean allow_redirects, Proxies proxies, List<ConnectionSpec> connectionSpecList) throws IOException {
        return request("post", url, params, data, headers, timeout, allow_redirects, proxies, connectionSpecList);
    }


    private static OkHttpClient initClient(String proxy_user, String proxy_pwd, String proxy_host, String proxy_port, Proxy.Type type, int timeout, boolean allow_redirects, List<ConnectionSpec> connectionSpecList) {
        if (connectionSpecList == null) {
            connectionSpecList = Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.RESTRICTED_TLS);
        }
        if (proxy_user != null) {
            return new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .connectionSpecs(connectionSpecList)
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
            return new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .connectionSpecs(connectionSpecList)
                    .followRedirects(allow_redirects)
                    .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2, Protocol.QUIC))
                    .proxy(new Proxy(type, new InetSocketAddress(proxy_host, Integer.parseInt(proxy_port))))
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
