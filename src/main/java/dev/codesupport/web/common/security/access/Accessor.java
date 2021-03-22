package dev.codesupport.web.common.security.access;

/**
 * Used for access evaluations with no specific associated class type
 */
public enum Accessor {
    NONE,
    ARTICLE,
    ARTICLES,
    ARTICLE_REVISION,
    ARTICLE_REVISIONS,
    CURRENT,
    DISCORD,
    IMAGE,
    PASSWORD,
    PERMISSION,
    USER,
    TOKEN
}
