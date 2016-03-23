/**
 * Julien Leroy on 23.03.16.
 */
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Client
{
    private int port;
    private String host;

    private Protocol protocol;

    public Client(String host, int port)
    {
        this.port =port;
        this.host = host;

        try
        {
            protocol = new Protocol(new Socket(host, port));

        }catch (IOException e)
        {
            e.printStackTrace();
        }

        protocol.readKey();
    }

    public void write(String strToWrite){
        protocol.write(strToWrite);
    }

    public void print(){
        System.out.println("Server Respond: " + protocol.read());
    }

    public void colseAll()
    {
        protocol.closeAll();
    }



    public static void main(String arg[])
    {
        Client c = new Client("localhost", 4444);

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true)
        {
            System.out.print(">");
            input = scanner.nextLine();
            c.write(input);
            c.print();

            if (input.equals(".quitter"))
                break;


        }

        c.colseAll();
    }
}
