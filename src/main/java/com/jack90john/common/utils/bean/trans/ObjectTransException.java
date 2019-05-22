package com.jack90john.common.utils.bean.trans;

/**
 * 对象转换异常
 *
 * @author jack
 * @version 1.0.0
 * @since 2.1.0.RELEASE
 */
public class ObjectTransException extends RuntimeException {
    public ObjectTransException() {
        super();
    }

    public ObjectTransException(String message) {
        super(message);
    }

    public ObjectTransException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectTransException(Throwable cause) {
        super(cause);
    }
}
