package entity;

import java.io.Serializable;
/**
 * @Author: YangRunTao
 * @Description: 通用返回结果成功与否包装类
 * @Date: 2019/05/08 18:07
 * @Modified By:
 */
public class Result implements Serializable {

    private static final long serialVersionUID = 758545729795438093L;
    private Boolean Success;//是否成功标记
    private String message;//消息
    public Result() {
    }

    public Result(Boolean success, String message) {
        Success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public void setSuccess(Boolean success) {
        Success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
