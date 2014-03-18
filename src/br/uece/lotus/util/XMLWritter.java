
package br.uece.lotus.util;

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
    private List<String> mStack = new ArrayList<>();

    public XMLWritter(OutputStream stream) {
        mOut = new PrintStream(stream);
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
            mOut.print(value);
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
}
