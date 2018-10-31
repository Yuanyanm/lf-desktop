package top.lf.util;


import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.xpath.XPathConstants;
import java.util.HashMap;
import java.util.Map;

public class Client {
    public static String CHL_TOKEN = "3A98CFDAFC8FA17543B4400A560D735F";
    public static String CHL_SECRETKEY = "35E0E08E8D1AB889";

    public static void main(String[] args) throws Exception {
        func01();//缴费单
        //func02();
        //func04();
        //System.out.println(DateUtil.currentDTime());
        //func06(); //扫码支付
    }


    public static void func01() {
        String url = "http://117.176.149.97:8991/ExternalServices/ZL_InformationService.asmx/OutPatient";
        try {
            //String reData = "<ROOT><TOKEN>sOuNjP0PAJkfLtASnytn1ubLwS6a16dVh1dkU3QcT3OUooMli05kmX/uS1oRvr2x</TOKEN><SERVICE>Payment.PayReceipt.Query</SERVICE></ROOT>";
            Document xmlDoc = DocumentHelper.createDocument();
            Element rootEle = xmlDoc.addElement("ROOT");
            Element eleToken = rootEle.addElement("TOKEN");
            eleToken.setText("sOuNjP0PAJkfLtASnytn1ubLwS6a16dVh1dkU3QcT3OUooMli05kmX/uS1oRvr2x");
            Element serviceEle = rootEle.addElement("SERVICE");
            serviceEle.setText("Payment.PayReceipt.Query");
            Element dataEle = DocumentHelper.createElement("DATAPARAM");
            /**
             *根据实际业务构造DataParam节点内部数据
             * */
            //398995(李波)  130460(陈思源)
            String dataParam = "<BRID>130460</BRID><CXTS>60</CXTS><JSKLB>扫码支付</JSKLB>";
            rootEle.addElement("DATAPARAM").setText(ZlCryptoTool.EncryptData(dataParam, CHL_SECRETKEY));
            String reData = xmlDoc.asXML();
            System.out.println(" reData---------- \n" + reData);
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("ReData", reData);
            String result = HttpUtil.post(url, paramMap);
            System.out.println(" postResult---------- \n" + result);
            Document outDoc = DocumentHelper.parseText(result);

            //outDoc.selectSingleNode("//ROOT");
            //System.out.println("STATE-> "+zlState.toString());

            org.w3c.dom.Document outXml = XmlUtil.parseXml(result);
            Object zlState = XmlUtil.getByXPath("//ROOT/STATE", outXml, XPathConstants.STRING);
            System.out.println("STATE-> " + zlState.toString());
            Object outData = XmlUtil.getByXPath("//ROOT/DATAPARAM", outXml, XPathConstants.STRING);
            System.out.println("OUT.DATAPARAM(解密前)-> " + outData.toString());
            System.out.println("OUT.DATAPARAM(解密后)-> " + ZlCryptoTool.DecryptData(outData.toString(), CHL_SECRETKEY));
        } catch (Exception e) {

        }

    }

    public static void func02() {
        String url = "http://117.176.149.97:8991/ExternalServices/ZL_InformationService.asmx/UserManager";
        try {
            Document xmlDoc = DocumentHelper.createDocument();
            Element rootEle = xmlDoc.addElement("ROOT");
            Element eleToken = rootEle.addElement("TOKEN");
            eleToken.setText("sOuNjP0PAJkfLtASnytn1ubLwS6a16dVh1dkU3QcT3OUooMli05kmX/uS1oRvr2x");
            Element serviceEle = rootEle.addElement("SERVICE");
            serviceEle.setText("BindCard.UserInfoByCardNO.Query");
            /**
             *根据实际业务构造DataParam节点内部数据
             * */
            String dataParam = "<ZJH>513922198802181090</ZJH><ZJLX>二代身份证</ZJLX>";
            rootEle.addElement("DATAPARAM").setText(ZlCryptoTool.EncryptData(dataParam, CHL_SECRETKEY));
            String reData = xmlDoc.asXML();
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("ReData原始数据：\n", reData);
            String result = HttpUtil.post(url, paramMap);
            System.out.println("接口返回数据：\n" + result);
            Document outDoc = DocumentHelper.parseText(result);
            org.w3c.dom.Document outXml = XmlUtil.parseXml(result);
            Object zlState = XmlUtil.getByXPath("//ROOT/STATE", outXml, XPathConstants.STRING);
            System.out.println("业务状态标识-STATE： " + zlState.toString());
            Object outData = XmlUtil.getByXPath("//ROOT/DATAPARAM", outXml, XPathConstants.STRING);
            System.out.println("OUT.DATAPARAM(解密前)-> " + outData.toString());
            System.out.println("OUT.DATAPARAM(解密后)-> " + ZlCryptoTool.DecryptData(outData.toString(), CHL_SECRETKEY));
        } catch (Exception e) {

        }

    }

