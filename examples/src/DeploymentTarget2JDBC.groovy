/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

/**
 * Loops through all deployments and lists the JDBC resources targeted to the
 * same targets as each deployment.
 */

def ipw = new com.middlewareman.groovy.util.IndentPrintWriter()
ipw.println status()
ipw.println new Date()
ipw.println()

drs = domainRuntimeServer.domainRuntimeService
domain = drs.DomainConfiguration
for (appDeployment in domain.AppDeployments) {
    ipw.indent("AppDeployment $appDeployment.Name") {
        for (appTarget in appDeployment.Targets) {
            ipw.indent("Target $appTarget.Name") {
                def jdbcOnTarget = domain.JDBCSystemResources.findAll { appTarget in it.Targets }
                for (jdbc in jdbcOnTarget) {
                    ipw.println "JDBCSystemResource $jdbc.Name"
                }
            }
        }
    }
}
