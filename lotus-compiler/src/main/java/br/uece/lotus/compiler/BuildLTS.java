/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.compiler;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.viewer.TransitionView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import java.util.List;

/**
 *
 * @author Yan Gurgel
 */
public class BuildLTS {

    private Project projeto;
    private UserInterface mUserInterface;
    private double probabilidade = 0;
    private String labelProbabilidade = "";
    private ProjectExplorer mProjectExplorer;
    private State state;
    private Component component;
    private ProjectExplorer projectExplorer;
    private String ltsAtual;
    private String nomeEstado;
    private String proxima;
    private String origemOU;
    private int layoutX = 10;
    private int layoutY = 10;
    private ArrayList<String> expressoes;
    private ArrayList<String> estadosPrincipais = new ArrayList<>();
    private ArrayList<String> novosEstados = new ArrayList<>();
    private ArrayList<String> transitions = new ArrayList<>();
    private ArrayList<String> transitionsOu = new ArrayList<>();
    private ArrayList<String> transitionsOuSolo = new ArrayList();

    public BuildLTS(Component c, ArrayList lts, Project p) {
        this.component = c;
        this.projeto = p;
        //component.setName(definirProcessoPrincipal(lts));
    }

    public void build(String codigo, ArrayList lts) {
        System.out.println("\n**************************************************************");
        //component.setName(definirProcessoPrincipal(lts));

        /**
         * *Substiuindo $ (pontos de probabilidade)**
         */
        for (int i = 0; i < lts.size(); i++) {
            int indice = i + 1;
            System.out.println("LTS: " + indice);
            String LTS = lts.get(i).toString();
            for (int j = 0; j < LTS.length(); j++) {
                if (LTS.charAt(j) == '$') {
                    StringBuilder sb = new StringBuilder(LTS);
                    sb.setCharAt(j, '.');
                    LTS = sb.toString();
                }
            }
            lts.remove(i);
            lts.add(i, LTS);
            //System.out.println(lts.get(i));
        }

        /**
         * *INCIANDO CONSTRUÇÃO**
         */
        for (int i = 0; i < lts.size(); i++) {

            ltsAtual = lts.get(i).toString();
            //if(i>0){
            String nomeLTS = definirOrigem(ltsAtual);
            component = mNewComponent(nomeLTS);
            
            //}

            System.out.println("\nLTS Atual" + ltsAtual);

            expressoes = converteArray(ltsAtual.split(","));

            for (int j = 0; j < expressoes.size(); j++) {
                definirEstadosPrincipais(expressoes.get(j));
            }
            definirErroStop(ltsAtual);
            //CRIA ESTADOS PRINCIPAIS
            Map<String, State> estadosPrincipaisMap = new HashMap<>();
            for (int j = 0; j < estadosPrincipais.size(); j++) {
                nomeEstado = estadosPrincipais.get(j);
                state = component.newState(j);
                if (nomeEstado.equals("ERROR")) {
                    state.setError(true);
                }
                if (nomeEstado.equals("STOP")) {
                    state.setFinal(true);
                }
                if (j == 0) {
                    component.setInitialState(state);
                }
                estadosPrincipaisMap.put(nomeEstado, state);
                layoutX = layoutX + 20;
                layoutY = layoutY + 40;
                state.setLayoutX(layoutX);
                state.setLayoutY(layoutY);
                state.setNameState(this.nomeEstado);
                System.out.println("CRIANDO ESTADO PRINCIPAL: " + this.nomeEstado);

            }

            //Definir estados e transições a partir do estados principais
            Map<String, State> estadoDestino = new HashMap<>();
            for (int j = 0; j < expressoes.size(); j++) {
                /**
                 * CONTEM OU '|'
                 */
                if (expressoes.get(j).contains("|")) {
                    System.out.println("***CONTEM OU***");
                    //definirTransitions(expressoes.get(j));
                    origemOU = definirOrigem(expressoes.get(j));
                    transitionsOu = definirTransitionsOu(expressoes.get(j));
                    //PECORRENDO CADA OU DA EXPRESSAO
                    for (int k = 0; k < transitionsOu.size(); k++) {
                        System.out.println("TransitionOu: " + transitionsOu.get(k));
                        //DEFININDO LABELS E RETORNOS DE ESTADO DO '|' ATUAL
                        transitionsOuSolo = definirTransitionsOuElementos(transitionsOu.get(k));
                        for (int l = 0; l < transitionsOuSolo.size(); l++) {
                            if (!(estadosPrincipais.contains(transitionsOuSolo.get(l)))) {
                                //Se é a primeira transição da expressão e não é estado principal
                                System.out.println("TransitionOuSOLO: " + transitionsOuSolo.get(l));
                                if (l == 0) {
                                    if (!(estadosPrincipais.contains(transitionsOuSolo.get(l + 1)))) {
                                        System.out.println("É a primeira transição da expressão e não é estado principal");

                                        String nomeEstado = transitionsOuSolo.get(l);
                                        State destino = component.newState(j);
                                        estadoDestino.put(nomeEstado, destino);
                                        probabilidade = definirProbabilidade(transitionsOuSolo.get(l));
                                        if (probabilidade != 0) {
                                            labelProbabilidade = definirLabelProbabilidade(transitionsOuSolo.get(l));
                                            component.buildTransition(estadosPrincipaisMap.get(origemOU), estadoDestino.get(transitionsOuSolo.get(l)))
                                                    .setLabel(labelProbabilidade)
                                                    .setProbability(probabilidade)
                                                    .setViewType(TransitionView.Geometry.LINE)
                                                    .create();
                                            labelProbabilidade = "";
                                        } else {
                                            component.buildTransition(estadosPrincipaisMap.get(origemOU), estadoDestino.get(transitionsOuSolo.get(l)))
                                                    .setLabel(transitionsOuSolo.get(l))
                                                    //.setProbability(0.4)
                                                    .setViewType(TransitionView.Geometry.LINE)
                                                    .create();
                                        }
                                    } else {

                                        System.out.println("É a primeira transição da expressão e não é estado principal");
                                        String origem;
                                        origem = definirOrigem(expressoes.get(j));
                                        probabilidade = definirProbabilidade(transitionsOuSolo.get(l));
                                        if (probabilidade != 0) {
                                            labelProbabilidade = definirLabelProbabilidade(transitionsOuSolo.get(l));
                                            component.buildTransition(estadosPrincipaisMap.get(origemOU), estadosPrincipaisMap.get(transitionsOuSolo.get(l + 1)))
                                                    .setLabel(labelProbabilidade)
                                                    .setProbability(probabilidade)
                                                    .setViewType(TransitionView.Geometry.LINE)
                                                    .create();
                                            labelProbabilidade = "";
                                        } else {
                                            component.buildTransition(estadosPrincipaisMap.get(origemOU), estadosPrincipaisMap.get(transitionsOuSolo.get(l + 1)))
                                                    .setLabel(transitionsOuSolo.get(l))
                                                    //.setProbability(0.4)
                                                    .setViewType(TransitionView.Geometry.LINE)
                                                    .create();
                                        }

                                    }

                                } /**
                                 * Se NÃO é a primeira transição da expressão E
                                 * NÃO é estado principal*
                                 */
                                else {
                                    /**
                                     * SE a proxima transition não for estado
                                     * principal
                                     */
                                    if (!(estadosPrincipais.contains(transitionsOuSolo.get(l + 1)))) {
                                        System.out.println("A proxima transition nao é estado principal");
                                        String nomeEstado = transitionsOuSolo.get(l);
                                        String anterior = transitionsOuSolo.get(l - 1);
                                        State destino = component.newState(j);
                                        estadoDestino.put(nomeEstado, destino);
                                        probabilidade = definirProbabilidade(transitionsOuSolo.get(l));
                                        if (probabilidade != 0) {
                                            labelProbabilidade = definirLabelProbabilidade(transitionsOuSolo.get(l));
                                            component.buildTransition(estadoDestino.get(anterior), estadoDestino.get(nomeEstado))
                                                    .setLabel(labelProbabilidade)
                                                    .setProbability(probabilidade)
                                                    .setViewType(TransitionView.Geometry.LINE)
                                                    .create();
                                            labelProbabilidade = "";
                                        } else {
                                            component.buildTransition(estadoDestino.get(anterior), estadoDestino.get(nomeEstado))
                                                    .setLabel(transitionsOuSolo.get(l))
                                                    //.setProbability(0.4)
                                                    .setViewType(TransitionView.Geometry.LINE)
                                                    .create();
                                        }
                                    } /**
                                     * SE a proxima transition FOR estado
                                     * principal
                                     */
                                    else {
                                        String origem = transitionsOuSolo.get(l - 1);
                                        System.out.println("Proxima transition é estado principal");

                                        probabilidade = definirProbabilidade(transitionsOuSolo.get(l));
                                        if (probabilidade != 0) {
                                            labelProbabilidade = definirLabelProbabilidade(transitionsOuSolo.get(l));
                                            component.buildTransition(estadoDestino.get(origem), estadosPrincipaisMap.get(transitionsOuSolo.get(l + 1)))
                                                    .setLabel(labelProbabilidade)
                                                    .setProbability(probabilidade)
                                                    .setViewType(TransitionView.Geometry.LINE)
                                                    .create();
                                            labelProbabilidade = "";
                                        } else {
                                            component.buildTransition(estadoDestino.get(origem), estadosPrincipaisMap.get(transitionsOuSolo.get(l + 1)))
                                                    .setLabel(transitionsOuSolo.get(l))
                                                    //.setProbability(0.4)
                                                    .setViewType(TransitionView.Geometry.LINE)
                                                    .create();
                                        }

                                    }

                                }

                            }

                        }
                        estadoDestino.clear();

                    }
                } /**
                 * **********************************************************************
                 ************************************************************************
                 * **********************************************************************
                 * **********************************************************************
                 * ********************************************************************
                 */
                /**
                 * NÃO CONTEM OU
                 */
                else {
                    System.out.println("***NÃO CONTEM OU***");
                    //retornando o array de transitions da expressao atual
                    definirTransitions(expressoes.get(j));
                    for (int k = 0; k < transitions.size(); k++) {
                        System.out.println("Transition : " + transitions.get(k));
                        if (!(estadosPrincipais.contains(transitions.get(k)))) {
                            //Se é a primeira transição da expressão e não é estado principal
                            if (k == 0) {
                                if (!(estadosPrincipais.contains(transitions.get(k + 1)))) {
                                    System.out.println("É a primeira transição da expressão e não é estado principal");
                                    String origem;
                                    String nomeEstado = transitions.get(k);
                                    origem = definirOrigem(expressoes.get(j));
                                    State destino = component.newState(j);
                                    estadoDestino.put(nomeEstado, destino);
                                    probabilidade = definirProbabilidade(transitions.get(k));
                                    if (probabilidade != 0) {
                                        labelProbabilidade = definirLabelProbabilidade(transitions.get(k));
                                        component.buildTransition(estadosPrincipaisMap.get(origem), estadoDestino.get(transitions.get(k)))
                                                .setLabel(labelProbabilidade)
                                                .setProbability(probabilidade)
                                                .setViewType(TransitionView.Geometry.LINE)
                                                .create();
                                        labelProbabilidade = "";
                                    } else {
                                        component.buildTransition(estadosPrincipaisMap.get(origem), estadoDestino.get(transitions.get(k)))
                                                .setLabel(transitions.get(k))
                                                //.setProbability(0.4)
                                                .setViewType(TransitionView.Geometry.LINE)
                                                .create();
                                    }
                                } else {

                                    System.out.println("É a primeira transição da expressão e não é estado principal");
                                    String origem;
                                    origem = definirOrigem(expressoes.get(j));
                                    probabilidade = definirProbabilidade(transitions.get(k));
                                    if (probabilidade != 0) {
                                        labelProbabilidade = definirLabelProbabilidade(transitions.get(k));
                                        component.buildTransition(estadosPrincipaisMap.get(origem), estadosPrincipaisMap.get(transitions.get(k + 1)))
                                                .setLabel(labelProbabilidade)
                                                .setProbability(probabilidade)
                                                .setViewType(TransitionView.Geometry.LINE)
                                                .create();
                                        labelProbabilidade = "";
                                    } else {
                                        component.buildTransition(estadosPrincipaisMap.get(origem), estadosPrincipaisMap.get(transitions.get(k + 1)))
                                                .setLabel(transitions.get(k))
                                                //.setProbability(0.4)
                                                .setViewType(TransitionView.Geometry.LINE)
                                                .create();
                                    }

                                }

                            } /**
                             * Se NÃO é a primeira transição da expressão E NÃO
                             * é estado principal*
                             */
                            else {
                                /**
                                 * SE a proxima transition não for estado
                                 * principal
                                 */
                                if (!(estadosPrincipais.contains(transitions.get(k + 1)))) {
                                    System.out.println("A proxima transition nao é estado principal");
                                    String nomeEstado = transitions.get(k);
                                    String anterior = transitions.get(k - 1);
                                    State destino = component.newState(j);
                                    estadoDestino.put(nomeEstado, destino);
                                    probabilidade = definirProbabilidade(transitions.get(k));
                                    if (probabilidade != 0) {
                                        labelProbabilidade = definirLabelProbabilidade(transitions.get(k));
                                        component.buildTransition(estadoDestino.get(anterior), estadoDestino.get(nomeEstado))
                                                .setLabel(labelProbabilidade)
                                                .setProbability(probabilidade)
                                                .setViewType(TransitionView.Geometry.LINE)
                                                .create();
                                        labelProbabilidade = "";
                                    } else {
                                        component.buildTransition(estadoDestino.get(anterior), estadoDestino.get(nomeEstado))
                                                .setLabel(transitions.get(k))
                                                //.setProbability(0.4)
                                                .setViewType(TransitionView.Geometry.LINE)
                                                .create();
                                    }
                                } /**
                                 * SE a proxima transition FOR estado principal
                                 */
                                else {
                                    String origem = transitions.get(k - 1);
                                    System.out.println("Proxima transition é estado principal");

                                    probabilidade = definirProbabilidade(transitions.get(k));
                                    if (probabilidade != 0) {
                                        labelProbabilidade = definirLabelProbabilidade(transitions.get(k));
                                        component.buildTransition(estadoDestino.get(origem), estadosPrincipaisMap.get(transitions.get(k + 1)))
                                                .setLabel(labelProbabilidade)
                                                .setProbability(probabilidade)
                                                .setViewType(TransitionView.Geometry.LINE)
                                                .create();
                                        labelProbabilidade = "";
                                    } else {
                                        component.buildTransition(estadoDestino.get(origem), estadosPrincipaisMap.get(transitions.get(k + 1)))
                                                .setLabel(transitions.get(k))
                                                //.setProbability(0.4)
                                                .setViewType(TransitionView.Geometry.LINE)
                                                .create();
                                    }

                                }

                            }

                        }
                    }
                }

                transitions.clear();
                transitionsOu.clear();
                transitionsOuSolo.clear();
                estadoDestino.clear();
            }
            ltsAtual = "";
            estadosPrincipais.clear();
            estadosPrincipaisMap.clear();


            TreeLayouter(component);
            
        }

        //return component;
    }// Fim do Build

