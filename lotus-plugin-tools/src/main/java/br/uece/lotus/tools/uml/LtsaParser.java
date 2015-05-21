/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.uml;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tools.uml.xmi.InteractionFragments;
import br.uece.lotus.viewer.TransitionView;
import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class LtsaParser {
    
    private final List<Mensagem> comunicacao;
    private final List<TabelaReferenciaID> relativo;
    private final List<InteractionFragments> loopsOuAlts;
    private final Component c;
    private static int idDisponivel;

    public LtsaParser(List<Mensagem> comunicacao, List<TabelaReferenciaID> relativo, List<InteractionFragments> loopsOuAlts , Component c) {
        this.comunicacao = comunicacao;
        this.relativo = relativo;
        this.loopsOuAlts = loopsOuAlts;
        this.c = c;
        idDisponivel = this.relativo.size()+1;
    }
    
    public Component parseLTSA(){
        State inicial = null;
        State msgOrig = null,msgDest = null,msgAux = null;
        String tipoDeDestinoAnterior="";
        for(Mensagem m : comunicacao){
            ///////////////////////////////////Verificando caso Inicial////////////////////////////////////////////////
            if(inicial == null){
                inicial = c.newState(0);
                inicial.setAsInitial();
                if(m.getRecebendo().getTipo().equals("actor")){
                    tipoDeDestinoAnterior = "actor";
                    msgOrig = c.newState(idrelativo(m.getEnviando().getXmiID()));
                    Transition t = c.newTransition(inicial, msgOrig);
                    t.setLabel("Resultados Esperados");
                    msgDest = c.newState(idrelativo(m.getRecebendo().getXmiID()));
                    Transition t2 = c.newTransition(msgOrig, msgDest);
                    t2.setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getLabel());
                }
                else if(m.getRecebendo().getTipo().equals("class")){
                    tipoDeDestinoAnterior = "class";
                    msgOrig = c.newState(idrelativo(m.getEnviando().getXmiID()));
                    Transition t = c.newTransition(inicial, msgOrig);
                    t.setLabel("Passos");
                    msgDest = c.newState(idrelativo(m.getRecebendo().getXmiID()));
                    Transition t2 = c.newTransition(msgOrig, msgDest);
                    t2.setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getLabel());
                }
            /////////////////////////////////Depois do inicial ja existir////////////////////////////////////////////////////////////////
            }else{
                ////////////////////////////////////////////Casos que o Destino nao exista/////////////////////////////////////////////////////
                if(tipoDeDestinoAnterior.equals(m.getRecebendo().getTipo()) && !stateExiste(c, idrelativo(m.getRecebendo().getXmiID()))){
                    msgOrig = msgDest;
                    msgDest = c.newState(idrelativo(m.getRecebendo().getXmiID()));
                    c.buildTransition(msgOrig, msgDest)
                           .setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getLabel())
                           .create();
                }
                else if(!tipoDeDestinoAnterior.equals(m.getRecebendo().getTipo()) && !stateExiste(c, idrelativo(m.getRecebendo().getXmiID()))){
                    if("actor".equals(m.getRecebendo().getTipo())){
                       tipoDeDestinoAnterior = "actor";
                       msgAux = msgDest;
                       msgOrig = c.newState(idrelativo(m.getEnviando().getXmiID()));
                       c.buildTransition(msgAux, msgOrig)
                               .setLabel("Resultados Esperados")
                               .create();
                       msgDest = c.newState(idrelativo(m.getRecebendo().getXmiID()));
                       c.buildTransition(msgOrig, msgDest)
                               .setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getLabel())
                               .create();
                    }
                    else if("class".equals(m.getRecebendo().getTipo())){
                        tipoDeDestinoAnterior = "class";
                        msgAux = msgDest;
                        msgOrig = c.newState(idrelativo(m.getEnviando().getXmiID()));
                        c.buildTransition(msgAux, msgOrig)
                                .setLabel("Passos")
                                .create();
                        msgDest = c.newState(idrelativo(m.getRecebendo().getXmiID()));
                        c.buildTransition(msgOrig, msgDest)
                                .setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getLabel())
                                .create();
                    }
                }
                /////////////////////////////////////////////Casos que o Destino já exista////////////////////////////////////////////////////
                else if(tipoDeDestinoAnterior.equals(m.getRecebendo().getTipo()) && stateExiste(c, idrelativo(m.getRecebendo().getXmiID()))){
                    msgAux = msgDest;
                    msgDest = c.newState(ultimoIDdisponivel());
                    c.buildTransition(msgAux, msgDest)
                            .setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getLabel())
                            .create();
                }
                else if(!tipoDeDestinoAnterior.equals(m.getRecebendo().getTipo()) && stateExiste(c, idrelativo(m.getRecebendo().getXmiID()))){
                    if("actor".equals(m.getRecebendo().getTipo())){
                       tipoDeDestinoAnterior = "actor";
                       msgAux = msgDest;
                       msgOrig = c.newState(ultimoIDdisponivel());
                       c.buildTransition(msgAux, msgOrig)
                               .setLabel("Resultados Esperados")
                               .create();
                       msgDest = c.newState(ultimoIDdisponivel());
                       c.buildTransition(msgOrig, msgDest)
                               .setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getLabel())
                               .create();
                    }
                    else if("class".equals(m.getRecebendo().getTipo())){
                        tipoDeDestinoAnterior = "class";
                        msgAux = msgDest;
                        msgOrig = c.newState(ultimoIDdisponivel());
                        c.buildTransition(msgAux, msgOrig)
                                .setLabel("Passos")
                                .create();
                        msgDest = c.newState(ultimoIDdisponivel());
                        c.buildTransition(msgOrig, msgDest)
                                .setLabel(m.getEnviando().getNome()+"."+m.getRecebendo().getNome()+"."+m.getLabel())
                                .create();
                    }
                }
            }
        }
        for(State s : c.getStates()){
            System.out.println(s.getLabel()+"  "+s.getID());
        }
        return c;
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
    
    private int idrelativo(String id){
        int i = 0;
        for(TabelaReferenciaID tri : relativo){
            if(tri.getIdClassOrActor().equals(id)){
                i=tri.getIdRelativo();
            }
        }
        return i;
    }
    
    private int ultimoIDdisponivel(){
        return idDisponivel++;
    }
}
