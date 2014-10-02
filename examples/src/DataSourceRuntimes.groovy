/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

println "Report all numerical and non-zero attributes of all JDBCDataSourceRuntimes" 
println domainRuntimeServer.home.address
println new Date()

def ipw = new com.middlewareman.groovy.util.IndentPrintWriter(tab:'\t')

def dsrs = domainRuntimeServer.home.getMBeans('com.bea:Type=JDBCDataSourceRuntime,*')

def byds = dsrs.groupBy {
	it.@objectName.getKeyProperty('Name')
}
for (dsName in byds.keySet()) {
	ipw.indent("\nDATASOURCE $dsName") {
		def byloc = byds[dsName].groupBy {
			it.@objectName.getKeyProperty('Location')
		}
		for (locName in byloc.keySet()) {
			ipw.indent("\nLOCATION $locName") {
				for (dsr in byloc[locName]) {
					dsr.properties.each { key, value ->
						if (value instanceof Number && value > 0)
							ipw.println "$key\t$value"
					}
				}
			}
		}
	}
}

