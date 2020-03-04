package com.jack90john.common.utils.bean.trans;

import java.io.*;

/**
 * Description
 *
 * @author jack
 * @version 1.0.0
 * @since
 */
public class DeepCloneUtil {

    @SuppressWarnings("unchecked")
    public static <T> T clone(T t) {
        if (t instanceof Serializable) {
            T result = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            ObjectOutputStream objectOutputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                //将对象写入流中
                objectOutputStream.writeObject(t);


                objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                //生成的新对象
                result = (T) objectInputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            return t;
        }

    }

}
