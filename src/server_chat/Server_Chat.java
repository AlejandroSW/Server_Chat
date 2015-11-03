package server_chat;

/**
 *
 * @author Alejandro
 */

import javax.swing.*;

public class Server_Chat {

    public static void main(String[] args) {
        Server_Setup chat = new Server_Setup();
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.serverStart();
    }
}
