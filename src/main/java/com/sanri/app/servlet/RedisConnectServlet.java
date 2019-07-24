package com.sanri.app.servlet;

import com.alibaba.fastjson.JSONObject;
import com.sanri.app.BaseServlet;
import com.sanri.frame.RequestMapping;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;
import sanri.utils.NumberUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/redis")
public class RedisConnectServlet extends BaseServlet {
    static File redisConfigDir;
    static {
       redisConfigDir = mkConfigPath("redis");
    }

    static Map<String,Jedis> jedisMap = new HashMap<String, Jedis>();

    /**
     * 写入 redis 连接信息
     * @param name
     * @param address
     * @return
     * @throws IOException
     */
    public int writeConfig(String name,String address,String auth) throws IOException {
        File config = new File(redisConfigDir,name);
        Map<String,String> obj = new HashMap<String, String>();
        obj.put("address",address);
        obj.put("auth",auth);
        FileUtils.writeStringToFile(config,JSONObject.toJSONString(obj));
        return 0;
    }

    public String [] connNames(){
        return redisConfigDir.list();
    }
    public String detail(String name) throws IOException {
       File config = new File(redisConfigDir,name);
       return FileUtils.readFileToString(config);
    }

    /**
     * 获取连接信息
     * @param name
     * @return
     * @throws IOException
     */
    public String redisInfo(String name) throws IOException {
        Jedis jedis = jedis(name);
        return jedis.info();
    }

    /**
     * 获取数据库数量
     * @param name
     * @return
     * @throws IOException
     */
    public int dbs(String name) throws IOException {
        Jedis jedis = jedis(name);
        List<String> databases = jedis.configGet("databases");
        int size = NumberUtil.toInt(databases.get(1));
        return size;
    }

    /**
     * 集合个数
     * @param name
     * @param index
     * @return
     * @throws IOException
     */
    public long dbSize(String name,String index) throws IOException {
        Jedis jedis = jedis(name);
        jedis.select(NumberUtil.toInt(index));
        return jedis.dbSize();
    }

