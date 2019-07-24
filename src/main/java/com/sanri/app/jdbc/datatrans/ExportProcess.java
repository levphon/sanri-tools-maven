package com.sanri.app.jdbc.datatrans;

import sanri.utils.NumberUtil;

/**
 * 单线程进度
 */
public class ExportProcess {
    private double percent = 0;
    private String nowDo;
    private Integer id;
    private String name;

    public ExportProcess() {
    }


    public ExportProcess(Integer id,String name) {
        this.id = id;
        this.name = name;
    }


    public ExportProcess(Integer id,String name, double percent, String nowDo) {
        this.id = id;
        this.name = name;
        this.percent = percent;
        this.nowDo = nowDo;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getNowDo() {
        return nowDo;
    }

    public void setNowDo(String nowDo) {
        this.nowDo = nowDo;
    }

    /**
     * 上传进度
     * @param percent
     * @param nowDo
     */
    public void process(double percent,String nowDo){
        this.percent = percent;
        this.nowDo = nowDo;
    }

    /**
     * 计算进度
     * @param percent
     * @param weight
     * @param nowDo
     */
    public double processCalc(double percent, double weight){
        return NumberUtil.toDouble(NumberUtil.round(percent * weight,2));
    }

    /**
     * 添加进度
     * @param percent
     * @param nowDo
     */
    public void plusProcess(double percent, String nowDo){
        this.percent += percent;
        this.percent = NumberUtil.toDouble(NumberUtil.round(this.percent,2));
        this.nowDo = nowDo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
