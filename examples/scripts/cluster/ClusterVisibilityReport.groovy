/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

/**
 * Print runtime data for running cluster members.
 *
 * Cluster members maintain their dynamic view of the cluster by receiving
 * heartbeat messages from other cluster members.
 * Because these messages are broadcast (whether with multicast or unicast),
 * the failure in receiving these messages is not detected by the sender.
 * This monitoring tool compares these individual runtime views to that
 * which is expected based on other runtime information.
 */

def show(collection) {
    collection ? collection.sort().join(' ') : ''
}

println status()
println new Date()

def drs = domainRuntimeServer.domainRuntimeService


Set allServerConfigNames = drs.domainConfiguration.servers.name as Set
def allServerRuntimes = drs.serverRuntimes
Set allServerRuntimeNames = allServerRuntimes.name as Set

for (clusterConfig in drs.domainConfiguration.clusters) {
    println "\nCLUSTER $clusterConfig.name"
    Map clusterMemberConfigMap = [:]
    for (serverConfig in clusterConfig.servers)
        clusterMemberConfigMap[serverConfig.name] = serverConfig
    Set clusterMemberConfigNames = clusterMemberConfigMap.keySet()
    Set clusterMemberRuntimeNames = allServerRuntimeNames.intersect(clusterMemberConfigNames)
    print "  Running members:    \t"
    println show(clusterMemberRuntimeNames)
    print "  Not running members:\t"
    println show(clusterMemberConfigNames - clusterMemberRuntimeNames)

    def dodgyServerNames = new HashSet()
    println "  Per configured cluster member"
    for (name in clusterMemberConfigNames?.sort()) {
        def clusterMemberRuntime = drs.lookupServerRuntime(name)
        if (clusterMemberRuntime) {
            println "    SERVER $name\t$clusterMemberRuntime.state"
            def clusterRuntime = clusterMemberRuntime.clusterRuntime
            Set sees = clusterRuntime.serverNames as Set
            Set shouldAlsoSee = clusterMemberRuntimeNames - sees
            dodgyServerNames += shouldAlsoSee
            print "      Sees   \t"
            println show(sees)
            print "      Missing\t"
            println show(shouldAlsoSee)
        } else {
            println "    $name: not running"
        }
    }
    if (dodgyServerNames)
        println "\n  DODGY SERVERS: ${show(dodgyServerNames)}\n"
}
