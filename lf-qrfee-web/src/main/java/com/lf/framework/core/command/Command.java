package com.lf.framework.core.command;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.Page;
import com.lf.framework.core.exception.ParameterException;
import com.lf.framework.core.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    protected static final String MESSAGE_TEMPLATE = "{0}: 格式错误";
    protected static final String SUCCESS_MESSAGE = "业务执行成功！";
    protected static final String FAIL_MESSAGE = "业务操作失败！";

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
        if (StringUtils.isBlank(message)) message = success ? SUCCESS_MESSAGE : FAIL_MESSAGE;
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
        startPage(in);
    }

    public Map<String, Object> getOut() {
        return out;
    }

    public String getDuration() {
        return MessageFormat.format("接口调用耗时{0}毫秒", new Date().getTime() - invokeStartDate.getTime());
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * getParams将分析请求头中是否含有分页参数并封装
     */
    @JsonIgnore
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.putAll(this.getIn());
        params.putAll(this.getRequestParams());
        return params;
    }

    /*
    * 动态分页参数分析(参数不能随便更改，通用分页插件中固定)
    * */
    private Map<? extends String, ? extends Object> getRequestParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        HttpServletRequest request = this.getRequest();
        String key, value;

        Boolean isPage = true;
        key = "isPage";
        value = request.getParameter(key);
        if (StringUtils.isNotBlank(value)) {
            isPage = "true".equals(value.trim());
            params.put(key, isPage);
        }

        if (!isPage) return params;
        key = "pageNo";
        value = request.getParameter(key);
        if (NumberUtils.isNumber(value)) {
            params.put(key, value);
        }

        key = "pageSize";
        value = request.getParameter(key);
        if (NumberUtils.isNumber(value)) {
            params.put(key, value);
        }

        key = "order";
        value = request.getParameter(key);
        if (StringUtils.isNotBlank(value)) {
            String orderBy = value;//StringUtil.camelHump2Underline(value);
            key = "orderDesc";
            value = request.getParameter(key);
            if (StringUtils.isNotBlank(value)) {
                orderBy += " " + value;
            }
            params.put("orderBy", orderBy);
        }
        return params;
    }

    /**
     * 获取map参数
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getInMap(String key) {
        return this.getIn(key, Map.class);
    }

    /**
     * 获取map参数
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getInMapObject(String key) {
        return this.getIn(key, Map.class);
    }

    /**
     * 添加输出(合并到out)
     * @param map 输出
     * @return
     */
    public Command setOutMap(Map<String, ?> map) {
        if (map == null || map.size() == 0) return this;
        this.out.putAll(map);
        Iterator<String> keys = map.keySet().iterator();
        Object object;
        while (keys.hasNext()) {
            object = map.get(keys.next());
            if (object == null) continue;
            if (!(object instanceof Page)) continue;

            @SuppressWarnings("unchecked")
            Page<Object> page = (Page<Object>) object;
            this.out.put("isPage", true);
            this.out.put("pageNo", page.getPageNum());
            this.out.put("pageSize", page.getPageSize());
            this.out.put("pages", page.getPages());
            this.out.put("totalSize", page.getTotal());
        }

        return this;
    }

    /**
     * 使用key 添加输出
     * @param key 键
     * @param map 输出
     * @return
     */
    public Command setOutMap(String key, Map<String, ?> map) {
        if (map == null || map.size() == 0) return this;
        this.out.put(key, map);
        Iterator<String> keys = map.keySet().iterator();
        Object object;
        if (!this.out.containsKey("pageNo")) {
            while (keys.hasNext()) {
                object = map.get(keys.next());
                if (object == null) continue;
                if (!(object instanceof Page)) continue;

                @SuppressWarnings("unchecked")
                Page<Object> page = (Page<Object>) object;
                this.out.put("isPage", true);
                this.out.put("pageNo", page.getPageNum());
                this.out.put("pageSize", page.getPageSize());
                this.out.put("pages", page.getPages());
                this.out.put("totalSize", page.getTotal());
            }
        }

        return this;
    }

    /**
     * 指定一个key 添加集合List(可分页)
     * @param key  键
     * @param list 集合
     * @return
     */
    @SuppressWarnings("unchecked")
    public Command setOutList(String key, List<?> list) {
        if (list == null) return this;
        this.out.put(key, list);
        if (!this.out.containsKey("pageNo") && list instanceof Page) {
            Page<Object> page = (Page<Object>) list;
            this.out.put("isPage", true);
            this.out.put("pageNo", page.getPageNum());
            this.out.put("pageSize", page.getPageSize());
            this.out.put("pages", page.getPages());
            this.out.put("totalSize", page.getTotal());
        }
        return this;
    }

    /**
     * 指定key为"list" 添加集合List (可分页)
     * @param list 集合
     * @return
     */
    public Command setOutList(List<?> list) {
        return setOutList("list", list);
    }

    /**
     * 添加输出
     * @param object 类型为Map,调用setOutMap输出; 类型为List 调用setOutList输出; 类型为元素据类型, 指定key为"value" 输出; 其他bean转Map, 合并到输出out
     * @return
     */
    @SuppressWarnings("unchecked")
    public Command setOut(Object object) {
        if (object == null) return this;
        else if (object instanceof Map) setOutMap((Map<String, ?>) object);
        else if (object instanceof List) setOutList((List<Object>) object);
        else if (isMetadataType(object)) this.out.put("value", object);
        else {

            String json = JSONObject.toJSONString(object);
            JSONObject jsonObject = JSONObject.parseObject(json);

            Iterator<String> keys = jsonObject.keySet().iterator();
            String key;
            Object value;
            while (keys.hasNext()) {
                key = keys.next();
                value = null;
                value = jsonObject.get(key);
                if (value != null) this.out.put(key, value);
            }
        }
        return this;
    }

    /**
     * 添加输出（指定key）
     * @param key    键
     * @param object 类型为Map,调用setOutMap输出; 类型为List 调用setOutList输出; 类型为元素据类型, 直接输出; 其他bean转Map, 合并到输出out
     * @return
     */
    @SuppressWarnings("unchecked")
    public Command setOut(String key, Object object) {
        if (object == null) return this;
        else if (object instanceof Map) setOutMap(key, (Map<String, ?>) object);
        else if (object instanceof List) setOutList(key, (List<Object>) object);
        else if (isMetadataType(object)) this.out.put(key, object);
        else this.out.put(key, object);
        return this;
    }

    /**
     * 直接提取输入参数 到bean中
     * @param t model
     * @return
     */
    public <T> T populate(T t) {
        String key = "";
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                key = property.getName();
                if (this.in.containsKey(key)) {
                    Object value = this.in.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    //分析setter入参类型,只会有一个参数,对Integer参数特殊处理
                    Class<?>[] pts = setter.getParameterTypes();
                    if (pts != null && pts.length > 0) {
                        //String typeName = pts[0].getTypeName();
                        String typeName = pts[0].getName();
                        if (typeName.equals("java.lang.Integer") && value != null) {
                            setter.invoke(t, Integer.parseInt(value.toString()));
                            continue;
                        }
                        if (typeName.equals("java.util.Date") && value != null) {
                            setter.invoke(t, DateUtils.parseDate(value.toString()));
                            continue;
                        }
                    }
                    setter.invoke(t, value);
                }
            }
        } catch (Exception e) {
            throw new ParameterException("参数【" + key + "】转换出错，请检查格式！");
        }
        return t;
    }


    /**
     * 根据key键 获取输入参数
     * @param key   键
     * @param clazz 类型
     * @return
     */
    public <T> List<T> getInArray(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key)) throw new SystemException("key must isn't a empty string.");
        if (null == clazz) throw new SystemException(MessageFormat.format("Do not get in[{0}](Object) clazz null.", key));

        String value = (String) this.in.get(key);
        if (StringUtils.isBlank(value)) return null;

        try {
            return JSONArray.parseArray(value, clazz);
        } catch (Exception e) {
            throw new ParameterException(MessageFormat.format("Get Parameter in[{0}](Array) formal error.", key), e);
        }
    }

    /**
     * 根据key键 获取输入参数
     * @param key 键
     * @return
     */
    public List<String> getInArrayString(String key) {
        return this.getInArray(key, String.class);
    }


    /**
     * 根据key键 获取输入参数
     * @param key          键
     * @param defaultValue 默认值
     * @param clazz        类型
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public <T> T getIn(String key, T defaultValue, Class<T> clazz) {
        if (StringUtils.isBlank(key)) throw new SystemException("key must isn't a empty string.");
        if (null == clazz && null == defaultValue) {
            if (null == defaultValue) throw new SystemException(MessageFormat.format("Do not get in[{0}] defaultValue null or clazz null.", key));
        }

        if (clazz == null) {
            clazz = (Class<T>) defaultValue.getClass();
        }

        String value = (String) this.in.get(key);
        if (StringUtils.isBlank(value)) {
            if (defaultValue != null) return defaultValue;
            else return null;
        }

        if (isMetadataClass(clazz)) return (T) value;

        try {
            return JSONObject.parseObject(value, clazz);
        } catch (Exception e) {
            throw new ParameterException(MessageFormat.format("Get a Parameter in[{0}] formal error.", key), e);
        }
    }

    /**
     * 根据key键 获取输入参数
     * @param key   键
     * @param clazz 类型
     * @return
     */
    public <T> T getIn(String key, Class<T> clazz) {
        if (null == clazz) throw new SystemException(MessageFormat.format("Do not set in[{0}] clazz null.", key));
        return this.getIn(key, null, clazz);
    }

    public <T> T getIn(String key, T defaultValue) {
        if (null == defaultValue) throw new SystemException(MessageFormat.format("Do not set in[{0}] defaultValue null .", key));
        return this.getIn(key, defaultValue, null);
    }

    /**
     * 根据key键 获取输入参数
     * @param key 键
     * @return
     */
    public String getInString(String key) {
        return this.getIn(key, String.class);
    }

    /**
     * 根据key键 获取输入参数
     * @param key 键
     * @return
     */
    public Integer getInInteger(String key) {
        return this.getIn(key, Integer.class);
    }

    /**
     * 根据key键 获取输入参数
     * @param key 键
     * @return
     */
    public Long getInLong(String key) {
        return this.getIn(key, Long.class);
    }

    /**
     * 根据key键 获取输入参数
     * @param key          键
     * @param defaultValue 默认值
     * @return
     */
    public String getInString(String key, String defaultValue) {
        return this.getIn(key, defaultValue);
    }

    /**
     * 根据key键 获取输入参数
     * @param key          键
     * @param defaultValue 默认值
     * @return
     */
    public Integer getInInteger(String key, Integer defaultValue) {
        return this.getIn(key, defaultValue);
    }

    /**
     * 根据key键 获取输入参数
     * @param key          键
     * @param defaultValue 默认值
     * @return
     */
    public Long getInLong(String key, Long defaultValue) {
        return this.getIn(key, defaultValue);
    }


    protected boolean startPage(Map<String, Object> in) {
        return true;
    }

    protected static Boolean isNumber(String value) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(value);
        return isNum.matches();
    }

    protected boolean isMetadataType(Object object) {
        if (object == null) return false;
        if (object instanceof String) return true;
        if (object instanceof Integer) return true;
        if (object instanceof Double) return true;
        if (object instanceof Float) return true;
        if (object instanceof Long) return true;
        if (object instanceof BigDecimal) return true;
        if (object instanceof Date) return true;
        if (object instanceof Boolean) return true;
        return false;
    }

    protected boolean isMetadataClass(Class<?> clazz) {
        if (clazz == null) return false;
        if (clazz == String.class) return true;
        if (clazz == Integer.class) return true;
        if (clazz == Double.class) return true;
        if (clazz == Float.class) return true;
        if (clazz == Long.class) return true;
        if (clazz == BigDecimal.class) return true;
        if (clazz == Boolean.class) return true;
        return false;
    }
}
