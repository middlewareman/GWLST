/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * GWLST version of Oracle's MonitorServlets.java.
 */
for (sr in domainRuntimeServer.domainRuntimeService.ServerRuntimes) {
    for (ar in sr.ApplicationRuntimes) {
        println "Application name: $ar.Name"
        for (cr in ar.ComponentRuntimes) {
            println "  Component name: $cr.Name"
            if (cr.Type == "WebAppComponentRuntime") {
                for (servlet in cr.Servlets) {
                    println "    Servlet name: $servlet.Name"
                    println "      Servlet context path: $servlet.ContextPath"
                    println "      Invocation Total Count : $servlet.InvocationTotalCount"
                }
            }
        }
    }
}
