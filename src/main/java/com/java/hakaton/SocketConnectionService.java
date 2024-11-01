package com.java.hakaton;

import com.java.hakaton.core.db.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

@Service
public class SocketConnectionService {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    private MyConfig config = new MyConfig();

    public boolean sendInfoToSocket(String databaseIndicator, String indicator, String dbIpAddress, int dbPort, String username, String password,
                                    String databaseName, int start, int end, String[] fields/* String tableName*/, ClusterNode node) {

        try (Socket socket = new Socket(node.getIp(), node.getPort());
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            StringBuilder message = new StringBuilder(databaseIndicator + " ");
            message.append(indicator).append(" ");
            message.append(dbIpAddress).append(" ");
            message.append(dbPort).append(" ");
            message.append(username).append(" ");
            message.append(password).append(" "); // TODO: Implement password hashing!!!
            message.append(databaseName).append(" ");
            message.append(start).append(" ");
            message.append(end).append(" ");
/*            message.append(tableName).append(" ");*/ // Add table name to the message

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

    public void sendDataToNodes(String dbIpAddress, int dbPort/* String tableName */, String username, String password, String databaseName, String databaseIndicator, String indicator, String[] fields) {
        List<ClusterNode> nodes = config.getNodes();
        int tableSize  = databaseService.getUserTableSize(dbIpAddress, dbPort, username, password, databaseName);
        int numNodes = nodes.size();

        int chunkSize = tableSize / numNodes;
        int remainder = tableSize % numNodes;

        int start = 0;
        for (ClusterNode node : nodes) {
            int currentChunkSize = chunkSize + (nodes.indexOf(node) < remainder ? 1 : 0);
            int end = start + currentChunkSize;

            boolean success = sendInfoToSocket(databaseIndicator, indicator, dbIpAddress, dbPort, username, password, databaseName, start, end, fields, node);

            if (!success) {
                System.err.println("Failed to send data to node: " + node.getIp() + ":" + node.getPort());
            }
            start = end;
        }
    }
}