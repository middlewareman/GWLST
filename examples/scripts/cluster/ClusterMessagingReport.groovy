/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * Print configuration data for cluster messaging for all clusters in the connected domain.
 *
 * A cluster has messaging mode of either unicast or multicast (default).
 *
 * Unicast allows naming a dedicated channel (network access point) that then
 * needs to be defined for each server instance. This model is prone to error
 * as these individual configurations must be in sync, and due to the nature
 * of broadcasting, the sender will not notice that an intended receiver is
 * misconfigured.
 *
 * Multicast has no configuration on each server instance.
 */

println status()
println new Date()

def drs = domainRuntimeServer.domainRuntimeService

for (cluster in drs.domainConfiguration.clusters) {
    println "\nCLUSTER $cluster.name"
    def map = cluster.properties
    def mode = map.clusterMessagingMode
    switch (mode) {
        case 'unicast':
            println "  UNICAST"
            def channelName = map.clusterBroadcastChannel
            println "    ClusterBroadcastChannel\t$channelName"
            if (channelName) {
                for (server in cluster.servers) {
                    def channel = server.lookupNetworkAccessPoint(channelName)
                    if (!channel) {
                        println "    Channel definition MISSING on Server $server.name"
                    } else {
                        println "\n    Channel definition on Server $server.name"
                        println "      ClusterAddress\t$channel.clusterAddress"
                        println "      ListenAddress:ListenPort\t$channel.listenAddress:$channel.listenPort"
                        println "      PublicAddress:PublicPort\t$channel.publicAddress:$channel.publicPort"
                    }
                }
            }
            println()
            map.each { key, value ->
                if (key ==~ /unicast.*/)
                    println "    $key \t$value"
            }
            break
        default:
            println "  MULTICAST (default)"
        case 'multicast':
            println "  MULTICAST"
            map.each { key, value ->
                if (key ==~ /unicast.*/)
                    println "    $key \t$value"
            }
            break
    }
}
