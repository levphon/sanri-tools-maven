package com.sanri.app.kafka;

import com.alibaba.fastjson.JSONObject;
import com.sanri.app.serializer.FastJsonSerializer;
import com.sanri.app.serializer.StringSerializer;
import com.sanri.app.servlet.KafkaServlet;
import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class BaseKafkaMonitor {
    protected Log logger = LogFactory.getLog(getClass());

    /**
     * 创建主题
     * @param topic
     * @param partitions
     * @param replication
     */
   ZkSerializer zkSerializer =  new StringSerializer();
   ZkSerializer fastJsonSerializer = new FastJsonSerializer();
    public void createTopic(ZkUtils zkUtils,String topic, int partitions, int replication){
        zkUtils.zkClient().setZkSerializer(zkSerializer);
        AdminUtils.createTopic(zkUtils,topic,partitions,replication, new Properties(), RackAwareMode.Enforced$.MODULE$);
        zkUtils.zkClient().setZkSerializer(fastJsonSerializer);
    }
    /**
     * 获取配置的连接信息
     * @param name
     * @return
     * @throws IOException
     */
    public KafkaConnInfo connInfo(String name) throws IOException {
        File file = new File(KafkaServlet.kafkaConnDir, name);
        String fileToString = FileUtils.readFileToString(file);
        KafkaConnInfo kafkaConnInfo = JSONObject.parseObject(fileToString, KafkaConnInfo.class);
        return kafkaConnInfo;
    }

    public String broker(String name) throws IOException {
        return connInfo(name).getBroker();
    }

    /**
     * 删除主题
     * @param zkUtils
     * @param topic
     */
    public void deleteTopic(ZkUtils zkUtils, String topic) {
        AdminUtils.deleteTopic(zkUtils,topic);
    }
}