    /**
     * 扫描所有的 key 数据
     * @param pattern
     * @param cursor
     * @return
     */
    public ScanResult<String> scan(String name,String index, String pattern, String cursor, int limit) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));

        ScanParams scanParams = new ScanParams();
        scanParams.count(limit);
        if(StringUtils.isNotBlank(pattern)) {
            scanParams.match(pattern);
        }
        ScanResult<String> scan = jedis.scan(cursor, scanParams);
        return scan;
    }

    /**
     * 查看 list 值列表
     * @param name
     * @param index
     * @param key
     * @param offset
     * @param limit
     * @param serialize
     * @return
     * @throws IOException
     */
    public List<Object> lrange(String name,String index,String key,long offset, long limit,String serialize) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));
        long end = (offset + 1) * limit;

        ZkSerializer zkSerializer = ZkServlet.zkSerializerMap.get(serialize);
        if(zkSerializer == null){zkSerializer = hexSerizlize;}

        List<byte[]> lrange = jedis.lrange(key.getBytes(charset), offset, end);
        List<Object> datas = new ArrayList<Object>();

        if(CollectionUtils.isNotEmpty(lrange)) {
            for (byte[] bytes : lrange) {
                Object data = zkSerializer.deserialize(bytes);
                datas.add(data);
            }
        }

        return datas;
    }


    /**
     * zset 搜索键数据
     * @param name
     * @param index
     * @param key
     * @param pattern
     * @param cursor
     * @param limit
     * @return
     * @throws IOException
     */
    public ScanResult<Object> zscan(String name,String index,String key,String pattern, String cursor, int limit,String serialize) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));

        ZkSerializer zkSerializer = ZkServlet.zkSerializerMap.get(serialize);
        if(zkSerializer == null){zkSerializer = hexSerizlize;}

        ScanParams scanParams = new ScanParams();
        scanParams.count(limit);
        if(StringUtils.isNotBlank(pattern)) {
            scanParams.match(pattern);
        }


        ScanResult<Tuple> scanResult = jedis.zscan(key.getBytes(charset), cursor.getBytes(charset), scanParams);

        List<Tuple> result = scanResult.getResult();
        List<Object> newResult = new ArrayList<Object>();

        for (Tuple tuple : result) {
            byte[] binaryElement = tuple.getBinaryElement();
            Object deserialize = zkSerializer.deserialize(binaryElement);
            newResult.add(deserialize);
        }
        return new ScanResult<Object>(scanResult.getStringCursor(),newResult);
    }

    /**
     * map 数据扫描
     * @param name
     * @param index
     * @param key
     * @param pattern
     * @param cursor
     * @param limit
     * @return
     * @throws IOException
     */
    public ScanResult<Map.Entry<String, String>> hscan(String name, String index, String key, String pattern, String cursor, int limit) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));

        ScanParams scanParams = new ScanParams();
        scanParams.count(limit);
        if(StringUtils.isNotBlank(pattern)) {
            scanParams.match(pattern);
        }

        ScanResult<Map.Entry<String, String>> hscan = jedis.hscan(key, cursor, scanParams);
        return hscan;
    }

    /**
     * 查看数据键类型
     * @param name
     * @param index
     * @param key
     * @return
     */
    public String type(String name,String index,String key) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));
        String type = jedis.type(key);
        return type;
    }

    /**
     * string 类型的数据读取
     * @param name
     * @param index
     * @param key
     * @param serialize
     * @return
     * @throws IOException
     */
    public Object readValue(String name,String index,String key,String serialize) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));

        //获取当前键类型
        String type = type(name, index, key);
        RedisType redisType = RedisType.parse(type);
        ZkSerializer zkSerializer = ZkServlet.zkSerializerMap.get(serialize);
        if(zkSerializer == null){zkSerializer = hexSerizlize;}

        if(redisType == RedisType.string){
            byte[] bytes = jedis.get(key.getBytes(charset));
            return zkSerializer.deserialize(bytes);
        }

        throw new IllegalArgumentException("类型不支持读取数据:"+type);
    }

    /**
     * 读取 hash key 数据
     * @param name
     * @param index
     * @param key
     * @param field
     * @param serialize
     * @return
     * @throws IOException
     */
    public Object readHashValue(String name,String index,String key,String field,String serialize) throws IOException {
         Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));

         ZkSerializer zkSerializer = ZkServlet.zkSerializerMap.get(serialize);
         if(zkSerializer == null){zkSerializer = hexSerizlize;}

        byte[] value = jedis.hget(key.getBytes(charset), field.getBytes(charset));
        return zkSerializer.deserialize(value);
    }

    /**
     * 查询集合数据长度
     * @param name
     * @param index
     * @param key
     * @return
     * @throws IOException
     */
    public long keyLength(String name,String index,String key) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));

        String type = type(name, index, key);
        RedisType redisType = RedisType.parse(type);
        switch (redisType){
            case Set:
            case ZSet:
                return jedis.zcard(key);
            case List:
                return jedis.llen(key);
            case Hash:
                return jedis.hlen(key);
            case string:
                return jedis.strlen(key);
        }

        return -1;
    }

    /**
     * 有效时间以秒为单位返回
     * @param name
     * @param index
     * @param key
     * @return
     * @throws IOException
     */
    public long ttl(String name,String index,String key) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));
        return jedis.ttl(key);
    }

    /**
     * 有效时间以秒秒为单位返回
     * @param name
     * @param index
     * @param key
     * @return
     * @throws IOException
     */
    public long pttl(String name,String index,String key) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));
        return jedis.pttl(key);
    }

    /**
     * 顶层 key 批量删除
     * @param name
     * @param index
     * @param key
     * @param prefix
     * @return
     * @throws IOException
     */
    public long batchDelete(String name,String index,String key,String prefix) throws IOException {
        Jedis jedis = jedis(name);jedis.select(NumberUtil.toInt(index));
        long delCount = 0;
        long cursor= 0;
        do{
            ScanResult<String> scanResult = scan(name, index, prefix, cursor + "", 1000);

            List<String> result = scanResult.getResult();
            jedis.del(result.toArray(new String []{}));

            delCount += result.size();
            logger.info("已经删除:"+delCount+" 个元素");

            cursor = NumberUtil.toLong(scanResult.getStringCursor());
        }while( cursor != 0);

        return delCount;
    }

    enum RedisType{
        string("string"),Set("set"),ZSet("zset"),Hash("hash"),List("list");
        private String value;

        RedisType(String value) {
            this.value = value;
        }

        public static RedisType parse(String type){
            RedisType[] values = RedisType.values();
            for (RedisType value : values) {
                if(value.value.equals(type)){
                    return value;
                }
            }
            return null;
        }
    }


    Jedis jedis(String name) throws IOException {
        Jedis jedis = jedisMap.get(name);
        if(jedis == null){
            JSONObject jsonObject = JSONObject.parseObject(detail(name));
            String address = jsonObject.getString("address");
            String auth = jsonObject.getString("auth");

            String[] split = StringUtils.split(address, ':');
            jedis = new Jedis(split[0], NumberUtil.toInt(split[1]), 1000, 60000);
            if(StringUtils.isNotBlank(auth)){
                jedis.auth(auth);
            }
            jedisMap.put(name,jedis);
        }
        return jedis;
    }

    private static ZkSerializer hexSerizlize = new HexDeserialize();

    private static class HexDeserialize implements ZkSerializer{
        @Override
        public byte[] serialize(Object o) throws ZkMarshallingError {
            throw new IllegalStateException("未实现序列化");
        }
        @Override
        public Object deserialize(byte[] bytes) throws ZkMarshallingError {
            return Hex.encodeHex(bytes);
        }
    }
}
