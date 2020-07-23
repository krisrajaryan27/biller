package com.setu.biller;

import com.setu.biller.repository.BillPaymentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Krishna Verma
 * @date 21/07/2020
 */
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = BillPaymentRepository.class)
public class BillerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillerApplication.class, args);
    }

}
