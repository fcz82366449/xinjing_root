package cn.easy.xinjing.bean.api;

/**
 * Created by raytine on 2017/6/23.
 */
public class GameEfsItemsBean  {
    /**游戏阶段**/
    private String gamephase;
    /**做的次数**/
    private Integer operationnumber;
    /**总个数**/
    private Integer totalnumber;
    /**正确的个数**/
    private Integer correctnumber;
    /**正确率**/
    private String correctrate;
    /**所用时间**/
    private String timeconsuming;
    /**是否完成**/
    private String completeif;
    /**错误个数*/
    private Integer inaccuracynumber;
    /**错误率*/
    private String inaccuracyrate;

    public Integer getInaccuracynumber() {
        return inaccuracynumber==null?0:inaccuracynumber;
    }

    public void setInaccuracynumber(Integer inaccuracynumber) {
        this.inaccuracynumber = inaccuracynumber;
    }

    public String getCorrectrate() {
        return correctrate;
    }

    public void setCorrectrate(String correctrate) {
        this.correctrate = correctrate;
    }

    public String getInaccuracyrate() {
        return inaccuracyrate;
    }

    public void setInaccuracyrate(String inaccuracyrate) {
        this.inaccuracyrate = inaccuracyrate;
    }

    public String getGamephase() {
        return gamephase;
    }

    public void setGamephase(String gamephase) {
        this.gamephase = gamephase;
    }

    public Integer getOperationnumber() {
        return operationnumber;
    }

    public void setOperationnumber(Integer operationnumber) {
        this.operationnumber = operationnumber;
    }

    public Integer getTotalnumber() {
        return totalnumber;
    }

    public void setTotalnumber(Integer totalnumber) {
        this.totalnumber = totalnumber;
    }

    public Integer getCorrectnumber() {
        return correctnumber;
    }

    public void setCorrectnumber(Integer correctnumber) {
        this.correctnumber = correctnumber;
    }




    public String getTimeconsuming() {
        return timeconsuming;
    }

    public void setTimeconsuming(String timeconsuming) {
        this.timeconsuming = timeconsuming;
    }

    public String getCompleteif() {
        return completeif;
    }

    public void setCompleteif(String completeif) {
        this.completeif = completeif;
    }
}
