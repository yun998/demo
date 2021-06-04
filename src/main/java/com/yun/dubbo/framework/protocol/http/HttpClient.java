package com.yun.dubbo.framework.protocol.http;

import com.alibaba.fastjson.JSON;
import com.yun.dubbo.framework.pojo.Invocation;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpClient {

    public String send(String hostName, Integer port, Invocation invocation){

        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        URI uri = null;
        try {
            uri = new URIBuilder().setScheme("http")
                    .setHost(hostName)
                    .setPort(port)
                    .setPath("/")
                    // .setParameters()
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // HttpPost("http://localhost:8080/");
        HttpPost httpPost = new HttpPost(uri);
        // 将对象转换为json字符串，并放入entity中
        StringEntity entity = new StringEntity(JSON.toJSONString(invocation), "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        InputStream inputStream = null;
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            inputStream = response.getEntity().getContent();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return inputStreamToString(inputStream);
    }

    private String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }
}
