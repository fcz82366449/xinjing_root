package cn.easy.xinjing.utils;

import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by fanchengzhi on 2017/5/23.
 */
public class JudgeUtity {

    /**
     * 判断是否为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }


    /**
     * 从URL中读取图片,转换成流形式.
     * @param destUrl
     * @return
     */
    public  static  String saveToFile(String destUrl){
        URL url = null;
        InputStream in = null;
        try{
            url = new URL(destUrl);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            in = httpUrl.getInputStream();

           String base64 = GetImageStrByInPut(in);
            return base64;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取输入流,转换为Base64字符串
     * @param input
     * @return
     */
    public static String GetImageStrByInPut(InputStream input) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            data = new byte[input.available()];
            input.read(data);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }
}
