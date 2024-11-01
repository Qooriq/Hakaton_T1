package com.java.hakaton.core.services;

import com.java.hakaton.core.data.ClusterNode;
import com.java.hakaton.core.configs.NodeService;
import com.java.hakaton.core.db.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;

@Service
public class SocketConnectionService {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private NodeService config;

    public boolean sendInfoToSocket(String databaseIndicator, String indicator, String dbIpAddress, int dbPort, String username, String password,
                                    String databaseName, int start, int end, String[] fields, String tableName, ClusterNode node, int tableSize) {
        try {
            Socket socket = new Socket(node.getIp(), node.getPort());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            StringBuilder message = new StringBuilder(databaseIndicator + " ");
            message.append(indicator).append(" ");
            message.append(start).append(" ");
            message.append(end).append(" ");

            message.append(fields.length).append(" ");
            for (String field : fields) {  // Now uses the retrieved fields
                message.append(field).append(" ");
            }
            message.append(dbIpAddress).append(":").append(dbPort).append("/").append(databaseName).append(" ");
            message.append(tableName).append(" ");
            message.append(username).append(" ");
            message.append(password);

            out.println(message.toString().trim());

            // Receive response from socket
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();

            // Append response to out.txt file
            try (PrintWriter fileWriter = new PrintWriter("out.txt", "UTF-8")) {
                fileWriter.println(response);
            }

            socket.close();
            return true;

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDataToNodes(String dbIpAddress, int dbPort, String tableName, String username, String
            password, String databaseName, String databaseIndicator, String indicator) throws SQLException, IOException {

        List<ClusterNode> nodes = config.getNodes();
        int tableSize = databaseService.getUserTableSize("127.0.0.1", dbPort, username, password, databaseName);
        int numNodes = nodes.size();

        int chunkSize = tableSize / numNodes;
        int remainder = tableSize % numNodes;
        List<List<String>> initialData = databaseService.getUserTableData("127.0.0.1", dbPort, username, password, databaseName, 0, 1, tableName);
        String[] fields = DataValidator.getNonConfident(initialData).toArray(new String[0]);

        int start = 0;
        for (int i = 0; i < numNodes; i++) {
            int currentChunkSize = chunkSize + (i < remainder ? 1 : 0);
            int end = start + currentChunkSize;

            boolean success = sendInfoToSocket(databaseIndicator, indicator, dbIpAddress, dbPort, username, password, databaseName, start, end, fields, tableName, nodes.get(i), tableSize);

            if (!success) {
                System.err.println("Failed to send data to node: " + nodes.get(i).getIp() + ":" + nodes.get(i).getPort());
            }
            start = end;
        }
    }
}