package server_chat;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

/**
 *
 * @author Alejandro
 */

public class Server_Setup extends JFrame{
    
    private JTextArea serverArea;
    private JButton connectButton;
    private JButton onlineClientsButton;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private LinkedList clientOutput;
    private LinkedList clientList;
    
    //Constructor
    public Server_Setup(){
        super("Server Chat App 2");
        
        connectButton = new JButton();
        connectButton.setText("Start Server");
        connectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    switch (connectButton.getText()) {
                        case "Start Server":
                            Thread starter = new Thread(new ServerStart());
                            starter.start();
                            connectButton.setText("End Server");
                            break;
                        case "End Server":
                            closeConnection();
                            connectButton.setText("Start Server");
                            break;
                    }
                }
            }
        );
        add(connectButton, BorderLayout.NORTH);
        
        onlineClientsButton = new JButton();
        onlineClientsButton.setText("Online Clients");
        onlineClientsButton.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    Iterator it = clientOutput.iterator();
                    serverArea.append("\n Online users : \n");
                    while (it.hasNext()) 
                    {
                        Object current_user = it.next();
                        serverArea.append((String) current_user);
                        serverArea.append("\n");
                    } 
                }
            }
        );
        add(onlineClientsButton, BorderLayout.SOUTH);
        
        serverArea = new JTextArea();
        add(new JScrollPane(serverArea));
        setSize(300,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
        
    public class ServerStart implements Runnable{
        
        @Override
        public void run()
        {
            clientOutput = new LinkedList();
            
            try {
            server = new ServerSocket(2222, 50);

                while(true){
                    try{
                        //Start listening
                        serverArea.append(" Waiting for someone to connect... \n");
                        connection = server.accept();
                        serverArea.append(" Got a connection \n");
                        //Create new client
                        output = new ObjectOutputStream(connection.getOutputStream());
                        clientOutput.add(output);
                        output.flush();
                        Thread client = new Thread(new ClientCreator(connection, output));
                        client.start();
                    }catch(EOFException eofException){

                    }
                } 
            }catch (IOException ioException){

            }
        }
    }
    
    public class ClientCreator implements Runnable {
        
        Socket client;
        String username;

        //Constructor
        ClientCreator(Socket connection, ObjectOutputStream output) {
            
            client = connection;
            
            try {    
                input = new ObjectInputStream(client.getInputStream());
                String anon="anon";
                Random generator = new Random(); 
                int a = generator.nextInt(999);
                String num = String.valueOf(a);
                anon=anon.concat(num);
                username=anon;
            } catch (IOException ex) {
                
            }
        }

        @Override
        public void run(){
            
            String message;
            
            try {                
                while(((message = (String)input.readObject())) != null)
                {
                    sendMessage(message);
                    if (message.equals(" USERNAME: is Disconnecting "))
                    {
                        clientOutput.remove(output);
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                
            }
        }
    }
    
    private void sendMessage(String message){
        
        Iterator it = clientOutput.iterator();
        
        while(it.hasNext()){
            try{
                serverArea.append(" Sending: " + message + "\n");
                ObjectOutputStream out = (ObjectOutputStream) it.next();
                out.writeObject(message);
                out.flush();
            }catch(IOException ioException){

            }            
        }
    }
    
    private void closeConnection(){
        sendMessage(" The Server is shutting all connections... ");
        serverArea.append(" Closing connections... \n");
        try{
            clientOutput.removeAll(clientOutput);
            output.close();
            input.close();
            connection.close();
            server.close();
        }catch(IOException ioException){
            
        }
    }   
}