    public static void func04() {
        String url = "http://117.176.149.97:8080/ExternalServices/ZL_InformationService.asmx/MedicalTechnology";
        try {
            Document xmlDoc = DocumentHelper.createDocument();
            Element rootEle = xmlDoc.addElement("ROOT");
            Element eleToken = rootEle.addElement("TOKEN");
            eleToken.setText("sOuNjP0PAJkfLtASnytn1ubLwS6a16dVh1dkU3QcT3OUooMli05kmX/uS1oRvr2x");
            Element serviceEle = rootEle.addElement("SERVICE");
            serviceEle.setText("Report.Record.Query");
            /**
             *根据实际业务构造DataParam节点内部数据
             * */
            //TYPE 1-门诊，2-住院，0-所有 ,RNOM TYPE为1传挂号单号，2传住院次数
            String dataParam = "<BRID>385301</BRID><TYPE>0</TYPE><RNOM></RNOM><DQYS>1</DQYS><JLTS>300</JLTS>";
            rootEle.addElement("DATAPARAM").setText(ZlCryptoTool.EncryptData(dataParam, CHL_SECRETKEY));
            String reData = xmlDoc.asXML();
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("ReData原始数据：\n", reData);
            paramMap.put("ReData", reData);
            String result = HttpUtil.post(url, paramMap);
            System.out.println("接口返回数据：\n" + result);
            Document outDoc = DocumentHelper.parseText(result);
            org.w3c.dom.Document outXml = XmlUtil.parseXml(result);
            Object zlState = XmlUtil.getByXPath("//ROOT/STATE", outXml, XPathConstants.STRING);
            System.out.println("业务状态标识-STATE： " + zlState.toString());
            Object outData = XmlUtil.getByXPath("//ROOT/DATAPARAM", outXml, XPathConstants.STRING);
            System.out.println("OUT.DATAPARAM(解密前)-> " + outData.toString());
            System.out.println("OUT.DATAPARAM(解密后)-> " + ZlCryptoTool.DecryptData(outData.toString(), CHL_SECRETKEY));
        } catch (Exception e) {

        }
    }

    public static void func03() {
        //WXPay wxPay = new WXPay();
        Map<String, String> inData = new HashMap<>();
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");
        inData.put("", "");

    }

