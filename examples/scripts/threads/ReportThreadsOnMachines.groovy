/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

/**
 * Report server instances by associated machine.
 */
def domain = domainRuntimeService.DomainConfiguration
def domainName = domain.Name
for (server in domain.Servers) {
    println "$domainName\t$server.Name\t$server.SelfTuningThreadPoolSizeMax\t${server.Machine?.Name}"
}
