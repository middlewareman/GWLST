/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

/**
 * Reset default cluster communication.
 */
editSave { domain ->
    for (cluster in domain.Clusters) {
        cluster.unSet 'ClusterAddress'
        cluster.unSet 'ClusterBroadcastChannel'
        cluster.unSet 'ClusterMessagingMode'
    }
}
