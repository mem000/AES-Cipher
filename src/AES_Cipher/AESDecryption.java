/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AES_Cipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Mahmoud Esmat
 */
public class AESDecryption {

    static Scanner input;
    static PrintWriter output;
    static FileInputStream fInput;
    static FileOutputStream fOutput;

    static String[][] Inverse_S_box = new String[16][16];
    static String[][] inv_mix_columns = new String[4][4];
//      public static void main(String[] args) throws FileNotFoundException, IOException {

    public static void AESDecryption_main(File words_file,
            File cipher_file, File decipher_file) throws FileNotFoundException, IOException {

        //KeyExpansion.KeyExpansion(key_file, words_file);
//        File words_file = new File("resources/Words.txt");
        //Word[] state = new Word[4];
//        output = new PrintWriter(deciphertext_file);
        int[] result = new int[16];

        int file_length = 0;
        input = new Scanner(new File("resources/Counter.txt"));
        file_length -= input.nextInt();

        fInput = new FileInputStream(cipher_file);

        while (fInput.available() != 0) {
            fInput.read();
            file_length++;
        }
        fInput.close();

        fOutput = new FileOutputStream(decipher_file);
        fInput = new FileInputStream(cipher_file);
        String[] s = new String[16];
        int counter = 0;
        while (fInput.available() != 0) {
            int readed = fInput.read();
//            if (readed == 254) {
//                output.println();
//                continue;
//            }
//               
            String hexa = Integer.toHexString(readed);
            if (hexa.length() == 1) {
                hexa = "0" + hexa;
            }
            //System.out.print(hexa + "  ");
            s[counter % s.length] = hexa;
            counter++;
            if (counter % 16 == 0) {

                Word[] state = AESDecryption(words_file, s);

                file_length -= 16;

                for (int k = 0; k < 4; k++) {
                    for (int j = 0; j < 4; j++) {
                        //fOutput.write(Integer.parseInt(state[k].bytes[j], 16));
                        result[k * 4 + j] = Integer.parseInt(state[k].bytes[j], 16);
//                        String bin = Integer.toBinaryString(Integer.parseInt(state[k].bytes[j], 16));
//                        while (bin.length() % 8 != 0) bin = "0" + bin;
                        //output.print(BinaryToString(bin));
                        //System.out.print(state[j].bytes[k]+ "  ");
                    }
                    //System.out.println();
                }
                int index = 16;
                index = (file_length < 0) ? (16 + file_length) : 16;
                for (int i = 0; i < index; i++) {
                    fOutput.write(result[i]);
                }
            }
        }

//        fOutput.close();
//        fInput.close();
//        System.out.println(file_length);
//        fOutput = new FileOutputStream("yahoo_3.jpg");
//        fInput = new FileInputStream("yahoo_3.jpg");
//        for (int i = 0; i < 896; i++) {
//            fOutput.write(fInput.read());
//        
//        }
        fOutput.close();
        fInput.close();

    }

