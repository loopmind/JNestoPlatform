/*
 * Copyright 2017 JNesto Team.
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
package com.jnesto.platform.windows;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public class JNestoDialog extends JDialog {

    private boolean bOk = false;

    public boolean isOk() {
        return bOk;
    }

    public void init() {
        ActionListener cancelEvent = (e -> {
            bOk = false;
            dispose();
        });
        getRootPane().registerKeyboardAction(cancelEvent, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public JNestoDialog() {
        init();
    }

    public JNestoDialog(Frame owner) {
        super(owner);
        init();
    }

    public JNestoDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    public JNestoDialog(Frame owner, String title) {
        super(owner, title);
        init();
    }

    public JNestoDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        init();
    }

    public JNestoDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        init();
    }

    public JNestoDialog(Dialog owner) {
        super(owner);
        init();
    }

    public JNestoDialog(Dialog owner, boolean modal) {
        super(owner, modal);
        init();
    }

    public JNestoDialog(Dialog owner, String title) {
        super(owner, title);
        init();
    }

    public JNestoDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        init();
    }

    public JNestoDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        init();
    }

    public JNestoDialog(Window owner) {
        super(owner);
        init();
    }

    public JNestoDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);
        init();
    }

    public JNestoDialog(Window owner, String title) {
        super(owner, title);
        init();
    }

    public JNestoDialog(Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
        init();
    }

    public JNestoDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
        init();
    }

}
