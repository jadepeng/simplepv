package com.jadepeng.simplepv.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.JSONPResponseBodyAdvice;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FastJsonConfigurer implements WebMvcConfigurer {

    /**
     * 设置 FastJson 作为默认的 java 对象与 json 互相转换的工具
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 创建配置类
        FastJsonConfig config = new FastJsonConfig();
        // 自定义格式化输出
        config.setSerializerFeatures(
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullBooleanAsFalse
        );
        // 处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(fastMediaTypes);
        converter.setFastJsonConfig(config);
        converters.add(0, converter);
    }

    /**
     * 对 JSONP 支持
     */
    @Bean
    public JSONPResponseBodyAdvice jsonpResponseBodyAdvice() {
        return new JSONPResponseBodyAdvice();
    }
}
