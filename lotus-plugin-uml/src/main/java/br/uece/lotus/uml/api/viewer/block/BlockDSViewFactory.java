/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.block;

/**
 *
 * @author Bruno Barbosa
 */
public class BlockDSViewFactory implements BlockDSView.Factory{

        @Override
        public BlockDSView create() {
            return new BlockDSViewImpl();
        }

    
}
