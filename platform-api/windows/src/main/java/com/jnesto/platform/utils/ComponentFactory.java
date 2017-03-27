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
package com.jnesto.platform.utils;

import com.jnesto.platform.actions.ActionReference;
import com.jnesto.platform.actions.ActionReferences;
import com.jnesto.platform.windows.JCheckBoxAction;
import com.jnesto.platform.windows.JMenuAction;
import com.jnesto.platform.windows.JMenuItemAction;
import java.awt.Component;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import com.jnesto.platform.windows.JRadioButtonAction;
import org.jdesktop.beansbinding.BindingGroup;

/**
 *
 * @author Flavio de Vasconcellos Correa.
 */
public final class ComponentFactory {
    
    private final static String MENUBARTAG = "MENU";
    private final static String TOOLBARTAG = "TOOLBAR";
    private final static String PATHSEPARATOR = "/";
    
    public static JToolBar createJToolbar(Collection<? extends JMenuAction> actions) {
        Objects.requireNonNull(actions);
        
        final JToolBar tbar = new JToolBar();
        
        tbar.setFocusable(false);
        
        Predicate<Action> predicate = a
                -> (a.getClass().isAnnotationPresent(ActionReferences.class)
                || a.getClass().isAnnotationPresent(ActionReference.class));
        
        Comparator<Action> comparator = (a, b) -> {
            ActionReference arA = loadActionReferenceByTag(a.getClass(), TOOLBARTAG);
            ActionReference arB = loadActionReferenceByTag(b.getClass(), TOOLBARTAG);
            int r = 0;
            if (arA != null && arB != null) {
                r = arA.path().compareToIgnoreCase(arB.path());
                if (r == 0) {
                    r = Integer.compare(arA.position(), arB.position());
                }
            }
            return r;
        };
        Consumer<Action> consumer = a -> {
            ActionReference ar = loadActionReferenceByTag(a.getClass(), TOOLBARTAG);
            if (ar != null) {
                if (ar.separatorAfter()) {
                    tbar.addSeparator();
                }
                JButton btn = new JButton(a);
                btn.setFocusable(false);
                btn.setHideActionText(true);
                tbar.add(btn);
                if (ar.separatorBefore()) {
                    tbar.addSeparator();
                }
            }
        };
        
        actions.stream().filter(predicate).sorted(comparator).forEach(consumer);
        
        return tbar;
    }
    
    public static JMenuBar createJMenuBar(Collection<? extends JMenuAction> actions) {
        return createJMenuBar(actions, null);
    }
 
    public static JMenuBar createJMenuBar(Collection<? extends JMenuAction> actions, JMenuBar mbar) {
        Objects.requireNonNull(actions);
        mbar = mbar == null ? new JMenuBar() : mbar;
        final Map<String, Component> mapComp = new HashMap<>();
        final Map<String, ButtonGroup> btnGroupMap = new HashMap<>();
        final BindingGroup bg = new BindingGroup();
        
        mapComp.put(MENUBARTAG, mbar);
        
        Predicate<Action> predicate = a
                -> (a.getClass().isAnnotationPresent(ActionReferences.class)
                || a.getClass().isAnnotationPresent(ActionReference.class));
        
        Comparator<Action> comparator = (a, b) -> {
            ActionReference arA = loadActionReferenceByTag(a.getClass(), MENUBARTAG);
            ActionReference arB = loadActionReferenceByTag(b.getClass(), MENUBARTAG);
            int r = 0;
            if (arA != null && arB != null) {
                r = arA.path().compareToIgnoreCase(arB.path());
                if (r == 0) {
                    r = Integer.compare(arA.position(), arB.position());
                }
            }
            return r;
        };
        
        Consumer<Action> consumer = a -> {
            ActionReference ar = loadActionReferenceByTag(a.getClass(), MENUBARTAG);
            if (ar != null) {
                Component comp = mapComp.get(ar.path());
                if (comp != null) {
                    Component item = null;
                    if (a instanceof JMenuItemAction) {
                        item = new JMenuItem(a);
                    } else {
                        if (a instanceof JCheckBoxAction) {
                            item = new JCheckBoxMenuItem(a);
                            BeanUtils.bindBuilder((JCheckBoxAction) a, "selected", (JCheckBoxMenuItem) item, "selected").bind();
                        } else {
                            if (a instanceof JRadioButtonAction) {
                                item = new JRadioButtonMenuItem(a);
                                if (!btnGroupMap.containsKey(ar.path())) {
                                    btnGroupMap.put(ar.path(), new ButtonGroup());
                                }
                                btnGroupMap.get(ar.path()).add((JRadioButtonMenuItem) item);
                                BeanUtils.bindBuilder((JRadioButtonAction) a, "selected", (JRadioButtonMenuItem) item, "selected").bind();
                            } else {
                                if (a instanceof JMenuAction) {
                                    item = new JMenu(a);
                                }
                            }
                        }
                    }
                    if (item != null) {
                        if (comp instanceof JMenuItem) {
                            if (ar.separatorBefore()) {
                                ((JMenuItem) comp).add(new JSeparator());
                            }
                            ((JMenuItem) comp).add(item);
                            if (ar.separatorAfter()) {
                                ((JMenuItem) comp).add(new JSeparator());
                            }
                        } else {
                            if (comp instanceof JMenuBar) {
                                ((JMenuBar) comp).add(item);
                            }
                        }
                    }
                    mapComp.put(ar.path() + PATHSEPARATOR + ar.id(), item);
                }
            }
        };
        
        actions.stream().filter(predicate).sorted(comparator).forEach(consumer);
        bg.bind();
        return mbar;
    }

    private static ActionReference loadActionReferenceByTag(Class clazz, String tag) {
        Objects.requireNonNull(tag);
        Objects.requireNonNull(clazz);
        if (clazz.isAnnotationPresent(ActionReferences.class)) {
            ActionReferences ars = (ActionReferences) clazz.getAnnotation(ActionReferences.class);
            for (ActionReference ar : ars.value()) {
                if (ar.path().startsWith(tag)) {
                    return ar;
                }
            }
        } else {
            if (clazz.isAnnotationPresent(ActionReference.class)) {
                ActionReference ar = (ActionReference) clazz.getAnnotation(ActionReference.class);
                if (ar.path().startsWith(tag)) {
                    return ar;
                }
            }
        }
        return null;
    }
    
    public static JButton createJButton(AbstractAction a) {
        Objects.requireNonNull(a);
        return new JButton(a);
    }
    
    public static JCheckBox createJCheckBox(AbstractAction a) {
        Objects.requireNonNull(a);
        if (a instanceof JCheckBoxAction) {
            return new JCheckBox(a);
        }
        return null;
    }
    
}
