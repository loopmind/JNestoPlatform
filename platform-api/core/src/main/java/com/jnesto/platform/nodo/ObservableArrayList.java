/*
 * Copyright 2018 flavio.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author loopmind
 * @param <E>
 */
public class ObservableArrayList<E> extends ArrayList<E> {

    private List<java.lang.ref.WeakReference<ListObserver<E>>> weakRefList;

    private enum ChangeEvent {
        ADD, UPDATE, REMOVE;
    }

    public interface ListObserver<E> {

        void onElementAdded(int index, E element);

        void onElementRemoved(int index, E element);

        void onElementUpdated(int index, E element);
    }

    public ObservableArrayList() {
        super();
        ensureObserver();
    }

    public ObservableArrayList(int capacity) {
        super(capacity);
        ensureObserver();
    }

    public ObservableArrayList(Collection<? extends E> collection) {
        super(collection);
        ensureObserver();
    }

    public ObservableArrayList<E> addObserver(ListObserver<E> observer) {
        weakRefList.add(new java.lang.ref.WeakReference<>(observer));
        return this;
    }

    public ObservableArrayList<E> removeObserver(ListObserver<E> observer) {
        weakRefList.remove(new java.lang.ref.WeakReference<>(observer));
        return this;
    }

    private void ensureObserver() {
        if (weakRefList == null) {
            weakRefList = new ArrayList<>();
        }
    }

    @Override
    public boolean add(E object) {
        boolean isAdded = super.add(object);
        if (isAdded) {
            callObservable(ChangeEvent.ADD, size()-1, object);
        }
        return isAdded;
    }
    
    @Override
    public void add(int idx, E object) {
        super.add(idx, object);
        callObservable(ChangeEvent.ADD, idx, object);
    }
    
    @Override
    public E set(int index, E element) {
        E object = super.set(index, element);
        callObservable(ChangeEvent.UPDATE, index, object);
        return object;
    }
    
    @Override
    public E remove(int index) {
        E object = super.remove(index);
        callObservable(ChangeEvent.UPDATE, index, object);
        return object;
    }

    @Override
    public boolean remove(Object object) {
        int index = super.indexOf(object);
        boolean isRemoved = super.remove(object);
        if (isRemoved) {
            callObservable(ChangeEvent.REMOVE, index, (E) object);
        }
        return isRemoved;
    }
    
    @Override
    public void clear() {
        Collection<ChangeObserver<E>> changes = new HashSet<>(size());
        for(int idx = 0; idx < size(); idx++) {
           changes.add(new ChangeObserver<>(idx, get(idx)));
        }
        changes.stream().forEach(o -> callObservable(ChangeEvent.REMOVE, o.getIndex(), o.getObject()));
        super.clear();
    }
    
    @Override
    public boolean addAll(Collection<? extends E> c) {
        int startIndex = size();
        boolean isAllAdded = super.addAll(c);
        Collection<ChangeObserver<E>> changes = new HashSet<>(size());
        for(int idx = startIndex; idx < size(); idx++) {
           changes.add(new ChangeObserver<>(idx, get(idx)));
        }
        changes.stream().forEach(o -> callObservable(ChangeEvent.UPDATE, o.getIndex(), o.getObject()));    
        return isAllAdded;
    }    

    private void callObservable(ChangeEvent methodId, int index, E element) {
        weakRefList
                .stream()
                .filter(p -> p.get() != null)
                .forEach(a -> {
                    switch (methodId) {
                        case ADD:
                            a.get().onElementAdded(index, element);
                            break;
                        case REMOVE:
                            a.get().onElementRemoved(index, element);
                            break;
                    }
                });
    }
    
    public static class ChangeObserver<E> {
        private int index;
        private E object;

        public ChangeObserver(int index, E object) {
            this.index = index;
            this.object = object;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public E getObject() {
            return object;
        }

        public void setObject(E object) {
            this.object = object;
        }
    }

}
