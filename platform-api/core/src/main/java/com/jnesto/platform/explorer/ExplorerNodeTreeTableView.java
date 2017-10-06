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
import java.awt.HeadlessException;
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

    public ExplorerNodeTreeTableView(TreeModel model) {
        config(model);
    }

    private void config(TreeModel model) {
        setLayout(new BorderLayout());
        add(
                new JScrollPane(
                        new FileNodeTreeTable((TreeTableModel) model)
                )
                , BorderLayout.CENTER
        );

    }

    public static void main(String[] args) {
        JFrame mframe = new ExplorerFrame();
        mframe.add(
                new ExplorerNodeTreeTableView(
                        new DefaultFileNodeModel(new FileNode())
                )
        );
        mframe.setVisible(true);
    }

    static class ExplorerFrame extends JFrame {

        public ExplorerFrame() throws HeadlessException {
            init();
        }

        private void init() {
            setSize(new Dimension(640, 480));
            setLayout(new BorderLayout());
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        }

    }

    static class FileNodeTreeTable extends JXTreeTable {

        public FileNodeTreeTable(TreeTableModel treeModel) {
            super(treeModel);
            init();
        }

        private void init() {
            setDragEnabled(true);
            setRootVisible(false);
            setShowsRootHandles(true);
            setColumnControlVisible(true);
            setRolloverEnabled(true);
        }

    }
}
