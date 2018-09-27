package com.bootdo.common.utils;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil {
	public String doPost(String url, Map<String, String> params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String result = "";
		try {
			JSONObject jobj = new JSONObject();
			for (Entry<String, String> entry : params.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				jobj.put(entry.getKey(), entry.getValue());
			}
			System.out.println("httpclientutil:before-execute" + jobj.toString());
			// nameValuePairs.add(new BasicNameValuePair("msg", (jobj.toString())));
			/*
			 * httppost.addHeader("Content-type", "application/json; charset=utf-8");
			 * httppost.setHeader("Accept", "application/json"); httppost.setEntity(new
			 * StringEntity(jobj.toString(), Charset.forName("UTF-8")));
			 */
			StringEntity stringEntity = new StringEntity(jobj.toString());
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");// 发送json数据需要设置contentType
			httpPost.setEntity(stringEntity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				String conResult = EntityUtils.toString(response.getEntity());
				System.out.println("httpclientutil:after-execute" + conResult);
				JSONObject sobj = JSONObject.parseObject(conResult);
				result = sobj.getString("result");
				String code = sobj.getString("code");
				if (code == null) {

				} else if (code.equals("500")) {

				} else {

				}
			} else {
				String err = response.getStatusLine().getStatusCode() + "";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
