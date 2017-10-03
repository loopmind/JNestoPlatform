/*
 * Copyright 2017 Flavio.
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

import java.io.File;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Flavio
 */
public class DefaultFileNodeModel extends DefaultNodeModel {

    protected static String[] columnFileNames = {
        "", 
        "",
        ResourceBundle.getBundle("resources/i18n/DefaultFileNodeModel").getString("SIZE"),
        ResourceBundle.getBundle("resources/i18n/DefaultFileNodeModel").getString("LASTMODIFIED")    
    };

    protected static Class[] columnFileClasses = {
        null,
        null,
        String.class,
        Date.class
    };

    public DefaultFileNodeModel(DefaultNode root) {
        super(root);
    }

    @Override
    public int getColumnCount() {
        return super.getColumnCount() + 2;
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof FileNode) {
            FileNode cast = (FileNode) node;
            File content = ((File) cast.getContent());
            switch (column) {
                case 0:
                case 1:
                    return super.getValueAt(node, column);
                case 2:
                    return content.isDirectory() ? 
                            null :  
                            NumberFormat.getIntegerInstance().format(content.length()).concat(" bytes");
                case 3:
                    return content.isDirectory() ? 
                            null : 
                            content.lastModified();
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
            case 1:
                return super.getColumnName(column);
            default:
                return columnFileNames[column];
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
            case 1:
                return super.getColumnClass(column);
            default:
                return columnFileClasses[column];
        }        
    }
}
