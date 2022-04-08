package icu.fanjie;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Params {
    private final HashMap<String, Object> map = new LinkedHashMap<>();

    public Params(String params) {
        if (params != null) {
            String[] split = params.split(";;;");
            for (String s : split) {
                String s1 = s.split(":")[0].trim();
                String s2 = "";
                try {
                    s2 = s.split(":")[1].trim();
                } catch (Exception ignore) {
                }
                map.put(s1, s2);
            }
        }
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            list.add(key + "=" + value);
        }
        return "?" + StringUtils.join(list, "&");
    }

    public int size() {
        return map.size();
    }
}
