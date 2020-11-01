package com.si.communication;

import com.si.encryption.AES;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class NodeA {
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
                Scanner input = new Scanner(System.in);
                System.out.print("Introduceti modul de operare: ");
                String mode = input.nextLine();
                out.println(mode);
                out.flush();

                String encryptedKey = in.readLine();
                String key = AES.decrypt(encryptedKey, k3);

                String responseB = in.readLine();
                System.out.println("[NOD A]: Am primit confirmarea. [B]: " + responseB);

                System.out.println("[NOD A]: Incepem trimiterea mesajului.");

                String text = new String(Files.readAllBytes(Paths.get("mesaj1.txt")), StandardCharsets.UTF_8);

                if(mode.equals("ECB")) {
                    for (int i = 0; i < text.length(); i += 16) {
                        if (i + 16 > text.length())
                            out.println(AES.encrypt(text.substring(i), key));
                        else
                            out.println(AES.encrypt(text.substring(i, i + 16), key));
                        out.flush();
                    }
                }
                else {
                    String auxIV = Server.iv;
                    for (int i = 0; i < text.length(); i += 16) {
                        auxIV = AES.encrypt(auxIV, key);
                        String textCopy;
                        StringBuilder sb = new StringBuilder();

                        if (i + 16 > text.length())
                            textCopy = text.substring(i);
                        else
                            textCopy = text.substring(i, i + 16);

                        for (int j = 0; j < textCopy.length(); j++) {
                            sb.append((char) (textCopy.charAt(j) ^ auxIV.charAt(j)));
                        }
                        out.println(sb.toString());
                        out.flush();
                    }
                }
                System.out.println("Am terminat! Incheiem comunicarea!");
                socket.close();
                System.exit(1);
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
