package com.company.project.integration.util;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.embedded.CassandraEmbeddedServerBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static info.archinnov.achilles.embedded.CassandraEmbeddedConfigParameters.CLUSTER_NAME;

@TestConfiguration
public class EmbeddedCassandraTestConfig {

    @Bean
    public Session createInMemorySession() {
        final Cluster cluster = CassandraEmbeddedServerBuilder
                .builder()
                .cleanDataFilesAtStartup(true)
                .withKeyspaceName("testlocal")
                .withScript("test-schema.cql")
                .withClusterName(CLUSTER_NAME)
                .buildNativeCluster();

        return cluster.connect("testlocal");
    }
}
