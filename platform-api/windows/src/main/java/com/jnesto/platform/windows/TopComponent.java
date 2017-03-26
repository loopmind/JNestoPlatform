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
package com.jnesto.platform.windows;

import com.jnesto.platform.lookup.ServiceProvider;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 *
 * @author Flávio de Vasconcellos Corrêa.
 */
public class TopComponent extends JComponent {

    private boolean open;
    private boolean hidden;
    private boolean docked;
    private boolean maximized;
    private boolean floating;
//    private DockingDesktop dockingDesktop;
    private List<TopComponentListener> tclList = Collections.synchronizedList(new LinkedList<>());

    public TopComponent() {

    }

    public void addTopComponentListener(TopComponentListener tcl) {
        Objects.requireNonNull(tcl, "TopComponentListener is a null value");
        tclList.add(tcl);
    }

    public void removeTopComponentListener(TopComponentListener tcl) {
        Objects.requireNonNull(tcl, "TopComponent is a null value");
        if (tclList.contains(tcl)) {
            tclList.remove(tcl);
        }
    }

    private void setOpen(boolean open) {
        this.open = open;
        tclList.stream().forEach(tcl -> {
            if (isOpen()) {
                tcl.componentOpened();
            } else {
                tcl.componentClosed();
            }
        });
    }

    public boolean isOpen() {
        return open;
    }

    private void setHidden(boolean hidden) {
        this.hidden = hidden;
        tclList.stream().forEach(tcl -> {
            if (isHidden()) {
                tcl.componentHidden();
            } else {
                tcl.componentShowing();
            }
        });
    }

    public boolean isHidden() {
        return hidden;
    }

    private void setDocked(boolean docked) {
        this.docked = docked;
    }

    public boolean isDocked() {
        return docked;
    }

    private void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    public boolean isMaximized() {
        return maximized;
    }

    private void setFloating(boolean floating) {
        this.floating = floating;
    }

    public boolean isFloating() {
        return floating;
    }

    public void open() {
    }

    @Retention(value = RUNTIME)
    @Target(value = {TYPE})
    public @interface Description {

        String title();

        String group();

        boolean maximized() default true;

        boolean minimized() default true;

        boolean deatached() default true;

        boolean closeable() default true;

        String iconBase() default "";
    }
}
