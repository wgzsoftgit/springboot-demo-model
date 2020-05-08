package com.demomodel.utils.de.http.fengzhuang;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.demomodel.utils.de.http.fengzhuang.util.BaseUserInfo;
import com.demomodel.utils.de.http.fengzhuang.util.HttpEnum;
import com.demomodel.utils.de.http.fengzhuang.util.HttpTestUtils;
import com.google.gson.Gson;

//public 
class DevInfo extends BaseUserInfo
{
    public static final String ACTION = "/videoService/device";

    private static String getDevTree(String ip, int port, String token) throws Exception{
        /*此处的orgCode为调用分级获取组织接口得到的orgCode
            id : 类型string ，必填。要查询组织的惟一编码。
            nodeType : 类型int ，必填。固定为1,表示组织
            typeCode : 类型string ，必填。检索类型，值不同返回的内容不同。 查询设备"01;1;ALL"。查询设备和通
            道"01;1;ALL;ALL"。仅查询通道"01;0;ALL;ALL"。
        */
        String orgCode="S4NbecfYB1BK4AJPGT6CVC";
        String content = "?id="+orgCode+
                        "&nodeType=1" +
                        "&typeCode=01;0;ALL;ALL" +
                        "&page=1" +
                        "&pageSize=100";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        Map<String, Object> rsp = new Gson().fromJson(response, Map.class);        //Gson的使用  转 
        List<Map<String, Object>> arr = (List<Map<String, Object>>)rsp.get("results");
        if(arr!=null&&arr.size()>0) {
            for (Map<String, Object> node : arr) {
                //枚举所有类型是通道的编码值
                DecimalFormat df = new DecimalFormat("######0");
                if (3 == Integer.valueOf(df.format(node.get("nodeType")))) {
                    System.out.println(node.get("channelName") + "=:" + node.get("channelId"));
                }
            }
        }
        return response;
    }

    //此方法为获取组织和设备树专用方法
    public static String getOrgDevTree(String orgCode,String mark) throws Exception{
        /*此处的orgCode为调用分级获取组织接口得到的orgCode
        nodeType的取值：查询设备"01;1;ALL"。查询设备和通道"01;1;ALL;ALL"。仅查询通道"01;0;ALL;ALL"。
        */
        String content = "?id="+orgCode+"&nodeType=1&typeCode=01;0;ALL;ALL&page=1&pageSize=100";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,Integer.valueOf(port),ACTION,token,content);
        Map<String, Object> rsp = new Gson().fromJson(response, Map.class);
        List<Map<String, Object>> arr = (List<Map<String, Object>>)rsp.get("results");
        int start=0;
        int total=0;
        if(arr!=null&&arr.size()>0) {
            for (Map<String, Object> node : arr) {
                //枚举所有类型是通道的编码值
                DecimalFormat df = new DecimalFormat("######0");
                if (3 == Integer.valueOf(df.format(node.get("nodeType")))) {
                    start++;
                }
            }
            if(start>0){
                System.out.print("            "+mark);
            }
            for (Map<String, Object> node : arr) {
                //枚举所有类型是通道的编码值
                DecimalFormat df = new DecimalFormat("######0");
                if (3 == Integer.valueOf(df.format(node.get("nodeType")))) {
                    total++;
                    System.out.print("（通道名称）"+node.get("channelName") + "=（通道编码）" + node.get("channelId")+"; ");
                }
            }
            if(total>0) {
                System.out.println();
            }
        }
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = getDevTree(ip, Integer.valueOf(port), token);
    }
}