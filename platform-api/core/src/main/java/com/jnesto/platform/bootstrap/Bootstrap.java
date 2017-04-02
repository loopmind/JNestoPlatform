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
package com.jnesto.platform.bootstrap;

import com.jnesto.platform.daemons.Daemon;
import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.plugin.BootstrapExtensionPoint;
import com.jnesto.platform.plugin.PluginManagerService;
import java.util.Comparator;
import java.util.function.Consumer;
import javax.swing.SwingWorker;
import ro.fortsoft.pf4j.ExtensionPoint;
import ro.fortsoft.pf4j.PluginManager;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public class Bootstrap {

    public static void main(String[] args) {
        initialize();
        starter();
    }

    private static void initialize() {
        Lookup.register(new PluginManagerService());
        PluginManager pluginManager = Lookup.lookup(PluginManager.class);
        if (pluginManager != null) {
            pluginManager.loadPlugins();
            pluginManager.startPlugins();
            pluginManager.getExtensions(ExtensionPoint.class).forEach(ep -> Lookup.register(ep));
        }
    }

    private static void starter() {
        startDaemons();
        startBootstrap();
    }
    
    private static void startBootstrap() {
        BootstrapExtensionPoint bep = Lookup.lookup(BootstrapExtensionPoint.class);
        if (bep != null) {
            bep.start();
        }
    }

    private static void startDaemons() {
        Comparator<Daemon> comparator = (da, db) -> {
            Daemon.Description descA = da.getClass().getAnnotation(Daemon.Description.class);
            Daemon.Description descB = db.getClass().getAnnotation(Daemon.Description.class);
            return descA.priority().compareTo(descB.priority());
        };
        Consumer<Daemon> consumer = (d) -> {
            Daemon.Description desc = d.getClass().getAnnotation(Daemon.Description.class);
            if(desc.asynch()) {
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
        Lookup.lookupAll(Daemon.class).stream().sorted(comparator).forEach(consumer);
    }

}
