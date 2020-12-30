package dev.codesupport.web.common.exception;

public class FileTooBigException extends RuntimeException {

    public FileTooBigException() {
        super("File is too large");
    }

    public FileTooBigException(long maxSize) {
        super("File is too large, max size is " + maxSize + " bytes");
    }

}
