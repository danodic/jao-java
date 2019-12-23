package com.danodic.jao.renderer;

import java.util.List;

import com.danodic.jao.core.AnimationLayer;

public interface IRenderer {
	public void setSample(Byte[] data);
	public void setData(Byte[] data);
	public void setDataSet(List<Byte[]> data);
	public void render(AnimationLayer animationObject);
	public void playSample();
	public void initialize(Object ... args);
}
