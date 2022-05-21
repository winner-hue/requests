package icu.fanjie;

import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable {
    private JSONObject jo = new JSONObject();
    private final String postType;

    public Data(String params, String postType) {
        this.postType = postType;
        if (params != null) {
            try {
                jo = JSONObject.parseObject(params);
            } catch (Exception e) {
                String[] split = params.split(";;;");
                for (String s : split) {
                    String s1 = s.split(":")[0].trim();
                    String s2 = "";
                    try {
                        s2 = s.split(":")[1].trim();
                    } catch (Exception ignore) {
                    }
                    jo.put(s1, s2);
                }
            }
        }
    }

    public String getPostType() {
        return postType;
    }

    public String getData() {
        return jo.toJSONString();
    }

    public RequestBody getFormData() {
        FormBody.Builder data = new FormBody.Builder();
        for (String key : jo.keySet()) {
            data.add(key, jo.getString(key));
        }
        return data.build();
    }

    public List<NameValuePair> getPostParams() {
        List<NameValuePair> params = new ArrayList<>();
        for (String key : jo.keySet()) {
            params.add(new BasicNameValuePair(key, jo.getString(key)));
        }
        return params;
    }
}
