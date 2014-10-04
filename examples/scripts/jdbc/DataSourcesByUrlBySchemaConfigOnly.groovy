/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

def ipw = new com.middlewareman.groovy.util.IndentPrintWriter()

ipw.println version
ipw.println new Date()
ipw.println domainRuntimeServer.home
ipw.println()

// Filter out defunct JDBCSystemResource without JDBCResource
def jdbcsList = domainRuntimeService.DomainConfiguration.JDBCSystemResources.findAll { it.JDBCResource }

// TODO: Split simple and multi datasource here rather than getting null Url and User

def jdbcsByUrl = jdbcsList.groupBy {
    it.JDBCResource?.JDBCDriverParams?.Url
}

jdbcsByUrl.each { url, jdbcsPerUrl ->
    ipw.indent("\nURL $url") {
        assert jdbcsPerUrl != null
        def jdbcsByUser = jdbcsPerUrl.groupBy {
            it.JDBCResource?.JDBCDriverParams?.Properties?.Properties?.find { it?.Name == 'user' }?.Value
        }
        jdbcsByUser.each { user, jdbcsPerUser ->
            //prefix, jndiName, user, pass, maxCapacity
            ipw.indent("\nUSER $user") {
                for (jdbc in jdbcsPerUser) {
                    def jdbcName = jdbc.Name
                    def resource = jdbc.JDBCResource
                    def password = resource.JDBCDriverParams.Password
                    def pool = resource.JDBCConnectionPoolParams
                    def datasource = resource.JDBCDataSourceParams
                    def initial = pool.InitialCapacity
                    def max = pool.MaxCapacity
                    def jndiNames = datasource.JNDINames
                    def dataSourceList = datasource.DataSourceList
                    ipw.indent("JDBC $jdbcName, $jndiNames, $user, $password, ($initial,) $max \t$dataSourceList") {
                        for (target in jdbc.Targets) {
                            ipw.println target
                        }
                    }
                }
            }
        }
    }
}

null
