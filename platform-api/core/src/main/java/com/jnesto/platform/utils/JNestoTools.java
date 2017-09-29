/*
 * Copyright (c) 2015-2017 JNesto Team.
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

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;

/**
 *
 * @author Flavio de Vasconcellos Correa.
 */
public class JNestoTools {

    public static AutoBinding bindBuilder(Object s, AutoBinding.UpdateStrategy us, String ps, Object t, String pt) {
        if (s != null && t != null && us != null && JNestoTools.isNonEmpty(ps) && JNestoTools.isNonEmpty(pt)) {
            return Bindings.createAutoBinding(
                    us,
                    s,
                    ps.startsWith("$") ? ELProperty.create(ps) : BeanProperty.create(ps),
                    t,
                    pt.startsWith("$") ? ELProperty.create(pt) : BeanProperty.create(pt)
            );
        }
        return null;
    }

    public static AutoBinding bindBuilder(Object source, AutoBinding.UpdateStrategy us, Enum propSource, Object target, String propTarget) {
        return bindBuilder(source, us, propSource.name(), target, propTarget);
    }

    public static AutoBinding bindBuilder(Object source, Enum propSource, Object target, String propTarget) {
        return bindBuilder(source, AutoBinding.UpdateStrategy.READ_WRITE, propSource.name(), target, propTarget);
    }

    public static AutoBinding bindBuilder(Object source, String propSource, Object target, String propTarget) {
        return bindBuilder(source, AutoBinding.UpdateStrategy.READ_WRITE, propSource, target, propTarget);
    }

    public static String maskFormat(String pMask, String pValue, boolean pReturnValueEmpty) {
        if (pReturnValueEmpty == true && (pValue == null || pValue.trim().equals(""))) {
            return "";
        }
        pMask = pMask.replaceAll("9", "#");
        for (int i = 0; i < pValue.length(); i++) {
            pMask = pMask.replaceFirst("#", pValue.substring(i, i + 1));
        }
        return pMask.replaceAll("#", "");
    }

    public static boolean isNonEmpty(String s) {
        return (s != null && !s.isEmpty());
    }

    public static boolean isEmpty(String s) {
        return !isNonEmpty(s);
    }

    public enum Orientation {
        RIGHT, LEFT;
    }

    public static String completeZeros(String source, int len, Orientation o) {
        int capacity = len - source.length();
        if (capacity <= 0) {
            return source;
        } else {
            StringBuilder sbZeros = new StringBuilder(capacity);
            while (sbZeros.length() < sbZeros.capacity()) {
                sbZeros.append("0");
            }
            return o.equals(Orientation.LEFT) ? sbZeros.toString().concat(source) : source.concat(sbZeros.toString());
        }
    }

    public static void changeFontStyle(Component comp, String name, Integer style, Integer size) {
        Font f = comp.getFont();
        String _name = name == null ? f.getName() : name;
        int _style = style == null ? f.getStyle() : style;
        int _size = size == null ? f.getSize() : size;
        Font n = new Font(_name, _style, _size);
        comp.setFont(n);
    }

    public final static String UNDO_ACTION = "Undo";

    public final static String REDO_ACTION = "Redo";

    public static void makeUndoable(JTextComponent textComponent) {
        final UndoManager undoMgr = new UndoManager();

        // Add listener for undoable events
        textComponent.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                undoMgr.addEdit(evt.getEdit());
            }
        });

        // Add undo/redo actions
        textComponent.getActionMap().put(UNDO_ACTION, new AbstractAction(UNDO_ACTION) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (undoMgr.canUndo()) {
                        undoMgr.undo();
                    }
                } catch (CannotUndoException e) {
                }
            }
        });
        textComponent.getActionMap().put(REDO_ACTION, new AbstractAction(REDO_ACTION) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (undoMgr.canRedo()) {
                        undoMgr.redo();
                    }
                } catch (CannotRedoException e) {
                }
            }
        });
        // Create keyboard accelerators for undo/redo actions (Ctrl+Z/Ctrl+Y)
        textComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), UNDO_ACTION);
        textComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), REDO_ACTION);
    }
}
