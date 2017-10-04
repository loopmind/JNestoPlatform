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

import java.util.Date;

/**
 *
 * @author Flavio
 */
public class DocumentFileNodeModel extends DefaultFileNodeModel {

    protected static String[] columnDocFileNames = {
        "",
        "",
        "",
        "",
        "Autores",
        "Assunto",
        "Modelo"
    };

    protected static Class[] columnDocFileClasses = {
        null,
        null,
        null,
        null,
        String.class,
        String.class,
        String.class
    };

    public DocumentFileNodeModel(DocumentFileNode root) {
        super(root);
    }

    @Override
    public int getColumnCount() {
        return columnDocFileNames.length;
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof FileNode) {
            switch (column) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return super.getValueAt(node, column);
                case 4:
                    return ((DocumentFileNode) root).getAuthors();
                case 5:
                    return ((DocumentFileNode) root).getSubject();
                case 6:
                    return ((DocumentFileNode) root).getModel();
                    
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
            case 2:
            case 3:
                return super.getColumnName(column);
            default:
                return columnDocFileNames[column];
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
            case 1:
            case 2:
            case 3:
                return super.getColumnClass(column);
            default:
                return columnDocFileClasses[column];
        }
    }
}
