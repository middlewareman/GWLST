/*
 * Copyright (c) 2011-2014 Andreas Nyberg.
 * Licensed under the Apache License, Version 2.0 [http://www.apache.org/licenses/LICENSE-2.0].
 * http://middlewareman.github.io/GWLST
 */

editSave { domain ->
    for (jdbcsr in domain.JDBCSystemResources) {
        if (jdbcsr.Name ==~ /.*MultiDS/) {
            jdbcsr.JDBCResource.JDBCDataSourceParams.FailoverRequestIfBusy = true
            println jdbcsr
        }
    }
}
