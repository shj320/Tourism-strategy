/**
 * author:sj
 */

package com.sj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MgrSite {
    public static void main(String[] args) {
        SpringApplication.run(MgrSite.class,args);
    }
}
