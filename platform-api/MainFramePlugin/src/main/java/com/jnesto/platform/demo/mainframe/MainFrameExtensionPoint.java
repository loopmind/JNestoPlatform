/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jnesto.platform.demo.mainframe;

import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.plugin.BootstrapExtensionPoint;
import com.jnesto.platform.utils.ComponentFactory;
import com.jnesto.platform.windows.JMenuAction;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider (
        id = "com.jnesto.platform.demo.mainframe.MainFrameExtensionPoint",
        service = {BootstrapExtensionPoint.class, ExtensionPoint.class}
)
@Extension
public class MainFrameExtensionPoint implements BootstrapExtensionPoint {
    private final Logger log = LoggerFactory.getLogger(MainFrameExtensionPoint.class);
    
    @Override
    public void start() {
        log.info("Start MainFrame...");
        SwingUtilities.invokeLater(() -> {
            JFrame mainframe = new JFrame();
            mainframe.setTitle("Integrare Desktop");
            mainframe.setSize(new Dimension(640, 480));
            mainframe.setLocationByPlatform(true);
            
            
            mainframe.setJMenuBar(ComponentFactory.createJMenuBar(Lookup.lookupAll(JMenuAction.class)));
            mainframe.getContentPane().setLayout(new BorderLayout());
            mainframe.getContentPane().add(ComponentFactory.createJToolbar(Lookup.lookupAll(JMenuAction.class)), BorderLayout.NORTH);
            mainframe.setVisible(true);
        });
    }
    
}
