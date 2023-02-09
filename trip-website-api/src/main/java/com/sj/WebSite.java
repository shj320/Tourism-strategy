/**
 * author:sj
 */

package com.sj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("generator.mapper")
public class WebSite {
    public static void main(String[] args) {
        SpringApplication.run(WebSite.class,args);
    }
}
