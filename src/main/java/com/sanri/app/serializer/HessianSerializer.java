package com.sanri.app.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements ZkSerializer {
    @Override
    public byte[] serialize(Object o) throws ZkMarshallingError {
        if (o == null)
            return new byte[0];
        ByteArrayOutputStream byteArrayOutputStream = null;
        byteArrayOutputStream = new ByteArrayOutputStream();
        // Hessian的序列化输出
        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
        try {
            hessianOutput.writeObject(o);
        } catch (IOException e) {
            throw new ZkMarshallingError(e);
        } finally {
            IOUtils.closeQuietly(byteArrayOutputStream);
            try {
                hessianOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        if (bytes == null)
            return null;

        HessianInput hessianInput = null;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // Hessian的反序列化读取对象
        hessianInput = new HessianInput(byteArrayInputStream);
        try {
            return hessianInput.readObject();
        } catch (IOException e) {
           throw new ZkMarshallingError(e);
        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
            try {
                hessianInput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
