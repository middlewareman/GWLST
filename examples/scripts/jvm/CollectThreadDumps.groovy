/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

def datef = new java.text.SimpleDateFormat('yyyyMMdd')
def timef = new java.text.SimpleDateFormat('HHmmss')
def domainName = domainRuntimeService.DomainConfiguration.Name
while (true) {
    def date = new Date()
    def datestamp = datef.format(date)
    def timestamp = timef.format(date)
    println "$datestamp-$timestamp"
    def root = new File("${domainName}/${datestamp}/${timestamp}")
    root.mkdirs()
    try {
        for (sr in domainRuntimeService.ServerRuntimes) {
            try {
                def serverName = sr.Name
                println serverName
                def name = new StringBuilder()
                name << "${domainName}-${sr.Name}-${datestamp}-${timestamp}"
                def ph = domainRuntimeServer.getProxyPlatformHome(serverName)
                def thread = ph.thread
                if (thread) name << '.' << thread.threadCount
                def jvm = sr.JVMRuntime
                String text = jvm.ThreadStackDump
                if (text.contains('STUCK')) name << '.STUCK'
                new File(root, name + '.txt').write(text)
                def tda = new com.middlewareman.util.ThreadDumpAnalyzer()
                tda.parse text
                new File(root, name + '.analysed.txt').withPrintWriter { out ->
                    out.println sr
                    out.println "Uptime ${jvm.Uptime / 1000 / 60 / 60} hours"
                    out.println "\nFreeCur\tFree%\tSizeCur\tSizeMax"
                    out.println "${jvm.HeapFreeCurrent >> 20} MB\t${jvm.HeapFreePercent}%\t${jvm.HeapSizeCurrent >> 20} MB\t${jvm.HeapSizeMax >> 20} MB"
                    out.println()
                    out.println "Active\tCapac\tAvail\tUnavail\tWaiting\tDataSource"
                    for (ds in sr.JDBCServiceRuntime.JDBCDataSourceRuntimeMBeans) {
                        out.println "$ds.ActiveConnectionsCurrentCount\t$ds.CurrCapacity\t$ds.NumAvailable\t$ds.NumUnavailable\t$ds.WaitingForConnectionCurrentCount\t$ds.Name"
                    }
                    out.println()
                    tda.report out
                }
            } catch (Exception e) {
                e.printStackTrace()
            } catch (StackOverflowError e) {
                println "StackOverflowError for $sr"
            }
        }
    } catch (Exception e) {
        e.printStackTrace()
    }
    println 'Sleeping...'
    Thread.sleep 30000
}
