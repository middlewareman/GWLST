/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

/**
 * Report transaction participants by server.
 */
import java.text.SimpleDateFormat

long sleep = 500

long lastReport = 0
long reportInterval = 10000

def serverNames = args
println "Server names: $serverNames"

Map server2xid2tx = [:]
Map server2resourceGroups = [:]

def df = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss')
println status()
def jtaRuntimes = serverNames ?
        serverNames.collect { domainRuntimeService.lookupServerRuntime(it).JTARuntime } :
        domainRuntimeService.ServerRuntimes.JTARuntime
while (true) {
    def timestamp = df.format(new Date())
    System.err.print 'Ping '
    System.err.println timestamp
    for (jta in jtaRuntimes) {
        def serverName = jta.@objectName.getKeyProperty('ServerRuntime')
        assert serverName
        def resourceGroups = server2resourceGroups.get(serverName)
        if (!resourceGroups) {
            resourceGroups = new LinkedHashSet()
            server2resourceGroups.put serverName, resourceGroups
        }
        def xid2tx = server2xid2tx.get(serverName)
        if (!xid2tx) {
            xid2tx = [:]
            server2xid2tx.put serverName, xid2tx
        }

        def txs = jta.JTATransactions.findAll { it.resourceNamesAndStatus }
        for (goneid in xid2tx.keySet() - (txs*.xid as Set)) {
            /* Transaction is gone: use report last known state and remove from map. */
            def gone = xid2tx[goneid]
            def resourceNames = gone.resourceNamesAndStatus.keySet()
            if (resourceGroups.add(resourceNames)) {
                /* New group */
                System.err.println "SERVER JTA\t$serverName"
                System.err.print '  NEW    \t'
                System.err.println resourceNames
                System.err.println '  CURRENT'
                resourceGroups.sort { it.size() }.each {
                    System.err.print '\t'
                    System.err.println it
                }
                def onlyOnes = resourceGroups.findAll { one ->
                    one.size() == 1 && !resourceGroups.any { more ->
                        more.size() > 1 && more.containsAll(one)
                    }
                }
                System.err.print '  SINGLES\t'
                System.err.println onlyOnes
                System.err.println()
            }
            xid2tx.remove goneid
        }
        for (tx in txs) {
            /* Update map with current (new and known) transactions. */
            xid2tx.put tx.xid, tx
        }
    }

    long now = System.currentTimeMillis()
    if (now - lastReport > reportInterval) {
        println timestamp
        for (server in server2resourceGroups.keySet().sort()) {
            println "SERVER $server"
            def groups = server2resourceGroups[server]
            groups.each {
                print '\tGROUP '
                println it
            }
            def singles = groups.findAll { one ->
                one.size() == 1 && !groups.any { more ->
                    more.size() > 1 && more.containsAll(one)
                }
            }
            singles.each {
                print '\tSINGLE '
                println it
            }
        }
        lastReport = now
    }
    Thread.sleep sleep
}
