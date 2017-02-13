/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat, Inc., and individual contributors
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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Stream;

import org.jboss.jbossset.bugclerk.Candidate;
import org.jboss.jbossset.bugclerk.RuleEngine;
import org.jboss.jbossset.bugclerk.Violation;
import org.jboss.jbossset.bugclerk.aphrodite.AphroditeClient;
import org.jboss.set.aphrodite.domain.Issue;

/**
 * @author wangc
 *
 */
public class ViolationHomeService implements ViolationHome {
    private static final AphroditeClient aphroditeClient;

    static {
        aphroditeClient = new AphroditeClient();
    }

    /*
     * @see org.jboss.set.assistant.ViolationHome#findViolations(org.jboss.set.aphrodite.domain.Issue)
     */
    @Override
    public Stream<Violation> findViolations(Issue issue) {
        // String seesionId = issue.getTrackerId().orElse("unknownId");
        // KIE_SESSION.getFactHandles(new ClassObjectFilter(Violation.class)).forEach(factHandle -> {
        // KIE_SESSION.delete(factHandle);
        // });
        RuleEngine RULE_ENGINE = new RuleEngine(new HashMap<String, Object>(0), aphroditeClient);
        Collection<Candidate> candidates = RULE_ENGINE.processBugEntry(Arrays.asList(new Candidate(issue)));
        RULE_ENGINE.shutdownRuleEngine();
        if (candidates != null && !candidates.isEmpty()) {
            return candidates.iterator().next().getViolations().stream();
        } else {
            return Stream.empty();
        }
    }
}
