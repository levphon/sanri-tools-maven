package learntest;

import redis.clients.jedis.Jedis;

import java.util.List;

public class TestRedisMain {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.0.39",6379,1000,60000);
        jedis.auth("hhxredis");

        Long dbSize = jedis.dbSize();
        Long db = jedis.getDB();
        System.out.println(db);

//        String info = jedis.info();
//        System.out.println(info);

        List<String> databases = jedis.configGet("databases");

        System.out.println(databases);
        jedis.close();
    }
}
