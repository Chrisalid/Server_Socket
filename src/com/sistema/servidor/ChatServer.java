package com.sistema.servidor;

import java.util.List;
import java.io.IOException;
import java.util.LinkedList;
import java.net.ServerSocket;

public class ChatServer {
    private final int PORT = 5000;
    private ServerSocket serverSocket;
    private final List<ClientSocket> clients = new LinkedList<ClientSocket>();

    public void startServer() throws IOException {
        this.serverSocket = new ServerSocket(this.PORT);

        System.out.println("Server Iniciado Na Porta: " + this.serverSocket.getLocalPort());

        this.clientConnectionLoop();
    }

    private void clientConnectionLoop() throws IOException {
        while (true) {
            ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
            System.out.println("O cliente: " + clientSocket.getRemoteSocketAddress() + " está conectado");

            clients.add(clientSocket);

            new Thread(() -> this.clientMessageLoop(clientSocket)).start();
        }
    }

    private void clientMessageLoop(ClientSocket clientSocket) {
        String message;
        try {
            while ((message = clientSocket.getMessage()) != null) {
                if ("/sair".equalsIgnoreCase(message))
                    return;

                System.out.println("Messagem de "
                        + clientSocket.getRemoteSocketAddress()
                        + " recebida:\n"
                        + message);

                sendMessageToAll(clientSocket, message);
            }
        } finally {
            System.out.println("Cliente " + clientSocket.getRemoteSocketAddress() + " desconectou do chat");
            clientSocket.close();
        }
    }

    private void sendMessageToAll(ClientSocket sender, String message) {
        for (ClientSocket client : clients) {
            if (!sender.equals(client))
                client.sendMessage(
                        sender.getRemoteSocketAddress()
                                + ": " + message);
        }
    }

}
