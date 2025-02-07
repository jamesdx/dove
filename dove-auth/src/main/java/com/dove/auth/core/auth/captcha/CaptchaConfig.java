package com.dove.auth.core.auth.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置
 */
@Configuration
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha captchaProducer() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        
        // 是否有边框
        properties.setProperty("kaptcha.border", "no");
        // 验证码文本字符颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        // 验证码图片宽度
        properties.setProperty("kaptcha.image.width", "160");
        // 验证码图片高度
        properties.setProperty("kaptcha.image.height", "60");
        // 验证码文本字符大小
        properties.setProperty("kaptcha.textproducer.font.size", "38");
        // 验证码文本字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 验证码文本字体样式
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
        // 验证码噪点颜色
        properties.setProperty("kaptcha.noise.color", "black");
        // 干扰实现类
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        // 图片样式
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        // 文字渲染器
        properties.setProperty("kaptcha.wordrenderer.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
        
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        
        return defaultKaptcha;
    }
} 