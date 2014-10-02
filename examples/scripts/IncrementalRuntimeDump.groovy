/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

import java.text.SimpleDateFormat
import java.util.regex.Matcher

if (!args) {
    System.err.println "Parameters: mbeanType [objectNameKey...]"
    System.err.println "Example: JTARuntime Location"
    System.exit 1
}

def tab = '\t'

String mbeanType = args[0]
List objectNameKeys = (args.size() > 1) ? args[1..-1] : []

if (!objectNameKeys) println "WARNING: no objectName keys may produce confusing output"

Matcher matcher = domainRuntimeServer.home.address =~ /.*:\/\/(.+):(\d+).*/
def host = matcher[0][1]
def port = matcher[0][2]

File file = new File("${mbeanType}-${host}-${port}.csv")
println "Filename: $file"

List propsKeys

def out
if (file.exists()) {
    /* If file exists, read first line to determine keys to include. */
    file.withReader {
        def line = it.readLine()
        List keys = line.tokenize(tab)
        assert keys[0] == 'Timestamp'
        assert keys[1..objectNameKeys.size()] == objectNameKeys
        propsKeys = keys[objectNameKeys.size() + 1..-1]
        out = file.newWriter(true).newPrintWriter()
    }
} else {
    out = file.newPrintWriter()
}

def timestamp = new SimpleDateFormat('yyyy-MM-dd hh:mm:ss').format(new Date())

Set dsrs = domainRuntimeServer.home.getMBeans("com.bea:Type=$mbeanType,*")

Map on2keyVal = [:]
for (dsr in dsrs) {
    Map props = propsKeys ?
            dsr.@home.getAttributes(dsr.@objectName, propsKeys as String[]) :
            dsr.properties.findAll {
                key, value ->
                    value instanceof Number
            }
    on2keyVal[dsr.@objectName] = props
}
if (!propsKeys) {
    Set keys = new HashSet()
    for (map in on2keyVal.values())
        keys.addAll map.keySet()
    propsKeys = keys.sort()

    out.print "Timestamp"
    for (key in objectNameKeys) out.print "$tab$key"
    for (key in propsKeys) out.print "$tab$key"
    out.println()
}

for (on in on2keyVal.keySet()) {
    out.print timestamp
    for (key in objectNameKeys) {
        def value = on.getKeyProperty(key)
        out.print "$tab$value"
    }
    Map props = on2keyVal[on]
    for (key in propsKeys) out.print "$tab${props[key]}"
    out.println()
}

out.close()
