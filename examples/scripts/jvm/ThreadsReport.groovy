/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * Loop through server runtimes and report thread usage.
 */
def serverNameToRuntime = [:]
domainRuntimeService.ServerRuntimes.each { serverNameToRuntime[it.Name] = it }
def serverNames = serverNameToRuntime.keySet().sort()

println "NAME\tSTARTED\tCURRENT\tPEAK\tCPUs\tMACHINE"
for (serverName in serverNames) {
    def ph = domainRuntimeServer.getProxyPlatformHome(serverName)
    def thread = ph.thread
    def os = ph.operatingSystem
    def machine = serverNameToRuntime[serverName].CurrentMachine
    println "$serverName\t$thread.totalStartedThreadCount\t$thread.threadCount\t$thread.peakThreadCount\t$os.availableProcessors\t$machine"
}
