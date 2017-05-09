/*
 * Copyright 2015 - 2017 JNesto Team.
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
package com.jnesto.platform.lookup;

/**
 * Define uma camada de restrição na pesquisa em Lookup.
 * 
 * @author Flavio de Vasconcellos Correa
 */
public interface LookupConstraints<T> {

    /**
     * Define o método de validação da restrição.
     * 
     * @param target Instância alvo para análise
     * @return Retorna @code{true} se válido, @code{false} caso contrário.
     */
    public boolean validate(T target);
}
