/* Copyright (c) 2014 Andreas Nyberg. http://middlewareman.github.io/GWLST */

editSave { domain ->
	for (jdbcsr in domain.JDBCSystemResources) {
		if (jdbcsr.Name ==~ /.*MultiDS/) {
			jdbcsr.JDBCResource.JDBCDataSourceParams.FailoverRequestIfBusy = true
			println jdbcsr
		}
	}
}
