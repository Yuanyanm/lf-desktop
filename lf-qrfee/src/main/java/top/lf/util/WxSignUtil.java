package top.lf.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.security.MessageDigest;
import java.util.*;

/**
 * @Author: YuanYan
 * @Create At: 2018-10-09 15:24
 * @Description:
 */
public class WxSignUtil {
    public static final String USER_AGENT = "WXPaySDK/3.0.9" +
            " (" + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version") +
            ") Java/" + System.getProperty("java.version") + " HttpClient/" + HttpClient.class.getPackage().getImplementationVersion();

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String createSign(String characterEncoding, SortedMap<String,String> parameters) throws Exception {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v +"&");
            }
        }
        //Scscdslqyqzyyyweixin201810221030
        sb.append("key=Scscdslqyqzyyyweixin201810221030");//Key为商户平台的API秘钥
        System.out.println("-----------------------------------------------");
        System.out.println(sb.toString());
        System.out.println("-----------------------------------------------\n");
        String sign = MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        //sign = WXPayUtil.MD5(sb.toString()).toUpperCase();
        return sign;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println(">>>模拟微信支付<<<");
        System.out.println("==========华丽的分隔符==========");
        //微信api提供的参数
        String appid = "wxebaed8c8956f55d9";//APPID
        String mch_id = "1514679851";//商户号
        String device_info = "D1000";
        String body = "微信刷卡支付测试-"+RandomUtil.UpperString(5);
        String nonce_str = RandomUtil.UUID();
        SortedMap<String,String> parameters = new TreeMap<String, String>();
        parameters.put("appid", "wxebaed8c8956f55d9");
        parameters.put("mch_id", "1514679851");
        parameters.put("device_info", "D1000");
        parameters.put("body", "微信刷卡支付测试-"+RandomUtil.UpperString(5));
        parameters.put("nonce_str", RandomUtil.UUID());//随机字符串
        parameters.put("out_trade_no",RandomUtil.UUID());//商户测交易号
        parameters.put("total_fee","1");//交易金额(分)
        parameters.put("fee_type","CNY");//币种
        parameters.put("spbill_create_ip","192.168.0.1");//调用微信支付API的机器IP
        parameters.put("time_start",DateUtil.currentDTime());// 交易起始时间,格式为yyyyMMddHHmmss
        //parameters.put("time_expire","");//交易失效时间,格式为yyyyMMddHHmmss，最小失效时间1分钟
        /**
         * 扫码支付授权码，设备读取用户微信中的条码或者二维码信息，用户
         * 刷卡条形码规则：18位纯数字，以10、11、12、13、14、15开头
         * */
        parameters.put("auth_code","134759957806951315");
        String characterEncoding = "UTF-8";
        String mySign = createSign(characterEncoding,parameters);
        System.out.println("签名(sign)："+mySign);
        parameters.put("sign",mySign);
        //String orderInfo = WXPayUtil.mapToXml(parameters);
        //System.out.println(orderInfo);


        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build(),
                null,
                null,
                null
        );

    HttpClient httpClient = HttpClientBuilder.create()
            .setConnectionManager(connManager)
            .build();
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/micropay");
       // StringEntity postEntity = new StringEntity(orderInfo, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", USER_AGENT + " " + "1514679851");
        //httpPost.setEntity(postEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = EntityUtils.toString(httpEntity, "UTF-8");
        //支付等待对话框
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("I have a great message for you!");
        alert.showAndWait();*/
        //支付结果标识
        boolean payFlag = false;
        while (!payFlag){
            //当前线程每两秒轮训一次支付结果
            Thread.sleep(2000);
            System.out.println("支付结果查询中... "+DateUtil.currentDateTime());
        }
        System.out.println("======================================= \n"+result);
    }

}
