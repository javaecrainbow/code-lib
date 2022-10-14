package com.salk.lib.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <p/>
 * Description
 * <p/>
 *
 * @author salkli
 * @date 2022/3/2
 */
@Component
public class PersonBean2 implements CommandLineRunner {
    //@PostConstruct
    public void init() {
        System.out.println("[post construct] "+this.getClass().getName());
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(" personBean CommandLineRunner2========");
    }
}
