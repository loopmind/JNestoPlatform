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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author Flavio
 */
public class DefaultFileNodeModel extends DefaultNodeModel {

    protected static String[] columnFileNames = {
        null,
        null,
        ResourceBundle.getBundle("com/jnesto/platform/nodes/resources/Bundle").getString("SIZE"),
        ResourceBundle.getBundle("com/jnesto/platform/nodes/resources/Bundle").getString("LASTMODIFIED")
    };

    protected static Class[] columnFileClasses = {
        null,
        null,
        Long.class,
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
                    return content.isDirectory()
                            ? null
                            : convertIntToByteMask(content.length());
                case 3:
                    return content.isDirectory()
                            ? null
                            : "  ".concat(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(content.lastModified()));
                default:
                    return null;
            }
        }
        return null;

    }

    protected String convertIntToByteMask(long len) {
        long clen = 0;
        if (len >= 1000 && len < 1000000) {
            clen = len / 1000;
        } else if (len >= 1000000 && len < 1000000000) {
            clen = len / 1000000;
        } else if (len >= 1000000000) {
            clen = len / 1000000000;
        } else {
            clen = len;
        }
        String sLen = NumberFormat.getIntegerInstance().format(clen);
        if (len >= 1000 && len < 1000000) {
            return sLen + " KB";
        } else if (len >= 1000000 && len < 1000000000) {
            return sLen + " MB";
        } else if (len >= 1000000000) {
            return sLen + " GB";
        } else {
            return sLen + " B";
        }
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