    public static Word[] AESDecryption(File words_file, String[] s) throws FileNotFoundException {
        input = new Scanner(new File("resources/Inverse_S-box.txt"));
        while (input.hasNext()) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    Inverse_S_box[i][j] = input.next();
                }
            }
        }
        input.close();

        input = new Scanner(new File("resources/inv_mix_columns.txt"));
        while (input.hasNext()) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    inv_mix_columns[i][j] = input.next();
                }
            }
        }
        input.close();

        Word[] w = new Word[44];
        Word[] state = new Word[4];
        input = new Scanner(words_file);
        input = new Scanner(words_file);
        while (input.hasNext()) {
            for (int i = 0; i < 44; i++) {
                w[i] = new Word(input.next(), input.next(), input.next(), input.next());
            }
        }
        input.close();

        Word[] round_key = new Word[4];
        for (int i = 0; i < 4; i++) {
            round_key[i] = w[i + 40];
        }

        for (int i = 0; i < 4; i++) {
            state[i] = new Word(s[i * 4], s[i * 4 + 1],
                    s[i * 4 + 2], s[i * 4 + 3]);
        }

        state = add_round_key(state, round_key);

        for (int i = 1; i <= 9; i++) {
            state = inv_shift_rows(state);
            state = inv_sub_bytes(state);

            for (int j = 0; j < 4; j++) {
                round_key[j] = w[j + 4 * (10 - i)];
            }
            state = add_round_key(state, round_key);

            state = inv_mix_columns(state);

        }

        state = inv_shift_rows(state);
        state = inv_sub_bytes(state);

        for (int i = 0; i < 4; i++) {
            round_key[i] = w[i];
        }
        state = add_round_key(state, round_key);

        return state;
    }

    public static Word[] add_round_key(Word[] state, Word[] round_key) {
        Word[] new_state = new Word[4];
        for (int i = 0; i < 4; i++) {
            new_state[i] = new Word();
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int x = Integer.parseInt(state[i].bytes[j], 16);
                int y = Integer.parseInt(round_key[i].bytes[j], 16);
                if (state[i].bytes[j].charAt(0) == round_key[i].bytes[j].charAt(0)) {
                    new_state[i].bytes[j] = "0";
                    new_state[i].bytes[j] += Integer.toHexString(x ^ y);
                } else {
                    new_state[i].bytes[j] = Integer.toHexString(x ^ y);
                }
            }
        }

        return new_state;
    }

    public static Word[] inv_sub_bytes(Word[] state) {
        Word[] new_state = new Word[4];
        for (int i = 0; i < 4; i++) {
            new_state[i] = new Word();
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int x = Integer.parseInt(state[i].bytes[j].charAt(0) + "", 16);
                int y = Integer.parseInt(state[i].bytes[j].charAt(1) + "", 16);
                new_state[i].bytes[j] = Inverse_S_box[x][y];
            }
        }
        return new_state;
    }

    public static Word[] inv_shift_rows(Word[] state) {
        Word[] new_state = new Word[4];
        for (int i = 0; i < 4; i++) {
            new_state[i] = new Word();
        }

        new_state[0].bytes[0] = state[0].bytes[0];
        new_state[1].bytes[0] = state[1].bytes[0];
        new_state[2].bytes[0] = state[2].bytes[0];
        new_state[3].bytes[0] = state[3].bytes[0];

        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                index = i + j;
                if (index > 3) {
                    index -= 4;
                }
                new_state[index].bytes[j] = state[i].bytes[j];
            }
        }
        return new_state;
    }

    public static Word[] inv_mix_columns(Word[] state) {
        Word[] new_state = new Word[4];
        for (int i = 0; i < 4; i++) {
            new_state[i] = new Word();
        }
        String temp = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                new_state[j].bytes[i] = "00";
                for (int k = 0; k < 4; k++) {
                    temp = product_in_GF(inv_mix_columns[i][k], state[j].bytes[k]);
                    int x = Integer.parseInt(new_state[j].bytes[i], 16);
                    int y = Integer.parseInt(temp, 16);
                    if (new_state[j].bytes[i].charAt(0) == temp.charAt(0)) {
                        new_state[j].bytes[i] = "0";
                        new_state[j].bytes[i] += Integer.toHexString(x ^ y);
                    } else {
                        new_state[j].bytes[i] = Integer.toHexString(x ^ y);
                    }
                }
            }
        }

        return new_state;
    }

    public static String product_in_GF(String l, String r) {
//        if (Integer.parseInt(l, 16) > Integer.parseInt(r, 16))
//            product_in_GF(r, l);

        String bR = "";
        String r2 = "";
        String result = r;
        String subR = "";

        int x = Integer.parseInt(l, 16);

        if (x == 1) {
            return result;
        } else if (x % 2 == 0) {
            r2 = product_in_GF(Integer.toHexString(x / 2), r);
            int y = Integer.parseInt(r2, 16);
            bR = Integer.toBinaryString(y);
            while (bR.length() != 8) {
                bR = "0" + bR;
            }
            if (bR.charAt(0) == '0') {
                result = bR.substring(1);
                result += "0";
                result = Integer.toHexString(Integer.parseInt(result, 2));
            } else {
                subR = bR.substring(1);
                subR += "0";
                if (subR.substring(0, 4) == "0001") {
                    result = "0";
                    result += Integer.toHexString(Integer.parseInt(subR, 2)
                            ^ Integer.parseInt("00011011", 2));
                } else {
                    result = Integer.toHexString(Integer.parseInt(subR, 2)
                            ^ Integer.parseInt("00011011", 2));
                }
            }
            if (result.length() == 1) {
                result = "0" + result;
            }
            return result;

        } else if (x % 2 != 0) {
            r2 = product_in_GF(Integer.toHexString(x - 1), r);
            if (r2.charAt(0) == r.charAt(0)) {
                result = "0";
                result += Integer.toHexString(Integer.parseInt(r2, 16)
                        ^ Integer.parseInt(r, 16));
            } else {
                result = Integer.toHexString(Integer.parseInt(r2, 16)
                        ^ Integer.parseInt(r, 16));
            }
        }
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }

    public static String stringToBinary(String in) {
        String s = "00000000";
        String output = "";
        for (int i = 0; i < in.length(); i++) {
            String s1 = Integer.toBinaryString((int) (in.charAt(i)));
            s1 = s.subSequence(0, s.length() - s1.length()) + s1;
            output += s1;
        }
        return output;
    }

    public static String BinaryToString(String in) {
        String out = "";
        int count = 1;
        String temp = in.charAt(0) + "";
        for (int i = 0; i <= in.length(); i++) {
            if (i % 8 == 0) {
                for (int j = count; j < i; j++) {
                    temp += in.charAt(j);
                }
                byte n = (byte) (Integer.parseInt(temp, 2));
                if (n != 0) {
                    char c = (char) n;
                    out += c;
                    count = i;
                    temp = "";
                }
            }
        }
        return out;
    }

}