    public static void func05() {
        //微信正式环境(4.3.2.1.	获取缴费单据信息)
        CHL_TOKEN = "4357283A44F6BB21AFA9658CDA0A98C3";
        CHL_SECRETKEY = "16E12DC2AE58B437";
        String url = "http://117.176.149.97:6888/ExternalServices/ZL_InformationService.asmx/OutPatient";
        try {
            //String reData = "<ROOT><TOKEN>sOuNjP0PAJkfLtASnytn1ubLwS6a16dVh1dkU3QcT3OUooMli05kmX/uS1oRvr2x</TOKEN><SERVICE>Payment.PayReceipt.Query</SERVICE></ROOT>";
            Document xmlDoc = DocumentHelper.createDocument();
            Element rootEle = xmlDoc.addElement("ROOT");
            Element eleToken = rootEle.addElement("TOKEN");
            eleToken.setText(ZlCryptoTool.EncryptData(CHL_TOKEN,CHL_SECRETKEY));
            Element serviceEle = rootEle.addElement("SERVICE");
            serviceEle.setText("Payment.PayReceipt.Query");
            Element dataEle = DocumentHelper.createElement("DATAPARAM");
            /**
             *根据实际业务构造DataParam节点内部数据
             * */
            //398995(李波)  130460(陈思源)
            String dataParam = "<BRID>337475</BRID><CXTS>500</CXTS><JSKLB>微信</JSKLB>";
            rootEle.addElement("DATAPARAM").setText(ZlCryptoTool.EncryptData(dataParam, CHL_SECRETKEY));
            String reData = xmlDoc.asXML();
            System.out.println(" reData---------- \n" + reData);
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("ReData", reData);
            String result = HttpUtil.post(url, paramMap);
            System.out.println(" postResult---------- \n" + result);
            Document outDoc = DocumentHelper.parseText(result);

            //outDoc.selectSingleNode("//ROOT");
            //System.out.println("STATE-> "+zlState.toString());

            org.w3c.dom.Document outXml = XmlUtil.parseXml(result);
            Object zlState = XmlUtil.getByXPath("//ROOT/STATE", outXml, XPathConstants.STRING);
            System.out.println("STATE-> " + zlState.toString());
            Object outData = XmlUtil.getByXPath("//ROOT/DATAPARAM", outXml, XPathConstants.STRING);
            System.out.println("OUT.DATAPARAM(解密前)-> " + outData.toString());
            System.out.println("OUT.DATAPARAM(解密后)-> " + ZlCryptoTool.DecryptData(outData.toString(), CHL_SECRETKEY));
        } catch (Exception e) {

        }
    }

    public static void func06() {
        String url = "http://117.176.149.97:8080/ExternalServices/ZL_InformationService.asmx/OutPatient";
        try {
            //String reData = "<ROOT><TOKEN>sOuNjP0PAJkfLtASnytn1ubLwS6a16dVh1dkU3QcT3OUooMli05kmX/uS1oRvr2x</TOKEN><SERVICE>Payment.PayReceipt.Query</SERVICE></ROOT>";
            Document xmlDoc = DocumentHelper.createDocument();
            Element rootEle = xmlDoc.addElement("ROOT");
            Element eleToken = rootEle.addElement("TOKEN");
            eleToken.setText("sOuNjP0PAJkfLtASnytn1ubLwS6a16dVh1dkU3QcT3OUooMli05kmX/uS1oRvr2x");
            Element serviceEle = rootEle.addElement("SERVICE");
            serviceEle.setText("Register.SignalSource.Query");
            Element dataEle = DocumentHelper.createElement("DATAPARAM");
            /**
             *根据实际业务构造DataParam节点内部数据
             * */
            //398995(李波)  130460(陈思源) 186-375   217-286
            String dataParam = "<RQ>2018-10-24</RQ><KSID>186</KSID><HZDW>微信</HZDW><YSID>375</YSID><YSXM></YSXM>";
            rootEle.addElement("DATAPARAM").setText(ZlCryptoTool.EncryptData(dataParam, CHL_SECRETKEY));
            String reData = xmlDoc.asXML();
            System.out.println(" reData---------- \n" + reData);
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("ReData", reData);
            String result = HttpUtil.post(url, paramMap);
            System.out.println(" postResult---------- \n" + result);
            Document outDoc = DocumentHelper.parseText(result);

            //outDoc.selectSingleNode("//ROOT");
            //System.out.println("STATE-> "+zlState.toString());

            org.w3c.dom.Document outXml = XmlUtil.parseXml(result);
            Object zlState = XmlUtil.getByXPath("//ROOT/STATE", outXml, XPathConstants.STRING);
            System.out.println("STATE-> " + zlState.toString());
            Object outData = XmlUtil.getByXPath("//ROOT/DATAPARAM", outXml, XPathConstants.STRING);
            System.out.println("OUT.DATAPARAM(解密前)-> " + outData.toString());
            System.out.println("OUT.DATAPARAM(解密后)-> " + ZlCryptoTool.DecryptData(outData.toString(), CHL_SECRETKEY));
        } catch (Exception e) {

        }

    }

}
