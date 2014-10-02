/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * GWLST version of Oracle's PrintServerState.
 */
for (sr in domainRuntimeServer.domainRuntimeService.ServerRuntimes) {
    println "Server name:$sr.Name state:$sr.State"
}
