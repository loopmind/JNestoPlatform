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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

/**
 * Implementa um registro de propósito geral, permitindo que clientes busquem
 * serviços (serviços via interfaces). Essa classe é inspirada no projeto
 * Netbeans Platform.
 *
 * @author Flavio de Vasconcellos Correa
 */
public final class Lookup {

    private final static Map<String, Object> mapID = Collections.synchronizedMap(new HashMap<>());
    private final static Map<Class, Collection> mapClass = Collections.synchronizedMap(new HashMap<>());

    /**
     * Mapeamento de um objeto concreto baseado em uma classe como chave de
     * identificação.
     *
     * @param ref
     * @param target
     */
    public static synchronized void register(Class ref, Object target) {
        if (ref.isInstance(target)) {
            if (!mapClass.containsKey(ref)) {
                mapClass.put(ref, Collections.synchronizedList(new LinkedList<>()));
            }
            mapClass.get(ref).add(target);
        } else {
            throw new RuntimeException(target + " not instance " + ref);
        }
    }

    /**
     * Mapeamento de um objeto concreto baseado em uma chave de identificação.
     *
     * @param id
     * @param target
     */
    public static synchronized void register(String id, Object target) {
        mapID.put(id, target);
    }

    /**
     * Registra e realiza o mapeamento de um objeto concreto, baseado em
     * Annotation.
     *
     * @param target - Objeto alvo.
     */
    public static synchronized void register(Object target) {
        Objects.requireNonNull(target, "target must not be a null.");
        Class clazz = target.getClass();
        if (clazz.isAnnotationPresent(ServiceProvider.class)) {
            ServiceProvider lid = (ServiceProvider) clazz.getAnnotation(ServiceProvider.class);
            Class[] service = lid.service();
            if (service != null && service.length > 0) {
                Arrays.asList(service).forEach(s -> {
                    register(s, target);
                });
            }
            if (!lid.id().isEmpty()) {
                register(lid.id(), target);
            }
            LoggerFactory.getLogger(Lookup.class).debug("Registering {}", lid.id());
        }
    }

    /**
     * Remove do registro um objeto concreto.
     *
     * @param target - Objeto alvo.
     */
    public static synchronized void unRegister(Object target) {
        Objects.requireNonNull(target, "target must not be a null.");
        Class clazz = target.getClass();
        if (clazz.isAnnotationPresent(ServiceProvider.class)) {
            ServiceProvider lid = (ServiceProvider) clazz.getAnnotation(ServiceProvider.class);
            if (!lid.id().isEmpty() && mapID.containsKey(lid.id())) {
                mapID.remove(lid.id());
            }
            Class[] service = lid.service();
            if (service != null) {
                Arrays.asList(service).forEach(s -> {
                    if (mapClass.containsKey(s)) {
                        mapClass.get(s).remove(target);
                    }
                });
            }
            LoggerFactory.getLogger(Lookup.class).debug("Unregistering {}", lid.id());
        }
    }

    /**
     * Busca todos as instâncias que correspondam a determinada classe. Uso:
     * <pre>{@code
     *  for(IMeuServico srv : Lookup.lookupAll(IMeuServico.class) {
     *      srv.facaAlgo();
     *  }
     *  }</pre>
     *
     * @param clazz - Tipo da classe a que se deseja pesquisar.
     * @return Todas as ocorrências que pertençam a classe de pesquisa.
     */
    public static synchronized <T> Collection<? extends T> lookupAll(Class<? extends T> clazz) {
        return lookupAll(clazz, null);
    }

    /**
     * Busca todos as instâncias que correspondam a determinada classe e que
     * atendam aos critérios em @see LookupConstraints. Uso:
     * <pre>{@code
     *  LookupConstraints lc = (s) -> s.isAvaliable();
     *
     *  for(IMeuServico srv : Lookup.lookupAll(IMeuServico.class, lc) {
     *      srv.facaAlgo();
     *  }
     *  }</pre>
     *
     * @param clazz - Tipo da classe a que se deseja pesquisar.
     * @param lc - Define regras de restrição a busca.
     * @return Todas as ocorrências que pertençam a classe de pesquisa.
     */
    public static synchronized <T> Collection<? extends T> lookupAll(Class<? extends T> clazz, LookupConstraints lc) {
        Objects.requireNonNull(clazz, "parameter must not be a null.");
        Collection<? extends T> collection = Collections.EMPTY_LIST;
        if (mapClass.containsKey(clazz)) {
            collection = mapClass.get(clazz);
            if (lc != null) {
                collection = collection.stream().filter(e -> lc.validate(e)).collect(Collectors.toList());
            }
        }
        return collection;
    }

    /**
     * Busca todos as instâncias que correspondam a determinada classe e que
     * atendam aos critérios em @see LookupConstraints. Uso:
     * <pre>{@code
     *  LookupConstraints lc = (s) -> s.isAvaliable();
     *
     *  for(IMeuServico srv : Lookup.lookupAll(IMeuServico.class, lc) {
     *      srv.facaAlgo();
     *  }
     *  }</pre>
     *
     * @param lc - Define regras de restrição a busca.
     * @return Todas as ocorrências que pertençam a classe de pesquisa.
     */
    public static synchronized Collection lookupAll(LookupConstraints lc) {
        Objects.requireNonNull(lc, "parameter must not be a null.");
        Collection collection = Collections.synchronizedCollection(new LinkedList());
        Collections.addAll(collection, mapID.values());
        collection.stream()
                .filter(e -> lc.validate(e))
                .collect(Collectors.toList());
        return collection;
    }

    /**
     * Busca apenas a primeira ocorrência que corresponda a determinada classe.
     * Uso:
     * <pre>{@code
     * IMeuServiço srv = Lookup.lookup(IMeuServico.class);
     * if(srv != null) srv.facaAlgo();
     * }</pre>
     *
     * @param clazz - Tipo da classe a que se deseja pesquisar.
     * @return Um objeto que corresponda a classe de pesquisa.
     */
    public static synchronized <T> T lookup(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be a null.");
        if (mapClass.containsKey(clazz) && !mapClass.get(clazz).isEmpty()) {
            return (T) mapClass.get(clazz).iterator().next();
        }
        return null;
    }

    /**
     * Busca um determinda instância pelo valor do seu ID. O ID é determinada em
     * ServiceProvider {
     *
     * @see ServiceProvider#value}.
     *
     * Uso:
     * <p>
     * <code>
     * IMeuServiço srv = Lookup.lookupById("MeuServico");
     * if(srv != null) srv.facaAlgo();
     * </code></p>
     *
     * @param id String Chave de identificação de uma instância.
     * @return Um objeto que corresponda a chave de busca.
     */
    public static synchronized <T> T lookupById(String id) {
        Objects.requireNonNull(id, "id must not be a null.");
        if (id.isEmpty()) {
            throw new RuntimeException("id must not be a empty.");
        }
        if (mapID.containsKey(id)) {
            return (T) mapID.get(id);
        }
        return null;
    }

}
