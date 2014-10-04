/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

/**
 * Report transactions older than a certain time in seconds.
 */

def ipw = new com.middlewareman.groovy.util.IndentPrintWriter()

int age = 5
if (args) age = args[0] as int

def home = domainRuntimeServer ? domainRuntimeServer.home : runtimeServer.home
def jtas = home.getMBeans('com.bea:Type=JTARuntime,*')
ipw.println "Reporting transactions older than $age seconds on ${jtas*.@objectName*.getKeyProperty('Location')}"
for (jta in jtas) {
    def older = jta.getTransactionsOlderThan(age)
    if (older) {
        ipw.indent("\nJTA ${jta.@objectName}") {
            for (tx in older) {
                ipw.indent("\nTransaction") {
                    tx.properties.each { key, value ->
                        if (value != null) ipw.println "$key \t$value"
                    }
                }
            }
        }
    }
}
