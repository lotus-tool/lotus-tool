package br.uece.lotus.parser.lts;

import br.uece.lotus.model.ComponentModel;
import java.util.Map;

/**
 *
 * @author emerson
 */
public interface ContextoCompilacao {
    public static String TAG_PROCESSO_ID = "id";
    public static String TAG_PROCESSO_NOME = "nome";
    public static String TAG_PROCESSO_ORIGEM_ID = "origem_nome";
    public static String TAG_PROCESSO_DESTINO_ID = "destino_id";    
    public static String TAG_TRANSICAO_NOME = "transicao_nome";
    
    Map<String, Integer> getTabelaSimbolos();

    ComponentModel getGrafo();
    
    int gerarNovoId();
}
