package com.danodic.jao.support.renderers;

import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.extractor.IExtractor;
import com.danodic.jao.model.DataTypeModel;
import com.danodic.jao.renderer.IRenderer;

public class BadRenderer implements IRenderer {

    public BadRenderer() {
        throw new RuntimeException("Should not work.");
    }

    @Override
    public void initialize(Object... args) {
    }

    @Override
    public void setDataType(DataTypeModel dataType, IExtractor extractor) {

    }

    @Override
    public void render(JaoLayer layer, Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IRenderer clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void debug() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
