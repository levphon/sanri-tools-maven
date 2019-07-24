package com.sanri.app.serializer;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.nio.charset.Charset;

/**
 * 字符串序列化
 */
public class StringSerializer implements ZkSerializer {
    private Charset charset = Charset.forName("utf-8");
    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        if(data == null)return new byte[0];
        return ((String)data).getBytes(charset);
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        if(bytes == null)return null;
        return new String(bytes,charset);
    }
}
