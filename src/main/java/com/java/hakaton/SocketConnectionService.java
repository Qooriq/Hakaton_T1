package com.java.hakaton;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;

public class SocketConnectionService {
    private MyConfig config = new MyConfig();

    public getUserTableSize() {
        Connection connection =
    }


    public boolean sendInfoToSocket(String databaseIndicator, String dbIpAddress, int dbPort, String username, String password,
                                    String databaseName, int start, int end, String[] fields, ) {

        // todo implemtn password hashing
        //dbIndicator = 0
        //can be 1
        try (Socket socket = new Socket(ipAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            StringBuilder message = new StringBuilder(databaseIndicator + " ");
            message.append(ipAddress).append(" ");
            message.append(port).append(" ");
            message.append(username).append(" ");
            message.append(password).append(" ");
            message.append(databaseName).append(" ");
            message.append(start).append(" ");
            message.append(end).append(" ");

            for (String field : fields) {
                message.append(field).append(" ");
            }
            out.println(message.toString().trim());
            return true;

        } catch (IOException e) {
            System.err.println("Error sending data to socket: " + e.getMessage());
            return false;
        }
    }

    public void sendDataToNodes(Integer tableSize, String username, String password, String databaseName, String databaseIndicator, String[] fields) {
        List<ClusterNode> nodes = config.getNodes();
        int numNodes = nodes.size();

        int chunkSize = tableSize / numNodes;
        int remainder = tableSize % numNodes;

        int start = 0;
        for (ClusterNode node : nodes) {
            int currentChunkSize = chunkSize + (nodes.indexOf(node) < remainder ? 1 : 0);
            int end = start + currentChunkSize;
            boolean success = sendInfoToSocket(databaseIndicator, node.getIp(), node.getPort(), username, password, databaseName, start, end, fields);
            // TODO make logging
            if (!success) {
                System.err.println("Failed to send data to node: " + node.getIp() + ":" + node.getPort());
            }
            start = end;
        }
    }
}
