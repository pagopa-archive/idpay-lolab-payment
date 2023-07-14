package it.gov.pagopa.idpaylolabpayment.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteSpring;
import org.apache.ignite.configuration.DataPageEvictionMode;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.DeploymentMode;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.metric.jmx.JmxMetricExporterSpi;
import org.apache.ignite.spi.metric.log.LogExporterSpi;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteMemberConfiguration {

  public static final String SIMPLE_CACHE_EVICTION_REGION = "simple-cache-with-eviction";

  @Bean
  public Ignite ignite(
      IgniteConfiguration memberConfiguration,
      ApplicationContext applicationContext
  ) throws IgniteCheckedException {
    return IgniteSpring.start(memberConfiguration, applicationContext);
  }

  @Bean
  public IgniteConfiguration memberConfiguration(
      DataRegionConfiguration simpleCacheDataRegion
  ) {
    final var storageConfiguration = new DataStorageConfiguration();
    storageConfiguration.setDefaultDataRegionConfiguration(simpleCacheDataRegion);
    storageConfiguration.setSystemRegionMaxSize(50L * 1024 * 1024); // MB

    return new IgniteConfiguration()
        .setIgniteInstanceName("ignite-cluster")
        .setDataStorageConfiguration(storageConfiguration)
        .setPeerClassLoadingEnabled(true)
        .setDeploymentMode(DeploymentMode.CONTINUOUS)
        .setMetricExporterSpi(new JmxMetricExporterSpi())
        .setCommunicationSpi(new TcpCommunicationSpi());
  }

  @Bean
  public DataRegionConfiguration simpleCacheDataRegion() {
    final var region = new DataRegionConfiguration();
    region.setName(SIMPLE_CACHE_EVICTION_REGION);
    region.setInitialSize(20L * 1024 * 1024); // 20 MB
    region.setMaxSize(60L * 1024 * 1024); // Max 100 MB
    region.setPageEvictionMode(DataPageEvictionMode.RANDOM_2_LRU);
    region.setMetricsEnabled(true);
    region.setEvictionThreshold(0.8); // 80 %
    return region;
  }
}
