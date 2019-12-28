package com.danodic.jao.support.renderers;

import java.util.List;

import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.DataTypeModel;
import com.danodic.jao.renderer.IRenderer;

public class BadRenderer implements IRenderer {

    public BadRenderer() {
        throw new RuntimeException("Should not work.");
    }

    @Override
    public void setDataType(DataTypeModel dataType) {
    }

    @Override
    public void setSample(Byte[] data) {
    }

    @Override
    public void setData(Byte[] data) {
    }

    @Override
    public void setDataSet(List<Byte[]> data) {
    }

    @Override
    public void render(JaoLayer layer) {
    }

    @Override
    public void playSample() {
    }

    @Override
    public void initialize(Object... args) {
    }
    
}