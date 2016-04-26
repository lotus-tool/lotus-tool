/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.app.project;

import br.uece.lotus.project.XMLWritter;
import br.uece.lotus.uml.api.ds.ProjectDS;
import br.uece.lotus.uml.api.project.ProjectDSSerializer;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Bruno Barbosa
 */
public class ProjectDSxmlSerializer implements ProjectDSSerializer{

    @Override
    public ProjectDS parseStream(InputStream stream) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void toStream(ProjectDS p, OutputStream stream) throws Exception {
        
        try {
            XMLWritter xml = new XMLWritter(stream);
            xml.begin("project-msc");
            xml.attr("version", "1.0");
            xml.attr("name", p.getName());
        } catch (Exception e) {
            System.out.println("falta implementar na classe: ProjectDSxmlSerializer");
        }
        //continuacao
    }
    
}
