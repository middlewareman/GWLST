/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * Configures domain log to sharable location.
 */
editSave { domain ->
    def domainName = domain.Name
    def log = domain.Log
    log.FileName = "../../logs/domain/domain-${domainName}.log" as String
    log.FileMinSize = 5 << 10 // 5 MB instead of default 500 KB
}
