/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

/**
 * GWLST version of Oracle's PrintServerState.
 */
for (sr in domainRuntimeServer.domainRuntimeService.ServerRuntimes) {
    println "Server name:$sr.Name state:$sr.State"
}
