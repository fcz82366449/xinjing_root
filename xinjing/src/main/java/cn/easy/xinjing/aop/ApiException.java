package cn.easy.xinjing.aop;

/**
 * Created by chenzhongyi on 2016/10/10.
 */
public class ApiException extends RuntimeException {
    private int code;
    private String messgae;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.messgae = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }
}
