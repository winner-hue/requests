# requests
java requests by okhttp3

## install


- 直接使用jar包 下载jar包，通过 add as library 导入项目

- 通过pom导入 
```maven
       <dependency>
         <groupId>icu.fanjie</groupId>
         <artifactId>requests</artifactId>
         <version>1.0</version>
       </dependency>

```

- jar包引入 参考 https://www.cnblogs.com/zhaochi/p/12694275.html

## use

    public class Test {
        @org.junit.Test
        public void Header() throws IOException {
            Headers headers = new Headers("user-agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
            Response response = Requests.request("get", "https://www.baidu.com", null, null, headers, 10000, true, null, null);
            System.out.println(response.getHtml());
            System.out.println(response.getStatusCode());
            System.out.println(response.getCookie());
            System.out.println(response.response);
        }
    
        @org.junit.Test
        public void Proxies() throws IOException {
            Proxies proxies = new Proxies("127.0.0.1:10808", Proxy.Type.SOCKS);
            Headers headers = new Headers("user-agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
            Response response = Requests.request("get", "https://www.google.com", null, null, headers, 10000, true, proxies, null);
            System.out.println(response.getHtml());
            System.out.println(response.getStatusCode());
            System.out.println(response.getCookie());
            System.out.println(response.response);
        }
    
        @org.junit.Test
        public void PostMethod() throws IOException {
            Proxies proxies = new Proxies("127.0.0.1:10808", Proxy.Type.SOCKS);
            Headers headers = new Headers("user-agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36;;;" +
                    "x-csrftoken: 58134293276e1d4e660a9f792507237f;;;" +
                    "cookie: _pinterest_sess=TWc9PSY3cm9HRWIzcWRJMWYvU3l4cjF4YWRTMUkwYkI5bU0xWEV0R09pYkhSUDdRM2hIQ2xBR29ValFSSVV3MEkwZ1dScmMxZlhFT3A3TklvZXEzUUlyeGl5RmJUSnRVY1JVdnh4bmpuRklwbzI0Zz0mNElEZE1kdmNRN0h5Y20xbUo2TUdNbGlQVlNBPQ==; _auth=0; csrftoken=58134293276e1d4e660a9f792507237f;;;" +
                    "content-type:application/json;;;" +
                    "origin: https://trends.pinterest.com;;;" +
                    "referer: https://trends.pinterest.com/;;;" +
                    "x-new-site: true");
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            ja.add("pastel wall color ideas");
            jo.put("queries", ja);
            Data data = new Data(jo.toJSONString(), null);
            Response response = Requests.request("post", "https://trends.pinterest.com/pin_search/", null, data, headers, 10000, true, proxies, null);
            System.out.println(response.getHtml());
            System.out.println(response.getStatusCode());
            System.out.println(response.getCookie());
            System.out.println(response.response);
        }
    }
