package com.danodic.jao.renderer;

import java.util.List;

import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.DataTypeModel;

public interface IRenderer {
	public void setDataType(DataTypeModel dataType);
	public void setSample(Byte[] data);
	public void setData(Byte[] data);
	public void setDataSet(List<Byte[]> data);
	public void render(JaoLayer layer);
	public void playSample();
	public void initialize(Object ... args);
}
