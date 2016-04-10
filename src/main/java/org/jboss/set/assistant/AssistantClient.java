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

package org.jboss.set.assistant;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.domain.Issue;
import org.jboss.set.aphrodite.domain.Label;
import org.jboss.set.aphrodite.domain.Patch;
import org.jboss.set.aphrodite.domain.PatchState;
import org.jboss.set.aphrodite.domain.Repository;
import org.jboss.set.aphrodite.domain.Stream;
import org.jboss.set.aphrodite.domain.StreamComponent;
import org.jboss.set.aphrodite.spi.AphroditeException;
import org.jboss.set.aphrodite.spi.NotFoundException;

/**
 * @author wangc
 *
 */
public class AssistantClient {

    private static Logger logger = Logger.getLogger(AssistantClient.class.getCanonicalName());

    private static Aphrodite aphrodite;

    private Properties properties; // FIXME

    private AssistantClient() {
        logger.info("starting AssistantClient.");
    }

    public static synchronized Aphrodite getAphrodite() throws AphroditeException {
        if (aphrodite == null) {
            aphrodite = Aphrodite.instance();
        }
        return aphrodite;
    }

    public Properties getProperties() {
        return properties;
    }

    // several common functions already provided by Aphrodite

    public List<Label> getGithubLabels(Patch patch) throws NotFoundException {
        return aphrodite.getLabelsFromPatch(patch);
    }

    public List<Patch> getPatchesByState(Repository repository, PatchState state) throws NotFoundException {
        return aphrodite.getPatchesByState(repository, state);
    }

    public Map<StreamComponent, List<Patch>> getPatchesByState(String streamName, PatchState state) throws NotFoundException {
        Map<StreamComponent, List<Patch>> patches = new HashMap<>();
        Stream stream = aphrodite.getStream(streamName);
        Collection<StreamComponent> components = stream.getAllComponents();
        for (StreamComponent component : components)
            patches.put(component, aphrodite.getPatchesByState(component.getRepository(), state));
        return patches;
    }

    public List<Patch> getPatchesByState(URL url, PatchState state) throws NotFoundException {
        Repository repository = aphrodite.getRepository(url);
        return aphrodite.getPatchesByState(repository, state);
    }

    public List<Issue> getIssuesAssociatedWith(Patch patch) {
        return aphrodite.getIssuesAssociatedWith(patch);
    }
}
