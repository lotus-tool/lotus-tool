/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.project;

import br.uece.lotus.uml.api.ds.ProjectDS;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Bruno Barbosa
 */
public interface ProjectDSSerializer {
    
    ProjectDS parseStream(InputStream stream) throws Exception;

    void toStream(ProjectDS p, OutputStream stream) throws Exception;
}
