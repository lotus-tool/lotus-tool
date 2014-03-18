/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.parser;

import br.uece.lotus.model.ComponentModel;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author emerson
 */
public interface GrafoMarshaller {
    void marshaller(ComponentModel g, OutputStream output) throws IOException;
}
