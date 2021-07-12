package com.souche.canal.ng.common.account;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class AccountHelper {

    public static void main(String[] args) {
        MysqlAccount canalAccount =
                getCanalAccount(
                        "https://stream-test.dasouche-inc.net/mysql/credentials",
                        "https://stream-test.dasouche-inc.net/mysql/token",
                        "483f0f3c-8d1a-c324-788e-2dd61c47becd",
                        "2f2cdf43-31e2-9826-06bb-fb29252345fc",
                        "test.database3804.scsite.net",
                        3804
                );
        System.out.println(canalAccount);
    }

    public static MysqlAccount getCanalAccount(String accountRequestUrl, String tokenRequestUrl, String tokenRoleId, String tokenSecretId, String host, Integer port) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(MysqlAccountConstant.HOST_KEY, host);
        requestParams.put(MysqlAccountConstant.DB_KEY, "*");
        requestParams.put(MysqlAccountConstant.ROLE_KEY, MysqlAccountConstant.CANAL_ACCOUNT_ROLE);
        if (port != null) {
            requestParams.put(MysqlAccountConstant.PORT_KEY, port);
        }
        return getAccount(accountRequestUrl, tokenRequestUrl, tokenRoleId, tokenSecretId, requestParams);
    }


    private static MysqlAccount getAccount(String accountRequestUrl, String tokenRequestUrl, String tokenRoleId, String tokenSecretId, Map<String, Object> requestParams) {
        String authToken = getAuthToken(tokenRequestUrl, tokenRoleId, tokenSecretId);
        MysqlAccount account = new MysqlAccount();
        if (StringUtils.isBlank(authToken)) return account;
        JSONObject response = HttpRequestUtil.postRequest(accountRequestUrl, requestParams, authToken);
        if (null != response && response.containsKey(MysqlAccountConstant.DATA_KEY)) {
            JSONObject data = response.getJSONObject(MysqlAccountConstant.DATA_KEY);
            if (null != data && data.containsKey(MysqlAccountConstant.PASSWORD_KEY) && data.containsKey(MysqlAccountConstant.USERNAME_KEY)) {
                String password = data.getString(MysqlAccountConstant.PASSWORD_KEY);
                String username = data.getString(MysqlAccountConstant.USERNAME_KEY);
                account.setSuccess(true);
                account.setUsername(username);
                account.setPassword(password);
            }
        }
        return account;
    }

    private static MysqlAccount getAccount(String accountRequestUrl, String host, String role, String tokenRequestUrl, String tokenRoleId, String tokenSecretId) {
        String authToken = getAuthToken(tokenRequestUrl, tokenRoleId, tokenSecretId);
        MysqlAccount account = new MysqlAccount();
        if (StringUtils.isBlank(authToken)) return account;

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(MysqlAccountConstant.HOST_KEY, host);
        requestParams.put(MysqlAccountConstant.DB_KEY, "*");
        requestParams.put(MysqlAccountConstant.ROLE_KEY, role);

        JSONObject response = HttpRequestUtil.postRequest(accountRequestUrl, requestParams, authToken);
        if (null != response && response.containsKey(MysqlAccountConstant.DATA_KEY)) {
            JSONObject data = response.getJSONObject(MysqlAccountConstant.DATA_KEY);
            if (null != data && data.containsKey(MysqlAccountConstant.PASSWORD_KEY) && data.containsKey(MysqlAccountConstant.USERNAME_KEY)) {
                String password = data.getString(MysqlAccountConstant.PASSWORD_KEY);
                String username = data.getString(MysqlAccountConstant.USERNAME_KEY);
                account.setSuccess(true);
                account.setUsername(username);
                account.setPassword(password);
            }
        }
        return account;
    }

    private static String getAuthToken(String url, String roleId, String secretId) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(MysqlAccountConstant.ROLE_ID_KEY, roleId);
        requestParams.put(MysqlAccountConstant.SECRECT_ID_KEY, secretId);
        JSONObject response = HttpRequestUtil.postRequest(url, requestParams, null);
        String token = null;
        if (response != null && response.containsKey(MysqlAccountConstant.AUTH_KEY)) {
            token = response.getJSONObject(MysqlAccountConstant.AUTH_KEY).getString(MysqlAccountConstant.CLIENT_TOKEN_KEY);
        }
        return token;
    }
}
