package testing;

import com.si.encryption.AES;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] argv) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        List<String> encryptedList = new ArrayList<>();

        StringBuilder decryptedText = new StringBuilder();
        String ivv = generateString();
        String key1 = "nSVB42NVkfzc2Vx0";
        String auxIV = ivv;
        String text = "Acesta este doar un text exemplu. Tema se face pentru materia securitatea informatiei.";


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

        auxIV = ivv;
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

    public static String generateString() {
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
}
