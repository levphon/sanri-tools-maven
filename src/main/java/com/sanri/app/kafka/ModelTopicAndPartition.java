package com.sanri.app.kafka;

import kafka.common.TopicAndPartition;

public class ModelTopicAndPartition  {
    private TopicAndPartition topicAndPartition;

    public ModelTopicAndPartition() {
    }

    public ModelTopicAndPartition(TopicAndPartition topicAndPartition) {
        this.topicAndPartition = topicAndPartition;
    }

    public String getTopic(){
        return topicAndPartition.topic();
    }
    public int getPartition(){
        return topicAndPartition.partition();
    }

}
