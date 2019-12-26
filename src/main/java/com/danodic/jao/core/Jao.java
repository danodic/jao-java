package com.danodic.jao.core;

import java.util.ArrayList;
import java.util.List;

public class Jao {

    private List<JaoLayer> layers;

	private Long startTime;
	private Long elapsed;
	private Long frameTime;

	private float scaleFactor;

	public Jao() {
		this(1.0f);
	}

	public Jao(float scaleFactor) {
		layers = new ArrayList<JaoLayer>();
		this.scaleFactor = scaleFactor;
	}

	public void addLayer(JaoLayer layer) {
		this.layers.add(layer);
    }

	public void render () {
		updateElapsed();
		for (JaoLayer layer : layers)
			layer.render(elapsed);
	}
	
	public void reset () {
		for (JaoLayer layer : layers)
			layer.reset();
	}
	
	public void prepareToFinish () {
		for (JaoLayer layer : layers)
			layer.setLoop(false);
	}
	
	private void updateElapsed() {
		if (startTime == null)
			startTime = System.currentTimeMillis();
		
		frameTime = System.currentTimeMillis(); 
		elapsed = frameTime - startTime;
	}
	
	public long getElapsed() {
		updateElapsed();
		return elapsed;
	}

	public boolean isDone() {
		for (JaoLayer layer : layers) {
			if (!layer.isDone())
				return false;
		}

		return true;
	}

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
	
	public List<JaoLayer> getLayers() {
		return layers;
	}

	public Long getFrameTime() {
		return frameTime;
	}

	public void addLayers(List<JaoLayer> layers) {
        layers.addAll(layers);
	}

}
