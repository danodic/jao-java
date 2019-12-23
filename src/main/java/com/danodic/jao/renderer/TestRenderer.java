package com.danodic.jao.renderer;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.danodic.jao.core.AnimationLayer;

public class TestRenderer implements IRenderer {
	
	String currentData;

	public void update(AnimationLayer animationObject) {
	}

	public void render(AnimationLayer animationObject) {
		String render;
		
		render = "Test Render:";
		
		for(String key : animationObject.getParameters().keySet()) {
			render += String.format("\n..%s=>%s", key, animationObject.getParameters().get(key).toString());
		}
		
		//render += "\n==> Data: " + currentData;
		
		System.out.println(render);
	}

	@Override
	public void setData(Byte[] data) {
		currentData = new String(ArrayUtils.toPrimitive(data));
	}


	@Override
	public void setSample(Byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(Object... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSample() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDataSet(List<Byte[]> data) {
		// TODO Auto-generated method stub
		
	}

}
