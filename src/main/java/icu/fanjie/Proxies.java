package icu.fanjie;

import java.io.Serializable;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import static icu.fanjie.Util.random;

public class Proxies implements Serializable {
    private final List<String[]> proxiesList = new ArrayList<>();
    private final Proxy.Type type;

    public Proxies(String proxies, Proxy.Type type) {
        this.type = type;
        if (proxies != null) {
            String[] split = proxies.split(";;;");
            for (String s : split) {
                if (s.split("@").length > 1) {
                    String user_password = s.split("@")[0];
                    String ip_port = s.split("@")[1];
                    String user = user_password.split(":")[0];
                    String password = user_password.split(":")[1];
                    String ip = ip_port.split(":")[0];
                    String port = ip_port.split(":")[1];
                    String[] proxy = {"auth", user, password, ip, port};
                    proxiesList.add(proxy);
                } else {
                    String ip = s.split(":")[0];
                    String port = s.split(":")[1];
                    String[] proxy = {"no_auth", ip, port};
                    proxiesList.add(proxy);
                }
            }
        }
    }

    public String[] randomProxy() {
        return proxiesList.get(random.nextInt(proxiesList.size()));
    }

    public int size() {
        return proxiesList.size();
    }

    public Proxy.Type getType() {
        return type;
    }
}
