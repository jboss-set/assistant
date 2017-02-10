/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2016, Red Hat, Inc., and individual contributors
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

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.domain.Issue;
import org.jboss.set.aphrodite.domain.spi.IssueHome;
import org.jboss.set.aphrodite.simplecontainer.SimpleContainer;
import org.jboss.set.aphrodite.spi.NotFoundException;
//import org.jboss.invocation.Interceptor;
//import org.jboss.invocation.InterceptorInvocationHandler;
//import org.jboss.invocation.Interceptors;
//import org.jboss.invocation.proxy.ProxyConfiguration;
//import org.jboss.invocation.proxy.ProxyFactory;
//import org.jboss.set.aphrodite.issue.trackers.jira.JiraIssueHelper;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
//import java.net.URL;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
import java.util.stream.Stream;

import javax.naming.NameNotFoundException;

//import static org.jboss.set.overview.Util.unchecked;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class IssueHomeService implements IssueHome {
    private static final Logger logger = Logger.getLogger(IssueHomeService.class.getCanonicalName());
//    private final JiraRestClient jiraRestClient = Container.getJiraRestClient();
//    private final Cache<String, PayloadIssue> cache = new Cache<>();
    private final AtomicInteger proxyNum = new AtomicInteger(0);

    private static Aphrodite aphrodite;

    static {
        try {
            aphrodite = SimpleContainer.instance().lookup(Aphrodite.class.getSimpleName(), Aphrodite.class);
        } catch (NameNotFoundException e) {
            logger.log(Level.SEVERE, "Can not get aphrodite service due to : ", e);
        }
    }

//    public Collection<Issue> findByPayload(final Payload payload) {
//        String jql = "project = JBEAP AND (fixVersion = " + payload.getFixVersion();
//        final String sprint = payload.getSprint();
//        if (sprint != null) jql += " OR Sprint = \"" + payload.getSprint() + "\"";
//        jql += ")";
//        // Note that the following fields: summary, issuetype, created, updated, project and status are required.
//        final Set<String> fields = new HashSet<>(Arrays.asList("summary", "issuetype", "created", "updated", "project", "status", "key"));
//        final Iterable<com.atlassian.jira.rest.client.api.domain.Issue> issues = jiraRestClient.getSearchClient().searchJql(jql, null, null, fields).claim().getIssues();
//        // TODO: pre-fill the cache with the obtained results
//        return StreamSupport.stream(issues.spliterator(), true)
//                .map(issue -> proxy(issue.getKey()))
//                .collect(Collectors.toList());
//    }
//
//    public Issue findByPrimaryKey(final String primaryKey) {
//        throw new RuntimeException("NYI: org.jboss.set.payload.report.IssueHome.findByPrimaryKey");
//        //return proxy(primaryKey);
//    }
//
//    private Issue load(final String primaryKey) {
//        com.atlassian.jira.rest.client.api.domain.Issue jiraIssue = unchecked(() -> jiraRestClient.getIssueClient().getIssue(primaryKey).get());
//        final URL url = unchecked(() -> new URL("https://issues.jboss.org/browse/" + jiraIssue.getKey()));
//        final Issue issue = new Issue();
//        JiraIssueHelper.copy(url, jiraIssue, issue);
//        issue.setURL(url);
//        return issue;
//    }
//
//    private Issue proxy(final String primaryKey) {
//        final ProxyConfiguration<Issue> proxyConfiguration = new ProxyConfiguration<Issue>()
//                .setClassLoader(IssueHome.class.getClassLoader())
//                .setProxyName(Issue.class.getPackage(), "Issue$Proxy" + proxyNum.getAndIncrement())
//                .setSuperClass(Issue.class);
//        final ProxyFactory<Issue> proxyFactory = new ProxyFactory<>(proxyConfiguration);
//        final Interceptor interceptor = Interceptors.getChainedInterceptor(new InstanceLoadInterceptor<String, Issue>(primaryKey, (key) -> {
//            synchronized (cache) {
//                Issue issue = cache.get(key);
//                if (issue == null) {
//                    issue = load(key);
//                    cache.put(key, issue);
//                }
//                return issue;
//            }
//        }), Interceptors.getInvokingInterceptor());
//        return unchecked(() -> proxyFactory.newInstance(new InterceptorInvocationHandler(interceptor)));
//    }

    public Stream<Issue> findUpstreamReferences(Issue issue) {
        Set<Issue> upstreamIssues = new HashSet<>();
        if (aphrodite != null) {
            for (URL url : issue.getDependsOn()) {
                try {
                    upstreamIssues.add(aphrodite.getIssue(url));
                } catch (NotFoundException e) {
                    logger.log(Level.WARNING, "Unable to find issue with url: " + url, e);
                }
            }
        }
        return upstreamIssues.stream();
    }
}
