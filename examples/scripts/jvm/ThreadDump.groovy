/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * Pull a thread dump for WebLogic 10.0.
 * If admin server, you may name servers to collect, or all will be collected.
 * Otherwise, only connected server will be returned.
 */

if (domainRuntimeServer) {
    println "Connected to admin server"
    def drs = domainRuntimeServer.domainRuntimeService
    def servers = args ? args.collect { drs.lookupServerRuntime it } : drs.ServerRuntimes
    for (server in servers) {
        if (server) {
            println "\nServer $server.@objectName on $server.@home\n"
            println server.JVMRuntime.ThreadStackDump
        } else {
            println "\nServer $serverName not found"
        }
    }
} else {
    println "Connected to managed server"
    def server = runtimeServer.runtimeService.ServerRuntime
    println server.JVMRuntime.ThreadStackDump
}
