package com.souche.canal.ng.common.account;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class HttpRequestUtil {

    private static InputStream inputStream;
    private static BufferedReader br;


    public static JSONObject postRequest(String requestUrl, Map<String, Object> parameters, String token) {
        JSONObject jsonObject = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(requestUrl);
        post.setHeader("Content-Type", "application/json");
        if (StringUtils.isNotBlank(token)) {
            post.setHeader("X-Vault-Token", token);
        }
        if (parameters != null && !parameters.isEmpty()) {
            post.setEntity(new StringEntity(JSON.toJSONString(parameters), "UTF-8"));
        }
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            jsonObject = handleResult(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(httpClient);
        }
        return jsonObject;
    }

    /**
     * 处理http响应结果
     *
     */
    private static JSONObject handleResult(CloseableHttpResponse httpResponse) throws IOException, JSONException {
        JSONObject response = null;
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == MysqlAccountConstant.REQUEST_OK) {
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                inputStream = entity.getContent();
                if (null == inputStream) return response;
                br = new BufferedReader(new InputStreamReader(inputStream));
                String line = br.readLine();
                if (StringUtils.isBlank(line)) return response;
                response = JSON.parseObject(line);
            }
        }
        return response;
    }


    /**
     * 关闭资源
     *
     */
    private static void close(CloseableHttpClient httpClient) {
        try {
            httpClient.close();
            if (br != null) {
                br.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
