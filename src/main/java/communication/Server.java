package communication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {
    public static final int PORT = 8100;
    private Socket socket1 = null ;
    private Socket socket2 = null ;
    private String opMode;
    private String k1;
    private String k2;
    private String k3;

    public Server() throws IOException {
        k1 = generateString();
        k2 = generateString();
        k3 = generateString();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Waiting for client 1");
                socket1 = serverSocket.accept();
                System.out.println("Client 1 accepted !");

                System.out.println("Waiting for client 2");
                socket2 = serverSocket.accept();
                System.out.println("Client 2 accepted !");
                System.out.println("Let's start!");
                runServer();

            }
        } catch (IOException e) {
            System.err.println("Ooops... " + e);
        }
    }

    /**
     * LINIILE 59-61: Comunicarea intre noduri se efectueaza prin intermediul serverului.
     *      Se realizeaza astfel doi pasi deodata. Nodul A trimite modul de operare catre server.
     *      Acesta primeste modul de operare, il retine si il trimite catre nodul B.
     *
     */
    private void runServer() {
        try (BufferedReader in1 = new BufferedReader(
                new InputStreamReader(socket1.getInputStream()));
             PrintWriter out1 = new PrintWriter(socket1.getOutputStream(), true);
             BufferedReader in2 = new BufferedReader(
                     new InputStreamReader(socket2.getInputStream()));
             PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true)
        ) {

            while (true) {
                String operateMode = in1.readLine();
                out2.println(operateMode);
                opMode = operateMode;
                out2.flush();



            }

        }
        catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket1.close();
                socket2.close();
            } catch (IOException e) {
                System.err.println (e);
            }
        }
    }


    public String generateString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < 16; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static void main ( String [] args ) throws IOException {
        Server server = new Server();
    }
}
