/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author emerson
 */
public class SimpleScanner {

    private int mLine;
    private String mBuffer;
    private final BufferedReader mSource;
    private int mCurrentBufferIndex;

    public SimpleScanner(Reader r) throws IOException {
        mSource = new BufferedReader(r);        
        proximaLinha();
    }

    public boolean has(Pattern p) {        
        if (mBuffer == null) {              
            return false;
        }
        Matcher m = p.matcher(mBuffer);
        boolean r = m.find(mCurrentBufferIndex);        
        return r;
    }

    public String next(Pattern p) throws IOException {
        if (mBuffer == null) {            
            throw new InputMismatchException("unexpected end of file");
        }
        Matcher m = p.matcher(mBuffer);        
        if (m.find(mCurrentBufferIndex)) {
            mCurrentBufferIndex = m.end();            
            if (mBuffer.length() == mCurrentBufferIndex) {
                proximaLinha();
            }
            String aux = m.group();  
//            System.out.println("next " + p + " = " + aux);
//            print();
            return aux;
        }
        print();
        throw new InputMismatchException("expected " + p + " but " + mBuffer.substring(mCurrentBufferIndex) + " was founded at line " + mLine);
    }

    private void proximaLinha() throws IOException {
        mCurrentBufferIndex = 0;
        while (true) {
            mBuffer = mSource.readLine();
            mLine++;            
//            System.out.println("proximaLinha(): " + mBuffer);
            if (mBuffer == null) {
                break;
            }
            mBuffer = mBuffer.trim();
            if (!mBuffer.isEmpty()) {
                mBuffer = mBuffer.replace('\t', ' ');
                break;
            }            
        }
    }

    public void print() {
        System.out.println(mBuffer);
        for (int i = 0; i < mCurrentBufferIndex; i++) {
            System.out.print(" ");
        }
        System.out.println("^");
    }
    
    
}
