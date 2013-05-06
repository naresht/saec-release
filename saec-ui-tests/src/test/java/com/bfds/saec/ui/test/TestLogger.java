package com.bfds.saec.ui.test;

import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogger extends TestWatchman {
    
    private static Logger log = LoggerFactory.getLogger(TestLogger.class);
    
    @Override
    public void starting(FrameworkMethod method) {
        log.info("Running test {} ...", method.getName() );
    }

    @Override
    public void failed(Throwable e, FrameworkMethod method) {
        
        log.error( "Failed: {} ", method.getName(), e );
    }

    @Override
    public void succeeded(FrameworkMethod method) {
        log.info( "{} succeeded.", method.getName());
    }
}