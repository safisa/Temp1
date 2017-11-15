/////////////////////////////////////////////////////////////////////////
// Utils - Project Functions and Globals
/////////////////////////////////////////////////////////////////////////

package com.k2view.cdbms.lu.k2_ref;

import java.util.*;
import java.sql.*;
import java.math.*;
import java.io.*;

import com.k2view.cdbms.shared.*;
import com.k2view.cdbms.sync.*;
import com.k2view.cdbms.lut.*;
import com.k2view.cdbms.shared.logging.LogEntry.*;
import com.k2view.cdbms.func.oracle.OracleToDate;
import com.k2view.cdbms.func.oracle.OracleRownum;
import com.k2view.cdbms.usershared.UserCodeDescribe.*;
import com.k2view.cdbms.usershared.*;

import static com.k2view.cdbms.usershared.UserCodeDescribe.FunctionType.*;

public class Utils extends UtilsShared {

	


    public Utils(LUType luType, ExecutionContext context) throws ClassNotFoundException, SQLException {
        super(luType, context);
        LUDBBase ludb = getLudb();
        if (ludb != null) {
            if (ludb.getConnection() != null) {
                initSqliteUDFs(ludb.getConnection());
            }
        }
    }

    ////////////////////////////////////////////
    // Register as LUDB functions (Sqlite UDFs)
    ////////////////////////////////////////////
    protected void initSqliteUDFs(Connection conn)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

		
    }


	


	
}
