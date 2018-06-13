package cn.easy.xinjing.bean.api;

import cn.easy.xinjing.domain.SingleselRecord;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Lenovo on 2017/8/16.
 */
public class SingleselRecordReturnBean extends ApiBaseBean {
    private String evaluatingId;
    private Timestamp evaluationTime;
    private BigDecimal totalScore;

    public String getEvaluatingId() {
        return evaluatingId;
    }

    public void setEvaluatingId(String evaluatingId) {
        this.evaluatingId = evaluatingId;
    }

    public Timestamp getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Timestamp evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }
}