    private void TreeLayouter(Component component) {
        final int PADDING_X = 200;
        final int PADDING_Y = 100;

        List<State> states = new ArrayList<>();
        List<State> visitedStates = new ArrayList<>();
        State is = component.getInitialState();
        is.setLayoutX(30);
        is.setLayoutY(30);
        states.add(is);

        while (!states.isEmpty()) {
            State s = states.remove(0);
            int i = 0;
            double currentX = s.getLayoutX();
            double currentY = s.getLayoutY();
            for (Transition t : s.getOutgoingTransitions()) {
                State destiny = t.getDestiny();
                if (!visitedStates.contains(destiny)) {
                    destiny.setLayoutX(currentX + PADDING_X);
                    destiny.setLayoutY(currentY + PADDING_Y * i);
                    states.add(destiny);
                    i++;
                }
            }
            visitedStates.add(s);
        }
    }

    private String definirLabelProbabilidade(String prob) {
        int parentFech = 0;
        char charAux;
        String strAux = "";
        for (int i = 0; i < prob.length(); i++) {
            if (prob.charAt(i) == ')') {
                parentFech = i;
            }
        }
        for (int i = parentFech + 1; i < prob.length(); i++) {
            charAux = prob.charAt(i);
            strAux = Character.toString(charAux);
            labelProbabilidade = labelProbabilidade.concat(strAux);
        }

        System.out.println("labelProbabilidade:" + labelProbabilidade);
        return labelProbabilidade;
    }

