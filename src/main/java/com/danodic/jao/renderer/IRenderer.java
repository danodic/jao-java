package com.danodic.jao.renderer;

import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.extractor.IExtractor;
import com.danodic.jao.model.DataTypeModel;

public interface IRenderer {
	public void setDataType(DataTypeModel dataType, IExtractor extractor);
	public void render(JaoLayer layer);
	public void initialize(Object ... args);
}
