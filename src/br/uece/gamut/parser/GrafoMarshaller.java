/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.parser;

import br.uece.gamut.Grafo;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author emerson
 */
public interface GrafoMarshaller {
    void marshaller(Grafo g, OutputStream output) throws IOException;
}