    //Retorna um Array onde cada posição é uma transition ou estado principal
    private ArrayList definirTransitionsOuElementos(String expressao) {
        ArrayList arrayAux = new ArrayList();

        arrayAux = converteArray(expressao.split("->"));

        return arrayAux;
    }

    //Retorna um Array onde cada posição é um possivel caminho
    private ArrayList definirTransitionsOu(String expressao) {
        ArrayList expressOu = new ArrayList();
        ArrayList expre = new ArrayList();
        expre = converteArray(expressao.split("="));
        expressao = expre.get(1).toString();
        String strAux1 = "";
        String strAux2 = "";
        char cAux;
        for (int i = 1; i < expressao.length() - 1; i++) {
            cAux = expressao.charAt(i);
            strAux1 = Character.toString(cAux);
            strAux2 = strAux2.concat(strAux1);

        }
        System.out.println("String modificada ou: " + strAux2);
        expressOu = converteArray(strAux2.split("[|]"));

        return expressOu;
    }

    private double definirProbabilidade(String prob) {
        int parentAbrindo = 0;
        int parentFechando = 0;
        String concatenado = "";
        probabilidade = 0.0;
        if (prob.contains("(")) {
            for (int i = 0; i < prob.length(); i++) {
                if (prob.charAt(i) == '(') {
                    parentAbrindo = i;
                }
                if (prob.charAt(i) == ')') {
                    parentFechando = i;
                }
            }
            for (int i = parentAbrindo + 1; i < parentFechando; i++) {
                char a = prob.charAt(i);
                String b = Character.toString(a);

                concatenado = concatenado.concat(b);
            }
            probabilidade = Double.parseDouble(concatenado);
        }

        if (probabilidade != 0) {

        } else {
            probabilidade = 0;
        }
        System.out.println("PROBABILIDADE: " + probabilidade);
        return probabilidade;
    }

