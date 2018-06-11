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
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;
import br.uece.lotus.uml.designer.windowLTS.Layouter;

import java.util.*;
import java.util.stream.StreamSupport;

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
    private ArrayList<HmscLigacao> visitados;
    private ProjectExplorerPluginDS pep;
    StandardModelingView mview;
    Layouter l = new Layouter();
    
    public MakeLTSGeneral(List<Hmsc> listHmsc, List<ComponentDS> listBmsc, List<Component> ltsGerados,StandardModelingView mViewer, ProjectExplorerPluginDS projectExplorerPluginDS){
        this.ltsGerados = ltsGerados;
        this.listHmsc = listHmsc;
        this.listBmsc = listBmsc;
        this.pep = projectExplorerPluginDS;
        ltsActors = new HashMap<>();
        visitados = new ArrayList<>();
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
        try {
            int id_lifes = 0;
            for (String ator : allActors) {
                id_lifes++;
                Component linhaDeVidaAtor = new Component();
                linhaDeVidaAtor.setName("Life " + ator);
                linhaDeVidaAtor.setID ((pep.getSelectedProjectDS().getID() * 1000) + 400 + id_lifes);
                Hmsc inicial = listHmsc.get(0);
                montagemRecursiva(inicial, ator, linhaDeVidaAtor, null, false);
                
                l.layout(linhaDeVidaAtor);
                preComposicao.add(linhaDeVidaAtor);
                
                visitados.clear(); //Limpa para o proximo ator criar a linha de vida.
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
        mview.getComponentBuildDS().createListLTS(preComposicao);
        //Faz a composicao parelela dos LTS completo
//        geral = ParallelComposition(preComposicao);
        geral = ParallelComposition(this.ltsGerados);
        
        return geral;
    }
    
    private void montagemRecursiva(Hmsc hmsc, String ator, Component linhaDeVidaAtor, State ligacao,boolean foiVisitado){
        List<Component> atores = ltsActors.get(hmsc.getLabel());
        State inicialDoHmsc = null;
        
        for(Component c : atores){
            if(c.getName().equals(ator)){
                State src = null;
                State dst = null;
                //Inicio do LTS 
                if(linhaDeVidaAtor.getStatesCount() == 0){
                    System.out.println("Chegou no primeiro hmsc do ator. hMSC: "+hmsc.getLabel()+" Ator: "+ator);
                    for(Transition t : c.getTransitions()){
                        if(src == null){
                            src = linhaDeVidaAtor.newState(t.getSource().getID());
                            inicialDoHmsc = src;
                            linhaDeVidaAtor.setInitialState(src);
                        }
                        if(dst == null){
                            dst = linhaDeVidaAtor.newState(t.getDestiny().getID());
                            linhaDeVidaAtor.buildTransition(src, dst)
                                .setGuard(t.getGuard())
                                .setProbability(t.getProbability())
                                .setLabel(t.getLabel())
                                .create();
                            src = dst;
                            dst = null;
                        }
                    }
                //Reconhece uma continuacao em outro hMSC
                }else{
                    System.out.println("Reconhece uma continuacao em: "+hmsc.getLabel());
                    if(ligacao == null){
                        ligacao = linhaDeVidaAtor.getStateByID(linhaDeVidaAtor.getStatesCount()-1);
                        inicialDoHmsc = ligacao;
                    }
                    if(!foiVisitado){
                        src = ligacao;
                        inicialDoHmsc = src;
                        System.out.println("Hmsc: "+hmsc.getLabel()+" Ainda nao Visitado!!!");
                        System.out.println("ligacao eh: "+src.getLabel());
                        for(Transition t : c.getTransitions()){
                            dst = linhaDeVidaAtor.newState(linhaDeVidaAtor.getStatesCount());
                            linhaDeVidaAtor.buildTransition(src, dst)
                                    .setGuard(t.getGuard())
                                    .setProbability(t.getProbability())
                                    .setLabel(t.getLabel())
                                    .create();
                            src = dst;
                        }
                        System.out.println("ultima ligacao: "+src.getLabel()+ " do ator :"+ator);
                    //Continuacao em um hMSC ja visitado
                    }else{
                        System.out.println("Hmsc: "+hmsc.getLabel()+" Ja Visitado");
                        inicialDoHmsc = ligacao;
                        src = linhaDeVidaAtor.getStateByID(linhaDeVidaAtor.getStatesCount()-2);//penultimo
                        dst = resgateStateInicial(hmsc);
                        
                        State sUltimo = linhaDeVidaAtor.getStateByID(linhaDeVidaAtor.getStatesCount()-1);
                        Transition tUltima = src.getTransitionTo(sUltimo);
                        
                        if(dst != null && tUltima != null){
                            linhaDeVidaAtor.buildTransition(src, dst)
                                    .setLabel(tUltima.getLabel())
                                    .setGuard(tUltima.getGuard())
                                    .setProbability(tUltima.getProbability())
                                    .setViewType(1)
                                    .create();
                            
                            linhaDeVidaAtor.remove(sUltimo);
                            setNewStateFinal(hmsc, dst);
                            ajustarIDs(linhaDeVidaAtor);
                        }      
                        System.out.println("ultima ligacao: "+src.getLabel()+ " do ator :"+ator);
                    }
                }
            }
        }
        //verificar saidas do hmsc
        Component aux = linhaDeVidaAtor;
        State liga = aux.getStateByID(aux.getStatesCount()-1);
        
        if(!visitado(hmsc)){
            visitados.add(new HmscLigacao(hmsc, inicialDoHmsc, liga));
        }
        
        if(!foiVisitado){
            for(TransitionMSC t :hmsc.getOutgoingTransitionsList()){
                Hmsc dst;
                try {
                    dst = ((HmscView) t.getDestiny()).getHMSC();
                } catch (Exception e) {
                    dst = (Hmsc) t.getDestiny();
                }
                System.out.println("tem uma saida para o hmsc: "+dst.getLabel());
                if(!visitado(dst)){
                    montagemRecursiva(dst, ator, linhaDeVidaAtor, resgateStateFinal(hmsc),false);
                }else{
                    montagemRecursiva(dst, ator, linhaDeVidaAtor, resgateStateFinal(hmsc),true);
                }
            }
        }
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
        }
        build.setName(ator);//referencia o ator do respectivo hmsc
        return build;
    }

    private Component ParallelComposition(List<Component> Components){
        int tam = Components.size();
        if (tam < 2) {
            throw new RuntimeException("Select at least 2(two) components!");
        }
            Hmsc inicial = mview.getComponentBuildDS().getHmsc_inicial();
        List<Component> comp = new ArrayList<Component>();
        for(Component c : Components){
            try {
                    comp.add(c.clone());
            }catch(Exception e){

            }
        }
        compor(inicial, comp);
        Component component = new Component();
        try {
            component = pegar_lts(inicial, comp);
        }catch(Exception e){

        }
        ajustarIDs(component);
        return component;
    }

    private Component pegar_lts (Hmsc h, List<Component> c){
        Component a = null;
        for(Component comp : c){ // Pega o LTS
            if(comp.getName().equals("LTS "+h.getLabel())){
                a = comp;
             //   c.remove(a);
            }
        }
        return a;
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
    
    private void ajustarIDs(Component build) {
        int id = 0;
        List<State> visitados = new ArrayList<>();
        Iterator<State> it = build.getStates().iterator();

        while(it.hasNext()){
            State s = it.next();
            if(!visitados.contains(s)){
                s.setNameState("" + id);
                s.setLabel(""+id);
                s.setID(id++);
                visitados.add(s);
            }else{
                it.remove();
            }
        }
    }
    
    private boolean visitado(Hmsc hmsc){
        boolean viu = false;
        for(HmscLigacao visto : visitados){
            if(visto.getHmsc() == hmsc){
                viu = true;
            }
        }
        return viu;
    }
    
    private State resgateStateFinal(Hmsc hmsc){
        State state = null;
        for(HmscLigacao h : visitados){
            if(h.getHmsc() == hmsc){
                state = h.getUltimo();
            }
        }
        return state;
    }
    
    private State resgateStateInicial(Hmsc hmsc){
        State state = null;
        for(HmscLigacao h : visitados){
            if(h.getHmsc() == hmsc){
                state = h.getInicial();
            }
        }
        return state;
    }
    
    private void setNewStateFinal(Hmsc hmsc, State s){
        for(HmscLigacao h : visitados){
            if(h.getHmsc() == hmsc){
                h.setUltimo(s);
            }
        }
    }
    
    private class HmscLigacao{
        Hmsc hmsc;
        State inicial;
        State ultimo;

        public HmscLigacao(Hmsc hmsc, State inicial, State ultimo) {
            this.hmsc = hmsc;
            this.inicial = inicial;
            this.ultimo = ultimo;
        }

        public Hmsc getHmsc() {
            return hmsc;
        }

        public void setHmsc(Hmsc hmsc) {
            this.hmsc = hmsc;
        }

        public State getInicial() {
            return inicial;
        }

        public void setInicial(State inicial) {
            this.inicial = inicial;
        }

        public State getUltimo() {
            return ultimo;
        }

        public void setUltimo(State ultimo) {
            this.ultimo = ultimo;
        }
        
    }

    private void compor(Hmsc hmsc, List<Component> components){
     //   this.ltsGerados;
     //   this.listHmsc;

        Component a = pegar_lts(hmsc, components);
        System.out.println("A é: "+ a.getName());
        Hmsc h = null;
        TransitionMSC t = null;

        for(TransitionMSC transitionMSC : hmsc.getOutgoingTransitionsList()){
            h = (Hmsc)transitionMSC.getDestiny();
            t = transitionMSC;
            compor(h, components);

            Component b = pegar_lts(h, components);
            System.out.println("A final state é: "+a.getFinalState().getLabel());

            b.getInitialState().clearIniFin();
            a.getInitialState().clearIniFin();
            a.getFinalState().clearIniFin();

                a.add(b.getInitialState());
                a.buildTransition(a.getFinalState(), b.getInitialState())
                        .setLabel(t.getLabel())
                        .setViewType(1)
                        .create();

            // Remover Erro do Componente com mais de 1 final e mais de 1 inicial



            add_lts_in_lts(a,b);

        }

        if( h == null && t == null){
            return;
        }

        // Antes adicionar todos os Estados de b em a, bem como as transições, basicamente é adicionar o lts b no lts a
        // Trocar var LTS gerados por um List com os LTS copiados, para não interferir nos fragments.



        // Cria a ligação entre o LTS dos Hmsc's

/*
        State final_state = null;
        for(State state : b.getStates()) {
            for (Transition transition : state.getOutgoingTransitions()) {
                a.buildTransition(state, transition.getDestiny())
                        .setLabel(transition.getLabel())
                        .setViewType(1)
                        .create();
            }
            if(state.isFinal()){
                System.out.println("Tem final sim");
            }
            final_state = state;
        }
        a.setFinalState(final_state);
*/
    }

    private void add_lts_in_lts(Component a, Component b){
        State src;
        List<State> visitados = new ArrayList<State>();
        Iterator it = b.getStates().iterator();
        //System.out.println("Quantidade de Estadoss: " + StreamSupport.stream(b.getStates().spliterator(), false).count());
        while (it.hasNext()) {
            src = (State) it.next();
//            if(visitados.contains(src)){
 //               continue;
  //          }
            int count = src.getOutgoingTransitionsCount();
            for (int i = 0; i < count;i++) {
                Transition t = src.getOutgoingTransitionsList().get(i);
                System.out.println("Aqui vei" + t.getDestiny().getLabel());
                t.getDestiny().clearIniFin();
                t.setValue("view.type",1);
                a.add(t.getDestiny());
                a.add(t);
            }
            if(count == 0){
                a.add(src);
            }
//            visitados.add(src);
        }
    }
}
