package com.miqtech.netty.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON辅助类
 */
public class JsonUtils {

	// JSON ObjectMapper
	public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	static {
		// 允许多余的JSON数据被忽略
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 不输出MAP里值为Null的字段
		OBJECT_MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		// 不输出普通字段里值为Null的字段
		OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	/**
	 * 私有构造器 防止实例化
	 */
	private JsonUtils() {
	}

	public static String objectToString(Object obj) {
		try {
			return OBJECT_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {

			return null;
		}
	}
}
