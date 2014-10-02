/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

/**
 * Report Heap usage before and after GC for WebLogic 10.0.
 */

def header() {
    "FreeCur\tFree%\tSizeCur\tSizeMax"
}

def describe(jvm) {
    "${jvm.HeapFreeCurrent >> 20} MB\t${jvm.HeapFreePercent}%\t${jvm.HeapSizeCurrent >> 20} MB\t${jvm.HeapSizeMax >> 20} MB"
}

println status()
println new Date()
println()

def drs = domainRuntimeServer.domainRuntimeService

print "Server     \t"
println header()

for (jvm in drs.ServerRuntimes.JVMRuntime) {
    println jvm.Name
    print "  beforeGC \t"
    println describe(jvm)
    jvm.runGC()
    print "  afterGC  \t"
    println describe(jvm)
}
