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

import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.plugin.BootstrapExtensionPoint;
import com.jnesto.platform.plugin.PluginManagerService;
import javax.swing.SwingUtilities;
import ro.fortsoft.pf4j.ExtensionPoint;
import ro.fortsoft.pf4j.PluginManager;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public class Bootstrap {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            initialize();
            starter();
        });
    }

    private static void initialize() {
        Lookup.register(new PluginManagerService());
        PluginManager pluginManager = Lookup.lookup(PluginManager.class);
        if(pluginManager != null) {
            pluginManager.loadPlugins();
            pluginManager.startPlugins();
            pluginManager.getExtensions(ExtensionPoint.class).forEach(ep -> Lookup.register(ep));
        }
    }

    private static void starter() {
        BootstrapExtensionPoint bep = Lookup.lookup(BootstrapExtensionPoint.class);
        if(bep != null) {
            bep.start();
        }
    }
    
}
