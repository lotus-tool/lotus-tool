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
import br.uece.lotus.model.ParallelState;
import br.uece.lotus.uml.api.ds.BlockDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.api.viewer.hMSC.HmscViewImpl;
import br.uece.lotus.uml.api.viewer.hMSC.StandardModelingView;
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;
import br.uece.lotus.uml.designer.windowLTS.Layouter;
import br.uece.lotus.viewer.TransitionView;
import javafx.scene.control.Alert;

import java.util.*;
import java.util.stream.StreamSupport;

/**
 *
 * @author Bruno Barbosa
 */
public class GeneralLTSMaker {
    
    private final List<Component> ltsGerados;
    private Component geral;
    private final List<Hmsc> listHmsc;
    private final List<ComponentDS> listBmsc;
    private HashMap<String,List<Component>> ltsActors;
    private ArrayList<HmscLigacao> visitados;
    private ProjectExplorerPluginDS pep;
    StandardModelingView mview;
    Layouter l = new Layouter();

    private final List<Transition> listSelf;
    private final List<Hmsc> listVisitados;
    private final List<Transition> listCircTransition;
    private final HashMap<Hmsc, List<Transition> > listHmsc_Inicial;
    private static HashMap<Transition, TransitionMSC> listTransition_Relation = new HashMap<>();
    
    public GeneralLTSMaker(List<Hmsc> listHmsc, List<ComponentDS> listBmsc, List<Component> ltsGerados, StandardModelingView mViewer, ProjectExplorerPluginDS projectExplorerPluginDS){
        this.ltsGerados = ltsGerados;
        this.listHmsc = listHmsc;
        this.listBmsc = listBmsc;
        this.pep = projectExplorerPluginDS;
        ltsActors = new HashMap<>();
        visitados = new ArrayList<>();
        geral = new Component();
        mview = mViewer;

        this.listSelf = new ArrayList<>();
        this.listVisitados = new ArrayList<>();
        this.listCircTransition = new ArrayList<>();

        this.listHmsc_Inicial = new HashMap<>();
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
            e.printStackTrace();
//            throw new RuntimeException(e.getMessage());
        }
        
        mview.getComponentBuildDS().createListLTS(preComposicao);
        //Faz a composicao parelela dos LTS completo
//        geral = ParallelComposition(preComposicao);
        ParallelComposition(this.ltsGerados);
        
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

    private void ParallelComposition(List<Component> Components){
        int tam = Components.size();
        if (tam < 2) {
            throw new RuntimeException("Select at least 2(two) components!");
        }
        Hmsc inicial = mview.getComponentBuildDS().getHmsc_inicial();

        List<Component> comp = new ArrayList<Component>();


        // Clono para n interferir nos lts gerados
        for(Component c : Components){
            try {
                    comp.add(c.clone());
            }catch(Exception e){

            }
        }
        criar_list_initial();
        Component component = pegar_lts(inicial,ltsGerados);
/*
        List<Transition> aux_list = new ArrayList<>();
        for(Transition transition : component.getInitialState().getOutgoingTransitionsList())
            try{
                aux_list.add(transition.clone());
            }catch (Exception e){
        }
        listHmsc_Inicial.putValue(inicial, aux_list);
*/


        for(State state : component.getStatesList()){
            System.out.println("State é: "+state.getLabel());
            int transition_count = state.getOutgoingTransitionsCount();
            for(int i = 0; i < transition_count; i++){
                Transition transition = state.getOutgoingTransitionsList().get(i);
                try {
                    geral.remove(transition);
                    geral.add(transition.clone());
                }catch (Exception e){

                }
                System.out.print("Transition é: "+transition.getLabel()+"\t");
            }

            geral.add(state);
            System.out.println();

        }
        listVisitados.add(inicial);

        compor2(inicial);

        if(mview.getComponentBuildDS().getProb() > 0){
            adicionar_probabilidades();
        }

        System.out.println("AGORA É: "+listTransition_Relation.size());

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

        Component a = pegar_lts(hmsc, components);
        Hmsc h = null;
        TransitionMSC t = null;
        for(TransitionMSC transitionMSC : hmsc.getOutgoingTransitionsList()){
            if(transitionMSC.getDestiny() instanceof Hmsc) {
                h = (Hmsc) transitionMSC.getDestiny();
            }else{
                h = ((HmscViewImpl) transitionMSC.getDestiny()).getHMSC();
            }


            t = transitionMSC;
            if(hmsc == h) {
                this.listSelf.add(new Component().buildTransition(a.getFinalState(), a.getInitialState())
                        .setViewType(1)
                        .setLabel(a.getFinalState().getIncomingTransitionsList().get(0).getLabel())
                        .create());
                continue;
            }
            if(!listVisitados.contains(h)) {
                compor(h, components);
            }

            Component b = pegar_lts(h, components);

            System.out.println("A é: "+ a.getName());
            System.out.println("B é: "+ b.getName());

            if(listVisitados.contains(h)) {

                for(Transition transition_circ : b.getInitialState().getOutgoingTransitionsList()) {
                    Transition transition = new Component().buildTransition(a.getFinalState(), transition_circ.getDestiny())
                            .setLabel(transition_circ.getLabel())
                            .setViewType(1)
                            .create();
                    System.out.println("CIRC: "+transition_circ.getLabel()+ "  DST: "+transition_circ.getDestiny().getID());
                    this.listCircTransition.add(transition);
                }
                continue;

            }
            add_lts_in_lts(a, b);
            listVisitados.add(h);

            int count = b.getInitialState().getOutgoingTransitionsCount();
            a.getFinalState().setFinal(false); // Sempre é o B que vai ter estado final O (E).
            for(int i = 0 ; i < count ;i++){ // Elimina o inicial e linka o A as saidas do inicial de B
                Transition transition = b.getInitialState().getOutgoingTransitionsList().get(i);
                if(!a.getTransitionsList().contains(transition)){
                    a.buildTransition(a.getFinalState(), transition.getDestiny())
                            .setViewType(1)
                            .setLabel(transition.getLabel())
                            .create();
                }
                a.getTransitionsList().remove(transition);
            }
        }


        if( h == null && t == null){
            return;
        }

    }

