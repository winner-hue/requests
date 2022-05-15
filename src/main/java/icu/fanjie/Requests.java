package icu.fanjie;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Requests {

    public static Response request(String method, String url, Params params, Data data, Headers headers, int timeout, Boolean allow_redirects, Proxies proxies, List<ConnectionSpec> connectionSpecList, Boolean verify, String clientType) throws IOException {

        if (params != null && params.size() > 0) {
            url = url + params.toString();
        }
        if (verify == null) {
            verify = true;
        }
        if (allow_redirects == null) {
            allow_redirects = true;
        }
        if (clientType == null || "okhttp".equals(clientType)) {
            return okhttpResponse(proxies, timeout, allow_redirects, connectionSpecList, verify, clientType, method, headers, data, url);
        } else if ("httpclient".equals(clientType)) {
            return httpclientResponse(proxies, timeout, allow_redirects, connectionSpecList, verify, clientType, method, headers, data, url);
        } else {
            return curlResponse(proxies, timeout, allow_redirects, connectionSpecList, verify, clientType, method, headers, data, url);
        }

    }

    public static Response get(String url, Params params, Headers headers, int timeout, boolean allow_redirects, Proxies proxies, List<ConnectionSpec> connectionSpecList, Boolean verify, String clientType) throws IOException {
        return request("get", url, params, null, headers, timeout, allow_redirects, proxies, connectionSpecList, verify, clientType);
    }

    public static Response post(String url, Params params, Data data, Headers headers, int timeout, boolean allow_redirects, Proxies proxies, List<ConnectionSpec> connectionSpecList, Boolean verify, String clientType) throws IOException {
        return request("post", url, params, data, headers, timeout, allow_redirects, proxies, connectionSpecList, verify, clientType);
    }

    private static Response curlResponse(Proxies proxies, int timeout, boolean allow_redirects, List<ConnectionSpec> connectionSpecList, boolean verify, String clientType, String method, Headers headers, Data data, String url) throws IOException {
        return null;
    }

    private static Response httpclientResponse(Proxies proxies, int timeout, boolean allow_redirects, List<ConnectionSpec> connectionSpecList, boolean verify, String clientType, String method, Headers headers, Data data, String url) throws IOException {
        Response resp = new Response();
        HttpClient client = null;
        RequestConfig config = null;
        if (proxies != null && proxies.size() > 0) {
            String[] proxy = proxies.randomProxy();
            if (proxy[0].equals("auth")) {
                client = initClient(proxy[1], proxy[2], proxy[3], proxy[4], verify);
                config = initConfig(proxy[3], proxy[4], timeout, allow_redirects);
            } else {
                client = initClient(null, null, proxy[1], proxy[2], verify);
                config = initConfig(proxy[1], proxy[2], timeout, allow_redirects);
            }
        } else {
            client = initClient(null, null, null, null, verify);
            config = initConfig(null, null, timeout, allow_redirects);
        }

        if (method.equalsIgnoreCase("get")) {
            HttpGet get = new HttpGet(url);
            return returnHttpclientResponse(get, config, client, resp);
        } else {
            HttpPost post = new HttpPost(url);
            if (data.getPostType() == null || "json".equalsIgnoreCase(data.getPostType())) {
                String dataData = data.getData();
                post.setEntity(new StringEntity(dataData));
            } else {
                List<NameValuePair> postParams = data.getPostParams();
                post.setEntity(new UrlEncodedFormEntity(postParams));
            }
            return returnHttpclientResponse(post, config, client, resp);
        }
    }

    private static Response returnHttpclientResponse(HttpRequestBase request, RequestConfig config, HttpClient client, Response resp) throws IOException {
        request.setConfig(config);
        HttpResponse execute = client.execute(request);
        resp.setResponse(execute);
        resp.setRequest(request);
        String html = EntityUtils.toString(execute.getEntity(), "utf-8");
        resp.setHtml(html);
        resp.setStatusCode(execute.getStatusLine().getStatusCode());
        Header[] headerList = execute.getHeaders("Set-Cookie");
        List<String> cookies = new ArrayList<>();
        for (Header s : headerList) {
            String cookie = s.getValue().split(";")[0];
            cookies.add(cookie);
        }
        resp.setCookie(StringUtils.join(cookies, "; "));
        return resp;
    }

    private static Response okhttpResponse(Proxies proxies, int timeout, boolean allow_redirects, List<ConnectionSpec> connectionSpecList, boolean verify, String clientType, String method, Headers headers, Data data, String url) throws IOException {
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
            if (data.getPostType() == null || "json".equalsIgnoreCase(data.getPostType())) {
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

    private static RequestConfig initConfig(String proxy_host, String proxy_port, int timeout, boolean allow_redirects) {
        RequestConfig.Builder config = RequestConfig.custom();
        config.setRedirectsEnabled(allow_redirects);
        config.setConnectTimeout(timeout);
        config.setConnectionRequestTimeout(timeout);
        if (proxy_host != null && proxy_port != null) {
            config.setProxy(new HttpHost(proxy_host, Integer.parseInt(proxy_port)));
        }
        return config.build();
    }

    private static HttpClient initClient(String proxy_user, String proxy_pwd, String proxy_host, String proxy_port, boolean verify) {
        HttpClientBuilder client = HttpClients.custom();
        if (!verify) {
            client.setSSLSocketFactory(SSLSocketClient.getHttpclientSSLVerify());
        }

        if (proxy_user != null && proxy_pwd != null) {
            HttpHost host = new HttpHost(proxy_host, Integer.parseInt(proxy_port));
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials(proxy_user, proxy_pwd));
            client.setDefaultCredentialsProvider(provider);
        }
        return client.build();
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
