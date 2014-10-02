/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * Report server instances by associated machine.
 */
def domain = domainRuntimeService.DomainConfiguration
def domainName = domain.Name
for (server in domain.Servers) {
    println "$domainName\t$server.Name\t$server.SelfTuningThreadPoolSizeMax\t${server.Machine?.Name}"
}
