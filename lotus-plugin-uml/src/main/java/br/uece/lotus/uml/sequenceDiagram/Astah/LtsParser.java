/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.sequenceDiagram.Astah;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.CombinedFragments;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.InteractionFragments;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.InteractionOperand;
import br.uece.lotus.viewer.TransitionView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class LtsParser {
    
    private final List<Mensagem> comunicacao;
    private final List<TabelaReferenciaID> relativoClassifier;
    private final List<InteractionFragments> loopsOuAlts;
    private final Component c;
    private static int idDisponivel;

    public LtsParser(List<Mensagem> comunicacao, List<TabelaReferenciaID> relativoClassifier, List<InteractionFragments> loopsOuAlts , Component c) {
        this.comunicacao = comunicacao;
        this.relativoClassifier = relativoClassifier;
        this.loopsOuAlts = loopsOuAlts;
        this.c = c;
        idDisponivel = this.relativoClassifier.size()+1;
    }
    
    public Component parseLTSA(){
        State srcState = null;
        State dstState = null;
        
        //=================================Montar linha de vida=================================================
        
        for(Mensagem mensagem : comunicacao){
            if(componentIsEmpty()){
                if(mensagem.getEnviando().getXmiID().equals(mensagem.getRecebendo().getXmiID())){
                    srcState = c.newState(idrelativoClassifier(mensagem.getEnviando().getXmiID()));
                    dstState = srcState;
                    c.buildTransition(srcState, dstState)
                            .setLabel(mensagem.getEnviando().getNome()+"."+mensagem.getRecebendo().getNome()+"."+mensagem.getMsg().replaceAll("\\+", ""))
                            .create();
                }else{
                    srcState = c.newState(idrelativoClassifier(mensagem.getEnviando().getXmiID()));
                    dstState = c.newState(idrelativoClassifier(mensagem.getRecebendo().getXmiID()));
                    c.buildTransition(srcState, dstState)
                            .setLabel(mensagem.getEnviando().getNome()+"."+mensagem.getRecebendo().getNome()+"."+mensagem.getMsg().replaceAll("\\+", ""))
                            .setViewType(1)
                            .create();
                }
            }else{
                if(mensagem.getEnviando().getXmiID().equals(mensagem.getRecebendo().getXmiID())){
                    srcState = dstState;
                    c.buildTransition(srcState, dstState)
                            .setLabel(mensagem.getEnviando().getNome()+"."+mensagem.getRecebendo().getNome()+"."+mensagem.getMsg().replaceAll("\\+", ""))
                            .setViewType(1)
                            .create();
                }else{
                    if(!stateExiste(c, idrelativoClassifier(mensagem.getRecebendo().getXmiID()))){
                        srcState = dstState;
                        dstState= c.newState(idrelativoClassifier(mensagem.getRecebendo().getXmiID()));
                        c.buildTransition(srcState, dstState)
                                .setLabel(mensagem.getEnviando().getNome()+"."+mensagem.getRecebendo().getNome()+"."+mensagem.getMsg().replaceAll("\\+", ""))
                                .setViewType(1)
                                .create();
                    }
                    else if(stateExiste(c, idrelativoClassifier(mensagem.getRecebendo().getXmiID()))){
                        srcState = dstState;
                        dstState = c.getStateByID(Integer.parseInt(mensagem.getRecebendo().getXmiID()));
                        c.buildTransition(srcState, dstState)
                                .setLabel(mensagem.getEnviando().getNome()+"."+mensagem.getRecebendo().getNome()+"."+mensagem.getMsg().replaceAll("\\+", ""))
                                .setViewType(1)
                                .create();
                    }
                }
            }
        }
        
        //=======================================Loop e Alt ===============================================
        try{
            mesclarBlocos();//Trata blocos internos
            for(InteractionFragments itf : loopsOuAlts){
                for(CombinedFragments combf : itf.getCombinedFrags()){
                    switch(combf.getOperator()){
                        case "loop":{//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            for(InteractionOperand intop : combf.getInteractionOperands()){
                                State origem = null , destino = null;
                                List<Transition> transInterOperand = new ArrayList<>();
                                for(Mensagem m : comunicacao){
                                    for(String s : intop.getXmiIdRefMsg()){
                                        if(m.getXmiIdMsg().equals(s)){
                                            for(Transition t : c.getTransitions()){
                                                String msg = m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getMsg().replaceAll("\\+", "");
                                                if(msg.equals(t.getLabel())){
                                                    transInterOperand.add(t);
                                                }
                                            }
                                        }
                                    }
                                }
                                origem = transInterOperand.get(transInterOperand.size()-1).getSource();
                                destino = transInterOperand.get(0).getSource();
                                c.buildTransition(origem, destino)
                                        .setLabel(transInterOperand.get(transInterOperand.size()-1).getLabel())
                                        .setViewType(TransitionView.Geometry.CURVE)
                                        .create();
                                if(transInterOperand.get(transInterOperand.size()-1).getDestiny().getIncomingTransitionsCount()==1 &&
                                                        transInterOperand.get(transInterOperand.size()-1).getDestiny().getOutgoingTransitionsCount()==0){
                                    c.remove(transInterOperand.get(transInterOperand.size()-1).getDestiny());
                                }
                                else if(transInterOperand.get(transInterOperand.size()-1).getDestiny().getIncomingTransitionsCount()>1 ||
                                                        transInterOperand.get(transInterOperand.size()-1).getDestiny().getOutgoingTransitionsCount()>0){
                                    List<Transition> paraRemover = new ArrayList<>();
                                    State src = transInterOperand.get(transInterOperand.size()-1).getSource();
                                    State dsty = transInterOperand.get(transInterOperand.size()-1).getDestiny();
                                    c.remove(transInterOperand.get(transInterOperand.size()-1));
                                    //arrumando saidas do ultimo
                                    for(Transition t : dsty.getOutgoingTransitionsList()){
                                        c.buildTransition(src, t.getDestiny())
                                                .setLabel(t.getLabel())
                                                .create();
                                        paraRemover.add(t);
                                    }
                                    //arrumando entradas do ultimo
                                    for(Transition t : dsty.getIncomingTransitionsList()){
                                        c.buildTransition(t.getSource(), src)
                                                .setLabel(t.getLabel())
                                                .create();
                                        paraRemover.add(t);
                                    }
                                    for(Transition t : paraRemover){
                                        c.remove(t);
                                    }
                                    c.remove(dsty);
                                }
                            }
                        };break;
                        case "alt":{////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            State gancho = null;
                            int i = 0;
                            HashMap<Integer,List<Transition>> mapa = new HashMap<>();
                            for(InteractionOperand intop : combf.getInteractionOperands()){
                                List<Transition> transInterOperand = new ArrayList<>();
                                for(Mensagem m : comunicacao){
                                    for(String s : intop.getXmiIdRefMsg()){
                                        if(m.getXmiIdMsg().equals(s)){
                                            for(Transition t : c.getTransitions()){
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
                            State ligacaoFinal = null;
                            for(int j=0;j<mapa.size();j++){ // Quebrando os alts
                                for(int k=0;k<mapa.get(j).size();k++){
                                    Transition t = mapa.get(j).get(k);
                                    Transition remover = null;
                                    if(j==0 && k==0){
                                        gancho = t.getSource();
                                    }
                                    else if(j>0 && k==0){
                                        c.buildTransition(gancho, t.getDestiny())
                                                .setLabel(t.getLabel())
                                                .create();
                                        remover = t;
                                    }
                                    //se houver transicao de saida do ultimo
                                    if(j==(mapa.size()-1) && k==mapa.get(j).size()-1){
                                        if(t.getDestiny().getOutgoingTransitionsCount()>0){
                                            ligacaoFinal = t.getDestiny();
                                        }
                                    }
                                    if(remover!=null){
                                        c.remove(remover);
                                    }
                                }
                            }
                            if(ligacaoFinal != null){
                                for(int j=0;j<mapa.size();j++){ // Arrumando saidas das pontas soltas
                                    for(int k=0;k<mapa.get(j).size();k++){
                                        Transition t = mapa.get(j).get(k);
                                        if(j!=(mapa.size()-1) && k==(mapa.get(j).size()-1)){
                                            c.buildTransition(t.getSource(), ligacaoFinal)
                                                    .setLabel(t.getLabel())
                                                    .create();
                                            c.remove(t.getDestiny());
                                        }
                                    }
                                }
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
                        };break;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return c;
    }

    private boolean componentIsEmpty() {
        return c.getInitialState() == null;
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
    
    private int ultimoIDdisponivel(){
        return idDisponivel++;
    }
}
