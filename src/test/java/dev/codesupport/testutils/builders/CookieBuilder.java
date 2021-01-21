package dev.codesupport.testutils.builders;

import javax.servlet.http.Cookie;

public class CookieBuilder {

    private String name;
    private String value;
    private int maxAge;
    private boolean secure;
    private boolean httpOnly;
    private String comment;
    private String domain;
    private String path;
    private int version;

    private CookieBuilder() {

    }

    public static CookieBuilder builder() {
        return new CookieBuilder();
    }

    public Cookie build() {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);
        cookie.setComment(comment);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setVersion(version);

        return cookie;
    }

    public CookieBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CookieBuilder value(String value) {
        this.value = value;
        return this;
    }

    public CookieBuilder maxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public CookieBuilder secure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public CookieBuilder httpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public CookieBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public CookieBuilder domain(String domain) {
        this.domain = domain;
        return this;
    }

    public CookieBuilder path(String path) {
        this.path = path;
        return this;
    }

    public CookieBuilder version(int version) {
        this.version = version;
        return this;
    }
}
