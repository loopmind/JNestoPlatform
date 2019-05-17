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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author loopmind
 * @param <E>
 */
public class ObservableHashSet<E> extends HashSet<E> {

    private List<WeakReference<ListObserver<E>>> weakReferenceList;

    private enum ChangeEvent {
        ADD, UPDATE, REMOVE;
    }

    public interface ListObserver<E> {

        void onElementAdded(E element);

        void onElementRemoved(E element);
    }

    public ObservableHashSet() {
        super();
        ensureObserver();
    }

    public ObservableHashSet(Collection<? extends E> c) {
        super(c);
        ensureObserver();
    }

    public ObservableHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        ensureObserver();
    }

    public ObservableHashSet(int initialCapacity) {
        super(initialCapacity);
        ensureObserver();
    }

    public ObservableHashSet<E> addObserver(ObservableHashSet.ListObserver<E> observer) {
        weakReferenceList.add(new WeakReference<>(observer));
        return this;
    }

    private void ensureObserver() {
        if (weakReferenceList == null) {
            weakReferenceList = new ArrayList<>();
        }
    }

    @Override
    public void clear() {
        Collection<E> changes = new HashSet<>(size());
        changes.addAll(Collections.unmodifiableSet(this));
        changes.stream().forEach(a -> callObservable(ChangeEvent.REMOVE, a));
        super.clear();
    }

    @Override
    public boolean remove(Object o) {
        boolean isRemoved = super.remove(o);
        if (isRemoved) {
            callObservable(ObservableHashSet.ChangeEvent.REMOVE, (E) o);
        }
        return isRemoved;
    }

    @Override
    public boolean add(E o) {
        boolean isAdded = super.add(o);
        if (isAdded) {
            callObservable(ChangeEvent.ADD, o);
        }
        return isAdded;
    }

    private void callObservable(ObservableHashSet.ChangeEvent methodId, E element) {
        weakReferenceList
                .stream()
                .forEach(a -> {
                    switch (methodId) {
                        case ADD:
                            a.get().onElementAdded((E) element);
                            break;
                        case REMOVE:
                            a.get().onElementRemoved((E) element);
                            break;
                    }
                });
    }

}
