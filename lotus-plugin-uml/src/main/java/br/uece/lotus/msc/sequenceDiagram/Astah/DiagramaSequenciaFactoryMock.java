/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.sequenceDiagram.Astah;

import java.io.File;

/**
 *
 * @author Bruno Barbosa
 */
public class DiagramaSequenciaFactoryMock extends DiagramaSequenciaFactory {
    
    @Override
    DiagramaSequencia build(File xmi) {
        SaxParse parse = new SaxParse();
        DiagramaSequencia ds = parse.fazerParse(xmi);
        System.out.println("Criou o model");
        return ds;
    }
}
