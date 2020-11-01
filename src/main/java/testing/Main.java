package testing;

import com.encryption.AES;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] argv) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        List<String> encryptedList = new ArrayList<>();

        StringBuilder decryptedText = new StringBuilder();
        //String iv = generateString();
        String key1 = "nSVB42NVkfzc2";
        String auxIV = iv;

        Scanner in = new Scanner(System.in);
        String text = in.nextLine();


        /**
         * EBC
         */
        /*for(int i = 0; i < text.length(); i += 16) {
            if(i + 16 > text.length())
                encryptedList.add(AES.encrypt(text.substring(i), key1));
            else
                encryptedList.add(AES.encrypt(text.substring(i, i + 16), key1));
        }

        for(String s : encryptedList) {
            decryptedText.append(AES.decrypt(s, key1));
        }
        */

        for(int i = 0; i < text.length(); i += 16) {
            auxIV = AES.encrypt(auxIV, key1);
            String textCopy;
            StringBuilder sb = new StringBuilder();

            if(i + 16 > text.length())
                textCopy = text.substring(i);
            else
                textCopy = text.substring(i, i + 16);

            for(int j = 0; j < textCopy.length(); j++) {
                sb.append((char)(textCopy.charAt(j) ^ auxIV.charAt(j)));
            }

            encryptedList.add(sb.toString());
        }

        auxIV = iv;
        for(String s : encryptedList) {
            auxIV = AES.encrypt(auxIV, key1);
            StringBuilder sb = new StringBuilder();

            for(int j = 0; j < s.length() && j < auxIV.length(); j++) {
                sb.append((char)(s.charAt(j) ^ auxIV.charAt(j)));
            }
            decryptedText.append(sb.toString());
        }

        System.out.println("Textul decriptat este: " + decryptedText);


    }

    /*public static String generateString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < 16; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }*/
}
