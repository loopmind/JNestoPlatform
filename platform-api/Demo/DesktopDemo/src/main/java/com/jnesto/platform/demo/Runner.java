/*
 * Copyright 2017 flavio.
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
package com.jnesto.platform.demo;

import com.jnesto.platform.lookup.Lookup;
import java.awt.event.ActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.ExtensionPoint;
import ro.fortsoft.pf4j.PluginManager;

/**
 *
 * @author flavio
 */
public class Runner {

    private static final Logger log = LoggerFactory.getLogger(Runner.class);
    private static PluginManager pm;

    static public void start() {
        pm = Lookup.lookup(PluginManager.class);
        if (pm != null) {

            pm.loadPlugins();
            
            pm.startPlugins();

//            pm.startPlugin("external-plugin");
            
            pm.getExtensions(ExtensionPoint.class).forEach(p -> Lookup.register(p));
            
            Lookup.lookupAll(ActionListener.class).forEach(al -> log.info(al.toString()));
        
            pm.stopPlugins();
        }

    }

}
