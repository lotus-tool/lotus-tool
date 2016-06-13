/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.model.ParallelCompositor;
import br.uece.lotus.uml.api.ds.BlockDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.api.viewer.hMSC.StandardModelingView;
import br.uece.lotus.uml.designer.windowLTS.Layouter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class MakeLTSGeneral {
    
    private final List<Component> ltsGerados;
    private Component geral;
    private final List<Hmsc> listHmsc;
    private final List<ComponentDS> listBmsc;
    private HashMap<String,List<Component>> ltsActors;
    private ArrayList<State> pilhaDeLigacao;
    StandardModelingView mview;
    Layouter l = new Layouter();
    
    public MakeLTSGeneral(List<Hmsc> listHmsc, List<ComponentDS> listBmsc, List<Component> ltsGerados,StandardModelingView mViewer){
        this.ltsGerados = ltsGerados;
        this.listHmsc = listHmsc;
        this.listBmsc = listBmsc;
        ltsActors = new HashMap<>();
        pilhaDeLigacao = new ArrayList<>();
        geral = new Component();
        mview = mViewer;
    }
    
    public Component produce(){
        ArrayList<Component> preComposicao = new ArrayList<>();
        ArrayList<String> allActors = verificarPossiveisAtores();
       
        //Salva no hashmap os lts de cada ator referente ao hmsc 
        for(Hmsc hmsc : listHmsc){
            String key = hmsc.getLabel();
            ArrayList<Component> compAtores = new ArrayList<>();
            for(Component lts : ltsGerados){
                if(("LTS "+hmsc.getLabel()).equals(lts.getName())){
                    for(String ator : allActors){
                        compAtores.add(createLTS_By_Actor_In_Hmsc(ator,lts));
                    }
                }
            }
            ltsActors.put(key, compAtores);
        }
        
        //Monta o LTS completo de cada ator com base no conjunto de Hmsc
        for(String ator : allActors){
            Component linhaDeVidaAtor = new Component();
            linhaDeVidaAtor.setName("Life "+ator);
            
            Hmsc inicial = listHmsc.get(0);
            montagemRecursiva(inicial,ator,linhaDeVidaAtor, null);
            
            
            l.layout(linhaDeVidaAtor);
            System.out.println("Conseguiu fazer layout do: "+linhaDeVidaAtor.getName());
            preComposicao.add(linhaDeVidaAtor);
        }
        
        mview.getComponentBuildDS().createListLTS(preComposicao);//teste somente para ver se esta montando
        
        
        //Faz a composicao parelela dos LTS completo
        geral = ParallelComposition(preComposicao);
        
        return geral;
    }
    
    private void montagemRecursiva(Hmsc inicial, String ator, Component linhaDeVidaAtor, State ligacao){
        List<Component> atores = ltsActors.get(inicial.getLabel());
        
        for(Component c : atores){
            if(c.getName().equals(ator)){
                State src = null;
                State dst = null;
                if(linhaDeVidaAtor.getStatesCount() == 0){
                    System.out.println("chegou no primeiro hmsc");
                    for(Transition t : c.getTransitions()){
                        if(src == null){
                            src = linhaDeVidaAtor.newState(t.getSource().getID());
                            linhaDeVidaAtor.setInitialState(src);
                        }
                        if(dst == null){
                            dst = linhaDeVidaAtor.newState(t.getDestiny().getID());
                            linhaDeVidaAtor.buildTransition(src, dst)
                                //.setGuard(t.getGuard())
                                //.setProbability(t.getProbability())
                                .setLabel(t.getLabel())
                                .create();
                            src = dst;
                            dst = null;
                        }
                    }
                }else{
                    System.out.println("reconhece uma continuacao");
                    if(ligacao == null){
                        ligacao = linhaDeVidaAtor.getStateByID(linhaDeVidaAtor.getStatesCount()-1);
                        System.out.println("ligacao nula recebe: "+ligacao.getLabel());
                    }
                    src = ligacao;
                    System.out.println("ligacao eh: "+src.getLabel());
                    for(Transition t : c.getTransitions()){
                        dst = linhaDeVidaAtor.newState(linhaDeVidaAtor.getStatesCount());
                        linhaDeVidaAtor.buildTransition(src, dst)
                                .setLabel(t.getLabel())
                                .create();
                        src = dst;
                        System.out.println("ultima ligacao: "+ligacao.getLabel()+ " do ator :"+ator);
                    }
                }
            }
        }
        //verificar saidas do hmsc
        Component aux = null;
        aux = linhaDeVidaAtor;
        State liga = aux.getStateByID(aux.getStatesCount()-1);
        
        for(TransitionMSC t :inicial.getOutgoingTransitionsList()){
            pilhaDeLigacao.add(liga);
            Hmsc dst = ((HmscView) t.getDestiny()).getHMSC();
            System.out.println("tem uma saida para o hmsc: "+dst.getLabel());
            montagemRecursiva(dst, ator, linhaDeVidaAtor, pilhaDeLigacao.get(pilhaDeLigacao.size()-1));
            pilhaDeLigacao.remove(liga);
        }
    }

    private Component ParallelComposition(List<Component> Components){
        int tam = Components.size();
        if (tam < 2) {
            throw new RuntimeException("Select at least 2(two) components!");
        }
        Component a = Components.get(0);
        Component b = Components.get(1);
        Component c = new ParallelCompositor().compor(a, b);
        String name = a.getName() + " || " + b.getName();
        for(int i = 2; i < tam; i++){
            b = Components.get(i);
            c = new ParallelCompositor().compor(c, b);
            name += " || " + b.getName();
        }
        c.setName(name);
        return c;
    }

    private ArrayList<String> verificarPossiveisAtores() {
        ArrayList<String> nomes = new ArrayList<>();
        
        for(ComponentDS bmsc : listBmsc){
            for(BlockDS ator : bmsc.getBlockDS()){
                if(!nomes.contains(ator.getLabel())){
                    nomes.add(ator.getLabel());
                    System.out.println("ator: "+ator.getLabel());
                }
            }
        }
        
        return nomes;
    }

    private Component createLTS_By_Actor_In_Hmsc(String ator, Component lts) {
        Component build = new Component();
        State src = null;
        State dst = null;
        for(Transition t : lts.getTransitions()){
            String[] quebra = t.getLabel().split("\\.");
            if(ator.equals(quebra[0]) || ator.equals(quebra[1])){
                if(dst == null){
                    src = build.newState(t.getSource().getID());
                    dst = build.newState(t.getDestiny().getID());
                    build.buildTransition(src, dst)
                            .setLabel(t.getLabel())
                            .setGuard(t.getGuard())
                            .setProbability(t.getProbability())
                            .create();
                }else{
                    src = dst;
                    dst = build.newState(t.getDestiny().getID());
                    build.buildTransition(src, dst)
                            .setLabel(t.getLabel())
                            .setGuard(t.getGuard())
                            .setProbability(t.getProbability())
                            .create();
                }
            }
        }
        if (build.getStatesCount() > 0) {
            ajustarIDs(build);
            build.setInitialState(build.getStateByID(0));
            l.layout(build);
            System.out.println("Conseguiu fazer layout do: " + build.getName());
        }
        build.setName(ator);//referencia o ator do respectivo hmsc
        return build;
    }

    private void ajustarIDs(Component build) {
        int id = 0;
        for(State s : build.getStates()){
            s.setID(id++);
        }
    }
}
