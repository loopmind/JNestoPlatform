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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author loopmind
 * @param <E>
 */
public abstract class AbstractNodo<E> implements Nodo<E> {

    private boolean childsOnDemand = false;
    private boolean childsLoaded = false;
    private Nodo<E> parent;
    private E cargo;
    private Collection<NodoCargoListener> nodoCargoListers;
    private Collection<NodoChildListener> nodoChildListeners;
    private Collection<NodoOwnerListener> nodoOwnerListeners;
    private List<Nodo<E>> childs;
    private final Collection<NodoConnector> nodoConnector = new HashSet<>();

    
    public AbstractNodo(Nodo<E> owner, E cargo, boolean childsOnDemand) {
        this.parent = owner;
        this.cargo = cargo;
        this.childsOnDemand = childsOnDemand;
    }

    public AbstractNodo(Nodo<E> owner, E cargo) {
        this(owner, cargo, false);
    }

    public AbstractNodo(Nodo<E> owner) {
        this(owner, null, false);
    }
    
    public AbstractNodo() {
        this(null, null, false);
    }

    @Override
    public Nodo<E> setParent(Nodo<E> nodo) {
        Nodo<E> oldOwner = parent;
        parent = nodo;
        if (!(oldOwner == null || oldOwner.nodos().contains(nodo))) {
            oldOwner.nodos().remove(nodo);
        }
        if (!(nodo == null || nodoOwnerListeners.isEmpty())) {
            nodoOwnerListeners.forEach(a -> a.changeOwnerEvent(this, oldOwner, nodo));
        }
        return this;
    }

    @Override
    public Nodo<E> getParent() {
        return this.parent;
    }

    @Override
    public List<Nodo<E>> childs(int capacity) {
        if(childs == null) {
            childs = childCollectionFactory(true, capacity);
        }
        return childs;
    }

    @Override
    public List<Nodo<E>> nodos() {
        childs(0);
        if (!isChildsLoaded() && !isLeaf()) {
            nodoConnector.stream().forEach(f -> {
                childs.addAll(f.getNodos());
            });
        }
        setChildsLoaded(true);        
        return childs;
    }

    @Override
    public E getCargo() {
        return this.cargo;
    }

    @Override
    public Nodo<E> setCargo(E cargo) {
        E oldCargo = this.cargo;
        if (!Objects.equals(this.cargo, cargo)) {
            this.cargo = cargo;
            if (!(nodoCargoListers == null || nodoCargoListers.isEmpty())) {
                nodoCargoListers.forEach(a -> a.cargoChangedEvent(this, oldCargo, cargo));
            }
        }
        return this;
    }

    @Override
    public Nodo<E> removeNodoOwnerListener(NodoOwnerListener l) {
        if (!(nodoOwnerListeners == null || l == null)) {
            nodoOwnerListeners.remove(l);
        }
        return this;
    }

    @Override
    public Nodo<E> addNodoOwnerListener(NodoOwnerListener l) {
        if (nodoOwnerListeners == null) {
            nodoOwnerListeners = Collections.synchronizedSet(new HashSet<>());
        }
        nodoOwnerListeners.add(l);
        return this;
    }

    @Override
    public Nodo<E> removeNodoCargoListener(NodoCargoListener l) {
        if (nodoCargoListers == null) {
            nodoCargoListers.remove(l);
        }
        return this;
    }

    @Override
    public Nodo<E> addNodoCargoListener(NodoCargoListener l) {
        if (nodoCargoListers == null) {
            nodoCargoListers = Collections.synchronizedSet(new HashSet<>());
        }
        nodoCargoListers.add(l);
        return this;
    }

    @Override
    public Nodo<E> removeNodoChildListener(NodoChildListener l) {
        if (!(nodoChildListeners == null || l == null)) {
            nodoChildListeners.remove(l);
        }
        return this;
    }

    @Override
    public Nodo<E> addNodoChildListener(NodoChildListener l) {
        if (nodoChildListeners == null) {
            nodoChildListeners = Collections.synchronizedSet(new HashSet<>());
        }
        nodoChildListeners.add(l);
        return this;
    }

    @Override
    public boolean isChildsOnDemand() {
        return childsOnDemand;
    }

    @Override
    public void setChildsOnDemand(boolean childsOnDemand) {
        this.childsOnDemand = childsOnDemand;
    }

    @Override
    public boolean isChildsLoaded() {
        return childsLoaded;
    }

    @Override
    public void setChildsLoaded(boolean childsLoaded) {
        this.childsLoaded = childsLoaded;
    }

    private List<Nodo<E>> childCollectionFactory(boolean concurrency, int capacity) {
        ObservableArrayList<Nodo<E>> list = new ObservableArrayList<>(capacity);
        list.addObserver(new ObservableArrayList.ListObserver<Nodo<E>>() {

            @Override
            public void onElementAdded(int index, Nodo<E> element) {
                callObservable(AbstractNodo.this, element, NodoAction.ADD);
            }

            @Override
            public void onElementRemoved(int index, Nodo<E> element) {
                callObservable(AbstractNodo.this, element, NodoAction.REMOVE);
            }

            @Override
            public void onElementUpdated(int index, Nodo<E> element) {
                callObservable(AbstractNodo.this, element, NodoAction.UPDATE);
            }

            private void callObservable(Nodo<E> source, Nodo<E> child, Nodo.NodoAction action) {
                if (nodoChildListeners != null) {
                    nodoChildListeners
                            .stream()
                            .filter(p -> p != null)
                            .forEach(a -> a.childEvent(source, child, action));
                }
            }

        }
        );
        return concurrency ? Collections.synchronizedList(list) : list;
    }

    @Override
    public void removeFactory(NodoConnector factory) {
        nodoConnector.remove(factory);
    }

    @Override
    public void addFactory(NodoConnector factory) {
        nodoConnector.add(factory);
    }

}
