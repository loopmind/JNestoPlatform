/*
 * Copyright 2018 JNesto Team.
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
package com.jnesto.platform.nodo;

import java.io.Serializable;
import java.util.List;

/**
 * Um <em>Nodo</em> estabelece os elementos e métodos necessários para 
 * representar um elemento dentro de uma hierarquia de objetos.
 * Um <em>Nodo</em> tem como propósito a representação de um objeto, contido
 * na estrutura <em>cargo</em>, em uma árvore hierarquicamente composta.
 * 
 * @author loopmind
 * @param <E>
 */
public interface Nodo<E> extends Cloneable, Serializable {

    public @interface Description {
        Class[] source();
    }
    
    public enum NodoAction {
        ADD, REMOVE, UPDATE
    }

    public interface NodoCargoListener<C> {

        void cargoChangedEvent(Nodo<C> source, C oldCargo, C newCargo);
    }

    public interface NodoChildListener<C> {

        void childEvent(Nodo<C> source, Nodo<C> child, Nodo.NodoAction action);
    }

    public interface NodoOwnerListener<C> {

        void changeOwnerEvent(Nodo<C> source, Nodo<C> older, Nodo<C> newer);
    }

    E getCargo();

    List<? extends Nodo<E>> childs(int capacity);

    List<? extends Nodo<E>> nodos();

    boolean isLeaf();

    Nodo<E> setParent(Nodo<E> parent);

    Nodo<E> getParent();
    
    default boolean isSon(Nodo parent, Nodo son) {
        return son.getParent() == parent;
    }

    Nodo<E> setCargo(E cargo);

    Nodo<E> addNodoChildListener(NodoChildListener l);

    Nodo<E> removeNodoChildListener(NodoChildListener l);

    Nodo<E> addNodoCargoListener(NodoCargoListener l);

    Nodo<E> removeNodoCargoListener(NodoCargoListener l);

    Nodo<E> addNodoOwnerListener(NodoOwnerListener l);

    Nodo<E> removeNodoOwnerListener(NodoOwnerListener l);

    public boolean isChildsOnDemand();

    public void setChildsOnDemand(boolean childsOnDemand);

    public boolean isChildsLoaded();

    public void setChildsLoaded(boolean childsLoaded);
    
    public void addFactory(NodoConnector factory);
    
    public void removeFactory(NodoConnector factory);
}
