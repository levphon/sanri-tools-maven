package com.sanri.app.kafka;

public class KafkaConnInfo {
    private String broker;
    private String connName;
    private String version;
    private String zkConnString;
    private String thirdpartTool;

    public KafkaConnInfo() {
    }

    public KafkaConnInfo(String broker, String connName, String version, String zkConnString) {
        this.broker = broker;
        this.connName = connName;
        this.version = version;
        this.zkConnString = zkConnString;
    }

    public String getBroker() {
        return broker;
    }

    public String getConnName() {
        return connName;
    }

    public String getVersion() {
        return version;
    }

    public KafkaVersion getKafkaVersion(){
        return KafkaVersion.parse(this.version);
    }


    public String getZkConnString() {
        return zkConnString;
    }

    public enum KafkaVersion{
        OLD("old"),NEW("new");
        private String value;

        KafkaVersion(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static KafkaVersion parse(String version){
            KafkaVersion[] values = KafkaVersion.values();
            for (KafkaVersion kafkaVersion : values) {
                if(kafkaVersion.value.equals(version)){
                    return kafkaVersion;
                }
            }
            return null;
        }
    }

    public String getThirdpartTool() {
        return thirdpartTool;
    }

    public void setThirdpartTool(String thirdpartTool) {
        this.thirdpartTool = thirdpartTool;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setZkConnString(String zkConnString) {
        this.zkConnString = zkConnString;
    }
}
