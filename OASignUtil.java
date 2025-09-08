package com.800best.common.util;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.800best.common.demo.AccountValidateRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author : Zhang.lock@800best.com
 */
public class OaLifeSignUtil {

    public static final String EQUAL_SIGN = "=";
    public static final String APP_ID = "appId";
    public static final String SERVICE_TYPE = "serviceType";
    public static final String BIZ_PARAM = "bizParam";
    public static final String TIMESTAMP = "timestamp";

    public static String calcSign(final String appId, final String bizParam, final String serviceType, final Long timestamp,
                                  final String secret) throws UnsupportedEncodingException {
        byte[] bytes = buildSignStr(appId, bizParam, serviceType, timestamp, secret)
                .getBytes(StandardCharsets.UTF_8);
        return new String(DigestUtils.md5DigestAsHex(bytes).getBytes(StandardCharsets.UTF_8));
    }

    private static String buildSignStr(final String appId, final String bizParam, final String serviceType, final Long timestamp,
                                       final String secret) {
        return APP_ID + EQUAL_SIGN + appId +
                BIZ_PARAM + EQUAL_SIGN + bizParam +
                SERVICE_TYPE + EQUAL_SIGN + serviceType +
                TIMESTAMP + EQUAL_SIGN + timestamp +
                secret;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        post();
        post2();
        long l = 1710841223672L;
        System.out.println(l);
        String querySalesOrderList = calcSign("QUERY_ASN_LIST",
                "{\"page\":\"1\",\"pageSize\":\"200\"}"
                , "CREATE_SALES_ORDER", l, "bEk9bqlnPu9IJdPEUzpykqc0MxksIGnz\t");
        System.out.println(querySalesOrderList);

        //=====================OA=============

        AccountValidateRequest request = new AccountValidateRequest();
        request.setPassword("Ace080933");
        String bizData = JSON.toJSONString(request);
        Map<String, String> params = new HashMap<>();
        params.put("partnerID", "OA_TEST");
        params.put("partnerKey", "OAkey");
        params.put("serviceType", "CUSTOMER");

        params.put("bizData", bizData);

        String sign = doSign(bizData, "UTF-8", "OAkey");
        params.put("sign", sign);

        System.out.println(JSON.toJSON(params));
    }

    public static String doSign(String bizData, String charset, String keys) {
        String sign = "";
        bizData = bizData + keys;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bizData.getBytes(charset));
            byte[] b = md.digest();
            StringBuilder output = new StringBuilder(32);
            for (int i = 0; i < b.length; i++) {
                String temp = Integer.toHexString(b[i] & 0xff);
                if (temp.length() < 2) {
                    output.append("0");
                }
                output.append(temp);
            }
            sign = output.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sign;
    }


    public static String post() {
        // URL of the API endpoint
        String url = "http://oa.800best.icu/best/oa";

        // Create a map to hold the form data
        Map<String, Object> formData = new HashMap<>();
        formData.put("serviceType", "CUSTOMER");
        formData.put("partnerKey", "OA12345");
        formData.put("sign", "ccdf76b5fe158066818731fccdf24919");
        formData.put("bizData", "{\"password\":\"Ace080933\"}");
        formData.put("partnerID", "OA_TEST");

        HttpResponse response = HttpRequest.post(url)
                .form(formData)
                .timeout(4000)
                .setConnectionTimeout(4000)
                .setReadTimeout(4000)
                .execute();

        System.out.println(response.body());
        response.close();
        return response.body();
    }

}
