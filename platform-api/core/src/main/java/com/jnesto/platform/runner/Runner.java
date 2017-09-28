/*
 * Copyright 2017 JNesto Team.
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
import com.jnesto.platform.exception.StartupPointNotFoundException;
import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.messenger.MessengerSingleton;
import com.jnesto.platform.plugin.PluginManagerService;
import javax.swing.SwingWorker;
import com.jnesto.platform.plugin.StartupExtensionPoint;
import java.awt.EventQueue;
import java.util.Arrays;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginManager;
import org.slf4j.LoggerFactory;

/**
 * A classe de início da aplicação.
 *
 * @author Flavio de Vasconcellos Correa
 */
public final class Runner {

    /**
     * Constrói um objeto Runner.
     *
     * @param args matriz de argumentos String
     */
    protected Runner(String[] args) {
        List<String> largs = Arrays.asList(args);
        if (largs.contains("--guiapp")) {
            initializeLookAndFeel();
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
        Lookup.register(MessengerSingleton.getInstance());
    }

    /**
     * Inicializa a camada de gerenciamento de plugins.
     *
     */
    protected void initializePluginManager() {
        PluginManager pluginManager;
        Lookup.register(new PluginManagerService());
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
        Lookup.lookupAll(Daemon.class)
                .stream()
                .sorted((da, db) -> {
                    Daemon.Description descA = da.getClass().getAnnotation(Daemon.Description.class);
                    Daemon.Description descB = db.getClass().getAnnotation(Daemon.Description.class);
                    return descA.priority().compareTo(descB.priority());
                }).forEach((d) -> {
            Daemon.Description desc = d.getClass().getAnnotation(Daemon.Description.class);
            if (desc.asynch()) {
                (new SwingWorker<Daemon, Void>() {
                    @Override
                    protected Daemon doInBackground() throws Exception {
                        d.start();
                        return d;
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
     * @throws StartupPointNotFoundException
     */
    protected void runApplication() throws StartupPointNotFoundException {
        StartupExtensionPoint bep = Lookup.lookup(StartupExtensionPoint.class);
        if (bep != null) {
            bep.start();
        } else {
            throw new StartupPointNotFoundException("Não foi definida uma classe de inicialização.");
        }
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
            } catch (StartupPointNotFoundException ex) {
                LoggerFactory.getLogger(Runner.class).error("{}", ex);
            }
        });
    }
}
