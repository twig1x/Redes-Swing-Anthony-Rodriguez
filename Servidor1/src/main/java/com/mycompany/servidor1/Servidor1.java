/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.servidor1;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor1 {
    private static final String DATABASE_FILE = "usuarios.txt";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor en línea.");
            
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + socket.getInetAddress());

                executorService.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream())
            ) {
                String username = dis.readUTF();
                String password = dis.readUTF();

                if (validateUser(username, password)) {
                    dos.writeUTF("Inicio de sesión exitoso.");
                } else {
                    dos.writeUTF("Error al iniciar sesión. Usuario o contraseña incorrectos.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean validateUser(String username, String password) {
            try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}