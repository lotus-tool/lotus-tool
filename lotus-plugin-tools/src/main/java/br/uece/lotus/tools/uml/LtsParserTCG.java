/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.uml;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.tools.uml.xmi.CombinedFragments;
import br.uece.lotus.tools.uml.xmi.InteractionFragments;
import br.uece.lotus.tools.uml.xmi.InteractionOperand;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class LtsParserTCG {
    private final List<Mensagem> comunicacao;
    private final List<TabelaReferenciaID> relativoClassifier;
    private final List<InteractionFragments> loopsOuAlts;
    private final Component c;
    private static int idDisponivel;
    
    public LtsParserTCG(List<Mensagem> comunicacao, List<TabelaReferenciaID> relativoClassifier, List<InteractionFragments> loopsOuAlts , Component c) {
        this.comunicacao = comunicacao;
        this.relativoClassifier = relativoClassifier;
        this.loopsOuAlts = loopsOuAlts;
        this.c = c;
        idDisponivel = this.relativoClassifier.size()+1;
    }
    
    public Component parseLTSA(){
        try{
            mesclarBlocos();
            List<State[]> loops = new ArrayList<>();
            String tipoAnterior = "";
            State org = null, dst = null;
            for(int i=0;i<comunicacao.size();i++){
                Mensagem m = comunicacao.get(i);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////
                if(c.getInitialState()==null){
                    State[] loop = verificarLoop(m);
                    if(loop != null){
                        tipoAnterior = m.getRecebendo().getTipo();
                        loops.add(loop);
                        State srcCond = c.newState(ultimoIDdisponivel());
                        srcCond.setAsInitial();
                        org = loop[0];
                        dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                        c.buildTransition(srcCond, org)
                                .setLabel("Condicoes Iniciais")
                                .create();
                        State constrainName = c.newState(ultimoIDdisponivel());
                        
                    }
                    else if(verificarAlt(m) != null){
                        tipoAnterior = m.getRecebendo().getTipo();

                    }
                    else{
                        tipoAnterior = m.getRecebendo().getTipo();
                        org = c.newState(idrelativoClassifier(m.getEnviando().getXmiID()));
                        dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                        if(m.getRecebendo().getTipo().equals("actor")){
                            State resultesperados = c.newState(ultimoIDdisponivel());
                            resultesperados.setAsInitial();
                            c.buildTransition(resultesperados, org)
                                    .setLabel("Resultados Esperados")
                                    .create();
                        }else{
                            State passos = c.newState(ultimoIDdisponivel());
                            passos.setAsInitial();
                            c.buildTransition(passos, org)
                                    .setLabel("Passos")
                                    .create();
                        }
                        c.buildTransition(org, dst)
                                .setLabel(labelMsg(m))
                                .create();
                    }
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////
                }else{
                    if(verificarLoop(m) != null){

                    }
                    else if(verificarAlt(m) != null){

                    }
                    else{
                        if(m.getRecebendo().getTipo().equals(tipoAnterior)){
                            org = dst;
                            dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                            c.buildTransition(org, dst)
                                    .setLabel(labelMsg(m))
                                    .create();
                        }else{
                            org = dst;
                            if(m.getRecebendo().getTipo().equals("actor")){
                                State resul = c.newState(ultimoIDdisponivel());
                                dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                c.buildTransition(org, resul)
                                        .setLabel("Resultados Esperados")
                                        .create();
                                c.buildTransition(resul, dst)
                                        .setLabel(labelMsg(m))
                                        .create();
                            }else{
                                State resul = c.newState(ultimoIDdisponivel());
                                dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                c.buildTransition(org, resul)
                                        .setLabel("Passos")
                                        .create();
                                c.buildTransition(resul, dst)
                                        .setLabel(labelMsg(m))
                                        .create();
                            }
                        }
                    }
                    tipoAnterior = m.getRecebendo().getTipo();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        return c;
    }
    
    private String labelMsg(Mensagem m){
        return m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getMsg().replaceAll("\\+", "");
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

    private State[] verificarLoop(Mensagem m) {
        State[] inicioEfim = null;
        State ini = null, fim = null;
        String msg = labelMsg(m);
        for(InteractionFragments intf : loopsOuAlts){
            for(CombinedFragments combf : intf.getCombinedFrags()){
                if(combf.getOperator().equals("loop")){
                    String msgId = combf.getInteractionOperands().get(0).getXmiIdRefMsg().get(0);
                    if(msgId.equals(m.getXmiIdMsg())){
                        inicioEfim = new State[2];
                        ini = c.newState(idrelativoClassifier(m.getEnviando().getXmiID()));
                        fim = c.newState(idrelativoClassifier(combf.getInteractionOperands()
                                .get(0).getXmiIdRefMsg()
                                .get(combf.getInteractionOperands().get(0).getXmiIdRefMsg().size()-1)));
                        inicioEfim[0] = ini;
                        inicioEfim[1] = fim;
                    }
                }
            }
        }
        return inicioEfim;
    }

    private Object verificarAlt(Mensagem m) {
        return null;
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
    
}
