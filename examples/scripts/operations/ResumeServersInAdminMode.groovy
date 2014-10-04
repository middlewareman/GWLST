/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

for (sr in domainRuntimeService.ServerRuntimes) {
    if (sr.State == "ADMIN") {
        println "Resuming $sr.Name"
        sr.resume()
    }
}
