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

package org.jboss.set.assistant.evaluator.impl.pullrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.set.aphrodite.domain.Issue;
import org.jboss.set.aphrodite.domain.Patch;
import org.jboss.set.aphrodite.spi.StreamService;
import org.jboss.set.assistant.data.IssueData;
import org.jboss.set.assistant.evaluator.Evaluator;
import org.jboss.set.assistant.evaluator.EvaluatorContext;
import org.jboss.set.assistant.evaluator.Util;

/**
 * @author egonzalez
 *
 */
public class IssuesRelatedEvaluator implements Evaluator {

    @Override
    public String name() {
        return "Issues Related evaluator";
    }

    @Override
    public void eval(EvaluatorContext context, Map<String, Object> data) {
        List<Issue> issues = context.getIssues();
        Map<String, List<String>> issueStream = new HashMap<>();

        for (Issue issue : issues) {
            List<String> streams = Util.getStreams(issue);
            issueStream.put(issue.getTrackerId().get(), streams);
        }

        StreamService service = context.getStreamService();
        Patch patch = context.getPatch();
        List<String> streams = service.getStreamsBy(patch.getRepository(), patch.getCodebase()).stream().map(e -> e.getName())
                .collect(Collectors.toList());

        data.put("issuesRelated", issues.stream()
                .filter(e -> {
                    List<String> intersect = new ArrayList<>(streams);
                    intersect.retainAll(issueStream.get(e.getTrackerId().get()));
                    return !intersect.isEmpty();
                })
                .map(e -> new IssueData(e.getTrackerId().get(), issueStream.get(e.getTrackerId().get()), e.getURL()))
                .collect(Collectors.toList()));

        data.put("issuesOtherStreams", issues.stream()
                .filter(e -> {
                    List<String> intersect = new ArrayList<>(streams);
                    intersect.retainAll(issueStream.get(e.getTrackerId().get()));
                    return intersect.isEmpty();
                })
                .map(e -> new IssueData(e.getTrackerId().get(), issueStream.get(e.getTrackerId().get()), e.getURL()))
                .collect(Collectors.toList()));
    }

}