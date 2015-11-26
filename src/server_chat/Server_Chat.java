package server_chat;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author Alejandro
 */
public class Server_Chat extends JFrame {
    
    private ServerSocket server;
    private Socket connection;
    /*private ServerSocket server2;
    private Socket connection2;*/
    private LinkedList clientList;
    
    public class ServerStart implements Runnable{
        
        @Override
        public void run()
        {
            clientList = new LinkedList();
            
            try {
            server = new ServerSocket(2222, 50);
            //server2 = new ServerSocket(2223, 50);

                while(true)
                {
                    try{
                        //Start listening
                        ServerActionArea.append(" Waiting for someone to connect... \n");
                        connection = server.accept();
                        ServerActionArea.append(" Got a connection \n");
                        
                        //Start listening 2
                        /*ServerActionArea.append(" Waiting for second connection... \n");
                        connection2 = server2.accept();
                        ServerActionArea.append(" Got a connection \n");*/
                        
                        //Create new client
                        ClientCreator client = new ClientCreator(connection/*, connection2*/);
                        clientList.add(client);
                        
                        //Start a new thread
                        Thread clientStarter = new Thread(client);
                        clientStarter.start();
                    }catch(EOFException eofException){

                    }
                } 
            }catch (IOException ioException){

            }
        }
    }
    
    public class ClientCreator implements Runnable {    
        
        Socket connection;
        Socket connection2;
        ObjectInputStream  input;
        ObjectOutputStream output;
        InputStreamReader  input2;
        ObjectOutputStream output2;
        String username;
        String[] data;
        String welcome;

        //Constructor
        ClientCreator(Socket conn/*, Socket conn2*/) {
            
            try { 
                //Message streams and socket
                connection = conn;
                input = new ObjectInputStream(connection.getInputStream());
                output = new ObjectOutputStream(connection.getOutputStream());
                
                //Command streams and socket
                /*connection2 = conn2;
                input2 = new InputStreamReader(connection2.getInputStream());
                output2 = new ObjectOutputStream(connection2.getOutputStream());*/ 
                
                //Welcome input message from client to resolve its nickname
                welcome = (String)input.readObject();
                data = welcome.split(":");
                username = data[0];
            } catch (IOException | ClassNotFoundException ex) {
                
            }
        }

        @Override
        public void run(){       
            String message;
            //String command;
            
            try {                
                while((message = (String)input.readObject()) != null)
                {
                    //command = (String)input.readObject();
                    
                    /*if (command.equals("Disconnect"))
                    {
                        sendMessage(message);
                        
                        //Close message streams and socket
                        output.close();
                        input.close();
                        connection.close();
                        
                        //Close command streams and socket
                        output2.close();
                        input2.close();
                        connection2.close();
                        
                        //Remove client from the list
                        clientList.remove(this);
                    }else
                    {
                        sendMessage(message);
                    }*/                  
                    
                    sendMessage(message);
                    data = message.split(":");
                    if (data[1].equals(" is Disconnecting "))
                    {
                        //Close message streams, socket and remove client from list
                        input.close();
                        output.close();
                        connection.close();
                        clientList.remove(this);
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                
            }
        }
    }
    
    private void sendMessage(String message){        
        Iterator it = clientList.iterator();
        ClientCreator client;        
        
        while(it.hasNext())
        {
            try{                
                ServerActionArea.append(" Sending: " + message + "\n");
                client = (ClientCreator) it.next();
                client.output.writeObject(message);
                client.output.flush();
            }catch(IOException ioException){

            }            
        }
    }
    
    private void closeConnection(){        
        sendMessage(" The Server is shutting all connections... ");
        ServerActionArea.append(" Closing connections... \n");
        Iterator it = clientList.iterator();
        ClientCreator client;
        
        try{
            client = (ClientCreator) it.next();
            
            //Close message streams and socket
            client.input.close();
            client.output.close();
            client.connection.close();
            
            //Close command streams and socket
            /*client.input2.close();
            client.output2.close();
            client.connection2.close();*/
            
            //Remove client from the list
            it.remove();
        }catch(IOException ioException){
            
        }
    }

    public Server_Chat() {
        initComponents();
    }
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ServerActionArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        ServerUsersArea = new javax.swing.JTextArea();
        ConnectionButton = new javax.swing.JButton();
        UsersButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ServerActionArea.setColumns(20);
        ServerActionArea.setRows(5);
        jScrollPane1.setViewportView(ServerActionArea);

        ServerUsersArea.setColumns(20);
        ServerUsersArea.setRows(5);
        jScrollPane2.setViewportView(ServerUsersArea);

        ConnectionButton.setText("Start Server");
        ConnectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectionButtonActionPerformed(evt);
            }
        });

        UsersButton.setText("Show Users");
        UsersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsersButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Server Actions");

        jLabel2.setText("Connected Users");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                    .addComponent(ConnectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UsersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ConnectionButton)
                    .addComponent(UsersButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ConnectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectionButtonActionPerformed
        switch (ConnectionButton.getText()) {
            case "Start Server":
                Thread starter = new Thread(new ServerStart());
                starter.start();
                ConnectionButton.setText("End Server");
                break;
            case "End Server":
                closeConnection();
                ConnectionButton.setText("Start Server");
                break;
        }
    }//GEN-LAST:event_ConnectionButtonActionPerformed

    private void UsersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsersButtonActionPerformed
        Iterator it = clientList.iterator();
        ClientCreator client;
        ServerUsersArea.setText("");

        while (it.hasNext())
        {
            client = (ClientCreator) it.next();
            ServerUsersArea.append(client.username + "\n");
        }
    }//GEN-LAST:event_UsersButtonActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server_Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Server_Chat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ConnectionButton;
    private javax.swing.JTextArea ServerActionArea;
    private javax.swing.JTextArea ServerUsersArea;
    private javax.swing.JButton UsersButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
