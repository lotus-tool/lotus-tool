/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.uml;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tools.uml.xmi.CombinedFragments;
import br.uece.lotus.tools.uml.xmi.InteractionFragments;
import br.uece.lotus.tools.uml.xmi.InteractionOperand;
import br.uece.lotus.viewer.TransitionView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class LtsaParserTCG {
    
    private final List<Mensagem> comunicacao;
    private final List<TabelaReferenciaID> relativoClassifier;
    private final List<InteractionFragments> loopsOuAlts;
    private final Component c;
    private List<Transition> tDoComponent = new ArrayList<>();
    private static int idDisponivel;

    public LtsaParserTCG(List<Mensagem> comunicacao, List<TabelaReferenciaID> relativoClassifier, List<InteractionFragments> loopsOuAlts , Component c) {
        this.comunicacao = comunicacao;
        this.relativoClassifier = relativoClassifier;
        this.loopsOuAlts = loopsOuAlts;
        this.c = c;
        idDisponivel = this.relativoClassifier.size()+1;
    }
    
    public Component parseLTSA(){
        State org = null , dst = null;
        
        //=================================Montar linha de vida=================================================
        for(Mensagem m : comunicacao){
            if(c.getInitialState() == null){
                org = c.newState(idrelativoClassifier(m.getEnviando().getXmiID()));
                dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                c.buildTransition(org, dst)
                        .setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getMsg().replaceAll("\\+", ""))
                        .create();
            }else{
                if(!stateExiste(c, idrelativoClassifier(m.getRecebendo().getXmiID()))){
                    org = dst;
                    dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                    c.buildTransition(org, dst)
                            .setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getMsg().replaceAll("\\+", ""))
                            .create();
                }
                else if(stateExiste(c, idrelativoClassifier(m.getRecebendo().getXmiID()))){
                    org = dst;
                    dst = c.newState(ultimoIDdisponivel());
                    c.buildTransition(org, dst)
                            .setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getMsg().replaceAll("\\+", ""))
                            .create();
                }
            }
        }
        
        //=======================================Arrumando LTS TCG===========================================================================
        mesclarBlocos();//Trata blocos internos
        Transition anterior = null;
        tDoComponent.addAll((Collection<? extends Transition>) c.getTransitions());
        for(Transition t : tDoComponent){
            //Verificar se t Ã© inicio de algum loop ou alt
            Mensagem m = verificarQualEhAMsg(t);
            CombinedFragments combf = verificarMsgNosBlocos(m);
            if(combf != null){
                switch (combf.getOperator()) {
                    case "loop":
                        loop(combf,anterior);
                        break;
                    case "alt":
                        alt(combf,anterior);
                        break;
                }
            }
            else{
                normal(m,t,anterior);
            }
            anterior = t;
        }
        //arrumarLabels();
        return c;
    }
    
    private void normal(Mensagem m, Transition t, Transition anterior){
        String tipo = m.getRecebendo().getTipo();
        try{
        switch (tipo) {
            case "actor":{
                if(anterior == null){
                    State src = c.newState(ultimoIDdisponivel());
                    src.setAsInitial();
                    c.buildTransition(src, t.getSource())
                            .setLabel("Resultados Esperados")
                            .create();
                }else{
                    Mensagem msgAnterior = verificarQualEhAMsg(anterior);
                    if(!m.getRecebendo().getTipo().equals(msgAnterior.getRecebendo().getTipo())){
                        State src = c.newState(ultimoIDdisponivel());
                        c.buildTransition(src, t.getSource())
                                .setLabel("Resultados Esperados")
                                .create();
                        for(Transition trans : anterior.getSource().getTransitionsTo(t.getSource())){
                            c.buildTransition(trans.getSource(), src)
                                    .setLabel(trans.getLabel())
                                    .create();
                            c.remove(trans);
                        }
                    }
                }
            }
                break;
            case "class":{
                if(anterior == null){
                    State src = c.newState(ultimoIDdisponivel());
                    src.setAsInitial();
                    c.buildTransition(src, t.getSource())
                            .setLabel("Passos")
                            .create();
                }else{
                    Mensagem msgAnterior = verificarQualEhAMsg(anterior);
                    if(!m.getRecebendo().getTipo().equals(msgAnterior.getRecebendo().getTipo())){
                        State src = c.newState(ultimoIDdisponivel());
                        c.buildTransition(src, t.getSource())
                                .setLabel("Passos")
                                .create();
                        for(Transition trans : anterior.getSource().getTransitionsTo(t.getSource())){
                            c.buildTransition(trans.getSource(), src)
                                    .setLabel(trans.getLabel())
                                    .create();
                            c.remove(trans);
                        }
                    }
                }
            }
                break;
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void loop(CombinedFragments combf, Transition anterior){
        List<Transition> transInterOperand = new ArrayList<>();
        for(InteractionOperand intop : combf.getInteractionOperands()){
            for(Mensagem m : comunicacao){
                for(String s : intop.getXmiIdRefMsg()){
                    if(m.getXmiIdMsg().equals(s)){
                        for(Transition t : tDoComponent){
                            String msg = m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getMsg().replaceAll("\\+", "");
                            if(msg.equals(t.getLabel())){
                                transInterOperand.add(t);
                            }
                        }
                    }
                }
            }
        }
        if(anterior == null){
            State src = c.newState(ultimoIDdisponivel());
            src.setAsInitial();
            State src2 = c.newState(ultimoIDdisponivel());
            c.buildTransition(src, src2)
                    .setLabel("Condicoes Iniciais")
                    .create();
            c.buildTransition(src2, transInterOperand.get(0).getSource())
                    .setLabel(combf.getInteractionOperands().get(0).getInteractionConstraintName())
                    .create();
            c.buildTransition(transInterOperand.get(transInterOperand.size()-1).getDestiny(), src2)
                    .setLabel("Condicoes Iniciais")
                    .setViewType(TransitionView.Geometry.CURVE)
                    .create();
        }else{
            State src = c.newState(ultimoIDdisponivel());
            State src2 = c.newState(ultimoIDdisponivel());
            State src3 = c.newState(ultimoIDdisponivel());
            c.buildTransition(src, src2)
                    .setLabel("Condicoes Iniciais")
                    .create();
            c.buildTransition(src2, src3)
                    .setLabel(combf.getInteractionOperands().get(0).getInteractionConstraintName())
                    .create();
            c.buildTransition(src3, transInterOperand.get(0).getDestiny())
                    .setLabel(transInterOperand.get(0).getLabel())
                    .create();
            c.buildTransition(transInterOperand.get(transInterOperand.size()-1).getDestiny(), src2)
                    .setLabel("Condicoes Iniciais")
                    .setViewType(TransitionView.Geometry.CURVE)
                    .create();
            State srcTransInterOperandGet0 = transInterOperand.get(0).getSource();
            c.remove(transInterOperand.get(0));
            for(Transition t : srcTransInterOperandGet0.getIncomingTransitionsList()){
                c.buildTransition(t.getSource(), src)
                        .setLabel(t.getLabel())
                        .create();
            }
            for(Transition t : srcTransInterOperandGet0.getOutgoingTransitionsList()){
                if(t.getDestiny().getID()<src.getID()){
                    c.buildTransition(src, t.getDestiny())
                        .setLabel(t.getLabel())
                        .setViewType(TransitionView.Geometry.CURVE)
                        .create();
                }else{
                    c.buildTransition(src, t.getDestiny())
                        .setLabel(t.getLabel())
                        .create();
                }
            }
            c.remove(transInterOperand.get(0).getSource());
        }
    }
    
    private void alt(CombinedFragments combf, Transition anterior){
        int i = 0;
        HashMap<Integer,List<Transition>> mapa = new HashMap<>();
        for(InteractionOperand intop : combf.getInteractionOperands()){
            List<Transition> transInterOperand = new ArrayList<>();
            for(Mensagem m : comunicacao){
                for(String s : intop.getXmiIdRefMsg()){
                    if(m.getXmiIdMsg().equals(s)){
                        for(Transition t : tDoComponent){
                            String msg = m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getMsg().replaceAll("\\+", "");
                            if(msg.equals(t.getLabel())){
                                transInterOperand.add(t);
                            }
                        }
                    }
                }
            }
            mapa.put(i, transInterOperand);
            i++;
        }
        State gancho = null;
        if(anterior == null){
            gancho = encontrarGancho(mapa);
            State srcCondicoes = c.newState(ultimoIDdisponivel());
            srcCondicoes.setAsInitial();
            c.buildTransition(srcCondicoes, gancho)
                    .setLabel("Condicoes Inicias")
                    .create();
            // Quebrando os alts
            State ligacaoFinal = null;
            for(int j=0;j<mapa.size();j++){ 
                for(int k=0;k<mapa.get(j).size();k++){
                    Transition t = mapa.get(j).get(k);
                    Transition remover = null;
                    if(j==0 && k==0){
                        State guard = c.newState(ultimoIDdisponivel());
                        c.buildTransition(gancho, guard)
                                .setLabel(combf.getInteractionOperands().get(j).getInteractionConstraintName())
                                .create();
                        List<Transition> paraRemover = new ArrayList<>();
                        for(Transition trans : gancho.getOutgoingTransitionsList()){
                            if(trans.getDestiny() != guard){
                                c.buildTransition(guard, trans.getDestiny())
                                    .setLabel(trans.getLabel())
                                    .create();
                                paraRemover.add(trans);
                            }
                        }
                        for(Transition trans : paraRemover){
                            c.remove(trans);
                        }
                    }
                    else if(j>0 && k==0){
                        State guard = c.newState(ultimoIDdisponivel());
                        c.buildTransition(gancho, guard)
                                .setLabel(combf.getInteractionOperands().get(j).getInteractionConstraintName())
                                .create();
                        c.buildTransition(guard, t.getDestiny())
                                .setLabel(t.getLabel())
                                .create();
                        remover = t;
                    }
                    if(remover != null){
                        c.remove(remover);
                    }
                    //Verificar algo depois do Alt
                    if(j==(mapa.size()-1) && k==mapa.get(j).size()-1){
                        if(t.getDestiny().getOutgoingTransitionsCount()>0){
                            ligacaoFinal = t.getDestiny();
                        }
                    }
                }
                
            }
            if(ligacaoFinal != null){
                // Arrumando saidas das pontas soltas
                Transition pontaSolta = null;
                for(int j=0;j<mapa.size();j++){ 
                    for(int k=0;k<mapa.get(j).size();k++){
                        String s = mapa.get(j).get(k).getLabel();
                        if(j!=(mapa.size()-1) && k==(mapa.get(j).size()-1)){
                            for(Transition t2 : c.getTransitions()){
                                if(t2.getLabel().equals(s)){
                                    pontaSolta = t2;
                                }
                            }  
                        }
                    }
                }
                c.buildTransition(pontaSolta.getSource(), ligacaoFinal)
                                            .setLabel(pontaSolta.getLabel())
                                            .create();
                                    c.remove(pontaSolta.getDestiny());
                ligacaoFinal = null;
            }
            //Arrumando Transicoes Duplas
            for(Transition t : c.getTransitions()){
                State origem = t.getSource();
                State destino = t.getDestiny();
                int contagem = 0;
                for(Transition t2 : origem.getOutgoingTransitions()){
                    if(t2.getDestiny() == destino){
                        contagem++;
                    }
                }
                if(contagem>1){
                    t.setValue("view.type", 1);
                }
            
            }
            
        }else{
            /*gancho = encontrarGancho(mapa);
            State srcCondicoes = c.newState(ultimoIDdisponivel());
            List<Transition> paraRemover = new ArrayList<>();
            for(Transition t : gancho.getIncomingTransitions()){
                c.buildTransition(t.getSource(), srcCondicoes)
                        .setLabel(t.getLabel())
                        .create();
                paraRemover.add(t);
            }
            c.buildTransition(srcCondicoes, gancho)
                    .setLabel("Condicoes Iniciais")
                    .create();
            for(Transition t : paraRemover){
                c.remove(t);
            }*/
            
        }
    }
    
    private void mesclarBlocos(){
        for(InteractionFragments itf : loopsOuAlts){
            for(CombinedFragments combf : itf.getCombinedFrags()){
                for(InteractionOperand intop : combf.getInteractionOperands()){
                    List<String> msgs = intop.getXmiIdRefMsg();
                    if(intop.getInteractionOperandFrags().size()>0){
                        for(String s : intop.getInteractionOperandFrags()){
                            for(CombinedFragments combf2 : itf.getCombinedFrags()){
                                if(s.equals(combf2.getXmiIdCombinedFragment())){
                                    for(InteractionOperand intop2 : combf2.getInteractionOperands()){
                                        msgs.addAll(intop2.getXmiIdRefMsg());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean stateExiste(Component c,int id){
        boolean existe = false;
        for(State s : c.getStates()){
            if(s.getID() == id){
                existe = true;
                break;
            }
        }
        return existe;
    }
    
    private int idrelativoClassifier(String id){
        int i = 0;
        for(TabelaReferenciaID tri : relativoClassifier){
            if(tri.getIdClassOrActor().equals(id)){
                i=tri.getIdRelativo();
            }
        }
        return i;
    }
    
    private int ultimoIDdisponivel(){
        return idDisponivel++;
    }

    private Mensagem verificarQualEhAMsg(Transition t) {
        Mensagem msg = null;
        for(Mensagem m : comunicacao){
            if(t.getLabel().equals(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getMsg().replaceAll("\\+", ""))){
                msg = m;
                break;
            }
        }
        return msg;
    }

    private CombinedFragments verificarMsgNosBlocos(Mensagem msg) {
        CombinedFragments combfrag = null;
        for(InteractionFragments intf : loopsOuAlts){
            for(CombinedFragments combf : intf.getCombinedFrags()){
                for(InteractionOperand intop : combf.getInteractionOperands()){
                    for(int i=0;i<intop.getXmiIdRefMsg().size();i++){
                        if(msg.getXmiIdMsg().equals(intop.getXmiIdRefMsg().get(0))){
                            combfrag = combf;
                        }
                    }
                }
            }
        }
        return combfrag;
    }

    private void arrumarLabels() {
        List<State> statesDoComponent = (List<State>) c.getStates();
        int label = 0;
        State initial = c.getInitialState();
        initial.setLabel(String.valueOf(label));
        label++;
        double ultimoArrumadoX = initial.getLayoutX();
        double ultimoArrumadoY = initial.getLayoutY();
        boolean maisAhEsquerda = true;
        for(State s : statesDoComponent){
            for(State ss : statesDoComponent){
                if(s.getLayoutX()<=ss.getLayoutX()){
                    
                }
            }
            
        }
    }

    private State encontrarGancho(HashMap<Integer, List<Transition>> mapa) {
        //Encontrando o gancho
        State gancho = null;
        Transition tran = mapa.get(0).get(0);
        for(Transition t : c.getTransitions()){
            if(tran.getLabel().equals(t.getLabel())){
                gancho = t.getSource();
            }
        }
        return gancho;
    }
}
