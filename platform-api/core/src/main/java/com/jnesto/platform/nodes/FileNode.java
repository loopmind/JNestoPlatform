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
package com.jnesto.platform.nodes;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author flavio
 */
public class FileNode extends AbstractNode {

    private final Logger log = LoggerFactory.getLogger(FileNode.class);
    private List<FileNode> childs;

    private File content;

    public FileNode() {
        this(new File(File.separator));
    }

    public FileNode(File content) {
        this.content = content;
    }

    @Override
    public Object getContent() {
        return content;
    }
    
    @Override
    public void setContent(Object content) {
        this.content = (File) content;
    }

    @Override
    public String getType() {
        return (content.isDirectory() ? 
                ResourceBundle.getBundle("com/jnesto/platform/nodes/resources/Bundle").getString("DIRECTORY") : 
                ResourceBundle.getBundle("com/jnesto/platform/nodes/resources/Bundle").getString("FILE"));
    }

    @Override
    public String getDisplayName() {
        return content.getName();
    }

    @Override
    public Integer getChildCount() {
        String[] children = content.list();
        if (children != null) {
            return children.length;
        }
        return null;
    }

    @Override
    public ImageIcon getIcon() {
        return null;
    }

    @Override
    public List getChildNodes() {
        if (childs == null && content != null) {
            childs = new ArrayList<>();
            Arrays.asList(content.list())
                    .stream()
                    .sorted((sa, sb) -> sa.compareToIgnoreCase(sb))
                    .forEach(name -> childs.add(new FileNode(new File((File) content, name))));
        }
        return childs != null ? childs : Collections.EMPTY_LIST;
    }

}