    private void criar_list_initial() {
        for(Hmsc hmsc : mview.getComponentBuildDS().getBlocos()){
            Component component = pegar_lts(hmsc,ltsGerados);
            List<Transition> aux_lisTransitions = new ArrayList<>();
            for(Transition transition : component.getInitialState().getOutgoingTransitionsList()){
                try{
                    aux_lisTransitions.add(transition.clone());
                }catch(Exception e){

                }
            }
            listHmsc_Inicial.put(hmsc,aux_lisTransitions);
        }
    }

    private void convergir_estado_final(Component component){
        int id = component.getStatesCount() -1;
        System.out.println("ID é: "+id);
        int states_count = component.getStatesCount();
        for (int i = 0;i < states_count; i++){
            State state = component.getStatesList().get(i);
            if(state.getID() != id){
                int transition_count = state.getOutgoingTransitionsCount();
                for(int j = 0; j < transition_count; j++){
                    Transition transition = state.getOutgoingTransitionsList().get(j);
                    System.out.println("SOURCE: "+transition.getSource() == null);
                    System.out.println("DESTINY: "+transition.getDestiny().getLabel());
                    if (transition.getDestiny().isFinal()) {
                        component.buildTransition(state, component.getStateByID(id))
                                .setLabel(transition.getLabel())
                                .setViewType(1)
                                .create();
                        // Remover a transição do component e decrementar ambos contadores, J e transition_count
                        component.getTransitionsList().remove(transition);
                        j--;
                        transition_count--;
                        break;
                    }
                }
            }
            if(state.isFinal()){
                component.getStatesList().remove(state);
                i--;
                states_count--;
            }
        }
        component.getStateByID(id).setFinal(true);
    }

    private void add_lts_in_lts(Component a, Component b){
        // Verificar se já existe o estado em algum component e a trasição também!
        State src;
        List<State> visitados = new ArrayList<State>();
        Iterator it = b.getStates().iterator();
        while (it.hasNext()) {
            src = (State) it.next();
            if(visitados.contains(src)){
                continue;
            }
            int count = src.getOutgoingTransitionsCount();
            for (int i = 0; i < count;i++) {
                Transition t = src.getOutgoingTransitionsList().get(i);
                t.setValue("view.type",1);
                if(a.getTransitionByLabel(t.getLabel()) == null){
                    a.add(t.getDestiny());
                    a.add(t);
                }
            }
            if(count == 0){
                a.add(src);
            }
            visitados.add(src);
        }
        //a.getStatesList().remove(a.getFinalState());
    }

