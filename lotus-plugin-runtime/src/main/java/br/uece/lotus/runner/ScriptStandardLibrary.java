package br.uece.lotus.runner;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by emerson on 16/06/15.
 */
public class ScriptStandardLibrary {
    
    private final TextArea mTxtOutput;
    private final TextField mTxtInput;
    private List<String> mInputs = new ArrayList<>();
    private final Semaphore mDigitou = new Semaphore(0);
    private final Semaphore mMutex = new Semaphore(1);

    public ScriptStandardLibrary(TextArea txtOutput, TextField txtInput) {
        mTxtOutput = txtOutput;
        mTxtInput = txtInput;
        mTxtInput.setOnAction((ActionEvent e) -> {
            try {
                mMutex.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            mInputs.add(mTxtInput.getText());
            mMutex.release();
            mDigitou.release();
            print(mTxtInput.getText());
            mTxtInput.clear();
        });
    }

    public void print(String s) {
        Platform.runLater(() -> {
            mTxtOutput.appendText(s);
            mTxtOutput.appendText("\n");
        });
    }

    public Object read() {
        Platform.runLater(() -> {
            mTxtInput.requestFocus();
        });
        try {
            mDigitou.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            mMutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String s = mInputs.remove(0);
        mMutex.release();
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            //ignora
        }
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            //ignora
        }
        return s;
    }

    public void inicializar(ScriptEngine se) {
        try {
            se.eval("function read() { return std.read(); }");
            se.eval("function print(s) { std.print(s); }");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
