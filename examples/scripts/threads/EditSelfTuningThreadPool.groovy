/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

/**
 * Configure thread pool for servers.
 */
editSave { domain ->
    domain.Servers.each { server ->
        server.SelfTuningThreadPoolSizeMax = 50
    }
}
