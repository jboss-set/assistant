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

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.jboss.set.aphrodite.domain.PullRequest;
import org.jboss.set.assistant.evaluator.Evaluator;
import org.jboss.set.assistant.evaluator.EvaluatorContext;

/**
 * @author egonzalez
 *
 */
public class UpstreamEvaluator implements Evaluator {

    private Pattern UPSTREAM_NOT_REQUIRED = Pattern.compile(".*no.*upstream.*required.*", Pattern.CASE_INSENSITIVE);

    @Override
    public String name() {
        return "Upstream Evaluator";
    }

    @Override
    public void eval(EvaluatorContext context, Map<String, Object> data) {
        PullRequest pullRequest = context.getPullRequest();
        Set<PullRequest> related = context.getRelated();

        if (!UPSTREAM_NOT_REQUIRED.matcher(pullRequest.getBody()).find()) {
            if (!related.isEmpty()) {
                data.put("messages", "missing upstream issue link");
            }
        }
    }
}