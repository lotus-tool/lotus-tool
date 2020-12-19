package br.uece.lotus.compiler;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.helpers.window.Window;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.viewer.ComponentView;
import br.uece.lotus.viewer.ComponentViewImpl;
import br.uece.seed.app.UserInterface;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Yan Gurgel
 */
public class CompilerWindow extends AnchorPane implements Window, Initializable {

    //private SimulatorContext mSimulatorContext;
    @FXML
    private VBox mVBox;

    @FXML
    private SplitPane mSplitPane;

    @FXML
    private AnchorPane AnchorPane1;

    @FXML
    private TextArea mTextArea;

    @FXML
    private ToolBar mToolbar;

    @FXML
    private Button mBtnCompile;

    @FXML
    private Button mBtnSave;

    @FXML
    private Button mBtnLoad;

    @FXML
    private AnchorPane AnchorPane2;

    @FXML
    private TabPane mTabPane;

    @FXML
    private Tab mTab;

    @FXML
    private AnchorPane AnchorPane3;

    @FXML
    private Label mLabel;

    @FXML
    private Label mNameProject;

    @FXML
    private TextArea consoleTextArea;

    private Node mNode;
    private ComponentView mViewer;
    private UserInterface userInterface;
    private ProjectExplorer projectExplorer;
    private Component mComponent;

    private String texto;
    private String codigo;
    private String processoPrincipal;
    private ArrayList<String> declaracao = new ArrayList<>();
    private ArrayList<String> retornos = new ArrayList<>();
    private ArrayList<String> lts;
    private ArrayList<String> expressoes;
    private ArrayList<String> expressãoAtualSplit;
    private ArrayList<String> tokensDirSplit;
    private String[] maiusculas = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private String[] tokensRestri = {"(", ")", "|", "="};
    private String[] tokensRestri2 = {")", "|", "="};
    private boolean ok;

    @Override
    public String getTitle() {
        return "[Compiler]";
    }

    @Override
    public Node getNode() {
        return mNode;
    }

    public void setNode(Parent node) {
        this.mNode = node;
    }

    @Override
    public Component getComponent() {
        return mViewer.getComponent();
    }

