package com.yujun.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Configuration
@PropertySource("classpath:config.properties")
@EnableScheduling
@ComponentScan("com.yujun")
@EnableAutoConfiguration 
@Controller
public class Start {
	@RequestMapping("/")
	 @ResponseBody
    String home() {
        return "Hello World!";
    }
	
	@RequestMapping("/hello")  
	public String hello(Map<String, Object> model){  
	    List<String> l= new ArrayList(); 
	    l.add("��ඣ�hadoop");  
	    l.add("��ඣ�hbase");  
	    l.add("��ඣ�hive");  
	    l.add("��ඣ�pig");  
	    l.add("��ඣ�zookeeper");  
	    l.add("��ඣ�����ɢ��");  
	    //�����ݴ��map���棬����ֱ����velocityҳ�棬ʹ��key����  
	    model.put("data",l);  
	  
	    return "hello";  
	}  
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Start.class, args);
    }

}
