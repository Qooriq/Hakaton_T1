package com.java.hakaton.core.configs;

import com.java.hakaton.core.data.ClusterNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {
    private List<ClusterNode> nodes = List.of(new ClusterNode("name1", "10.160.120.141", 8555)
            , new ClusterNode("name2", "192.168.100.2", 5055));

    public List<ClusterNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ClusterNode> nodes) {
        this.nodes = nodes;
    }
}