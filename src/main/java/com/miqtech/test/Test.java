package com.miqtech.test;

import com.fasterxml.jackson.databind.JavaType;
import com.miqtech.netty.util.JsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author zhangyuqi
 * @create 2017年07月21日
 */
public class Test {
	public static void main(String[] args) {
		BufferedReader reader = null;
		String insert = "insert into lol_hero_tag(hero_id, tag_en, tag_cn, is_valid, create_date, update_date) values({0}, ${1}$, ${2}$, 1, ${3}$, null);";
		Map<String, String> tag_cn = new HashMap<String, String>();
		tag_cn.put("Fighter", "战士");
		tag_cn.put("Mage", "法师");
		tag_cn.put("Assassin", "刺客");
		tag_cn.put("Tank", "坦克");
		tag_cn.put("Marksman", "射手");
		tag_cn.put("Support", "辅助");
		try {
			reader = new BufferedReader(new InputStreamReader(
					Thread.currentThread().getContextClassLoader().getResourceAsStream(("lol.txt"))));
			StringBuilder newFile = new StringBuilder();

			StringBuilder text = new StringBuilder();
			char[] buff = new char[1024];

			String line;
			while ((line = reader.readLine()) != null) {
				text.append(line.trim());
			}
			reader.close();

			JavaType javaType = JsonUtils.OBJECT_MAPPER.getTypeFactory().constructType(DataMessage.class);
			DataMessage dataMessage = JsonUtils.OBJECT_MAPPER.readValue(text.toString(), javaType);

			Map<String, Map<String, Object>> data = dataMessage.getData();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = format.format(new Date());
			for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {
				Map<String, Object> hero = entry.getValue();
				List<String> tags = (List<String>) hero.get("tags");
				for (String tag : tags) {
					newFile.append(MessageFormat.format(insert, hero.get("key"), tag, tag_cn.get(tag), date));
					newFile.append("\n");
				}
			}
			System.out.println(newFile.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
