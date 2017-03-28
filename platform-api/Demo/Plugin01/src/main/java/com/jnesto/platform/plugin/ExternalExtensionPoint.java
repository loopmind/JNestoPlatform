/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jnesto.platform.plugin;

import com.jnesto.platform.demo.api.ActionAPI;
import com.jnesto.platform.lookup.ServiceProvider;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Extension;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "com.jnesto.platform.demo.api.ExternalExtensionPoint",
        service = {ActionListener.class}
)
@Extension
public class ExternalExtensionPoint implements ActionAPI {
    private final Logger log = LoggerFactory.getLogger(ExternalExtensionPoint.class);
    
    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("Eu sou um plugin externo! UUUUUhhhhuuuuu! Funcionou!");
    }
    
}
