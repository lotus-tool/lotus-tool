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
abstract class DiagramaSequenciaFactory {
    
    abstract DiagramaSequencia build(File file);
    
}
