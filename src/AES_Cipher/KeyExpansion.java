/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AES_Cipher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Mahmoud
 */
public class KeyExpansion {

    static Scanner input;
    static PrintWriter output;

    static String[][] S_box = new String[16][16];

    //public static void main(String[] args) throws FileNotFoundException {
    public static void KeyExpansion(File key_file, File words_file) throws FileNotFoundException {

        input = new Scanner(new File("resources/S-box.txt"));
        while (input.hasNext()) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    S_box[i][j] = input.next();
                }
            }
        }
        input.close();

        Word[] w = new Word[44];
        for (int i = 0; i < 44; i++) {
            w[i] = new Word();
        }
        Word temp;

        input = new Scanner(key_file);
        String k = input.nextLine();
        input.close();

        String k_hexa = "";
        for (int i = 0; i < k.length(); i += 2) {
            String bin = stringToBinary(k.substring(i, i + 2));
            k_hexa += Integer.toHexString(Integer.parseInt(bin, 2));

        }

        String[] key = new String[16];

        for (int i = 0; i < k_hexa.length(); i += 2) {
            key[i / 2] = k_hexa.substring(i, i + 2);
            System.out.println(key[i / 2]);

        }

        for (int i = 0; i < 4; i++) {
            w[i] = new Word(key[4 * i], key[4 * i + 1],
                    key[4 * i + 2], key[4 * i + 3]);
        }

        for (int i = 4; i < 44; i++) {
            temp = w[i - 1];

            if (i % 4 == 0) {
                temp = SubWord(RotWord(temp));
                Word Rcon = Rcon(i / 4);

                for (int j = 0; j < 4; j++) {
                    int x = Integer.parseInt(temp.bytes[j], 16);
                    int y = Integer.parseInt(Rcon.bytes[j], 16);
                    if (temp.bytes[j].charAt(0) == Rcon.bytes[j].charAt(0)) {
                        temp.bytes[j] = "0";
                        temp.bytes[j] += Integer.toHexString(x ^ y);

                    } else {
                        temp.bytes[j] = Integer.toHexString(x ^ y);
                    }
                }
            }

            for (int j = 0; j < 4; j++) {
                int x = Integer.parseInt(w[i - 4].bytes[j], 16);
                int y = Integer.parseInt(temp.bytes[j], 16);
                if (w[i - 4].bytes[j].charAt(0) == temp.bytes[j].charAt(0)) {
                    w[i].bytes[j] = "0";
                    w[i].bytes[j] += Integer.toHexString(x ^ y);
                } else {
                    w[i].bytes[j] = Integer.toHexString(x ^ y);
                }
            }
        }
        output = new PrintWriter(words_file);
        for (int i = 0; i < 44; i++) {
            for (int j = 0; j < 4; j++) {
                output.print(w[i].bytes[j] + "  ");
                System.out.print(w[i].bytes[j] + "  ");
            }
            output.println();
            System.out.println();
        }
        output.close();

    }

    public static Word RotWord(Word temp) {
        Word w = new Word();

        for (int i = 0; i < 3; i++) {
            w.bytes[i] = temp.bytes[i + 1];
        }
        w.bytes[3] = temp.bytes[0];

        return w;
    }

    public static Word SubWord(Word w) {
        Word new_W = new Word();

        for (int i = 0; i < 4; i++) {
            int x = Integer.parseInt(w.bytes[i].charAt(0) + "", 16);
            int y = Integer.parseInt(w.bytes[i].charAt(1) + "", 16);
            new_W.bytes[i] = S_box[x][y];
        }

        return new_W;
    }

    public static Word Rcon(int j) {
        Word w = new Word();

        w.bytes[1] = w.bytes[2] = w.bytes[3] = "00";

        switch (j) {
            case 1:
                w.bytes[0] = "01";
                break;
            case 2:
                w.bytes[0] = "02";
                break;
            case 3:
                w.bytes[0] = "04";
                break;
            case 4:
                w.bytes[0] = "08";
                break;
            case 5:
                w.bytes[0] = "10";
                break;
            case 6:
                w.bytes[0] = "20";
                break;
            case 7:
                w.bytes[0] = "40";
                break;
            case 8:
                w.bytes[0] = "80";
                break;
            case 9:
                w.bytes[0] = "1B";
                break;
            default:
                w.bytes[0] = "36";
                break;
        }

        return w;
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
}
