package top.lf.core.cmd;

import cn.hutool.core.util.StrUtil;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: YuanYan
 * @Create At: 2018-11-14 11:50
 * @Description:
 */
public class Command {
    private Integer status = 200; //请求服务状态码
    private Boolean success = true;
    private String message;
    private Map<String, Object> in = new HashMap<>();
    private Map<String, Object> out = new HashMap<String, Object>();
    private Date invokeStartDate = new Date(); //统计输出接口费时

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public String getMessage() {
        if (StrUtil.isBlank(message)) message = success ? SUCCESS_MESSAGE : FAIL_MESSAGE;
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Map<String, Object> getIn() {
        return in;
    }
    public void setIn(Map<String, Object> in) {
        if (in == null) return;
        this.in = in;
        //startPage(in);
    }
    public Map<String, Object> getOut() {
        return out;
    }

    public String getDuration() {
        return MessageFormat.format("接口调用总计{0}毫秒", new Date().getTime() - invokeStartDate.getTime());
    }

    // commad method extend
    protected static final String MESSAGE_TEMPLATE = "{0}: 格式错误";
    protected static final String SUCCESS_MESSAGE = "业务执行成功！";
    protected static final String FAIL_MESSAGE = "操作失败";
}
