package top.lf.util;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.xpath.XPathConstants;
import java.util.HashMap;

/**
 * @Author: YuanYan
 * @Create At: 2018-10-09 17:29
 * @Description:
 */
public class Test {
    public static String CHL_TOKEN = "3A98CFDAFC8FA17543B4400A560D735F";
    public static String CHL_SECRETKEY = "35E0E08E8D1AB889";
    public static void main(String[] args) {
        func02();
    }

    public static void func02() {
        String url = "http://117.176.149.97:8991/ExternalServices/ZL_InformationService.asmx/UserManager";
        try {
            Document xmlDoc = DocumentHelper.createDocument();
            Element rootEle = xmlDoc.addElement("ROOT");
            Element eleToken = rootEle.addElement("TOKEN");
            eleToken.setText("sOuNjP0PAJkfLtASnytn1ubLwS6a16dVh1dkU3QcT3OUooMli05kmX/uS1oRvr2x");
            Element serviceEle= rootEle.addElement("SERVICE");
            serviceEle.setText("BindCard.UserInfoByCardNO.Query");
            /**
             *根据实际业务构造DataParam节点内部数据
             * */
            String dataParam = "<ZJH>513922198802181090</ZJH><ZJLX>二代身份证</ZJLX>";
            rootEle.addElement("DATAPARAM").setText(ZlCryptoTool.EncryptData(dataParam,"35E0E08E8D1AB889"));
            String reData = xmlDoc.asXML();
            HashMap<String, Object> paramMap = new HashMap<>();
            System.out.println("ReData原始数据：\n"+reData+"\n");
            paramMap.put("ReData", reData);
            String result = HttpUtil.post(url, paramMap);
            System.out.println("接口返回数据：\n"+result+"\n");
            Document outDoc = DocumentHelper.parseText(result);
            org.w3c.dom.Document outXml = XmlUtil.parseXml(result);
            Object zlState = XmlUtil.getByXPath("//ROOT/STATE", outXml, XPathConstants.STRING);
            System.out.println("业务状态标识-STATE： "+zlState.toString()+"\n");
            Object outData = XmlUtil.getByXPath("//ROOT/DATAPARAM", outXml, XPathConstants.STRING);
            System.out.println("OUT.DATAPARAM(解密前)-> "+outData.toString()+"\n");
            System.out.println("OUT.DATAPARAM(解密后)-> "+ZlCryptoTool.DecryptData(outData.toString(),"35E0E08E8D1AB889"));
        } catch (Exception e) {

        }

    }
}
