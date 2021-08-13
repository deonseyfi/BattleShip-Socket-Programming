

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
    final static int ServerPort = 5000;

    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Scanner scn = new Scanner(System.in);


        InetAddress ip = InetAddress.getByName("localhost");


        Socket s = new Socket(ip, ServerPort);


        DataInputStream dataIn = new DataInputStream(s.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {


                    String messageOut = scn.nextLine();

                    try {

                        dataOut.writeUTF(messageOut);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {

                        String messageIn = dataIn.readUTF();
                        System.out.println(messageIn);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }
} 
