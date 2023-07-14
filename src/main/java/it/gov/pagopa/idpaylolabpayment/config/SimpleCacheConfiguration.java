package it.gov.pagopa.idpaylolabpayment.config;

import static it.gov.pagopa.idpaylolabpayment.config.IgniteMemberConfiguration.SIMPLE_CACHE_EVICTION_REGION;

import it.gov.pagopa.idpaylolabpayment.domain.Transaction;
import it.gov.pagopa.idpaylolabpayment.infrastructure.idpay.ignite.TransactionMongoCacheAdapterFactory;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class SimpleCacheConfiguration {

  @Bean
  public IgniteCache<String, String> simpleCache(Ignite ignite) {

    final var configuration = new CacheConfiguration<String, String>()
        .setName("simple-cache")
        .setCacheMode(CacheMode.PARTITIONED)
        .setOnheapCacheEnabled(true)
        .setBackups(1)
        .setDataRegionName(SIMPLE_CACHE_EVICTION_REGION);

    return ignite.getOrCreateCache(configuration);
  }

  @Bean
  @DependsOn("transactionReactiveDao")
  public IgniteCache<String, Transaction> transactionCache(Ignite ignite) {
    final var configuration = new CacheConfiguration<String, Transaction>()
        .setName("transaction-cache")
        .setCacheMode(CacheMode.PARTITIONED)
        .setOnheapCacheEnabled(false)
        .setReadThrough(true)
        .setWriteThrough(true)
        //.setWriteBehindEnabled(true)
        //.setWriteBehindFlushFrequency(3000) // every 5 seconds flush data
        //.setWriteBehindFlushSize(1000) // evert 1000 update
        .setTypes(String.class, Transaction.class)
        .setCacheStoreFactory(new TransactionMongoCacheAdapterFactory().setDataSourceBean("transactionReactiveDao"))
        .setDataRegionName(SIMPLE_CACHE_EVICTION_REGION);

    return ignite.getOrCreateCache(configuration);
  }
}
