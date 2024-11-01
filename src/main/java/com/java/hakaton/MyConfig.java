package com.java.hakaton;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "nodes")
public class MyConfig {
    private List<ClusterNode> nodes;

    public List<ClusterNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ClusterNode> nodes) {
        this.nodes = nodes;
    }
}