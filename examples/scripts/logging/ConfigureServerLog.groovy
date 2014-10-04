/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

/**
 * Configures server log to sharable location.
 */
editSave { domain ->
    for (server in domain.Servers) {
        def serverName = server.Name
        def log = server.Log
        log.FileName = "../../logs/servers/${serverName}/server-${serverName}.log" as String
        log.FileMinSize = 5 << 10 // 5 MB instead of default 500 KB
    }
}
