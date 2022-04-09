package icu.fanjie;

import okhttp3.Request;

public class Response {
    public int statusCode;
    public String html;
    public Request request;
    public okhttp3.Response response;
    public String cookie;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public okhttp3.Response getResponse() {
        return response;
    }

    public void setResponse(okhttp3.Response response) {
        this.response = response;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
