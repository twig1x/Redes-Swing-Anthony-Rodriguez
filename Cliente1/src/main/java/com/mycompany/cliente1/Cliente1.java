/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.cliente1;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;

public class Cliente1 extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public Cliente1() {
        setTitle("Inicio de sesión");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Iniciar sesión");

        loginButton.addActionListener(this);

        add(new JLabel("Usuario:"));
        add(usernameField);
        add(new JLabel("Contraseña:"));
        add(passwordField);
        add(loginButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Cliente1();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            try {
                Socket socket = new Socket("localhost", 12345);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                dos.writeUTF(username);
                dos.writeUTF(password);

                String response = dis.readUTF();

                JOptionPane.showMessageDialog(this, response);

                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
