/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AES_Cipher;

/**
 *
 * @author Mahmoud Esmat
 */
public class Word {

    String[] bytes = new String[4];

    public Word(String b0, String b1, String b2, String b3) {
        this.bytes[0] = b0;
        this.bytes[1] = b1;
        this.bytes[2] = b2;
        this.bytes[3] = b3;
    }

    public Word() {
    }

}
