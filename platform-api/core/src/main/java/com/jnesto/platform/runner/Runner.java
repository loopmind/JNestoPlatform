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

import com.jnesto.platform.daemons.Daemon;
import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.messenger.MessengerSingleton;
import com.jnesto.platform.plugin.PluginManagerService;
import java.util.Comparator;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import ro.fortsoft.pf4j.ExtensionPoint;
import ro.fortsoft.pf4j.PluginManager;
import com.jnesto.platform.plugin.StartupExtensionPoint;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public final class Runner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> (new Runner()).runApplication());
    }

    protected void initialize() {
        // Initialize Messenger Layer
        Lookup.register(MessengerSingleton.getInstance());
        // Initialize Plugin Manager
        PluginManager pluginManager;
        Lookup.register(new PluginManagerService());
        if ((pluginManager = Lookup.lookup(PluginManager.class)) != null) {
            pluginManager.loadPlugins();
            pluginManager.startPlugins();
            pluginManager.getExtensions(ExtensionPoint.class).forEach(ep -> Lookup.register(ep));
        }
    }

    public void runApplication() {
        initialize();
        startDaemons();
        startApplication();
    }

    protected void startApplication() {
        StartupExtensionPoint bep;
        if ((bep = Lookup.lookup(StartupExtensionPoint.class)) != null) {
            bep.start();
        }
    }

    protected void startDaemons() {
        Comparator<Daemon> comparator = (da, db) -> {
            Daemon.Description descA = da.getClass().getAnnotation(Daemon.Description.class);
            Daemon.Description descB = db.getClass().getAnnotation(Daemon.Description.class);
            return descA.priority().compareTo(descB.priority());
        };
        Consumer<Daemon> consumer = (d) -> {
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
        };
        Lookup.lookupAll(Daemon.class)
                .stream()
                .sorted(comparator)
                .forEach(consumer);
    }

}
