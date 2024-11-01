package com.java.hakaton;

import com.java.hakaton.core.db.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

@Service
public class SocketConnectionService {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private MyConfig config;

    public boolean sendInfoToSocket(String databaseIndicator, String indicator, String dbIpAddress, int dbPort, String username, String password,
                                    String databaseName, int start, int end, String[] fields, String tableName, ClusterNode node) {

        try (Socket socket = new Socket(node.getIp(), node.getPort());
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            StringBuilder message = new StringBuilder(databaseIndicator + " ");
            message.append(indicator).append(" ");
            message.append(start).append(" ");
            message.append(end).append(" ");

            for (String field : fields) {  // Now uses the retrieved fields
                message.append(field).append(" ");
            }
            message.append(dbIpAddress).append(":").append(dbPort).append("/").append(databaseName).append(" ");
            message.append(tableName).append(" ");
            message.append(username).append(" ");
            message.append(password);

            out.println(message.toString().trim());
            return true;

        } catch (IOException e) {
            System.err.println("Error sending data to socket: " + e.getMessage());
            return false;
        }
    }

    public void sendDataToNodes(String dbIpAddress, int dbPort, String tableName, String username, String password, String databaseName, String databaseIndicator, String indicator) throws SQLException, IOException {

        List<ClusterNode> nodes = config.getNodes();
        int tableSize = databaseService.getUserTableSize(dbIpAddress, dbPort, username, password, databaseName);
        int numNodes = nodes.size();

        int chunkSize = tableSize / numNodes;
        int remainder = tableSize % numNodes;
        List<List<String>> initialData = databaseService.getUserTableData(dbIpAddress, dbPort, username, password, databaseName, 0, 1, tableName);
        String[] fields = DataValidator.getNonConfident(initialData).toArray(new String[0]);

        int start = 0;
        for (int i = 0; i < numNodes; i++) {
            int currentChunkSize = chunkSize + (i < remainder ? 1 : 0);
            int end = start + currentChunkSize;

            boolean success = sendInfoToSocket(databaseIndicator, indicator, dbIpAddress, dbPort, username, password, databaseName, start, end, fields, tableName, nodes.get(i));

            if (!success) {
                System.err.println("Failed to send data to node: " + nodes.get(i).getIp() + ":" + nodes.get(i).getPort());
            }
            start = end;
        }
    }
}