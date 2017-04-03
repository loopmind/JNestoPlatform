package com.jnesto.platform.demo.mainframe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public class MainFramePlugin extends Plugin {

    private final Logger log = LoggerFactory.getLogger(MainFramePlugin.class);

    public MainFramePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

}
