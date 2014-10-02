/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

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
