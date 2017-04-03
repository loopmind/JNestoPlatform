package com.jnesto.platform.demo.lookandfeel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 *
 * @author flavio
 */
public class LookAndFeelPlugin extends Plugin {
    private final Logger LOGGER = LoggerFactory.getLogger(LookAndFeelPlugin.class);
    
    public LookAndFeelPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
   
}
