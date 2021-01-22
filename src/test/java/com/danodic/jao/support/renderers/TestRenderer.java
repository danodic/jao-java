package com.danodic.jao.support.renderers;

import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.extractor.IExtractor;
import com.danodic.jao.model.DataTypeModel;
import com.danodic.jao.renderer.IRenderer;

public class TestRenderer implements IRenderer {

    @Override
    public void initialize(Object... args) {
    }

    @Override
    public void setDataType(DataTypeModel dataType, IExtractor extractor) {
    }

    @Override
    public void render(JaoLayer layer, Object... args) {
    }

    @Override
    public void dispose() {
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
