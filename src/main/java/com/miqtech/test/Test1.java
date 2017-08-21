package com.miqtech.test;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ${DESCRIPTION}
 *
 * @author zhangyuqi
 * @create 2017年07月25日
 */
public class Test1 {
	public static void main(String[] args) throws InterruptedException {
		//Map<String, String> ss = new HashMap<String, String>();
		//ss.put("4", String.valueOf(System.currentTimeMillis()));
		//Thread.sleep(10);
		//ss.put("3", String.valueOf(System.currentTimeMillis()));
		//Thread.sleep(10);
		//ss.put("2", String.valueOf(System.currentTimeMillis()));
		//Thread.sleep(10);
		//ss.put("1", String.valueOf(System.currentTimeMillis()));
		//
		//TreeMap<String, String> a = new TreeMap();
		//for (Map.Entry<String, String> entry : ss.entrySet()) {
		//	a.put(entry.getValue(), entry.getKey());
		//}
		//
		//System.out.println(a.toString());
		//List<String> aaaa = new ArrayList<String>();
		//aaaa.addAll(a.values());
		//System.out.println(aaaa);
		//
		//
		//String netTypeAndStatus = "1,2";
		//netTypeAndStatus = netTypeAndStatus.substring(0, netTypeAndStatus.indexOf(","));
		//System.out.println(netTypeAndStatus);

		//String aa = String.valueOf(5.0 * 100);
		//System.out.println(aa);
		//System.out.println(NumberUtils.toInt(aa));

		//byte[] bytes = Base64Utils.decodeFromString("d3lkc18yMzA0ODAxNzI=");
		//System.out.println(new String(bytes));

		List<Map<String, Object>> userAccountList = new ArrayList<Map<String, Object>>();
		Map<String, String> userAccount = new TreeMap<String, String>();
		userAccount.put("1502273310377", "00163efffe05fed9-00006ec3-00000031-7b35d38456762d93-bcef131c");
		userAccount.put("1502273322940", "00163efffe05fed9-00006ec3-00000032-2535af4076767298-b8ba4cc0");

		for (String channelId : userAccount.values()) {
			System.out.println(channelId);
		}

		Map<String, Object> userMap1 = (Map<String, Object>) JSONObject.parse(
				"{\"tcls\": 261,\"accountId\": 33,\"password\": \"d3lkc19xODczODA0NzYw\",\"heroId\": 11,\"serverId\": 30,\"userId\": 302,\"account\": \"d3lkc18xNjY2NTMyMjc=\"}");
		userAccountList.add(userMap1);

		if (!CollectionUtils.isEmpty(userAccountList))

		{
			for (Map<String, Object> userMap : userAccountList) {
				if (userMap.get("accountId") == null) {
					continue;
				}
				System.out.println(userMap.get("accountId"));
			}
		}

	}
}
