/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package br.uece.lotus.project;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class XMLWritter {

    private static final int INICIO = 0;
    private static final int TAG_STARTED = 1;
    private static final int FIM = 2;
    private final PrintStream mOut;
    private int mEstado;
    private int mNivel;
    private final List<String> mStack = new ArrayList<>();

    public XMLWritter(OutputStream stream) {
        mOut = new PrintStream(stream);
        mOut.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    }

    private void ident(int n) {
        for (int i = 0; i < n; i++) {
            mOut.print("\t");
        }
    }

    //Adiciona tag
    public void begin(String tagName) {
        if (mEstado == INICIO || mEstado == TAG_STARTED) {
            if (mStack.size() > 0) {
                mOut.print(">\n");
            }
            ident(mNivel++);
            mOut.print("<");
            mOut.print(tagName);
            mStack.add(0, tagName);
            mEstado = TAG_STARTED;
        } else {
            throw new IllegalStateException();
        }
    }

    public void attr(String name, Object value) {
        if (mEstado == TAG_STARTED) {
            mOut.print(" ");
            mOut.print(name);
            mOut.print("=\"");
            mOut.print(text(value.toString()));
            mOut.print("\"");
        } else {
            throw new IllegalStateException();
        }
    }

    public void end() {
        if (mEstado == TAG_STARTED) {
            mOut.print(">\n");
            ident(--mNivel);
            mOut.print("</");
            mOut.print(mStack.remove(0));
            if (mStack.isEmpty()) {
                mOut.print(">");
                mEstado = FIM;
            } else {
                mEstado = TAG_STARTED;
            }
        }
    }

    private String text(String value) {
        value = value.replaceAll("&", "&amp");
        value = value.replaceAll("\"", "&quot;");
        value = value.replaceAll("'", "&apos;");
        value = value.replaceAll("<", "&lt;");
        value = value.replaceAll(">", "&gt;");
        return value;
    }
}
