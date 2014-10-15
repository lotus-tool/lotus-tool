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

    static ProjectSerializer serializer = new ProjectXMLSerializer();
    private static int N_MEDIA = 30;

    public static void main(String[] args) throws Exception {
        System.out.println(serializer.getClass().getSimpleName() + " benchmark");
        System.out.println("-- start --");
        System.out.println("factor\ttime (ms)");
        testMedia(1000);        
        testMedia(2000);
        testMedia(3000);
        testMedia(4000);
        testMedia(5000);
        System.out.println("-- end --");
    }

    private static void testMedia(int n) throws Exception {
        double soma = 0;
        for (int i = 0; i < N_MEDIA; i++) {
            soma += test(n);
        }
        double media = soma / N_MEDIA;
        System.out.printf("%d\t%f\n", n, media);
    }

    private static double test(int n) throws Exception {
        Project p = createRandomProject(n);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        serializer.toStream(p, output);
        long t0 = System.nanoTime();
        Project pr = serializer.parseStream(new ByteArrayInputStream(output.toByteArray()));
        long dt = System.nanoTime() - t0;
        if (!pr.getComponent(0).equals(p.getComponent(0))) {
            throw new IllegalStateException("project differents!");
        }
        return dt / 1_000_000.0;        
    }

    private static Project createRandomProject(int n) {
        Project p = new Project();
        p.setName("p1");
        Component c = new Component();
        c.setAutoUpdateLabels(true);
        c.setName("c1");
        p.addComponent(c);
        
        c.newState(0);
        for (int i = 1; i < n; i++) {
            c.newState(i);
            c.buildTransition(i - 1, i)
                    .setGuard("" + Math.random())
                    .setProbability(Math.random())
                    .setLabel("" + Math.random())
                    .create();
        }
        c.buildTransition(n - 1, 0)
                .setGuard("" + Math.random())
                .setProbability(Math.random())
                .setLabel("" + Math.random())
                .create();
        
        return p;
    }

}
