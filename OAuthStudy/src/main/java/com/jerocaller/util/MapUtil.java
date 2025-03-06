package com.jerocaller.util;

import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapUtil {
    
    /**
     * Map 자료 구조 내 저장된 모든 key, value 값을 logging한다. 
     * 
     * @param map
     * @return
     */
    public static String logMap(Map<String, Object> map) {
        
        StringBuilder stringBuilder = new StringBuilder();
        
        log.info("===== Map Logging =====");
        for (Entry<String, Object> entry : map.entrySet()) {
            String oneData = String.format(
                "%s : %s", 
                entry.getKey(), 
                entry.getValue().toString() 
            );
            log.info(oneData);
            stringBuilder.append(oneData + "\n");
        }
        log.info("===== Map Logging End =====");
        
        return stringBuilder.toString();
    }
    
}