    private String definirOrigem(String origem) {
        ArrayList expre = new ArrayList();
        expre = converteArray(origem.split("="));
        origem = expre.get(0).toString();
        expre.clear();
        return origem;
    }

    private String definirProcessoPrincipal(ArrayList lts) {
        String processo = "";
        ArrayList expre = new ArrayList();
        processo = lts.get(0).toString();
        expre = converteArray(processo.split(","));
        processo = expre.get(0).toString();
        expre.clear();
        expre = converteArray(processo.split("="));
        processo = expre.get(0).toString();
        expre.clear();

        return processo;
    }

    private ArrayList<String> definirTransitions(String expressao) {
        if (expressao.contains("|")) {
            ArrayList ladoDireito = new ArrayList();
            ArrayList ladoDireitoSemTransition = new ArrayList();
            ladoDireito = converteArray(expressao.split("="));
            expressao = ladoDireito.get(1).toString();
            expressao = expressao.substring(1, expressao.length() - 1);
            ladoDireito.clear();
            ladoDireito = converteArray(expressao.split("[|]"));
            expressao = "";

            for (int i = 0; i < ladoDireito.size(); i++) {
                expressao = ladoDireito.get(i).toString();
                ladoDireitoSemTransition = converteArray(expressao.split("->"));
                for (int j = 0; j < ladoDireitoSemTransition.size(); j++) {
                    String a = ladoDireitoSemTransition.get(j).toString();
                    transitions.add(a);
                }
            }

            return transitions;
        } else {
            ArrayList ladoDireito = new ArrayList();
            ladoDireito = converteArray(expressao.split("="));
            expressao = ladoDireito.get(1).toString();
            expressao = expressao.substring(1, expressao.length() - 1);

            transitions = converteArray(expressao.split("->"));
            ladoDireito.clear();

            return transitions;
        }
    }

