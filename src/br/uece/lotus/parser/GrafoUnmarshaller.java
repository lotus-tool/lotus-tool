/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.parser;

import br.uece.lotus.model.ComponentModel;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author emerson
 */
public interface GrafoUnmarshaller {
    public void unmarshaller(InputStream input, ComponentModel g) throws Exception;
}
