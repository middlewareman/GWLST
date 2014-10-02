/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * Configure thread pool for servers.
 */
editSave { domain ->
    domain.Servers.each { server ->
        server.SelfTuningThreadPoolSizeMax = 50
    }
}
