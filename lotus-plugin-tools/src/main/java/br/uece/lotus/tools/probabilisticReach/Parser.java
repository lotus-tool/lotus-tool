/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.tools.probabilisticReach;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Note
 */
public class Parser {
    
    public int[] toInt (String[] tokens){
        int size = tokens.length;
        String next = null;
        int[] targets = new int[size];
        if(isNumeric(tokens[0])){
            JOptionPane.showMessageDialog(null, "Provided target states arent in the expected format!");
            return null;
        }
        targets[0] = Integer.parseInt(tokens[0]);
        for(int i = 1; i < size; i++){
            if(tokens[i] == "||" && (!isNumeric(tokens[i+1])) || i == size-1){
                JOptionPane.showMessageDialog(null, "Provided target states arent in the expected format!");
                return null;
            }
            
        }
        return targets;
    }
    
    public static boolean isNumeric (String str){
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
