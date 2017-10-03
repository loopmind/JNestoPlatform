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
package com.jnesto.platform.nodes;

import java.util.ResourceBundle;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

/**
 *
 * @author Flavio Vasconcellos Correa
 */
public class DefaultNodeModel extends AbstractTreeTableModel {

    protected static String[] columnNames = {
        ResourceBundle.getBundle("resources/i18n/DefaultNodeModel").getString("DESCRIPTION"),
        ResourceBundle.getBundle("resources/i18n/DefaultNodeModel").getString("TYPE")
    };
    
    protected static Class[] columnClasses = {
        String.class,
        String.class
    };

    public DefaultNodeModel(DefaultNode root) {
        super(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultNode getRoot() {
        return (DefaultNode) root;
    }

    /**
     * Sets the root for this tree table model. This method will notify
     * listeners that a change has taken place.
     *
     * @param root the new root node to set
     */
    public void setRoot(DefaultNode root) {
        this.root = root;

        modelSupport.fireNewRoot();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public boolean isLeaf(Object node) {
        if (node instanceof DefaultNode) {
            DefaultNode cast = (DefaultNode) node;
            return (cast.getChildCount() == null || cast.getChildCount() == 0);
        }
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof DefaultNode) {
            DefaultNode cast = (DefaultNode) node;
            switch (column) {
                case 0:
                    return cast.getDisplayName();
                case 1:
                    return cast.getType();
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultNode getChild(Object parent, int index) {
        if (parent instanceof DefaultNode) {
            DefaultNode cast = (DefaultNode) parent;
            if (cast.getChildCount() > 0) {
                return (DefaultNode) cast.getChildNodes().get(index);
            }
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof DefaultNode) {
            DefaultNode cast = (DefaultNode) parent;
            return cast.getChildCount();
        }
        return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof DefaultNode && child instanceof DefaultNode) {
            DefaultNode parentCast = (DefaultNode) parent;
            if (parentCast.getChildNodes() != null) {
                DefaultNode cast = (DefaultNode) child;
                return parentCast.getChildNodes().indexOf(cast);
            }
        }
        return -1;
    }

}
