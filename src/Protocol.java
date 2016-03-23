/**
 * Julien Leroy on 23.03.16.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Protocol
{
    private int key;
    private static Random rand = new Random(4);

    private Socket clientSocket;

    private PrintWriter out;
    private BufferedReader in;

    public int getKey()
    {
        return key;
    }

    public Protocol(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        initStream();
    }

    //For the server
    //Generate the key and send it to the client
    public void initKey()
    {
        key = rand.nextInt();
        out.println(key);
    }

    //For the client
    //Read the key from the server
    public void readKey()
    {
        try
        {
            key = Integer.parseInt(in.readLine());
        }catch (IOException e){e.printStackTrace();}
    }

    public void write(String strToWrite)
    {
        int size = strToWrite.length();

        strToWrite = encrypte(strToWrite);

        out.println(size);
        char[] tab = strToWrite.toCharArray();

        out.write(tab);
        out.flush();
    }

    public String read()
    {
        char[] buff = null;
        try
        {
            String str;
            str = in.readLine();

            if (str == null)
                return null;

            int size = Integer.parseInt(str);

            buff = new char[size];

            in.read(buff);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        String rtn = new String(buff);
        rtn = decrypte(rtn);
        return rtn;
    }

    public String encrypte(String strToEncrypt)
    {
        String rtn = "";
        for (int i = 0; i < strToEncrypt.length(); ++i)
            rtn += (char)(strToEncrypt.charAt(i)+key);

        return rtn;
    }

    public String decrypte(String strToDecrypt)
    {
        String rtn = "";
        for (int i = 0; i < strToDecrypt.length(); ++i)
            rtn += (char)(strToDecrypt.charAt(i)-key);

        return rtn;
    }

    public void closeAll()
    {
        try
        {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    //Init the in and out steams
    void initStream ()
    {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
        }
        catch (IOException e) {e.printStackTrace();}
    }
}
