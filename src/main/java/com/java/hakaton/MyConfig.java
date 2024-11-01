package com.java.hakaton;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "nodes")
public class MyConfig {
    private List<ClusterNode> nodes = List.of(new ClusterNode("name1", "192.168.100.1", 5054)
            , new ClusterNode("name2", "192.168.100.2", 5055));

    public List<ClusterNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ClusterNode> nodes) {
        this.nodes = nodes;
    }
}