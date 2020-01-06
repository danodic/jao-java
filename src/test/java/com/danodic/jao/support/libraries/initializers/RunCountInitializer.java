package com.danodic.jao.support.libraries.initializers;

import com.danodic.jao.core.JaoLayer;

public class RunCountInitializer extends GenericInitializer {

    private int runCount = 0;

    @Override
    public void run(JaoLayer layer) {
        super.run(layer);
        runCount++;
    }

    public int getRunCount() {
        return runCount;
    }
    
}