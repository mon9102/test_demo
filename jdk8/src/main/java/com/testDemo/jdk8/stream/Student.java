package com.testDemo.jdk8.stream;

import java.math.BigDecimal;

/**
 * @Auther: zouren
 * @Date: 2019/3/27 09:30
 * @Description:
 */
public class Student {
    private String name;
    private Integer score;
    private BigDecimal avgScore;
    public Student(String name,Integer score){
        this.name=name;
        this.score=score;
    }
    public Student(String name,Integer score,BigDecimal avgScore){
        this.name=name;
        this.score=score;
        this.avgScore=avgScore;
    }
    public BigDecimal getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(BigDecimal avgScore) {
        this.avgScore = avgScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return this.name+":"+this.score;
    }
}
