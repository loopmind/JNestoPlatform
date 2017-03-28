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
package com.jnesto.platform.demo.api;

import com.jnesto.platform.lookup.ServiceProvider;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Extension;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "com.jnesto.platform.demo.api.SalutonMondoAction",
        service = {ActionListener.class, ActionAPI.class}
)
@Extension
public class SalutonMondoAction implements ActionAPI {
    Logger log = LoggerFactory.getLogger(SalutonMondoAction.class);
    
    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("Saluton Mondo! Mi ŝatas la babiladon!");
    }
    
}
