/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.project;

import br.uece.lotus.msc.api.model.msc.ProjectMSC;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Bruno Barbosa
 */
public interface ProjectMSCSerializer {
    
    ProjectMSC parseStream(InputStream stream) throws Exception;

    void toStream(ProjectMSC p, OutputStream stream) throws Exception;
}
