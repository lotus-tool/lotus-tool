package br.uece.lotus.benchmark;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.project.ProjectSerializer;
import br.uece.lotus.project.ProjectXMLSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author emerson
 */
public class SerializationBenchmark {

    private static final int[] STATE_VALUES = {        
        1000,
        1500,
        2000,
        2500,        
        3000,
        3500,
        4000,
        4500,
        5000
    };
    private static final double[] TRANSITION_VALUES = {
       0, 0.01, 0.02, 0.03, 0.04, 0.05 
    };
    static ProjectSerializer serializer = new ProjectXMLSerializer();
    private static int N_MEDIA = 10;    

    public static void main(String[] args) throws Exception {
        System.out.println(serializer.getClass().getSimpleName() + " benchmark");
        System.out.println("-- start --");
        System.out.println("state factor\ttransition factor\ttime (ms)");
        for (int i = 0; i < STATE_VALUES.length; i++) {
            for (int j = 0; j < TRANSITION_VALUES.length; j++) {
                testMedia(STATE_VALUES[i], TRANSITION_VALUES[j]);
            }
            System.out.println("");
        }
        System.out.println("-- end --");
    }

    private static void testMedia(double stateFactor, double transitionFactor) throws Exception {
        double soma = 0;
        for (int i = 0; i < N_MEDIA; i++) {
            soma += test((int) stateFactor, transitionFactor);            
        }
        double media = soma / N_MEDIA;
        System.out.printf("%f\t%f\t%f\n", transitionFactor, stateFactor, media);
        System.gc();
    }

    private static double test(int stateFactor, double transitionFactor) throws Exception {
        Project p = createRandomProject(stateFactor, transitionFactor);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        serializer.toStream(p, output);
        long t0 = System.nanoTime();
        Project pr = serializer.parseStream(new ByteArrayInputStream(output.toByteArray()));
        long dt = System.nanoTime() - t0;
        if (!pr.getComponent(0).equals(p.getComponent(0))) {
            throw new IllegalStateException("project differents!");
        }
        return dt / 1_000_000_000.0;
    }

    private static Project createRandomProject(int stateFactor, double transitionFactor) {
        Project p = new Project();
        p.setName("p1");
        Component c = new Component();
        c.setAutoUpdateLabels(true);
        c.setName("c1");
        p.addComponent(c);

        c.newState(0);
        int transitionCount = 1;
        if (transitionFactor > 0) {
            transitionCount = (int) Math.ceil(stateFactor * transitionFactor);
        }
        for (int i = 1; i < stateFactor; i++) {
            c.newState(i);
            for (int j = 0; j < transitionCount; j++) {
                c.buildTransition(i - 1, i)
                        .setGuard("" + Math.random())
                        .setProbability(Math.random())
                        .setLabel("" + Math.random())
                        .create();
            }
        }        
        for (int j = 0; j < transitionCount; j++) {
            c.buildTransition(stateFactor - 1, 0)
                    .setGuard("" + Math.random())
                    .setProbability(Math.random())
                    .setLabel("" + Math.random())
                    .create();
        }
        return p;
    }

}
