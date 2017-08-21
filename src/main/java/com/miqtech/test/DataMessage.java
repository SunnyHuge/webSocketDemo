package com.miqtech.test;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author zhangyuqi
 * @create 2017年07月21日
 */
public class DataMessage {
	@JsonProperty("data")
	final Map<String, Map<String, Object>> data;

	public DataMessage() {
		this.data = null;
	}

	public DataMessage(Map<String, Map<String, Object>> data) {
		this.data = data;
	}

	public Map<String, Map<String, Object>> getData() {
		return data;
	}
}
