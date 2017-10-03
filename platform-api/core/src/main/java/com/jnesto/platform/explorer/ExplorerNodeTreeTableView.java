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
package com.jnesto.platform.explorer;

import com.jnesto.platform.nodes.DefaultFileNodeModel;
import com.jnesto.platform.nodes.FileNode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.tree.TreeModel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public class ExplorerNodeTreeTableView extends JComponent {

    private JXTreeTable tree;

    public ExplorerNodeTreeTableView(TreeModel model) {
        config(model);
    }

    private void config(TreeModel model) {
        tree = new JXTreeTable((TreeTableModel) model);
        tree.setDragEnabled(true);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setColumnControlVisible(true);
        setLayout(new BorderLayout());
        add(new JScrollPane(tree), BorderLayout.CENTER);
        tree.setRolloverEnabled(true);
    }

    public static void main(String[] args) {
        JFrame mframe = new JFrame();
        mframe.setSize(new Dimension(640, 480));
        mframe.setLayout(new BorderLayout());
        mframe.add(new ExplorerNodeTreeTableView(new DefaultFileNodeModel(new FileNode())));
        mframe.setVisible(true);
        mframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit(0);
            }
        }
        );
    }

    private static void exit(int code) {
        System.exit(code);
    }
}
