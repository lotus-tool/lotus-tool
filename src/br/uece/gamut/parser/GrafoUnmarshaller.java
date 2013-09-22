/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.parser;

import br.uece.gamut.Grafo;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author emerson
 */
public interface GrafoUnmarshaller {
    public void unmarshaller(InputStream input, Grafo g) throws IOException;
}
