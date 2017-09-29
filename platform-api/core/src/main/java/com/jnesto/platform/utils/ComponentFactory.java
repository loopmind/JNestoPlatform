/*
 * Copyright 2015-2017 JNesto Team.
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

import com.jgoodies.common.swing.MnemonicUtils;
import com.jnesto.platform.actions.annotation.ActionReference;
import com.jnesto.platform.actions.annotation.ActionReferences;
import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.lookup.ServiceProvider;
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
import com.jnesto.platform.windows.JToolBarAction;
import com.jnesto.platform.windows.TopComponent;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.flexdock.view.View;
import org.flexdock.view.actions.DefaultDisplayAction;
import org.jdesktop.beansbinding.BindingGroup;

/**
 *
 * @author Flavio de Vasconcellos Correa.
 */
public final class ComponentFactory {

    private final static String MENUBARTAG = "MENU";
    private final static String TOOLBARTAG = "TOOLBAR";
    private final static String PATHSEPARATOR = "/";
    private final static String SELECTED_TAG = "selected";

    public static JToolBar createJToolbar(Collection<? extends JToolBarAction> actions) {
        return createJToolbar(actions, new JToolBar());
    }

    public static JToolBar createJToolbar(Collection<? extends JToolBarAction> actions, JToolBar tbar) {
        Objects.requireNonNull(actions);

        tbar.setFocusable(false);

        Predicate<Action> predicate = a
                -> ((a.getClass().isAnnotationPresent(ActionReferences.class)
                || a.getClass().isAnnotationPresent(ActionReference.class))
                && (loadActionReferenceByTag(a.getClass(), TOOLBARTAG) != null));

        Comparator<Action> comparator = (a, b) -> {
            ActionReference arA = loadActionReferenceByTag(a.getClass(), TOOLBARTAG);
            ActionReference arB = loadActionReferenceByTag(b.getClass(), TOOLBARTAG);
            return Integer.compare(arA.position(), arB.position());
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

        actions.stream()
                .filter(predicate)
                .sorted(comparator)
                .forEach(consumer);

        return tbar;
    }

    public static JMenuBar createJMenuBar(Collection actions) {
        return createJMenuBar(actions, new JMenuBar());
    }

    final static Map<String, Component> COMP_MAP = new HashMap<>();
    final static Map<String, ButtonGroup> BTNGROUP_MAP = new HashMap<>();
    final static BindingGroup BINDING_GROUP = new BindingGroup();

    public static JMenuBar createJMenuBar(Collection<? extends Object> actions, JMenuBar mbar) {
        Objects.requireNonNull(actions);

        COMP_MAP.put(MENUBARTAG, mbar);

        Predicate predicate = a
                -> ((a.getClass().isAnnotationPresent(ActionReferences.class)
                || a.getClass().isAnnotationPresent(ActionReference.class))
                && (loadActionReferenceByTag(a.getClass(), MENUBARTAG) != null));

        Comparator comparator = (a, b) -> {
            ActionReference arA = loadActionReferenceByTag(a.getClass(), MENUBARTAG);
            ActionReference arB = loadActionReferenceByTag(b.getClass(), MENUBARTAG);
            int r = arA.path().compareToIgnoreCase(arB.path());
            return r == 0 ? Integer.compare(arA.position(), arB.position()) : r;
        };

        Consumer consumer = a -> {
            ActionReference ar = loadActionReferenceByTag(a.getClass(), MENUBARTAG);
            if (ar != null) {
                Component comp = COMP_MAP.get(ar.path());
                if (comp != null) {
                    Component item = null;
                    if (a instanceof JMenuItemAction) {
                        item = new JMenuItem((JMenuItemAction) a);
                    } else {
                        if (a instanceof JCheckBoxAction) {
                            item = new JCheckBoxMenuItem((JCheckBoxAction) a);
                            JNestoTools.bindBuilder((JCheckBoxAction) a, SELECTED_TAG, (JCheckBoxMenuItem) item, SELECTED_TAG).bind();
                        } else {
                            if (a instanceof JRadioButtonAction) {
                                item = new JRadioButtonMenuItem((JRadioButtonAction) a);
                                if (!BTNGROUP_MAP.containsKey(ar.path())) {
                                    BTNGROUP_MAP.put(ar.path(), new ButtonGroup());
                                }
                                BTNGROUP_MAP.get(ar.path()).add((JRadioButtonMenuItem) item);
                                JNestoTools.bindBuilder((JRadioButtonAction) a, SELECTED_TAG, (JRadioButtonMenuItem) item, SELECTED_TAG).bind();
                            } else {
                                if (a instanceof JMenuAction) {
                                    item = new JMenu((JMenuAction) a);
                                } else {
                                    if (a instanceof TopComponent
                                            && a instanceof Container
                                            && a.getClass().isAnnotationPresent(ServiceProvider.class)) {
                                        ServiceProvider sp = a.getClass().getAnnotation(ServiceProvider.class);
                                        AbstractAction aa = new DefaultDisplayAction(sp.id());
                                        try {
                                            if (a.getClass().isAnnotationPresent(TopComponent.Description.class)) {
                                                TopComponent.Description description = a.getClass().getAnnotation(TopComponent.Description.class);
//                                                aa.putValue(AbstractAction.NAME, description.title());
                                                MnemonicUtils.configure(aa, description.title());
                                                aa.putValue(AbstractAction.SMALL_ICON, new ImageIcon(ImageIO.read(ComponentFactory.class.getResourceAsStream(description.iconBase()))));
                                                Lookup.register(DefaultDisplayAction.class, aa);
                                                if(a.getClass().isAnnotationPresent(ActionReference.class)) {
                                                    ActionReference aRef = a.getClass().getAnnotation(ActionReference.class);
                                                    Lookup.register(aRef.id(), aa);
                                                }
                                            }
                                        } catch (IOException ex) {
                                        }
                                        item = new JMenuItem(aa);

                                    }
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
                    COMP_MAP.put(ar.path() + PATHSEPARATOR + ar.id(), item);
                }
            }
        };

        actions.stream()
                .filter(predicate)
                .sorted(comparator)
                .forEach(consumer);
        BINDING_GROUP.bind();
        return mbar;
    }

    public static View createView(Container comp) {
        if (comp.getClass().isAnnotationPresent(TopComponent.Description.class)) {
            try {
                TopComponent.Description description = comp.getClass().getAnnotation(TopComponent.Description.class);
                View view = new View(description.title(), description.title());
                view.setContentPane(comp);
                Icon icon = new ImageIcon(ImageIO.read(ComponentFactory.class.getResourceAsStream(description.iconBase())));
                view.setIcon(icon);
                view.setTabIcon(icon);
                if (description.closeable()) {
                    view.addAction((Action) Lookup.lookupById("#CTL_CLOSEVIEWACTION"));
                }
                if (description.maximized()) {
                    view.addAction((Action) Lookup.lookupById("#CTL_MAXIMIZEDVIEWACTION"));
                }
                view.getTitlebar().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            Action action = ((Action) Lookup.lookupById("#CTL_MAXIMIZEDVIEWACTION"));
                            ActionEvent evt = new ActionEvent(view, 0, (String) action.getValue(AbstractAction.NAME));
                            action.actionPerformed(evt);
                        }
                    }

                });
                return view;
            } catch (IOException ex) {
            }
        }
        return null;
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
