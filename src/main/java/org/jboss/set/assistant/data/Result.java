/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.set.assistant.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangc
 *
 */
public class Result {
    private boolean mergeable;
    private List<String> description;

    public Result() {
        this.description = new ArrayList<String>();
    }

    public Result(final boolean mergeable) {
        this.mergeable = mergeable;
        this.description = new ArrayList<String>();
    }

    public Result(final boolean mergeable, final String... description) {
        this.mergeable = mergeable;
        this.description = new ArrayList<String>(Arrays.asList(description));
    }

    public Result changeResult(boolean mergeable, String description) {
        setMergeable(mergeable);
        addDescription(description);
        return this;
    }

    public boolean isMergeable() {
        return mergeable;
    }

    public void setMergeable(final boolean mergeable) {
        this.mergeable = mergeable;
    }

    public List<String> getDescription() {
        return description;
    }

    public void addDescription(final String... description) {
        this.description.addAll(Arrays.asList(description));
    }

    public void addDescription(final List<String> description) {
        this.description.addAll(description);
    }

    /**
     * Logical conjunction with another {@code Result} instance.
     *
     * @param other
     * @return
     */
    public Result and(final Result other) {
        setMergeable(isMergeable() && other.isMergeable());
        addDescription(other.getDescription());
        return this;
    }
}