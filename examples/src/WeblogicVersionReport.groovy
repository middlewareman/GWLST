/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * Reports the WebLogic version reported by running servers in a domain
 * grouped by version.
 * Note that version can only be queried from running servers.
 */

def ipw = new com.middlewareman.groovy.util.IndentPrintWriter()
ipw.println status()
ipw.println new Date()
ipw.println()

def drs = domainRuntimeServer.domainRuntimeService

def notRunning =
        (drs.DomainConfiguration.Servers.Name as Set) -
                (drs.ServerRuntimes.Name as Set)
if (notRunning)
    ipw.println "WARNING: The following servers are not available to be queried: $notRunning\n"

def domainMap = [:]
for (serverRuntime in drs.ServerRuntimes)
    domainMap[serverRuntime.name] = serverRuntime.WeblogicVersion

def domainReverseMap = domainMap.groupBy { name, version -> version }

def domainLevels = domainReverseMap.size()
if (domainLevels > 1)
    ipw.println "WARNING: There are $domainLevels different patchlevels in your domain.\n"

for (version in domainReverseMap.keySet()) {
    def nameSet = domainReverseMap[version].keySet().sort()
    ipw.indent("\nServers $nameSet:") { ipw.println version }
}
