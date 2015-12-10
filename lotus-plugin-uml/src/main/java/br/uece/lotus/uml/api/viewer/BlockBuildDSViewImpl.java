/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer;

import br.uece.lotus.uml.api.ds.BlockBuildDS;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 *
 * @author Bruno Barbosa
 */
public class BlockBuildDSViewImpl extends  Region implements BlockBuildDSView, BlockBuildDS.Listener{

    
    
    public BlockBuildDSViewImpl() {
        
    }
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public boolean isInsideBounds(Point2D point) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BlockBuildDS getBlockBuildDS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setBlockBuildDS(BlockBuildDS bbds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onChange(BlockBuildDS blockBuildDS) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
