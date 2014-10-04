/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

println "Report all numerical attributes of all JTARuntimes by Location"
println domainRuntimeServer.home.address
println new Date()

Set dsrs = domainRuntimeServer.home.getMBeans('com.bea:Type=JTARuntime,*')

Map on2keyVal = [:]
for (dsr in dsrs) {
    on2keyVal[dsr.@objectName] = dsr.properties.findAll { key, value ->
        value instanceof Number
    }
}

Set keys = new LinkedHashSet()
on2keyVal.each { on, keyVal ->
    keys.addAll(keyVal.keySet())
}
def keyList = keys.sort()

print "Location"
for (key in keyList) print "\t$key"
println()

for (on in on2keyVal.keySet()) {
    print on.getKeyProperty('Location')
    Map props = on2keyVal[on]
    for (key in keyList) print "\t${props[key]}"
    println()
}
