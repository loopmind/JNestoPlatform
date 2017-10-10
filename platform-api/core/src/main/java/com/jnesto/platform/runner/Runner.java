/*
 * Copyright (c) 2015-2017 JNesto Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jnesto.platform.runner;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jnesto.platform.daemons.Daemon;
import com.jnesto.platform.exception.StartupExtensionPointNotFoundException;
import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.messenger.MessengerSingleton;
import com.jnesto.platform.plugin.PluginManagerService;
import javax.swing.SwingWorker;
import com.jnesto.platform.plugin.StartupExtensionPoint;
import java.awt.EventQueue;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginManager;
import org.pf4j.RuntimeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A classe de início da aplicação.
 *
 * @author Flavio de Vasconcellos Correa
 */
public final class Runner {

    private static final Logger LOG;
    private Path pluginPath;

    static {
        LOG = LoggerFactory.getLogger(Runner.class);
    }

    /**
     * Constrói um objeto Runner.
     *
     * @param args matriz de argumentos String
     */
    protected Runner(String[] args) {
        LOG.info(java.util.ResourceBundle.getBundle("resources/bundle/Bundle").getString("START APPLICATION..."));
        List<String> largs = Arrays.asList(args);
        if (largs.contains("--development")) {
            System.setProperty("pf4j.mode", RuntimeMode.DEVELOPMENT.toString()); //NOI18N
        }
        if (largs.contains("--guiapp")) { //NOI18N
            initializeLookAndFeel();
        }
        if (largs.contains("--plugindir")) { //NOI18N
            pluginPath = Paths.get(largs.get(largs.indexOf("--plugindir") + 1)); //NOI18N
        }
        initializeMessenger();
        initializePluginManager();
        initializeDaemons();
    }

    /**
     * Inicializa a camada de mensagem.
     *
     */
    protected void initializeMessenger() {
        LOG.info(java.util.ResourceBundle.getBundle("resources/bundle/Bundle").getString("INITIALIZE MESSENGER SYSTEM..."));
        Lookup.register(MessengerSingleton.getInstance());
    }

    /**
     * Inicializa a camada de gerenciamento de plugins.
     *
     */
    protected void initializePluginManager() {
        LOG.info(java.util.ResourceBundle.getBundle("resources/bundle/Bundle").getString("INITIALIZE PLUGINMANAGER SYSTEM..."));
        PluginManager pluginManager;
        Lookup.register(new PluginManagerService(pluginPath));
        if ((pluginManager = Lookup.lookup(PluginManager.class)) != null) {
            pluginManager.loadPlugins();
            pluginManager.startPlugins();
            pluginManager.getExtensions(ExtensionPoint.class).forEach(ep -> Lookup.register(ep));
        }
    }

    /**
     * Inicializa a camada de aparência por padrão.
     *
     */
    protected void initializeLookAndFeel() {
        try {
            PlasticLookAndFeel.setPlasticTheme(new com.jgoodies.looks.plastic.theme.ExperienceBlue());
            UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.Plastic3DLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
        }
    }

    /**
     * Inicializa a camada de Daemons.
     *
     */
    protected void initializeDaemons() {
        LOG.info(java.util.ResourceBundle.getBundle("resources/bundle/Bundle").getString("INITIALIZE DAEMON SYSTEM..."));
        Lookup.lookupAll(Daemon.class)
                .stream()
                .sorted((da, db) -> {
                    return (da.getClass().getAnnotation(Daemon.Description.class)).priority().compareTo(
                            (db.getClass().getAnnotation(Daemon.Description.class)).priority()
                    );
                })
                .forEach((d) -> {
                    if ((d.getClass().getAnnotation(Daemon.Description.class)).asynch()) {
                        (new SwingWorker<Daemon, Void>() {
                            @Override
                            protected Daemon doInBackground() throws Exception {
                                d.start();
                                return d;
                            }

                            @Override
                            protected void done() {
                                LOG.info(java.util.ResourceBundle.getBundle("resources/bundle/Bundle").getString("DAEMON {} IS COMPLETED."), d.getClass().getAnnotation(Daemon.Description.class));
                            }

                        }).execute();
                    } else {
                        d.start();
                    }
                });
    }

    /**
     * Busca e executa a classe de aplicação. No conjunto de plugins é
     * necessário que haja ao menos uma classe que instancie a classe
     * StartupExtensionPoint.
     *
     * @throws StartupExtensionPointNotFoundException
     */
    protected void runApplication() throws StartupExtensionPointNotFoundException {
        StartupExtensionPoint bep = Lookup.lookup(StartupExtensionPoint.class);
        if (bep != null) {
            bep.start();
            return;
        }
        throw new StartupExtensionPointNotFoundException(java.util.ResourceBundle.getBundle("resources/bundle/Bundle").getString("STARTUPEXTENSIONPOINT IS NECESSARY."));
    }

    /**
     * Método padrão de início.
     *
     * @param args matriz de argumentos.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                (new Runner(args)).runApplication();
            } catch (StartupExtensionPointNotFoundException ex) {
                LoggerFactory.getLogger(Runner.class).error("{}", ex); //NOI18N
            }
        });
    }
}
