package com.si.communication;

import com.si.encryption.AES;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class NodeB {
    private static final String k3 = "nSVB42NVkfzc2Vx0";

    public static void main(String[] args) throws IOException {
        String serverAddress = "127.0.0.1";
        int PORT = 8100;
        try (Socket socket = new Socket(serverAddress, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             BufferedReader br = new BufferedReader(new FileReader("mesaj1.txt"));
        ) {
            while (true) {
                String mode = in.readLine();
                System.out.println("Modul de operare este: " + mode);

                String encryptedKey = in.readLine();
                String key = AES.decrypt(encryptedKey, k3);
                System.out.println("Am primit cheia!");

                out.println("OK!");
                out.flush();

                StringBuilder decryptedText = new StringBuilder();
                String block;
                if(mode.equals("ECB")) {

                    while (true) {
                        block = in.readLine();
                        if(block == null) {
                            System.out.println("Mesajul primit: " + decryptedText);
                            System.exit(1);
                        }
                        decryptedText.append(AES.decrypt(block, key));
                    }
                }
                else {
                    String auxIV = Server.iv;
                    while (true) {
                        block = in.readLine();
                        if(block == null) {
                            System.out.println("Mesajul primit: " + decryptedText);
                            System.exit(1);
                        }
                        auxIV = AES.encrypt(auxIV, key);
                        StringBuilder sb = new StringBuilder();

                        for(int j = 0; j < block.length() && j < auxIV.length(); j++) {
                            sb.append((char)(block.charAt(j) ^ auxIV.charAt(j)));
                        }
                        decryptedText.append(sb.toString());
                    }
                }

            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
