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
package com.jnesto.platform.exception;

/**
 * Essa exceção indica que não foi definido uma classe implementando a interface
 * com.jnesto.platform.plugin.StartupExtensionPoint.
 * 
 * @author Flavio de Vasconcellos Correa
 */
public class StartupPointNotFoundException extends Exception {

    /**
     * Constrói um objeto StartupPointNotFoundException.
     *
     * @param s uma mensagem String
     */
    public StartupPointNotFoundException(String s) {
        super(s);
    }

}
