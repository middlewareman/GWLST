/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

println "Report all numerical and attributes of all JDBCDataSourceRuntimes by Name and Location"
println domainRuntimeServer.home.address
println new Date()

Set dsrs = domainRuntimeServer.home.getMBeans('com.bea:Type=JDBCDataSourceRuntime,*')

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

print "Name\tLocation"
for (key in keyList) print "\t$key"
println()

for (on in on2keyVal.keySet()) {
    print on.getKeyProperty('Name')
    print "\t"
    print on.getKeyProperty('Location')
    Map props = on2keyVal[on]
    for (key in keyList) print "\t${props[key]}"
    println()
}
