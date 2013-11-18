/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.parser.lts;

/**
 *
 * @author emerson
 */
public class LSAVisitor {
    public static final String PROCESS_DEF = "PROCESS_DEF";
    public static final String PROCESS_BODY = "PROCESS_BODY";
    public static final String LOCAL_PROCESS = "LOCAL_PROCESS";
    public static final String BASE_PROCESS = "BASE_PROCESS";
    
    public static String BASE_PROCESS_TAIL = "BASE_PROCESS_TAIL";
    public static final String ACTION_LABEL_TAIL = "ACTION_LABEL_TAIL";
    public static final String ACTION_LABEL = "ACTION_LABEL";
    public static final String CHOICE = "CHOICE";
    public static final String BACTION = "BACTION";
    public static String CHOICE_PIPE = "CHOICE_PIPE";
    public static String CHOICE_TAIL = "CHOICE_TAIL";
    public static String PROCESS_BODY_LIST = "PROCESS_BODY_LIST";
    public static String BASE_PROCESS_END = "BASE_PROCESS_END";
    public static String BASE_PROCESS_STOP = "BASE_PROCESS_STOP";
    public static String BASE_PROCESS_ERROR = "BASE_PROCESS_ERROR";
    
}
