package br.uece.gamut.parser;

import br.uece.gamut.parser.lts.LSACompiler;
import java.io.File;

/**
 *
 * @author emerson
 */
public class GrafoParserFacade {
        
    /**
     * Pela extensão do arquivo retorna o unmarshaller correto.
     * @param f
     * @return 
     */
    public static GrafoUnmarshaller getUnmarshallerByFile(File f) {
        if (f.getName().endsWith(".lts")) {
            return new LSACompiler();
        }
        return null;
    }
    
    public static GrafoMarshaller getMarshallerByFile(File f) {
        return null;
    }
    
}
