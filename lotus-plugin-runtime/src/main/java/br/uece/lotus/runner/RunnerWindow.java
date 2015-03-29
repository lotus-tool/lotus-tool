package br.uece.lotus.runner;

import br.uece.seed.app.UserInterface;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by emerson on 29/03/15.
 */
public class RunnerWindow extends Writer {

    private final String mTitle;
    private final Runner mRunner;
    private StringBuilder mStringBuilder;
    private TextArea mTextArea;

    public RunnerWindow(Runner r) {
        mTextArea = new TextArea();
        mStringBuilder = new StringBuilder();
        mTitle = "Output " + r.getComponent().getName();
        r.getEngine().getContext().setWriter(this);
        mRunner = r;
    }

    public void show(UserInterface userInterface) throws ScriptException {
        userInterface.getBottomPanel().newTab(mTitle, getNode(), false);
        mRunner.start();
    }

    public Node getNode() {
        return mTextArea;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        mStringBuilder.append(cbuf, off, len);
        mTextArea.setText(mStringBuilder.toString());
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