    @Override
    public void setComponent(Component component) {
        mViewer.setComponent(component);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mViewer = new ComponentViewImpl();
        projectExplorer = (ProjectExplorer) resources.getObject("projectMenu");
        userInterface = (UserInterface) resources.getObject("userInterface");

        mBtnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter
                        = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    SaveFile(mTextArea.getText(), file);
                }
            }
        });

        mBtnLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter
                        = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    try {
                        codigo = new String(Files.readAllBytes(file.toPath()));
                    } catch (IOException ex) {
                        Logger.getLogger(CompilerWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    mTextArea.clear();
                    mTextArea.appendText(codigo);
                }
            }
        });

        mBtnCompile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Texto:" + mTextArea.getText());
                mLabel.setText("");
                consoleTextArea.clear();
                ok = true;
                ok = analysis();
                declaracao.clear();
                retornos.clear();
                if (ok == true) {
                    mLabel.setTextFill(Color.BLACK);
                    Component c = projectExplorer.getSelectedComponent();
                    if (c == null) {
                        c = new Component();
                    }
                    Project p = projectExplorer.getSelectedProject();

                    if (p == null) {
                        JOptionPane.showMessageDialog(null, "Select a Project...");
                        System.out.println("projeto NULO!");
                        return;
                    }
                    mNameProject.setText(p.getName());
                    BuildLTS LTS = new BuildLTS(c, lts, p);
                    LTS.build(codigo, lts);
                    System.out.println("Run...");
                    mLabel.setText(" Compiled sucessfully.");

                    //show(c, ok);
                }
            }
        });

    }

    public boolean analysis() {
        System.out.println("\n===================================================================");

        if (mTextArea.getText().equals("") | mTextArea.getText().equals(null)) {
            System.out.println("texto null");
            mLabel.setTextFill(Color.ORANGE);
            mLabel.setText(" Warning - text null:");
            consoleTextArea.appendText("");
            ok = false;
            return ok;
        }

        texto = mTextArea.getText();
        texto = texto.trim();
        while (texto.contains(" ") || texto.contains("\r") || texto.contains("\t") || texto.contains("\n")) {
            texto = texto.replaceAll(" ", "");
            texto = texto.replaceAll("\r", "");
            texto = texto.replaceAll("\t", "");
            texto = texto.replaceAll("\n", "");
        }
        codigo = texto;

        /**
         * *Requisito minimo de um '.' no final**
         */
        if ('.' != texto.charAt(texto.length() - 1)) {
            System.out.println("Error - expected '.'\n");
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" Error - expected:");
            consoleTextArea.appendText(" '.'\n");
            ok = false;

        }
        if (ok == false) {
            return ok;
        }
        System.out.println("\nEntrada: " + texto);
        System.out.println("\nSplit da entrada:\n");
        /**
         * *Tratamento inicial para ponto das probabilidades**
         */
        if (texto.contains("$")) {
            System.out.println("Error - Expressão possui mais de um símbolo '='");
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" Error - Symbol $");
            ok = false;
        }

        for (int i = 0; i < texto.length() - 1; i++) {

            if (texto.charAt(i) == '.' && (texto.charAt(i + 1) == '0'
                    || texto.charAt(i + 1) == '1'
                    || texto.charAt(i + 1) == '2'
                    || texto.charAt(i + 1) == '3'
                    || texto.charAt(i + 1) == '4'
                    || texto.charAt(i + 1) == '5'
                    || texto.charAt(i + 1) == '6'
                    || texto.charAt(i + 1) == '7'
                    || texto.charAt(i + 1) == '8'
                    || texto.charAt(i + 1) == '9')) {
                StringBuilder sb = new StringBuilder(texto);
                sb.setCharAt(i, '$');
                texto = sb.toString();
            }

        }
        /**
         * *Split principal que define o número de LTS's da entrada**
         */
        lts = converteArray(texto.split("[.]"));
        for (int i = 0; i < lts.size(); i++) {
            System.out.println("--- " + lts.get(i));
        }
        /**
         * *Cada LTS será posto em um novo arraylist para verificar suas
         * expressões**
         */
        System.out.println("\n*********Lista de LTS's:*********");
        for (int i = 0; i < lts.size(); i++) {
            texto = lts.get(i);

            /**
             * *PALAVRAS RESERVADAS**
             */
            declaracao.add("ERROR");
            declaracao.add("STOP");
            retornos.add("ERROR");
            retornos.add("STOP");
            retornos.add(definirProcessoPrincipal(lts.get(i)));
            /**
             * ***********************
             */

            /**
             * Tratamento dos pontos da prababilidade (2)*
             */
            for (int g = 0; g < texto.length(); g++) {
                if (texto.charAt(g) == '$') {
                    StringBuilder sb = new StringBuilder(texto);
                    sb.setCharAt(g, '.');
                    texto = sb.toString();
                }
            }

            /*Separa as expreções do LTS atual em posições do arrayList*/
            expressoes = converteArray(texto.split(","));
            System.out.println("\n==== Expressões do LTS: " + (i + 1) + " ====");

            /*Verificação individual de cada expressão de cada LTS*/
            for (int j = 0; j < expressoes.size(); j++) {
                int index = j;
                System.out.println("\nExpressão " + (j + 1) + ": " + expressoes.get(j));
                String expressãoAtual;
                expressãoAtual = expressoes.get(j);
                int count;
                /*Verificação de sinal '=' em cada expressão*/
                ok = contaIgualdade(expressãoAtual);
                if (ok == false) {
                    return ok;
                }

                //Split de =
                expressãoAtualSplit = converteArray(expressãoAtual.split("="));
                System.out.println("Declaração: " + expressãoAtualSplit.get(0));
                System.out.println("Expressão: " + expressãoAtualSplit.get(1));
                if (expressãoAtualSplit.get(0).isEmpty()) {
                    mLabel.setTextFill(Color.RED);
                    mLabel.setText(" Error - Declaration Null:");
                    consoleTextArea.appendText(" =" + expressãoAtualSplit.get(1));
                    ok = false;
                    return ok;
                }

                /*Separa delcaração de expressão para análises*/
                String declara = expressãoAtualSplit.get(0);
                System.out.println("Declaracao: " + declara);
                String express = expressãoAtualSplit.get(1);
                char a = declara.charAt(0);
                String letra = Character.toString(a);

                /*Metodo que não permite declarar reservadas*/
                ok = reservadas(declara, expressãoAtual);
                if (ok == false) {
                    return ok;
                }


                /*Método primeiraLetra verifica se a declaração está ok e armazena ela*/
                ok = primeiraLetra(letra, declara);
                if (ok == false) {
                    return ok;
                }

                /*Expressão passada caractere por caractere para arrayList para análise*/
                tokensDirSplit = converteArray(express.split(""));

                /*Verificação de parenteses da expressão*/
                ok = parenteses(tokensDirSplit, express);
                if (ok == false) {
                    return ok;
                }

                //Verifica minimo de ->
                ok = verificaTransition(tokensDirSplit, express);
                if (ok == false) {
                    return ok;
                }

                //Guarda Retorno
                ok = guardaRetorno(tokensDirSplit, express);
                if (ok == false) {
                    return ok;
                }
                /**
                 * ************************************************
                 */
                ok = verificaProbAll(lts);
                if (ok == false) {
                    return ok;
                }

                /**
                 * ************************************************
                 */
                //Verifica modelo probabilistico
                ok = probabilistico(tokensDirSplit, express);
                if (ok == false) {
                    return ok;
                }

            }
            //Verifica Retornos pelas declarações
            ok = verificaRetorno(retornos, declaracao);
            if (ok == false) {
                return ok;
            }
            tokensDirSplit.clear();
            declaracao.clear();
            retornos.clear();
        }
        return ok;
    }

    /*==========================================================================================*/
    private void SaveFile(String codigo, File file) {
        try {
            System.out.println(codigo);
            //Precisa utilizar replaceAll para escrever as quebras de linha
            String codigoFormatado = codigo.replaceAll("\n", "\r\n");

            PrintWriter printWriter;
            printWriter = new PrintWriter(file);
            printWriter.write(codigoFormatado);
            printWriter.close();

        } catch (IOException ex) {
            Logger.getLogger(CompilerWindow.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String definirProcessoPrincipal(String lts) {
        String processo = "";
        ArrayList expre = new ArrayList();
        processo = lts;
        expre = converteArray(processo.split(","));
        processo = expre.get(0).toString();
        expre.clear();
        expre = converteArray(processo.split("="));
        processo = expre.get(0).toString();
        expre.clear();

        return processo;
    }

    private boolean reservadas(String declara, String expressãoAtual) {
        if (declara.equals("ERROR") || declara.equals("STOP")) {
            System.out.println("Can not declare reserved word");
            mLabel.setTextFill(Color.ORANGE);
            mLabel.setText(" Warning - Can not declare reserved word:");
            consoleTextArea.appendText(expressãoAtual);
            ok = false;
            return ok;
        }

        return ok;
    }

    /*Conta parenteses em relação a prababilidade e conclue se todas as expressoes usam ou não*/
    private boolean verificaProbAll(ArrayList lts) {
        ArrayList numeroProbs = new ArrayList();
        ArrayList ltsVeri = new ArrayList();
        int count = 0;
        for (int i = 0; i < lts.size(); i++) {
            String strLTS = lts.get(i).toString();
            ltsVeri = converteArray(strLTS.split(","));
            for (int j = 0; j < ltsVeri.size(); j++) {
                String express = ltsVeri.get(j).toString();
                System.out.println("Express: " + express);
                //Conta o minimo de parenteses abrindo 
                for (int k = 0; k < express.length(); k++) {
                    if (express.charAt(k) == '(') {
                        count++;
                    }
                }
                numeroProbs.add(count);
                count = 0;
            }

            boolean num = false;
            int numero = 1;
            for (int k = 0; k < numeroProbs.size(); k++) {
                System.out.println("Numeros de probs: " + numeroProbs.get(k));
                numero = Integer.parseInt(numeroProbs.get(k).toString());
                //Se o numero for maior de 2 esta usando prababilidade logo todas as express devem ter no minimo um valor prababilistico
                if (numero >= 2) {
                    num = true;
                }
            }
            if (num == true) {
                for (int j = 0; j < numeroProbs.size(); j++) {
                    numero = Integer.parseInt(numeroProbs.get(j).toString());
                    if (numero < 2) {
                        System.out.println("Erros de prababilidade na expressão");
                        mLabel.setTextFill(Color.RED);
                        mLabel.setText(" Error - Incomplete probability values or ',' in LTS the expression:");
                        consoleTextArea.appendText(ltsVeri.get(j).toString());
                        ok = false;
                        return ok;
                    }
                }

            }
            numeroProbs.clear();
        }
        return ok;
    }

    private boolean probabilistico(ArrayList<String> tokensDirSplit, String express) {
        List le = Arrays.asList(tokensRestri);
        for (int i = 1; i < tokensDirSplit.size(); i++) {
            if (tokensDirSplit.get(i).equals("(")) {
                if (!le.contains(tokensDirSplit.get(i - 1))) {
                    System.out.println("Token ERRADO>>>" + tokensDirSplit.get(i - 1));
                    mLabel.setTextFill(Color.RED);
                    mLabel.setText(" Error - invalid expression:");
                    consoleTextArea.appendText(express);
                    ok = false;
                    return ok;
                }
            }
        }

        if (express.charAt(2) == '.') {
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" Error - Probabilistic operation invalid:");
            consoleTextArea.appendText(express);
            ok = false;
            return ok;
        }
        for (int i = 0; i < express.length() - 1; i++) {
            if (express.charAt(i) == '.') {
                if (express.charAt(i - 2) != '(') {
                    mLabel.setTextFill(Color.RED);
                    mLabel.setText(" Error - Probabilistic operation invalid:");
                    consoleTextArea.appendText(express);
                    ok = false;
                    return ok;
                }

            }
        }

        double total = 0;
        System.out.println("Express: " + express);
        for (int i = 1; i < tokensDirSplit.size() - 1; i++) {
            String valor = "";
            if (tokensDirSplit.get(i).equals("(")) {
                int idParAbrindo = i;
                for (int j = idParAbrindo; j < tokensDirSplit.size() - 1; j++) {
                    if (tokensDirSplit.get(j).equals(")")) {
                        int idParFechando = j;
                        for (int k = idParAbrindo + 1; k < idParFechando; k++) {
                            //System.out.println("tokens:" + tokensDirSplit.get(k));
                            valor = valor.concat(tokensDirSplit.get(k));

                        }
                        j = tokensDirSplit.size();
                    }

                }
                System.out.println("Número:" + valor);
                //verifica se o valor no parenteses contém apenas números
                try {
                    Double valorDouble = Double.parseDouble(valor);
                } catch (NumberFormatException vNumberFormatException) {
                    mLabel.setTextFill(Color.RED);
                    mLabel.setText(" Error - Probabilistic operation invalid:");
                    consoleTextArea.appendText(valor);
                    ok = false;
                    return ok;
                }

            }
        }
        /**
         * *PROBABILISTICO COM OU **
         */
        String express2 = "";
        int parentesesA = 0;
        int parentesesF = 0;
        int parentAbrinIndex = 0;
        int parentFechIndex = 0;
        String valor = "";
        if (express.contains("|")) {
            for (int i = 1; i < express.length() - 1; i++) {
                char a = express.charAt(i);
                express2 = express2.concat(Character.toString(a));
            }
            express = express2;
            System.out.println("EXPRESSAO SEM PARENTESE 1 >>>>" + express);
            ArrayList ou = converteArray(express.split("[|]"));
            for (int i = 0; i < ou.size(); i++) {
                String ouExpress = ou.get(i).toString();
                ArrayList ouSeta = converteArray(ouExpress.split("->"));
                for (int j = 0; j < ouSeta.size() - 1; j++) {
                    String b = ouSeta.get(j).toString();
                    System.out.println("String>>>>: " + b);
                    /*Verificar parenteses abrindo*/
                    for (int k = 0; k < b.length(); k++) {
                        if (b.charAt(k) == '(') {
                            parentesesA++;
                            parentAbrinIndex = k;

                        }
                        if (b.charAt(k) == ')') {
                            parentesesF++;
                            parentFechIndex = k;

                        }
                    }
                    if (parentesesA > 1) {
                        System.out.println("Erro Probabilistico");
                        mLabel.setTextFill(Color.RED);
                        mLabel.setText(" Error - Probability error '(':");
                        consoleTextArea.appendText(express);
                        ok = false;
                        return ok;

                    }
                    if (parentesesF > 1) {
                        System.out.println("Erro Probabilistico");
                        mLabel.setTextFill(Color.RED);
                        mLabel.setText(" Error - Probability error ')':");
                        consoleTextArea.appendText(express);
                        ok = false;
                        return ok;

                    }
                    if (parentAbrinIndex == 0 && parentFechIndex == 0) {
                        valor = "0.0";
                    } else {
                        for (int p = parentAbrinIndex + 1; p < parentFechIndex; p++) {
                            char a = b.charAt(p);
                            valor = valor.concat(Character.toString(a));
                        }
                    }

                    total = total + Double.parseDouble(valor);

                    valor = "";
                    parentesesA = 0;
                    parentesesF = 0;
                    parentAbrinIndex = 0;
                    parentFechIndex = 0;
                }
            }

            total = total * 100;
            System.out.println("TOTAL: " + total + "%");
            if (total >= 99.99 && total < 100) {
                total = 100;
            }
            // soma: 100% ou 0 caso não seja probabilistico
            if (total == 100 || total == 0) {
                System.out.println("Prob OK");
            } else {
                System.out.println("Erro Probabilistico");
                mLabel.setTextFill(Color.RED);
                mLabel.setText(" Error - Probability error:");
                consoleTextArea.appendText(express + "\nTotal: " + total + "%");
                ok = false;
                return ok;
            }

        } /**
         * **PROBABILISTICO SEM OU
         */
        else {
            for (int i = 1; i < express.length() - 1; i++) {
                char a = express.charAt(i);
                express2 = express2.concat(Character.toString(a));
            }
            express = express2;
            ArrayList seta = converteArray(express.split("->"));
            for (int j = 0; j < seta.size() - 1; j++) {
                String b = seta.get(j).toString();
                System.out.println("String>>>>: " + b);
                /*Verificar parenteses abrindo*/
                for (int k = 0; k < b.length(); k++) {
                    if (b.charAt(k) == '(') {
                        parentesesA++;
                        parentAbrinIndex = k;

                    }
                    if (b.charAt(k) == ')') {
                        parentesesF++;
                        parentFechIndex = k;

                    }
                }
                if (parentesesA > 1) {
                    System.out.println("Erro Probabilistico");
                    mLabel.setTextFill(Color.RED);
                    mLabel.setText(" Error - Probability error '(':");
                    consoleTextArea.appendText(express);
                    ok = false;
                    return ok;

                }
                if (parentesesF > 1) {
                    System.out.println("Erro Probabilistico");
                    mLabel.setTextFill(Color.RED);
                    mLabel.setText(" Error - Probability error ')':");
                    consoleTextArea.appendText(express);
                    ok = false;
                    return ok;

                }
                if (parentAbrinIndex == 0 && parentFechIndex == 0) {
                    valor = "0.0";
                } else {
                    for (int p = parentAbrinIndex + 1; p < parentFechIndex; p++) {
                        char a = b.charAt(p);
                        valor = valor.concat(Character.toString(a));
                    }
                }

                total = total + Double.parseDouble(valor);

                valor = "";
                parentesesA = 0;
                parentesesF = 0;
                parentAbrinIndex = 0;
                parentFechIndex = 0;
            }

            total = total * 100;
            System.out.println("TOTAL: " + total + "%");

            if (total >= 99.99 && total < 100) {
                total = 100;
            }
            // soma: 100% ou 0 caso não seja probabilistico
            if (total == 100 || total == 0) {
                System.out.println("Prob OK");
            } else {
                System.out.println("Erro Probabilistico");
                mLabel.setTextFill(Color.RED);
                mLabel.setText(" Error - Probability error:");
                consoleTextArea.appendText(express + "\nTotal: " + total + "%");
                ok = false;
                return ok;
            }
        }
        return ok;
    }

    private boolean verificaRetorno(ArrayList retornos, ArrayList declaracao) {

        //Retorno não declarado
        for (int i = 0; i < retornos.size(); i++) {
            String ret = retornos.get(i).toString();
            if (!(declaracao.contains(ret))) {
                System.out.println(retornos.get(i));
                System.out.println("\nWarning undeclared return:");
                mLabel.setTextFill(Color.ORANGE);
                mLabel.setText(" Warning - Undeclared return:");
                consoleTextArea.appendText(retornos.get(i).toString());
                ok = false;
                return ok;
            }
        }
        //Declaração sem retorno
        for (int i = 0; i < declaracao.size(); i++) {
            String decla = declaracao.get(i).toString();
            if (!(retornos.contains(decla))) {

                System.out.println(declaracao.get(i));
                System.out.println("\nWarning Declaration without return:");
                mLabel.setTextFill(Color.ORANGE);
                mLabel.setText(" Warning - Declaration without return:");
                consoleTextArea.appendText(declaracao.get(i).toString());
                ok = false;
                return ok;
            }
        }

        return ok;
    }

    private boolean guardaRetorno(ArrayList<String> tokensDirSplit, String express) {
        String retorno = "";
        String ou = "|";
        String trans = ">";
        ArrayList<String> strOu = new ArrayList<>();
        ArrayList<String> strTrans = new ArrayList<>();
        if (tokensDirSplit.contains(ou)) {
            String s = express;
            s = s.replace("(", "");
            s = s.replace(")", "");
            strOu = converteArray(s.split("[|]"));
            for (int i = 0; i < strOu.size(); i++) {
                String seta = strOu.get(i);
                strTrans = converteArray(seta.split("->"));
                retorno = strTrans.get(strTrans.size() - 1);
                retornos.add(retorno);
                strTrans.clear();
            }
        } else {
            int parents = tokensDirSplit.size() - 1;
            for (int i = parents; i >= 1; i--) {
                if (tokensDirSplit.get(i).equals(trans)) {
                    int idTrans = i;
                    for (int j = idTrans + 1; j < parents; j++) {
                        retorno = retorno.concat(tokensDirSplit.get(j));
                    }
                    retornos.add(retorno);
                    i = 0;
                }
            }
        }
        return ok;
    }

    private boolean verificaTransition(ArrayList<String> tokensDirSplit, String express) {
        List le = Arrays.asList(tokensRestri2);
        for (int i = 1; i < tokensDirSplit.size() - 1; i++) {
            if (tokensDirSplit.get(i).equals(">")) {
                if (le.contains(tokensDirSplit.get(i + 1))) {
                    System.out.println("Token ERRADO>>>" + tokensDirSplit.get(i - 1));
                    mLabel.setTextFill(Color.RED);
                    mLabel.setText(" ERROR - invalid expression:");
                    consoleTextArea.appendText(express);
                    ok = false;
                    return ok;
                }
            }
        }

        if (tokensDirSplit.get(1).equals("-") || tokensDirSplit.get(1).equals(">")) {
            System.out.println("\nError - Identifier, label set or range expected: " + express);
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" Error - Identifier, label set or range expected:");
            consoleTextArea.appendText(express);
            ok = false;
            return ok;

        }
        if (tokensDirSplit.get(tokensDirSplit.size() - 2).equals("-") || tokensDirSplit.get(tokensDirSplit.size() - 2).equals(">")) {
            System.out.println("\nError - can't finish '->': " + express);
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" Error - expected return:");
            consoleTextArea.appendText(express);
            ok = false;
            return ok;
        }
        int count = 0;
        for (int i = 0; i < tokensDirSplit.size() - 1; i++) {
            if (tokensDirSplit.get(i).equals("-") && tokensDirSplit.get(i + 1).equals(">")) {
                count++;
            }
        }
        if (count == 0) {
            System.out.println("\nError - expectetd least one '->': " + express);
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" Error - expectetd least one '->':");
            consoleTextArea.appendText(express);
            ok = false;
            return ok;
        }

        for (int i = 0; i < tokensDirSplit.size() - 1; i++) {
            if (tokensDirSplit.get(i).equals("-") && !(tokensDirSplit.get(i + 1).equals(">"))) {
                System.out.println("\nError - expectetd '->': " + express);
                mLabel.setTextFill(Color.RED);
                mLabel.setText(" Error - expectetd '->':");
                consoleTextArea.appendText(express);
                ok = false;
                return ok;
            }
            if (tokensDirSplit.get(i).equals(">") && (tokensDirSplit.get(i + 1).equals("-") || tokensDirSplit.get(i + 1).equals(">"))) {
                System.out.println("\nError - expectetd '->': " + express);
                mLabel.setTextFill(Color.RED);
                mLabel.setText(" Error - expectetd '->':");
                consoleTextArea.appendText(express);
                ok = false;
                return ok;
            }
            if (tokensDirSplit.get(i).equals(">") && !(tokensDirSplit.get(i - 1).equals("-"))) {
                System.out.println("\nError - expectetd '->': " + express);
                mLabel.setTextFill(Color.RED);
                mLabel.setText(" Error - expectetd '->':");
                consoleTextArea.appendText(express);
                ok = false;
                return ok;
            }

        }

        return ok;
    }

    //Método que verifica se primeira letra da declaração é maiúscula
    private boolean primeiraLetra(String letra, String declara) {
        // boolean ok = true;
        List le = Arrays.asList(maiusculas);
        if (!(le.contains(letra))) {
            System.out.println("\nError - Accurate declaration of the first capital letter: " + declara);
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" Error - Accurate declaration of the first capital letter:");
            consoleTextArea.appendText(declara);
            ok = false;
        } else {
            declaracao.add(declara);
        }
        return ok;
    }

    //Método que verifica o número de parenteses na expressão
    private boolean parenteses(ArrayList<String> tokensDirSplit, String express) {
        for (int i = 0; i < tokensDirSplit.size(); i++) {
            if (tokensDirSplit.get(i).contains("(") && tokensDirSplit.get(i + 1).contains(")")) {
                System.out.println("ERROR: - '('')' null " + express);
                mLabel.setTextFill(Color.RED);
                mLabel.setText(" ERROR: - '('')' null:");
                consoleTextArea.appendText(express);
                ok = false;
                return ok;
            }
        }
        if (!tokensDirSplit.get(0).contains("(")) {
            System.out.println("ERROR: - '(' initial: " + express);
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" ERROR: - expected '(' initial:");
            consoleTextArea.appendText(express);
            ok = false;
            return ok;
        }
        if (!tokensDirSplit.get(tokensDirSplit.size() - 1).contains(")")) {
            System.out.println("ERROR: - ')' final: " + express);
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" ERROR: - expected ')' final:");
            consoleTextArea.appendText(express);
            ok = false;
            return ok;
        }
        int countParentesesA = 0;
        int countParentesesF = 0;
        for (int i = 0; i < tokensDirSplit.size(); i++) {
            if (tokensDirSplit.get(i).contains("(")) {
                countParentesesA++;
            }
            if (tokensDirSplit.get(i).contains(")")) {
                countParentesesF++;
            }
        }
        if (countParentesesA == countParentesesF) {
            if (countParentesesA == 0 && countParentesesF == 0) {
                System.out.println("ERROR: - '(' ')' expected:" + express);
                mLabel.setTextFill(Color.RED);
                mLabel.setText(" ERROR: - expected '(' ')' expected:");
                consoleTextArea.appendText(express);
                ok = false;
                return ok;
            } else {
                System.out.println("Parenteses: OK");
            }

        } else {
            if (countParentesesA > countParentesesF) {
                System.out.println("ERROR: - ')' expected: " + express);
                mLabel.setTextFill(Color.RED);
                mLabel.setText(" ERROR: - ')' expected:");
                consoleTextArea.appendText(express);
                ok = false;
                return ok;
            } else if (countParentesesA < countParentesesF) {
                System.out.println("ERROR: - '(' expected:" + express);
                mLabel.setTextFill(Color.RED);
                mLabel.setText(" ERROR: - '(' expected:");
                consoleTextArea.appendText(express);
                ok = false;
                return ok;
            }
        }
        return ok;
    }

    //Método converte Vetor String em Arraylist    
    private ArrayList<String> converteArray(String[] vetor) {
        ArrayList<String> Tk = new ArrayList<>();
        for (int i = 0; i < vetor.length; i++) {
            Tk.add(vetor[i]);
        }
        return Tk;
    }

    //Método que verifica o número de '=' em cada expressão
    public boolean contaIgualdade(String t) {
        int count = 0;
        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i) == '=') {
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Error - Expressão não possui='");
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" ERROR: - expression need one sign of '=':");
            consoleTextArea.appendText(t);
            ok = false;
        } else if (count > 1) {
            System.out.println("Error - expression has more than one sign of '='");
            mLabel.setTextFill(Color.RED);
            mLabel.setText(" ERROR: - expression has more than one sign of '=':");
            consoleTextArea.appendText(t);
            ok = false;
        } else {
            System.out.println("\nNúmero de igualdades '=': ok");
        }
        return ok;
    }

    // Chama a janela do LTS compilado.
    public void show(Component c, boolean editable) {
        URL location;
        location = getClass().getResource("/fxml/LTSaba.fxml");
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setClassLoader(getClass().getClassLoader());
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) loader.load(location.openStream());
            int id = userInterface.getCenterPanel().newTab(c.getName() + " - [Compiler]", root,5,true);
            userInterface.getCenterPanel().showTab(id);
        } catch (IOException e) {
        }
    }
 

}
