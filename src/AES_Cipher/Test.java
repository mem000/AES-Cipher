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
 * @author Mahmoud
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        String s = "47";
        int x = Integer.parseInt("02", 16);
        int y = Integer.parseInt("0b", 16);
//       String bin = stringToBinary("h49d");
//       System.out.println("bin = " + bin);
//       String hexa = Integer.toHexString(Integer.parseInt(bin, 2));
//       System.out.println(hexa);
//       String bin2 = Integer.toBinaryString(Integer.parseInt(hexa, 16));
//       System.out.println("bin2 = " + bin2.length());
//       while (bin2.length() % 8 != 0) bin2 = "0" + bin2;
//       System.out.println("bin2 = " + bin2);
//       String myString = BinaryToString(bin2);
//       System.out.println("myString = " + myString);
//       System.out.println("x = " + x);
//        System.out.println("y = " + y);
//      String sx = Integer.toBinaryString(x);
//      String sy = Integer.toBinaryString(y);
//      while (sx.length()  != 8) sx = "0" + sx; 
//        System.out.println("x = " + sx);
//        System.out.println("y = " + sy);
//        
//         x = Integer.parseInt(sx, 2);
//         y = Integer.parseInt(sy, 2);
        //  System.out.println(Integer.toHexString(x ^ y));
        //  System.out.println(product_in_GF("0e", "01"));
        //System.out.println("0e = " + Integer.parseInt("0e", 16));
//         System.out.println(Integer.toHexString(Integer.parseInt("00011001", 2) ^ 
//                                           Integer.parseInt("00011011", 2)));

        //String sb = Integer.toBinaryString(x);
        //System.out.println(sb);
//        String re = Integer.toHexString(x ^ y);
//                System.out.println(re);
//           byte[] b = re.getBytes();
//           
//           System.out.println(b[0]);
//        byte[] by = sb.getBytes();
//        for (int i = 0; i < by.length; i++) {
//            System.out.print(by[i] + ", ");
//        }
//        System.out.println();
        //Scanner input = new Scanner(new File("Plaintext_33.txt"));
        //PrintWriter output = new PrintWriter("Plaintext_44.txt");
        FileInputStream fInput = new FileInputStream("Plaintext_44.txt");
        //FileOutputStream fOutput = new FileOutputStream("Plaintext_44.txt");

        // while(fInput.read() != -1) {
        // for (int i = 0; i < 3; i++) {
        // int rr = fInput.read();
        //fOutput.write(-1);
        System.out.println(fInput.read());

        //output.println(Integer.toHexString(rr)); 
        //  }   
        // }
        fInput.close();
        // fOutput.close();
//        input.close();
//        output.close();
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
                //if (subR.substring(0, 4) == "0000") result = 
                result = Integer.toHexString(Integer.parseInt(result, 2));
            } else {
                subR = bR.substring(1);
                subR += "0";
                System.out.println("subR.substring(0, 4) = " + subR.substring(0, 4));
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
