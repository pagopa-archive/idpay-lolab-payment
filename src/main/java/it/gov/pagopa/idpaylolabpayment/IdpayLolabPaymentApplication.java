package it.gov.pagopa.idpaylolabpayment;

import it.gov.pagopa.idpaylolabpayment.config.IgniteMemberConfiguration;
import org.apache.ignite.IgniteCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(IgniteMemberConfiguration.class)
public class IdpayLolabPaymentApplication {

	public static void main(String[] args) {
		final var context = SpringApplication.run(IdpayLolabPaymentApplication.class, args);

		// simple hello world example
		final var cache = (IgniteCache<String, String>) context.getBean("simpleCache");
		cache.put("test", "Hello World");
		System.out.println("From cache: " + cache.get("test"));
	}

}
