/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

def ipw = new com.middlewareman.groovy.util.IndentPrintWriter()

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
            ipw.indent("\nUSER $user") {
                for (jdbc in jdbcsPerUser) {
                    def jdbcName = jdbc.Name
                    def resource = jdbc.JDBCResource
                    def pool = resource.JDBCConnectionPoolParams
                    def datasource = resource.JDBCDataSourceParams
                    def initial = pool.InitialCapacity
                    def max = pool.MaxCapacity
                    def jndiNames = datasource.JNDINames
                    def dataSourceList = datasource.DataSourceList
                    ipw.indent("\nJDBC $jdbcName\t$initial\t$max\t$jndiNames\t$dataSourceList") {
                        for (target in jdbc.Targets) {
                            def name = target.@objectName.getKeyProperty('Name')
                            switch (target.@objectName.getKeyProperty('Type')) {
                                case 'Server':
                                    ipw.print("\nSERVER $name\t")
                                    def serverRuntime = domainRuntimeService.lookupServerRuntime(name)
                                    if (serverRuntime) {
                                        def jdbcServiceRuntime = serverRuntime.JDBCServiceRuntime
                                        def dsRuntime = jdbcServiceRuntime.JDBCDataSourceRuntimeMBeans.find { it?.Name == jdbcName }
                                        if (dsRuntime) {
                                            ipw.print dsRuntime.ActiveConnectionsHighCount
                                            ipw.print '\t'
                                            ipw.print dsRuntime.CurrCapacityHighCount
                                            ipw.print '\t'
                                            ipw.print dsRuntime.WaitSecondsHighCount
                                            ipw.print '\t'
                                            ipw.println dsRuntime.WaitingForConnectionHighCount
                                        } else {
                                            ipw.println "No JDBCDataSourceRuntime"
                                        }
                                    } else {
                                        ipw.println "No ServerRuntime"
                                    }
                                    break
                                case 'Cluster':
                                    ipw.println "\nCLUSTER $name"
                                    break
                                default:
                                    assert false, target
                            }
                        }
                    }
                }
            }
        }
    }
}

null
