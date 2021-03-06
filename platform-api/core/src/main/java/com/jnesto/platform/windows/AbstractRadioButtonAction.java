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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Flavio de Vasconcellos Correa.
 */
public abstract class AbstractRadioButtonAction extends AbstractAction implements JRadioButtonAction {

    private boolean selected = false;

    public static String PROP_SELECTED = "selected";

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void setSelected(boolean selected) {
        boolean oldStatus = this.selected;
        this.selected = selected;
        firePropertyChange(PROP_SELECTED, oldStatus, selected);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

}
