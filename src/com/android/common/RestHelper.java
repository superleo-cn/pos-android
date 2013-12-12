package com.android.common;

import java.util.Map;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 请求的工具类，只需要简单的绑定对象即可
 * 
 * @author hubo
 */
public class RestHelper {

	public static <T> T postJSON(String url, Class<T> t, Map<String, String> params) {
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// Add the Jackson message converter
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		// set all the Parameter
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		for (Map.Entry<String, String> entry : params.entrySet()) {
			builder.queryParam(entry.getKey(), entry.getValue());
		}

		// Make the HTTP GET request, marshaling the response from JSON to an
		// array of Events
		return (T) restTemplate.postForObject(builder.build().toUri(), null, t);

	}
}