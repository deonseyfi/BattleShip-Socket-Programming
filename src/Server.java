import java.io.*;
import java.util.*;
import java.net.*;


public class Server {


    static Vector<ClientHandler> vector = new Vector<>();


    static int i = 0;

    public Server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        Socket input;


        while (true) {

            input = serverSocket.accept();


            DataInputStream dataIn = new DataInputStream(input.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(input.getOutputStream());

            ClientHandler clients = new ClientHandler(input, "Player " + i, dataIn, dataOut);

            Thread thre = new Thread(clients);
            vector.add(clients);
            thre.start();
            i++;

        }
    }
}


class ClientHandler implements Runnable
{
    Scanner keyboard = new Scanner(System.in);
    private String name;
    final DataInputStream dataIn;
    final DataOutputStream dataOut;
    Socket input;
    boolean isloggedin;


    public ClientHandler(Socket input, String name,
                         DataInputStream dataIn, DataOutputStream dataOut) {
        this.dataIn = dataIn;
        this.dataOut = dataOut;
        this.name = name;
        this.input = input;
        this.isloggedin=true;
    }

    @Override
    public void run() {

        String received;
        while (true)
        {
            try
            {

                received = dataIn.readUTF();

                System.out.println(received);

                if(received.equals("Quit")){
                    this.isloggedin=false;
                    this.input.close();
                    break;
                }


                StringTokenizer st = new StringTokenizer(received, "#");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();


                for (ClientHandler mc : Server.vector)
                {

                    if (mc.name.equals(recipient) && mc.isloggedin==true)
                    {
                        mc.dataOut.writeUTF(this.name+" : "+MsgToSend);
                        break;
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try
        {
            this.dataIn.close();
            this.dataOut.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
} 
