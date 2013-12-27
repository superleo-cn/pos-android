package com.android.common;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 请求的工具类，只需要简单的绑定对象即可
 * 
 * @author hubo
 */
public class RestHelperSpring {

	public static <T> T postJSON(String url, Class<T> t, Map<String, String> params) {
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// Add the Jackson message converter
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		// 解决大量数据提交的错误

		// set all the Parameter
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				builder.queryParam(entry.getKey(), entry.getValue());
			}
		}

		// Make the HTTP GET request, marshaling the response from JSON to an
		// array of Events
		return (T) restTemplate.postForObject(builder.build().toUri(), null, t);
		// T response = restTemplate.exchange((builder.build().toUri(),
		// HttpMethod.POST, getHttpHeaders(), responseType).getBody();

	}

	public static <T> T getJSON(String url, Class<T> t) {
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// 解决大量数据提交的错误
		System.setProperty("http.keepAlive", "false");

		// Add the Jackson message converter
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		// Make the HTTP GET request, marshaling the response from JSON to an
		// array of Events
		return (T) restTemplate.getForObject(url, t);

	}

	public HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Connection", "Close");
		return headers;
	}
}