    private ArrayList<String> definirEstadosPrincipais(String expressao) {

        ArrayList declaracao = new ArrayList();
        declaracao = converteArray(expressao.split("="));
        System.out.println("Declaração: " + declaracao.get(0));
        String declara = declaracao.get(0).toString();
        estadosPrincipais.add(declara);
        declaracao.clear();

        return estadosPrincipais;
    }

    private ArrayList<String> definirErroStop(String ltsAtual) {
        if (ltsAtual.contains("ERROR")) {
            System.out.println("LTS possui estado de erro");
            estadosPrincipais.add("ERROR");

        }
        if (ltsAtual.contains("STOP")) {
            System.out.println("LTS possui estado final");
            estadosPrincipais.add("STOP");
        }
        return estadosPrincipais;
    }

    //Método converte Vetor String em Arraylist    
    private ArrayList<String> converteArray(String[] vetor) {
        ArrayList<String> Tk = new ArrayList<>();
        for (int i = 0; i < vetor.length; i++) {
            Tk.add(vetor[i]);
        }
        return Tk;
    }

    private Component mNewComponent(String nomeLTS) {

        String name = nomeLTS;

        Component c = new Component();

        c.setName(name);

        projeto.addComponent(c);

        return c;

    }
;

}//Fim da Classe BuildLTS