    private void compor2(Hmsc hmsc){
        Hmsc hmsc_next = null;
        for(TransitionMSC transitionMSC : hmsc.getOutgoingTransitionsList()){
            if(transitionMSC.getDestiny() instanceof Hmsc) {
                hmsc_next = (Hmsc) transitionMSC.getDestiny();
            }else{
                hmsc_next = ((HmscViewImpl) transitionMSC.getDestiny()).getHMSC();
            }

            // Realizar Checagem se existe a ação no estado aux.getFinal, se sim, pular ao próximo estado que aquela trasição leva e voltar, séria como adicionar
            // transição invertendo o Source e o Destiny.

            if(hmsc == hmsc_next){ // self-Transition
                Component aux = pegar_lts(hmsc_next,ltsGerados);
                List<Transition> selfs = new ArrayList<>();
                for(Transition t : aux.getInitialState().getOutgoingTransitionsList()) {
                    if(!selfs.contains(t)) {
                        Transition transition = geral.buildTransition(aux.getFinalState(), t.getDestiny())
                                .setLabel(t.getLabel())
                                .setViewType(1)
                                .setProbability(transitionMSC.getProbability())
                                .create();
                        listTransition_Relation.put(transition, transitionMSC);
                        selfs.add(t);
                    }
                }
                continue;
            }

            Component next = pegar_lts(hmsc_next, ltsGerados);

            if(!listVisitados.contains(hmsc_next) && hmsc_next != null){ // Se o próximo ainda n adicionado

                for(State state : next.getStatesList()){
                    int transition_count = state.getOutgoingTransitionsCount();
                    for(int i = 0; i < transition_count; i++){
                        Transition transition = state.getOutgoingTransitionsList().get(i);
                        try { // Bloco Feito, pois no Metodo ADD é adicionado novamente a transição na lista de transições do Source e do Destiny.
                            geral.remove(transition);
                            geral.add(transition.clone());
                        }catch (Exception e){

                        }
                        System.out.println("[1] Transition é: "+transition.getLabel()+"\t");
                    }
                    geral.add(state);

                }
                listVisitados.add(hmsc_next);
                compor2(hmsc_next);

                Component current = pegar_lts(hmsc,ltsGerados);
                current.getFinalState().setFinal(false);
                int t_count = next.getInitialState().getOutgoingTransitionsCount();
                for(int j = 0; j < t_count; j++) { // Cria a Ligação entre os HMSCs e retira o estado inicial.
                    Transition transition = next.getInitialState().getOutgoingTransitionsList().get(j);
                    Transition t = geral.buildTransition(current.getFinalState(), transition.getDestiny())
                            .setViewType(1)
                            .setLabel(transition.getLabel())
                            .setProbability(transitionMSC.getProbability())
                            .create();
                    listTransition_Relation.put(t, transitionMSC);
                    System.out.println("[2] Transition é: "+transition.getLabel()+"\t"+t.getLabel());


                    j--;
                    t_count--;
                }

                geral.remove(next.getInitialState());

              //  listHmsc_Inicial.putValue(hmsc_next, aux_list);
            }else if(hmsc_next != null){
                Component current = pegar_lts(hmsc,ltsGerados);
                List<Transition> next_in_circ = listHmsc_Inicial.get(hmsc_next);
                current.getFinalState().setFinal(false);


                for(Transition transition : next_in_circ) {
                    Transition t = geral.buildTransition(current.getFinalState(), transition.getDestiny())
                            .setLabel(transition.getLabel())
                            .setViewType(1)
                            .setProbability(transitionMSC.getProbability())
                            .create();
                    listTransition_Relation.put(t, transitionMSC);
                    System.out.println("[3] Transition é: "+transition.getLabel()+"\t"+t.getLabel());
                }

            }


        }
    }

    private void adicionar_probabilidades(){
        State erro = geral.newState(geral.getStatesCount()+1);
        geral.setErrorState(erro);
        for(State state : geral.getStatesList()){
            double prob_total = 0.0;
            int t_count = state.getOutgoingTransitionsCount();
            int n_states_prob_null = 0;

            for(int i = 0 ; i < t_count; i++){
                Transition transition = state.getOutgoingTransitionsList().get(i);
                System.out.println("TRANS NAME: "+ transition.getLabel());
                if(transition.getProbability() != null) {
                    prob_total += transition.getProbability();
                }else{
                    n_states_prob_null++;
                }
            }
            if(prob_total == 0.0){
                for(int j = 0; j < t_count; j++){
                    state.getOutgoingTransitionsList().get(j).setProbability(1.0 / t_count);
                }
            }else if(prob_total < 1.0){
             //   double new_probability = ( 1.0 - prob_total ) / (n_states_prob_null + 1); ( COM ERRO )
                if( n_states_prob_null != 0 ) {double new_probability = ( 1.0 - prob_total ) / (n_states_prob_null);}
                for(Transition transition : state.getOutgoingTransitionsList()){
                    if(transition.getProbability() == null){
                        transition.setProbability( new_probability);
                    }
                }
/*
                geral.buildTransition(state, erro)
                        .setViewType(1)
                        .setProbability(new_probability)
                        .setLabel("ERROR")
                        .create();
*/
            }

        }
    }

}
