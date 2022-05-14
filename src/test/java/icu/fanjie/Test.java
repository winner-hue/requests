package icu.fanjie;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.Proxy;

public class Test {
    @org.junit.Test
    public void Header() throws IOException {
        String url = "https://us.shein.com/Mesh-Panel-Shapewear-Panty-p-10320088-cat-3134.html?src_identifier=fc%3DWomen%60sc%3DNEW%20IN%60tc%3D0%60oc%3D0%60ps%3Dtab01navbar01%60jc%3Ddailynew_0&src_module=topcat&src_tab_page_id=page_home1651538654491&scici=navbar_WomenHomePage~~tab01navbar01~~1~~Special_whatsnew~~~~0";
        Headers headers = new Headers("user-agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
        Response response = Requests.request("get", url, null, null, headers, 10000, null, null, null, null, null);
        System.out.println(response.getHtml());
        System.out.println(response.getStatusCode());
        System.out.println(response.getCookie());
        System.out.println(response.response);
    }
//
//    @org.junit.Test
//    public void Proxies() throws IOException {
//        Proxies proxies = new Proxies("127.0.0.1:10808", Proxy.Type.SOCKS);
//        Headers headers = new Headers("user-agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
//        Response response = Requests.request("get", "https://www.google.com", null, null, headers, 10000, true, proxies, null);
//        System.out.println(response.getHtml());
//        System.out.println(response.getStatusCode());
//        System.out.println(response.getCookie());
//        System.out.println(response.response);
//    }
//
//    @org.junit.Test
//    public void PostMethod() throws IOException {
//        Proxies proxies = new Proxies("127.0.0.1:10808", Proxy.Type.SOCKS);
//        Headers headers = new Headers("user-agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36;;;" +
//                "x-csrftoken: 58134293276e1d4e660a9f792507237f;;;" +
//                "cookie: _pinterest_sess=TWc9PSY3cm9HRWIzcWRJMWYvU3l4cjF4YWRTMUkwYkI5bU0xWEV0R09pYkhSUDdRM2hIQ2xBR29ValFSSVV3MEkwZ1dScmMxZlhFT3A3TklvZXEzUUlyeGl5RmJUSnRVY1JVdnh4bmpuRklwbzI0Zz0mNElEZE1kdmNRN0h5Y20xbUo2TUdNbGlQVlNBPQ==; _auth=0; csrftoken=58134293276e1d4e660a9f792507237f;;;" +
//                "content-type:application/json;;;" +
//                "origin: https://trends.pinterest.com;;;" +
//                "referer: https://trends.pinterest.com/;;;" +
//                "x-new-site: true");
//        JSONObject jo = new JSONObject();
//        JSONArray ja = new JSONArray();
//        ja.add("pastel wall color ideas");
//        jo.put("queries", ja);
//        Data data = new Data(jo.toJSONString(), null);
//        Response response = Requests.request("post", "https://trends.pinterest.com/pin_search/", null, data, headers, 10000, true, proxies, null);
//        System.out.println(response.getHtml());
//        System.out.println(response.getStatusCode());
//        System.out.println(response.getCookie());
//        System.out.println(response.response);
//    }
}
