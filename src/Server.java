/**
 * Julien Leroy on 23.03.16.
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable
{
    private int port;
    private static int cnt = 1;

    private ServerSocket serverSocket;

    @Override
    public void run()
    {
        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {e.printStackTrace();}

        while (true)
        {
            try
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Client connected");
                new Thread(new ServerThread(clientSocket)).start();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    class ServerThread implements Runnable
    {
        private Protocol protocol;
        private int nb;

        public ServerThread(Socket clientSocket)
        {
            protocol = new Protocol(clientSocket);
            nb = cnt++;
        }

        @Override
        public void run()
        {
            protocol.initKey();
            System.out.println("Thread n" + nb + " created, key = " + protocol.getKey());

            while (true)
            {

                String str = protocol.read();

                if (str == null || str.equals(".quitter"))
                {
                    System.out.println("quit the thread");
                    protocol.write("The server closed the connection");
                    break;
                }
                System.out.println("Client " + nb + " says: " + str);

                protocol.write(str);



            }
            System.out.println("Client n" + nb + " disconnected - thread stoped");
            protocol.closeAll();
        }
    }

    public Server(int port)
    {
        this.port = port;
    }


    public static void main(String arg[])
    {
        new Thread(new Server(4444)).start();
    }

}
