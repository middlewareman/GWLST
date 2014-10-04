/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

def domain = domainRuntimeService.DomainConfiguration
String serverName = args.length ? args[0] : domain.AdminServerName
def server = domain.lookupServer(serverName)
assert server, "Could not look up server $serverName"
new File('startup.properties').withOutputStream {
    server.ServerStart.StartupProperties.store(it, "Exported by GWLST from $server on ${server.@home.address}")
}
new File('boot.properties').withOutputStream {
    server.ServerStart.BootProperties.store(it, "Exported by GWLST from $server on ${server.@home.address}")
}
