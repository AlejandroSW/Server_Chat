package server_chat;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

/**
 *
 * @author Alejandro
 */

public class Server_Setup extends JFrame{
    
    private JTextArea serverArea;
    private JTextField serverText;
    private JButton sendButton;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    
    //Constructor
    public Server_Setup(){
        super("Server Chat App");
        
        sendButton = new JButton();
        sendButton.setText("Send");
        sendButton.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    sendMessage(serverText.getText());
                    serverText.setText("");
                }
            }
        );
        add(sendButton, BorderLayout.NORTH);
        
        serverText = new JTextField();
        serverText.setEditable(false);
        serverText.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                    sendMessage(serverText.getText());
                    serverText.setText("");
                }
            }
        );
        add(serverText, BorderLayout.SOUTH);
        
        serverArea = new JTextArea();
        add(new JScrollPane(serverArea));
        setSize(300,400);
        setVisible(true);
        
    }
        
    public void serverStart(){
        try {
            server = new ServerSocket(2222, 50);
            while(true){
                try{
                    //Setup the connection between Server and Client
                    setConnection();
                    //Set textfield editable
                    editableTextField(true);
                    //Start Chating
                    receiveMessage();
                }catch(EOFException eofException){

                }finally{
                    closeConnection();
                }
            } 
        }catch (IOException ioException){
            
        }
    }
    
    public void setConnection() throws IOException{
        serverArea.append(" Waiting for someone to connect... \n");
        connection = server.accept();
        serverArea.append(" Now connected to " + connection.getInetAddress().getHostName());
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
    }
    
    private void receiveMessage() throws IOException{
        String message = " You are now connceted! \n";
        sendMessage(message);
        
        do{
            try{
                message = (String)input.readObject();
                serverArea.append("\n" + message);
            }catch(ClassNotFoundException classNotFoundException){
                
            }
        }while(!message.equals("CLIENT: END"));
    }
    
    private void sendMessage(String message){
        try{
            output.writeObject("SERVER: " + message);
            output.flush();
            serverArea.append("\nSERVER: " + message);
        }catch(IOException ioException){
            
        }
    }
    
    private void closeConnection(){
        serverArea.append("\n Closing connections... \n");
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            
        }
    }
    
    private void editableTextField(final boolean tof){
        SwingUtilities.invokeLater(
            new Runnable(){
                @Override
                public void run(){
                    serverText.setEditable(tof);
                }
            }
        );
    }
}
