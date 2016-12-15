[![Build Status](https://travis-ci.org/jboss-set/assistant.svg?branch=master)](https://travis-ci.org/jboss-set/assistant)

# THIS REPO IS DEPRECATED
## Code has been moved to https://github.com/jboss-set/prbz-overview
# assistant
A shared component based on Aphrodite where we define pull request logic and CDW workflow rule.

As per different JBoss EAP 6/7 product defined, we put common rules about GitHub pull request and issue tracker system work-flow as Evaluator implementation. Projects like pull-request-processor and overview page can load up their needed Evaluators as service to process on different purpose.

## Aphrodite configuration

As it depends on Aphrodite, it needs to complete "aphrodite.properties.json" and specify its path as a system property "aphrodite.config".
