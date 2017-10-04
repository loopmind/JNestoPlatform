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

/**
 *
 * @author Flavio
 */
public class DocumentFileNode extends FileNode {

    private String authors;
    private String subject;
    private String model;

    public DocumentFileNode() {
        super();
    }

    public DocumentFileNode(File content) {
        super(content);
    }

    public DocumentFileNode(File content, String authors, String subject) {
        super(content);
        this.authors = authors;
        this.subject = subject;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
