/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.sequenceDiagram;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.uml.sequenceDiagram.xmi.CombinedFragments;
import br.uece.lotus.uml.sequenceDiagram.xmi.InteractionFragments;
import br.uece.lotus.uml.sequenceDiagram.xmi.InteractionOperand;
import br.uece.lotus.uml.sequenceDiagram.xmi.SuportLoopTCG;
import br.uece.lotus.viewer.TransitionView;
import java.util.ArrayList;
import java.util.Collection;
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
            List<String[]> loops = new ArrayList<>();
            String tipoAnterior = "";
            State org = null, dst = null;
            for(Mensagem m : comunicacao){
                ////////////////////////////////////////////////////////////////////////////////////////////////////////
                if(c.getInitialState()==null){
                    SuportLoopTCG loop = verificarLoop(m);
                    String alt = verificarAlt(m);
                    if(loop != null){// Fazendo para Loop Inicial no Ds
                        tipoAnterior = m.getRecebendo().getTipo();
                        State srcCond = c.newState(ultimoIDdisponivel());
                        srcCond.setAsInitial();
                        State constraintName = c.newState(ultimoIDdisponivel());
                        c.buildTransition(srcCond, constraintName)
                                .setLabel("Condicoes Iniciais")
                                .create();
                        State tipo = c.newState(ultimoIDdisponivel());
                        c.buildTransition(constraintName, tipo)
                                .setLabel(loop.getConstrainName())
                                .create();
                        org = c.newState(idrelativoClassifier(loop.getLoop()[0]));
                        dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                        if(tipoAnterior.equals("actor")){
                           c.buildTransition(tipo, org)
                                   .setLabel("Resultados Esperados")
                                   .create();
                        }else{
                            c.buildTransition(tipo, org)
                                   .setLabel("Passos")
                                   .create();
                        }
                        c.buildTransition(org, dst)
                                .setLabel(labelMsg(m))
                                .create();
                        loop.getLoop()[0] = loop.getConstrainName();
                        loops.add(loop.getLoop());
                        receberProximoAlt = dst;
                    }
                    else if(!alt.equals("")){// Fazendo para Alt Inicial no Ds
                        tipoAnterior = m.getRecebendo().getTipo();
                        switch(alt){
                            case "gancho":{
                                System.out.println("Entrou no gancho inicial a msg: "+labelMsg(m));
                                org = ultimoDaRamificacao;
                                State tipo = c.newState(ultimoIDdisponivel());
                                dst = tipo;
                                if(m.getRecebendo().getTipo().equals("actor")){
                                    c.buildTransition(org, dst)
                                            .setLabel("Resultados Esperados")
                                            .create();
                                }else{
                                    c.buildTransition(org, dst)
                                            .setLabel("Passos")
                                            .create();
                                }
                                if(stateExiste(c, idrelativoClassifier(m.getRecebendo().getXmiID()))){
                                    org = dst;
                                    dst = c.newState(ultimoIDdisponivel());
                                    c.buildTransition(org, dst)
                                            .setLabel(labelMsg(m))
                                            .create();
                                }else{
                                    org = dst;
                                    dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                    c.buildTransition(org, dst)
                                            .setLabel(labelMsg(m))
                                            .create();
                                }
                                tipoAnterior = m.getRecebendo().getTipo();
                                ultimoDaRamificacao = dst;
                                receberProximoAlt = dst;
                                break;
                            }
                        }
                    }
                    else{ // Fazendo para Normal
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
                        receberProximoAlt = dst;
                    }
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////
                }else{
                    SuportLoopTCG loop = verificarLoop(m);
                    String alt = verificarAlt(m);
                    if(loop != null){//Loop para o meio do Ds
                        State srcCond = c.newState(ultimoIDdisponivel());
                        org = dst;
                        c.buildTransition(org, srcCond)
                                .setLabel("Condicoes Iniciais")
                                .create();
                        State constraintName = c.newState(ultimoIDdisponivel());
                        c.buildTransition(srcCond, constraintName)
                                .setLabel(loop.getConstrainName())
                                .create();
                        State tipo = c.newState(ultimoIDdisponivel());
                        if(m.getRecebendo().getTipo().equals("actor")){
                            c.buildTransition(constraintName, tipo)
                                    .setLabel("Resultados Esperados")
                                    .create();
                        }else{
                            c.buildTransition(constraintName, tipo)
                                    .setLabel("Passos")
                                    .create();
                        }
                        org = tipo;
                        dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                        c.buildTransition(org, dst)
                                .setLabel(labelMsg(m))
                                .create();
                        loop.getLoop()[0] = loop.getConstrainName();
                        loops.add(loop.getLoop());
                        tipoAnterior = m.getRecebendo().getTipo();
                        receberProximoAlt = dst;
                    }
                    else if(!alt.equals("")){//Alt para o meio do Ds
                        switch(alt){
                            case "gancho":{
                                System.out.println("Entrou no gancho no meio do ds a msg: "+labelMsg(m));
                                org = ultimoDaRamificacao;
                                State tipo = c.newState(ultimoIDdisponivel());
                                dst = tipo;
                                if(m.getRecebendo().getTipo().equals("actor")){
                                    c.buildTransition(org, dst)
                                            .setLabel("Resultados Esperados")
                                            .create();
                                }else{
                                    c.buildTransition(org, dst)
                                            .setLabel("Passos")
                                            .create();
                                }
                                if(stateExiste(c, idrelativoClassifier(m.getRecebendo().getXmiID()))){
                                    org = dst;
                                    dst = c.newState(ultimoIDdisponivel());
                                    c.buildTransition(org, dst)
                                            .setLabel(labelMsg(m))
                                            .create();
                                }else{
                                    org = dst;
                                    dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                    c.buildTransition(org, dst)
                                            .setLabel(labelMsg(m))
                                            .create();
                                }
                                tipoAnterior = m.getRecebendo().getTipo();
                                ultimoDaRamificacao = dst;
                                receberProximoAlt = dst;
                                break;
                            }
                            case "primeiro":{
                                System.out.println("reconheceu o primeiro a msg: "+labelMsg(m));
                                State tipo = c.newState(ultimoIDdisponivel());
                                org = ultimoDaRamificacao;
                                dst = tipo;
                                if(m.getRecebendo().getTipo().equals("actor")){
                                    c.buildTransition(org, dst)
                                            .setLabel("Resultados Esperados")
                                            .create();
                                }else{
                                    c.buildTransition(org, dst)
                                            .setLabel("Passos")
                                            .create();
                                }
                                if(stateExiste(c, idrelativoClassifier(m.getRecebendo().getXmiID()))){
                                    org = dst;
                                    dst = c.newState(ultimoIDdisponivel());
                                    c.buildTransition(org, dst)
                                            .setLabel(labelMsg(m))
                                            .create();
                                }else{
                                    org = dst;
                                    dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                    c.buildTransition(org, dst)
                                            .setLabel(labelMsg(m))
                                            .create();
                                }
                                tipoAnterior = m.getRecebendo().getTipo();
                                ultimoDaRamificacao = dst;
                                receberProximoAlt = dst;
                                if(primeiroEhUltimo){
                                    pontaFinal = dst;
                                    ganchoAlt = null;
                                    ligacaoDosAlts();
                                    primeiroEhUltimo = false;
                                }
                                break;
                            }
                            case "continuaRamificacao":{
                                System.out.println("Continuou a ramificacao a msg: "+labelMsg(m));
                                org = ultimoDaRamificacao;
                                if(stateExiste(c, idrelativoClassifier(m.getRecebendo().getXmiID()))){
                                    dst = c.newState(ultimoIDdisponivel());
                                    if(m.getRecebendo().getTipo().equals(tipoAnterior)){
                                        c.buildTransition(org, dst)
                                                .setLabel(labelMsg(m))
                                                .create();
                                    }else{
                                        State tipo = c.newState(ultimoIDdisponivel());
                                        if(m.getRecebendo().getTipo().equals("actor")){
                                            c.buildTransition(org, tipo)
                                                    .setLabel("Resultados Esperados")
                                                    .create();
                                        }else{
                                            c.buildTransition(org, tipo)
                                                    .setLabel("Passos")
                                                    .create();
                                        }
                                        org = tipo;
                                        c.buildTransition(org, dst)
                                                .setLabel(labelMsg(m))
                                                .create();
                                    }
                                    receberProximoAlt = dst;
                                }else{
                                    dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                    if(m.getRecebendo().getTipo().equals(tipoAnterior)){
                                        c.buildTransition(org, dst)
                                                .setLabel(labelMsg(m))
                                                .create();
                                    }else{
                                        State tipo = c.newState(ultimoIDdisponivel());
                                        if(m.getRecebendo().getTipo().equals("actor")){
                                            c.buildTransition(org, tipo)
                                                    .setLabel("Resultados Esperados")
                                                    .create();
                                        }else{
                                            c.buildTransition(org, tipo)
                                                    .setLabel("Passos")
                                                    .create();
                                        }
                                        org = tipo;
                                        c.buildTransition(org, dst)
                                                .setLabel(labelMsg(m))
                                                .create();
                                    }
                                    receberProximoAlt = dst;
                                }
                                tipoAnterior = m.getRecebendo().getTipo();
                                ultimoDaRamificacao = dst;
                                break;
                            }
                            case "ultimo":{
                                System.out.println("ultima msg do alt : "+labelMsg(m));
                                org = ultimoDaRamificacao;
                                if(stateExiste(c, idrelativoClassifier(m.getRecebendo().getXmiID()))){
                                    if(m.getRecebendo().getTipo().equals(tipoAnterior)){
                                        dst = c.newState(ultimoIDdisponivel());
                                        c.buildTransition(org, dst)
                                                .setLabel(labelMsg(m))
                                                .create();
                                    }else{
                                        State tipo = c.newState(ultimoIDdisponivel());
                                        if(m.getRecebendo().getTipo().equals("actor")){
                                            c.buildTransition(org, tipo)
                                                    .setLabel("Resultados Esperados")
                                                    .create();
                                        }else{
                                            c.buildTransition(org, tipo)
                                                    .setLabel("Passos")
                                                    .create();
                                        }
                                        org = tipo;
                                        dst = c.newState(ultimoIDdisponivel());
                                        c.buildTransition(org, dst)
                                                .setLabel(labelMsg(m))
                                                .create();
                                    }
                                    receberProximoAlt = dst;
                                }else{
                                    if(m.getRecebendo().getTipo().equals(tipoAnterior)){
                                        dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                        c.buildTransition(org, dst)
                                                .setLabel(labelMsg(m))
                                                .create();
                                    }else{
                                        State tipo = c.newState(ultimoIDdisponivel());
                                        if(m.getRecebendo().getTipo().equals("actor")){
                                            c.buildTransition(org, tipo)
                                                    .setLabel("Resultados Esperados")
                                                    .create();
                                        }else{
                                            c.buildTransition(org, tipo)
                                                    .setLabel("Passos")
                                                    .create();
                                        }
                                        org = tipo;
                                        dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                        c.buildTransition(org, dst)
                                                .setLabel(labelMsg(m))
                                                .create();
                                    }
                                    receberProximoAlt = dst;
                                }
                                ultimoDaRamificacao = dst;
                                pontaFinal = dst;
                                receberProximoAlt = dst;
                                ganchoAlt = null;
                                dst = ultimoDaRamificacao;
                                ligacaoDosAlts();
                                pontasSoltas.clear();
                                break;
                            }
                        }
                        tipoAnterior = m.getRecebendo().getTipo();
                    }
                    else{// Normal no meio do Ds
                        if(m.getRecebendo().getTipo().equals(tipoAnterior)){
                            org = dst;
                            if(stateExiste(c, idrelativoClassifier(m.getRecebendo().getXmiID()))){
                                dst = c.newState(ultimoIDdisponivel());
                                c.buildTransition(org, dst)
                                        .setLabel(labelMsg(m))
                                        .create();
                            }else{
                                dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                c.buildTransition(org, dst)
                                        .setLabel(labelMsg(m))
                                        .create();
                            }
                            receberProximoAlt = dst;
                        }else{
                            org = dst;
                            if(stateExiste(c, idrelativoClassifier(m.getRecebendo().getXmiID()))){
                                State tipo = c.newState(ultimoIDdisponivel());
                                if(m.getRecebendo().getTipo().equals("actor")){
                                    c.buildTransition(org, tipo)
                                            .setLabel("Resultados Esperados")
                                            .create();
                                }else{
                                    c.buildTransition(org, tipo)
                                            .setLabel("Passos")
                                            .create();
                                }
                                org = tipo;
                                dst = c.newState(ultimoIDdisponivel());
                                c.buildTransition(org, dst)
                                        .setLabel(labelMsg(m))
                                        .create();
                                receberProximoAlt = dst;
                            }else{
                                State tipo = c.newState(ultimoIDdisponivel());
                                if(m.getRecebendo().getTipo().equals("actor")){
                                    c.buildTransition(org, tipo)
                                            .setLabel("Resultados Esperados")
                                            .create();
                                }else{
                                    c.buildTransition(org, tipo)
                                            .setLabel("Passos")
                                            .create();
                                }
                                org = tipo;
                                dst = c.newState(idrelativoClassifier(m.getRecebendo().getXmiID()));
                                c.buildTransition(org, dst)
                                        .setLabel(labelMsg(m))
                                        .create();
                                receberProximoAlt = dst;
                            }
                        }
                    }
                    
                    tipoAnterior = m.getRecebendo().getTipo();
                }
            }
            //Ligacao dos loops
            for(String[] s : loops){
                //String das transitions "s"
                State ini=null, fim=null;
                for(Transition t : c.getTransitions()){
                    if(t.getLabel().equals(s[0])){
                        ini = t.getSource();
                    }
                    if(t.getLabel().equals(s[1])){
                        fim = t.getDestiny();
                    }
                }
                c.buildTransition(fim,ini)
                        .setLabel("Condicoes Iniciais")
                        .setViewType(TransitionView.Geometry.CURVE)
                        .create();
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

    //--------------------------------------LOOP--------------------------------------------------------------------
    private SuportLoopTCG verificarLoop(Mensagem m) {
        SuportLoopTCG loop = null;
        String[] inicioEfim = null;
        String ini = "", fim = "";
        for(InteractionFragments intf : loopsOuAlts){
            for(CombinedFragments combf : intf.getCombinedFrags()){
                if(combf.getOperator().equals("loop")){
                    String msgId = combf.getInteractionOperands().get(0).getXmiIdRefMsg().get(0);
                    if(msgId.equals(m.getXmiIdMsg())){
                        inicioEfim = new String[2];
                        ini = labelMsg(m);
                        Mensagem m2 = msgLoopFim(combf.getInteractionOperands()
                                .get(0).getXmiIdRefMsg()
                                .get(combf.getInteractionOperands().get(0).getXmiIdRefMsg().size()-1));
                        fim = labelMsg(m2);
                        inicioEfim[0] = ini;
                        inicioEfim[1] = fim;
                        loop = new SuportLoopTCG(combf.getInteractionOperands().get(0).getInteractionConstraintName(), inicioEfim);
                    }
                }
            }
        }
        return loop;
    }

    //--------------------------------------------ALT----------------------------------------------------------------
    private State receberProximoAlt = null;
    private State ganchoAlt = null, pontaFinal = null;
    private State ultimoDaRamificacao = null;
    private Boolean primeiroEhUltimo = false;
    private List<Mensagem> pontasSoltas = new ArrayList<>();
    private String verificarAlt(Mensagem m) {
        for(InteractionFragments intf : loopsOuAlts){
            for(CombinedFragments combf : intf.getCombinedFrags()){
                if(combf.getOperator().equals("alt")){
                    for(int i=0;i<combf.getInteractionOperands().size();i++){
                        for(int j=0;j<combf.getInteractionOperands().get(i).getXmiIdRefMsg().size();j++){
                            String xmiId = combf.getInteractionOperands().get(i).getXmiIdRefMsg().get(j);
                            if(xmiId.equals(m.getXmiIdMsg())){
                                if(i==0 && j==0){//ponta inicial das ramificacoes
                                    State origem = receberProximoAlt;
                                    State destino = null;
                                    if(c.getInitialState() == null){
                                        origem = c.newState(ultimoIDdisponivel());
                                        origem.setAsInitial();
                                        destino = c.newState(ultimoIDdisponivel());
                                        c.buildTransition(origem, destino)
                                                .setLabel("Condicoes Iniciais")
                                                .create();
                                        ganchoAlt = destino;
                                        State constraintName = c.newState(ultimoIDdisponivel());
                                        String labelConstraint = combf.getInteractionOperands().get(i).getInteractionConstraintName();
                                        origem = destino;
                                        destino = constraintName;
                                        c.buildTransition(origem, destino)
                                                .setLabel(labelConstraint)
                                                .create();
                                        ultimoDaRamificacao = destino;
                                    }else{
                                        destino = c.newState(ultimoIDdisponivel());
                                        c.buildTransition(origem, destino)
                                                .setLabel("Condicoes Iniciais")
                                                .create();
                                        ganchoAlt = destino;
                                        State constraintName = c.newState(ultimoIDdisponivel());
                                        String labelConstraint = combf.getInteractionOperands().get(i).getInteractionConstraintName();
                                        origem = destino;
                                        destino = constraintName;
                                        c.buildTransition(origem, destino)
                                                .setLabel(labelConstraint)
                                                .create();
                                        ultimoDaRamificacao = destino;
                                    }
                                    if(j==combf.getInteractionOperands().get(i).getXmiIdRefMsg().size()-1){
                                        pontasSoltas.add(m);
                                    }
                                    return "gancho";
                                }
                                if(i!=0 && j==0){//primeiro elemento de cada ramificacao
                                    State constraintName = c.newState(ultimoIDdisponivel());
                                    String labelConstraint = combf.getInteractionOperands().get(i).getInteractionConstraintName();
                                    c.buildTransition(ganchoAlt, constraintName)
                                            .setLabel(labelConstraint)
                                            .create();
                                    ultimoDaRamificacao = constraintName;
                                    if(i==combf.getInteractionOperands().size()-1 && // ponta final das ramificacoes
                                        j==combf.getInteractionOperands().get(i).getXmiIdRefMsg().size()-1){
                                        primeiroEhUltimo = true;
                                    }
                                    else if(j==combf.getInteractionOperands().get(i).getXmiIdRefMsg().size()-1){
                                        pontasSoltas.add(m);
                                    }
                                    return "primeiro";
                                }
                                else if(i==combf.getInteractionOperands().size()-1 && // ponta final das ramificacoes
                                        j==combf.getInteractionOperands().get(i).getXmiIdRefMsg().size()-1){
                                    return "ultimo";
                                }
                                if(j>0){//mais de um na ramificacao tal
                                    if(j==combf.getInteractionOperands().get(i).getXmiIdRefMsg().size()-1){//ultimo elemento de cada ramificacao
                                        System.out.println("Entrou no add ramificacao em (continuacaoRami..)");
                                        pontasSoltas.add(m);
                                        return "continuaRamificacao";
                                    }else{
                                        return "continuaRamificacao";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }
    
    private void ligacaoDosAlts(){
        
        for(Mensagem msg : pontasSoltas){
            List<Transition> aux = new ArrayList<>();
            aux.addAll((Collection<? extends Transition>) c.getTransitions());
            for(Transition t : aux){
                if(labelMsg(msg).equals(t.getLabel())){
                    System.out.println("Transition ponta solta: "+t.getLabel());
                    c.buildTransition(t.getSource(), pontaFinal)
                            .setLabel(t.getLabel())
                            .create();
                    c.remove(t.getDestiny());
                }
            }
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

    private Mensagem msgLoopFim(String xmiId) {
        Mensagem m = null;
        for(Mensagem m2 : comunicacao){
            if(xmiId.equals(m2.getXmiIdMsg())){
                m = m2;
            }
        }
        return m;
    }
}
