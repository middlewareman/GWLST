/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

for (sr in domainRuntimeService.ServerRuntimes) {
    if (sr.State == "ADMIN") {
        println "Resuming $sr.Name"
        sr.resume()
    }
}
