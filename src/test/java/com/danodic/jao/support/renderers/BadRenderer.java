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
    public void render(JaoLayer layer) {
    }

    @Override
    public void initialize(Object... args) {
    }

	@Override
	public void setDataType(DataTypeModel dataType, IExtractor extractor) {
		
	}
    
}