package com.lyshopping.util;

import com.lyshopping.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author liuying
 * json工具类
 **/
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static{
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        //取消默认的转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //忽略空Bean装json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        //所有的日期格式都统一为以下的格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略在json字符串中存在，但是在java对象中不存在对应属性的情况，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    //将对象转化为字符串
    public static <T> String obj2String(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
          log.warn("Parse object to String error",e);
          return null;
        }
    }

    //封装可以返回格式化好的json字符串
    public static <T> String obj2StringPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }
    //将字符串转化成对象
    public static <T> T String2Obj(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)str :objectMapper.readValue(str,clazz);
        } catch (IOException e) {
           log.warn("Parse String to Object error",e);
           return null;
        }
    }

    //复杂对象（list,map,set）反序列化
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)? str : objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    //复杂对象且可变长参数反序列化，需要写上返回值类型
    public static <T> T string2Obj(String str,Class<?> collectionClass,Class<?>...elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

//    public static void main(String[] args) {
//        User ul = new User();
//        ul.setId(99);
//        ul.setUsername("haha");
//        ul.setEmail("liu@163.com");
//        String userlJson = JsonUtil.obj2String(ul);
//        String userlJsonPretty = JsonUtil.obj2StringPretty(ul);
//        log.info("userlJson:{}",userlJson);
//        log.info("userlJsonPretty:{}",userlJsonPretty);
//    }
}
