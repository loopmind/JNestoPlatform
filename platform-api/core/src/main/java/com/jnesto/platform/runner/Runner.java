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
import org.pf4j.Extension;
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
     * @param args matriz de argumentos String-
     */
    protected Runner(String[] args) {
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
    }

    /**
     * Inicializa a camada de gerenciamento de plugins.
     *
     */
    protected void initializePluginManager() {
        PluginManager pluginManager;

        if ((pluginManager = Lookup.lookup(PluginManager.class)) != null) {
            pluginManager.loadPlugins();
            pluginManager.startPlugins();
            pluginManager.getExtensions(ExtensionPoint.class)
                    .stream()
                    .sorted((ea, eb) -> {
                        return ((Integer) ea.getClass().getAnnotation(Extension.class).ordinal())
                                .compareTo((Integer) eb.getClass().getAnnotation(Extension.class).ordinal());
                    }).forEach(ep -> Lookup.register(ep));
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
                                LOG.info("Daemon '{}' completed.", d.getClass().getAnnotation(Daemon.Description.class));
                            }

                        }).execute();
                    } else {
                        d.start();
                    }
                });
    }

    protected static void stopDaemons() {
        Lookup.lookupAll(Daemon.class)
                .stream()
                .sorted((db, da) -> {
                    return (da.getClass().getAnnotation(Daemon.Description.class)).priority().compareTo(
                            (db.getClass().getAnnotation(Daemon.Description.class)).priority()
                    );
                })
                .forEach((d) -> d.stop());
    }

    /**
     * Busca e executa a classe de aplicação. No conjunto de plugins é
     * necessário que haja ao menos uma classe que instancie a classe
     * StartupExtensionPoint.
     *
     * @throws StartupExtensionPointNotFoundException
     */
    protected void start() throws StartupExtensionPointNotFoundException {
        Lookup.register(MessengerSingleton.getInstance());
        Lookup.register(new PluginManagerService(pluginPath));
        initializePluginManager();
        initializeDaemons();
        if (Lookup.lookup(StartupExtensionPoint.class) == null) {
            throw new StartupExtensionPointNotFoundException("StartupExtensionPoint class not found.");
        }
        Lookup.lookup(StartupExtensionPoint.class).start();
    }

    public static void stop() {
        stopDaemons();
        PluginManager pm = Lookup.lookup(PluginManager.class);
        if (pm != null) {
            pm.stopPlugins();
        }
        System.exit(0);
    }

    /**
     * Método padrão de início.
     *
     * @param args matriz de argumentos.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                (new Runner(args)).start();
            } catch (StartupExtensionPointNotFoundException ex) {
                LoggerFactory.getLogger(Runner.class).error("StartupExtensionPointNotFoundException: {}", ex); //NOI18N
            }
        });
    }
}
