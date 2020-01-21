package com.ctw.audit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;

public class JsonUtil {

    private final static Logger logger = Logger.getLogger(JsonUtil.class);

    public static String stringify(Object object){
        if(object == null){
            return "null";
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e);
            return ObjectUtils.toString(object);
        }
    }

}
