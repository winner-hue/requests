package icu.fanjie;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Headers implements Serializable {
    private final HashMap<String, Object> headers = new LinkedHashMap<>();

    public Headers(String params) {
        if (params != null) {
            String[] split = params.split(";;;");
            for (String s : split) {
                String s1 = s.split(":")[0].trim();
                String s2 = "";
                try {
                    s2 = s.split(":")[1].trim();
                } catch (Exception ignore) {
                }
                headers.put(s1, s2);
            }
        }
    }

    public okhttp3.Headers getHeaders() {
        okhttp3.Headers.Builder header = new okhttp3.Headers.Builder();
        for (String key : headers.keySet()) {
            header.add(key, (String) headers.get(key));
        }
        return header.build();
    }

    public HashMap<String, Object> getOriginHeaders() {
        return headers;
    }

    public int size() {
        return headers.size();
    }
}
