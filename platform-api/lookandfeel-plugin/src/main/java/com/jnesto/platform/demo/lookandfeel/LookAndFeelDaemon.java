package com.jnesto.platform.demo.lookandfeel;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import com.jnesto.platform.daemons.AbstractDaemon;
import com.jnesto.platform.daemons.Daemon;
import com.jnesto.platform.lookup.ServiceProvider;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
@ServiceProvider(
        id = "com.jnesto.platform.demo.lookandfeel.LookAndFeelDaemon",
        service = {Daemon.class, ExtensionPoint.class}
)
@Daemon.Description (
        title = "look-and-feel-daemon",
        description = "Define a aparência do ambiente de trabalho",
        vendor = "jnesto.com",
        asynch = false
)
@Extension
public class LookAndFeelDaemon extends AbstractDaemon implements ExtensionPoint {
    private final Logger log = LoggerFactory.getLogger(LookAndFeelDaemon.class);
    
    @Override
    public void start() {
        try {
            super.start();
            PlasticLookAndFeel.setPlasticTheme(new ExperienceBlue());
            UIManager.setLookAndFeel( new PlasticLookAndFeel() );
            setStatus(Status.ON);
        } catch (UnsupportedLookAndFeelException ex) {
            setStatus(Status.FAILURE);
            log.error(null, ex);
        }
    }
    
}
