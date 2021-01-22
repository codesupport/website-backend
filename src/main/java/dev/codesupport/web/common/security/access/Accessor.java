package dev.codesupport.web.common.security.access;

/**
 * Used for access evaluations with no specific associated class type
 */
public enum Accessor {
    NONE,
    CURRENT,
    DISCORD,
    IMAGE,
    PERMISSION,
    USER,
    TOKEN
}
