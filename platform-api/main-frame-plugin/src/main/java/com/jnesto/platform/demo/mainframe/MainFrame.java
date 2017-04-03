package com.jnesto.platform.demo.mainframe;

import com.jnesto.platform.demo.i18n.ResourceBundleSingleton;
import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.plugin.BootstrapExtensionPoint;
import com.jnesto.platform.utils.ComponentFactory;
import com.jnesto.platform.windows.JMenuAction;
import com.jnesto.platform.windows.JToolBarAction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import static org.flexdock.docking.DockingConstants.CENTER_REGION;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.defaults.StandardBorderManager;
import org.flexdock.docking.drag.effects.EffectsManager;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.state.PersistenceException;
import org.flexdock.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Extension;

/**
 * Representa a janela principal da aplicação.
 *
 * @author Flavio de Vasconcellos Correa
 */
@ServiceProvider(
        id = "#CTL_MAINFRAME",
        service = BootstrapExtensionPoint.class
)
@Extension
public class MainFrame implements BootstrapExtensionPoint {

    private final ResourceBundle resourceBundle = ResourceBundleSingleton.getBundle();

    private final Logger log = LoggerFactory.getLogger(MainFrame.class);

    private DockingPort rootDockingPort;

    /**
     * Método de partida da aplicação.
     */
    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> {
            JToolBar tb = ComponentFactory.createJToolbar(Lookup.lookupAll(JToolBarAction.class));
            JMenuBar mb = ComponentFactory.createJMenuBar(Lookup.lookupAll(JMenuAction.class));
            JFrame mframe = new JFrame();
            mframe.setTitle(resourceBundle.getString("CTL_MAINFRAME_TITLE"));
            mframe.setSize(new Dimension(1024, 640));
            mframe.setLocationRelativeTo(null);
            mframe.setJMenuBar(mb);
            mframe.getContentPane().setLayout(new BorderLayout());
            mframe.getContentPane().add(tb, BorderLayout.NORTH);
            mframe.getContentPane().add((Container) getRootDockingPort(), BorderLayout.CENTER);
            mframe.addWindowListener(new WindowAdapter() {
                /**
                 * Intercepta o método de chamada quando a janela principal for
                 * fechada.
                 *
                 * @param e
                 */
                @Override
                public void windowClosing(WindowEvent e) {
                    Action a;
                    if ((a = Lookup.lookupById("#CTL_SHUTDOWNAPPLICATIONACTION")) != null) {
                        a.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "WINDOWCLOSED"));
                    }
                }
            });
            initLayout();
            mframe.setVisible(true);
        });
    }

    private void initLayout() {
        registerViews();
        DockingManager.setDefaultPersistenceKey("jnestodemo.xml");
        EffectsManager.setPreview(new org.flexdock.docking.drag.preview.AlphaPreview(Color.ORANGE, Color.YELLOW, 0.1f));
        try {
            if (!DockingManager.restoreLayout(true)) {
                setupDefaultLayout();
            }
        } catch (IOException | PersistenceException e) {
            setupDefaultLayout();
        }
        DockingManager.setAutoPersist(true);
    }

    private DockingPort getRootDockingPort() {
        if (rootDockingPort == null) {
            DefaultDockingPort port = new DefaultDockingPort();
//            port.setBorderManager(new StandardBorderManager(new ShadowBorder()));
            port.setBorderManager(new StandardBorderManager(BorderFactory.createEtchedBorder()));
            rootDockingPort = port;
        }
        return rootDockingPort;
    }

    private View createCustomView(String title) {
        View view = new View(title, title);
        view.setContentPane(new JPanel());
        view.getContentPane().setBackground(Color.WHITE);
        view.getContentPane().setFocusable(true);
        view.addAction((Action) Lookup.lookupById("#CTL_CLOSEVIEWACTION"));
        view.addAction((Action) Lookup.lookupById("#CTL_MAXIMIZEDVIEWACTION"));
        view.addAction((Action) Lookup.lookupById("#CTL_BELLACTION"));
        view.addAction((Action) Lookup.lookupById("#CTL_REFRESHACTION"));
        view.addAction((Action) Lookup.lookupById("#CTL_INFORMATIONVIEWACTION"));
        view.addAction((Action) Lookup.lookupById("#CTL_ADDVIEWACTION"));
        view.getTitlebar().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Action action = ((Action) Lookup.lookupById("#CTL_MAXIMIZEDVIEWACTION"));
                    ActionEvent evt = new ActionEvent(view, 0, (String) action.getValue(AbstractAction.NAME));
                    action.actionPerformed(evt);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //NOOP
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //NOOP
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //NOOP
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //NOOP
            }
        });
        return view;
    }

    private View editor, customers, accounts, leads, dashboard, mail, opportunity;

    private void registerViews() {
        try {
            editor = createCustomView("Editor");
            editor.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/book.png"))));
            editor.setTabIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/book.png"))));
            customers = createCustomView("Customers");
            customers.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/group.png"))));
            customers.setTabIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/group.png"))));
            accounts = createCustomView("Accounts");
            accounts.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/folder_user.png"))));
            accounts.setTabIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/folder_user.png"))));
            mail = createCustomView("Mail");
            mail.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/email.png"))));
            mail.setTabIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/email.png"))));
            leads = createCustomView("Leads");
            leads.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/heart.png"))));
            leads.setTabIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/heart.png"))));
            dashboard = createCustomView("Dashboard");
            dashboard.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/house.png"))));
            dashboard.setTabIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/house.png"))));
            dashboard.setTerritoryBlocked(CENTER_REGION, true);
            opportunity = createCustomView("Oportunity");
            opportunity.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/lightbulb.png"))));
            opportunity.setTabIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/lightbulb.png"))));
        } catch (IOException ex) {
        }

    }

    private void setupDefaultLayout() {
        getRootDockingPort().clear();
        DockingManager.dock(editor, getRootDockingPort());
        editor.dock(customers);
        editor.dock(accounts);
        editor.dock(leads);
        editor.dock(mail);
        editor.dock(dashboard);
        editor.dock(opportunity);

    }

}
