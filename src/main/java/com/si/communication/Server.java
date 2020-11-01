package com.si.communication;
import com.si.encryption.AES;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Server {
    public static final int PORT = 8100;
    private Socket socket1 = null ;
    private Socket socket2 = null ;
    private String opMode;
    private String k1;
    private String k2;
    private final String k3 = "nSVB42NVkfzc2Vx0";
    public final static String iv = "Ff33w3LABj14LCzB";

    public Server() throws IOException {
        k1 = generateString();
        k2 = generateString();

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
     * LINIILE 62-66: Comunicarea intre noduri se efectueaza prin intermediul serverului.
     *      Se realizeaza astfel doi pasi deodata. Nodul A trimite modul de operare catre server.
     *      Acesta primeste modul de operare, il retine si il trimite catre nodul B.
     * LINIILE 69-75: Serverul determina ce fel de operare se va face in functie de mesajul primit de la nodul A
     *      Encripteaza cheia aleasa si o trimite celor doua noduri.
     * LINIILE 82-83: Serverul asteapta de la nodul B raspuns pentru a putea incepe comunicarea intre cele doua noduri
     *      Trimite raspunsul nodului B si nodului A. Comunicarea poate incepe.
     * LINIILE 89-99: Se citeste din socket blocuri de text criptat de la nodul A.
     *      Se trimite fiecare bloc nodului B.
     *      In cazul in care s-au terminat de primit blocurile de la nodul A, se opreste si afiseaza mesaj corespunzator
     */
    private void runServer() {
        try (BufferedReader in1 = new BufferedReader(
                new InputStreamReader(socket1.getInputStream()));
             PrintWriter out1 = new PrintWriter(socket1.getOutputStream(), true);
             BufferedReader in2 = new BufferedReader(
                     new InputStreamReader(socket2.getInputStream()));
             PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true)
        ) {
            String operateMode = in1.readLine();
            out2.println(operateMode);
            opMode = operateMode;
            out2.flush();
            System.out.println("Modul de operare ales este: " + opMode);

            String encryptedKey;
            if(opMode.startsWith("ECB"))
                encryptedKey = AES.encrypt(k1, k3);
            else
                encryptedKey = AES.encrypt(k2, k3);
            out1.println(encryptedKey);
            out2.println(encryptedKey);
            out1.flush();
            out2.flush();
            System.out.println("Am trimis cheia celor doua noduri.");

            String responseB = in2.readLine();
            out1.println(responseB);
            out1.flush();
            System.out.println("Comunicarea poate incepe! [NOD B: " + responseB + "]");

            String block;
            while (true) {
                block = in1.readLine();
                if(block == null) {
                    socket1.close();
                    socket2.close();
                    System.out.println("Am teminat!");
                    System.exit(1);
                }
                out2.println(block);
                out2.flush();
            }


        }
        catch (IOException e) {
            System.err.println("Communication error... " + e);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            try {
                socket1.close();
                socket2.close();
            } catch (IOException e) {
                System.err.println (e);
            }
        }
    }

    /**
     * Se genereaza un String de 16 caractere pentru chei.
     * @return String-ul random generat
     */
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
