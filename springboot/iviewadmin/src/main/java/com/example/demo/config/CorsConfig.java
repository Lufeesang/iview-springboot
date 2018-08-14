package com.example.demo.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

public class CorsConfig {
	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址
		corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
		corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
		return corsConfiguration;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig()); // 4 对接口配置跨域设置
		return new CorsFilter();
	}
}
