package com.sanri.app.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;

/**
 * kryo 序列化
 */
public class KryoSerializer implements ZkSerializer {
    private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>();

    static {
        kryos.set(new Kryo());
    }

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        if (data == null) return new byte[0];
        Kryo kryo = kryos.get();
        kryo.setReferences(false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, data);
        output.flush();
        return baos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        if (bytes == null) return null;

        Kryo kryo = kryos.get();
        kryo.setReferences(false);
        Input input = new Input(bytes);
        return kryo.readClassAndObject(input);
    }
}
