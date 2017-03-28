/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jnesto.platform.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 *
 * @author flavio
 */
public class ExternalPlugin extends Plugin {
    Logger log = LoggerFactory.getLogger(ExternalPlugin.class);
    
    public ExternalPlugin(PluginWrapper wrapper) {
        super(wrapper);
        log.info("Constructor {}", getClass());
    }
   
}
