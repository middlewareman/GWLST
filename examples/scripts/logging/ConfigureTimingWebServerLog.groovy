/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * Configures time-taken webserver web log.
 */
editSave { domain ->
    String domainName = domain.Name
    for (server in domain.Servers) {
        String serverName = server.Name
        def wslog = server.WebServer.WebServerLog
        wslog.FileName = "/apps/bea/LOGS/${domainName}/${serverName}/access-${domainName}-${serverName}.log" as String
        wslog.FileMinSize = 5 << 10 // 5 MB instead of default 500 KB
        wslog.LogFileFormat = 'extended'
        wslog.ELFFields = 'date time cs-method cs-uri-stem time-taken cs-status bytes cs-uri-query'
        wslog.RotateLogOnStartup = true
    }
}
