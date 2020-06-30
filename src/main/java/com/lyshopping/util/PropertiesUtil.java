package com.lyshopping.util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
/**
 * @author liuying
 * 读取配置文件的工具类
 **/
public class PropertiesUtil {
        private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

        private static Properties props;

        static {
            String fileName = "dev/lyshopping.properties";
            props = new Properties();
            try {
                props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
            } catch (IOException e) {
                logger.error("配置文件读取异常",e);
            }
        }

        public static String getProperty(String key){
            String value = props.getProperty(key.trim());
            if(StringUtils.isBlank(value)){
                return null;
            }
            return value.trim();
        }

        /**
         * 获取配置文件中配置的值
         * key表示配置文件中配置好的属性的名称
         * defaultVa：表示给属性添加默认的值，防止报空指针
         * */
        public static String getProperty(String key,String defaultValue){

            String value = props.getProperty(key.trim());
            if(StringUtils.isBlank(value)){
                value = defaultValue;
            }
            return value.trim();
        }



    }

