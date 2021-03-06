/////////////////////////////////////////////////////////////////////////
// UtilsShared - Project Functions and Globals
/////////////////////////////////////////////////////////////////////////

package com.k2view.cdbms.usershared;

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

import static com.k2view.cdbms.usershared.UserCodeDescribe.FunctionType.*;

public class UtilsShared extends UserUtils {

	


    public UtilsShared(LUType luType, ExecutionContext context) throws ClassNotFoundException, SQLException {
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


	
	


	@desc("Perform a regular expression search and replace")
	@category("String")
	@out(name = "output", type = "String", desc = "")
	public String k2_regexp_replace(String pattern, String replacement, String subject) throws Exception {
		if(pattern == null || subject == null || replacement == null)
			return null;
		return subject.replaceAll(pattern, replacement);
	}


	@category("Utilities")
	@out(name = "o_val", type = "String", desc = "")
	public String k2_IF(String i_var, String i_var_check_val, String i_var_true_val, String i_var_false_val) throws Exception {
		if(i_var == null || i_var_check_val == null)
			return null;
		
		String o_val;
		if (i_var.equals(i_var_check_val)){
			o_val=i_var_true_val;
		}else{
			o_val=i_var_false_val;
		}
		return o_val;
	}


	@category("Date")
	@out(name = "year", type = "Integer", desc = "")
	@out(name = "month", type = "Integer", desc = "")
	@out(name = "day", type = "Integer", desc = "")
	@out(name = "hour", type = "Integer", desc = "")
	@out(name = "minute", type = "Integer", desc = "")
	@out(name = "second", type = "Integer", desc = "")
	@out(name = "fraction", type = "Integer", desc = "")
	public Object k2_breakDate(String dateStr, String format) throws Exception {
		if(dateStr == null || format == null)
			return null;
		
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat(format);
		java.util.Date date = dateFormat.parse(dateStr);
		
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTime(date);  
					
		Object[] arr = new Object[] {
			calendar.get(java.util.Calendar.YEAR),
			calendar.get(java.util.Calendar.MONTH) + 1,
			calendar.get(java.util.Calendar.DAY_OF_MONTH),
			calendar.get(java.util.Calendar.HOUR),
			calendar.get(java.util.Calendar.MINUTE),
			calendar.get(java.util.Calendar.SECOND),
			calendar.get(java.util.Calendar.MILLISECOND),
		};
					
		return arr;
	}


	@desc("Round fractions up - Returns the next highest integer value by rounding up value if necessary.")
	@category("Math")
	@out(name = "ceil", type = "Double", desc = "")
	public Double k2_ceil(Double value) throws Exception {
		if(value == null)
			return null;
		return java.lang.Math.ceil(value);
	}


	@category("Date")
	@out(name = "current_date", type = "String", desc = "")
	public String k2_currentDate() throws Exception {
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}


	@desc("Find position of first occurrence of a string")
	@category("String")
	@out(name = "pos", type = "Integer", desc = "")
	public Integer k2_strpos(String str, String substring) throws Exception {
		if(str == null || substring == null)
			return -1;
		return str.indexOf(substring);
	}


	@desc("minus one parameter from another to get a result")
	@category("Math")
	@out(name = "o_num", type = "Double", desc = "")
	public Double k2_minus(Double i_num1, Double i_num2) throws Exception {
		if(i_num1 == null || i_num2 == null)
			return null;
		return i_num1 - i_num2;
	}


	@desc("Strip whitespace (or other characters) from the beginning and end of a string")
	@category("String")
	@out(name = "o_str", type = "String", desc = "")
	public String k2_trim(String i_str, String i_charList) throws Exception {
		if(i_str == null)
			return null;
		if(i_charList == null || i_charList.isEmpty())
			i_charList = " ";
		return org.apache.commons.lang3.StringUtils.strip(i_str, i_charList);
	}


	@desc("Absolute value")
	@category("Math")
	@out(name = "o_num", type = "Object", desc = "")
	public Object k2_abs(Double i_num) throws Exception {
		if(i_num == null)
			return null;
		return java.lang.Math.abs(i_num);
	}


	@category("Date")
	@out(name = "current_timestamp", type = "String", desc = "")
	public String k2_currentTimeStamp() throws Exception {
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss:S");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}


	@desc("Find position of last occurrence of a char in a string")
	@category("String")
	@out(name = "pos", type = "Integer", desc = "")
	public Integer k2_strrpos(String str, String substring) throws Exception {
		if(str == null || substring == null)
			return -1;
		return str.lastIndexOf(substring);
	}


	@desc("Make a string uppercase")
	@category("String")
	@out(name = "o_str1", type = "String", desc = "")
	public String k2_strtoupper(String i_str1) throws Exception {
		if(i_str1 == null)
			return null;
		return i_str1.toUpperCase();
	}


	@desc("k2_Multiply two numbers to get a result")
	@category("Math")
	@out(name = "o_result", type = "Double", desc = "")
	public Double k2_multiply(Double i_num_1, Double i_num_2) throws Exception {
		if (i_num_1 == null || i_num_2 == null){
			return null;
		}else{
			return i_num_1 * i_num_2;
		}
	}


	@desc("Make a string lowercase")
	@category("String")
	@out(name = "o_str1", type = "String", desc = "")
	public String k2_strtolower(String i_str1) throws Exception {
		if(i_str1 == null)
			return null;
		return i_str1.toLowerCase();
	}


	@desc("Rounds a float - Returns the rounded value of val to specified precision (number of digits after the decimal point). precision can also be negative or zero (default).")
	@category("Math")
	@out(name = "round", type = "Double", desc = "")
	public Double k2_round(Double value, Integer precision) throws Exception {
		if (value==null || precision==null)
		      return null;
		
		java.math.BigDecimal bd = new java.math.BigDecimal(value);
		bd = bd.setScale(precision, java.math.RoundingMode.HALF_UP);
		return bd.doubleValue();
	}


	@desc("sum up to five parameters to get a result")
	@category("Math")
	@out(name = "o_num", type = "Double", desc = "")
	public Double k2_plus(Double i_num1, Double i_num2, Double i_num3, Double i_num4, Double i_num5) throws Exception {
		if (i_num1 == null) i_num1 = 0d;
		if (i_num2 == null) i_num2 = 0d;
		if (i_num3 == null) i_num3 = 0d;
		if (i_num4 == null) i_num4 = 0d;
		if (i_num5 == null) i_num5 = 0d;
		
		return i_num1 + i_num2 + i_num3 + i_num4 + i_num5;
	}


	@desc("Strip whitespace (or other characters) from the end of a string")
	@category("String")
	@out(name = "o_str", type = "String", desc = "")
	public String k2_rtrim(String i_str, String i_charList) throws Exception {
		if(i_str == null)
			return null;
		if(i_charList == null || i_charList.isEmpty())
			i_charList = " ";
		return org.apache.commons.lang3.StringUtils.stripEnd(i_str, i_charList);
	}


	@desc("Round fractions down - returns the next lowest integer value by rounding down value if necessary.")
	@category("Math")
	@out(name = "floor", type = "Double", desc = "")
	public Double k2_floor(Double value) throws Exception {
		if(value == null)
			return null;
		return java.lang.Math.floor(value);
	}


	@desc("This function will pad the i_pad_string to i_str according to i_pad_length")
	@category("String")
	@out(name = "o_new_var", type = "String", desc = "")
	public String k2_pad(@desc("The input string") String i_str, @desc("The i_pad_string may be truncated if the required number of padding characters can't be evenly divided by the pad_string's length") String i_pad_string, @desc("If the value of i_pad_length is negative, less than, or equal to the length of the input string, no padding takes place.") Integer i_pad_length, @desc("Optional argument i_pad_type can be STR_PAD_RIGHT, STR_PAD_LEFT, or STR_PAD_BOTH. If pad_type is not specified it is assumed to be STR_PAD_RIGHT") Boolean i_padToRight) throws Exception {
		if(i_str == null || i_pad_length == null)
			return null;
		
		if(i_padToRight == null)
			i_padToRight = true;
		
		String o_new_var;
		if(i_padToRight){
			o_new_var = org.apache.commons.lang3.StringUtils.rightPad(i_str, i_pad_length, i_pad_string);
		}else{
			o_new_var = org.apache.commons.lang3.StringUtils.leftPad(i_str, i_pad_length, i_pad_string);
		}
		return o_new_var;
	}


	@desc("Decodes a url-parameters String into a map.")
	@category("Parser")
	@out(name = "map", type = "Map<String,Object>", desc = "")
	public Map<String,Object> k2_UrlDecoder(@desc("the map that contains the input URL") Map<String,Object> map, @desc("the key of the entry in the map that contains the URL") String key) throws Exception {
		if (map == null) 
			return new HashMap<>();
		
		if (key == null || key.equals("null") || key.trim().isEmpty())
			throw new IllegalArgumentException("Key should not be null or empty");
		
		try {
			String val = (String) map.remove(key);
			final Map<String, String> map2 = com.google.common.base.Splitter.on('&').trimResults().withKeyValueSeparator("=").split(val);
			try {
				for (Map.Entry<String, String> entry : map2.entrySet()) {
					String nKey = java.net.URLDecoder.decode(entry.getKey(), "UTF-8");
					String nVal = java.net.URLDecoder.decode(entry.getValue(), "UTF-8");
					map.put(nKey, nVal);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return new HashMap<>();
			}
		}
		catch(Exception e) {
			return map;
		}		
		return map;
	}


	@desc("when the input is null return the value entered, else return the input itself")
	@category("String")
	@out(name = "output", type = "String", desc = "")
	public String k2_ifNull(String input, String value) throws Exception {
		if (input==null){
			return value;
		}
		else{
			return input;
		}
	}


	@desc("Strip whitespace (or other characters) from the beginning of a string")
	@category("String")
	@out(name = "o_str", type = "String", desc = "")
	public String k2_ltrim(@desc("The input string") String i_str, String i_charList) throws Exception {
		if(i_str == null)
			return null;
		if(i_charList == null || i_charList.isEmpty())
			i_charList = " ";
		return org.apache.commons.lang3.StringUtils.stripStart(i_str, i_charList);
	}


	@desc("This function will create a physical file based on an input string.")
	@category("FileSystem")
	public void k2_Createfile(@desc("The string to create the file based on") String i_fileContent, @desc("The name of the file to be created") String i_fileName, @desc("The location where to place the file in") String i_location, @desc("If set to 1, the content of the i_fileContent will be appended to an existing file") Integer i_append) throws Exception {
		if(i_fileContent==null || i_fileName==null || i_location==null)
			return;
		
		java.nio.file.Path path = java.nio.file.Paths.get(i_location, i_fileName);
		java.nio.file.StandardOpenOption options = 
			i_append == 1 ?
			java.nio.file.StandardOpenOption.APPEND :
			java.nio.file.StandardOpenOption.CREATE;
		java.nio.file.Files.write(
			path,
			i_fileContent.getBytes(java.nio.charset.StandardCharsets.UTF_8),
			options);
	}


	@desc("get the current instance ID")
	@category("Utilities")
	@out(name = "instanceID", type = "String", desc = "")
	public String k2_getInstanceID() throws Exception {
		return getInstanceID();
	}


	@desc("get the list of all files matching file_regExp in a specific path")
	@category("FileSystem")
	@out(name = "files", type = "Object[]", desc = "")
	public Object[] k2_find_files(@desc("Full path") String path, @desc("regular expression filter") String file_regExp) throws Exception {
		java.nio.file.Path dir = java.nio.file.Paths.get(path);
		ArrayList<String> al = new ArrayList<String>();
		
		// the Files class offers methods for validation
		if (!java.nio.file.Files.exists(dir)
				|| !java.nio.file.Files.isDirectory(dir)) {
			throw new java.io.IOException("Directory does not exist: "
					+ dir);
		}
		
		java.nio.file.PathMatcher pathMatcher;
		pathMatcher = java.nio.file.FileSystems.getDefault().getPathMatcher("regex:" + file_regExp);
		
		try (java.nio.file.DirectoryStream<java.nio.file.Path> ds = java.nio.file.Files
				.newDirectoryStream(dir)) {
			java.nio.file.Path pFile;
			for (java.nio.file.Path p : ds) {
				pFile = p.getFileName();
				if (pathMatcher != null && pathMatcher.matches(pFile)) {
					al.add(pFile.toUri().getRawPath());
				}
			}
		}
		
		return al.toArray();
	}


	@category("Parser")
	@type(RootFunction)
	@out(name = "result", type = "Map<String,Object>", desc = "")
	public void k2_StreamParserDelimited(@desc("a character to be used as a separator between records") String recordDelimiter, @desc("a character to be used as a separator between fields") String fieldDelimiter, @desc("a character to be used to enclose fields") String enclosingChar) throws Exception {
		
		if(enclosingChar == null || enclosingChar.isEmpty()){
			throw new IllegalArgumentException("Please Populate Mandatory Parameter: Enclosing Char");
		}
		
		if(fieldDelimiter == null || fieldDelimiter.isEmpty()){
			throw new IllegalArgumentException("Please Populate Mandatory Parameter: Field Delimiter");
		}
		
		if(recordDelimiter == null || recordDelimiter.isEmpty()){
			throw new IllegalArgumentException("Please Populate Mandatory Parameter: Record Delimiter");
		}
		
		InputStream stream = getStream();
		
		if (stream != null) {
			Object[] row = null;
			InputStreamReader reader = new InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8.name());
			do {
				if( reader != null ) {
					row = getStreamMap(reader, enclosingChar.charAt(0), fieldDelimiter.charAt(0), recordDelimiter);
					yield(row);
				}
			} while (row != null && row[0] != null);
		}
	}


	@category("Utilities")
	@out(name = "ind", type = "Boolean", desc = "")
	public Boolean k2_amIMinNode() throws Exception {
		if(inDebugMode()){
			return true;
		}
		
		//com.k2view.cdbms.cluster.ClusterUtils.getLiveNodes().iterator().next();
		List<java.net.InetAddress> nodes = new ArrayList<>(com.k2view.cdbms.cluster.ClusterUtils.getLiveMembers());
		Collections.sort(nodes, Comparator.comparing((java.net.InetAddress arr) -> arr.toString()));
		String aNodeId =  org.apache.cassandra.gms.Gossiper.instance.getHostId(nodes.get(0)).toString();
		String nodeID = com.k2view.cdbms.cluster.ClusterUtils.getNodeID().toString();
		
		if (nodeID.equals(aNodeId))
			return true;
		else
			return false;
	}


	@desc("Modulo of a number")
	@category("Math")
	@out(name = "o_mod_num", type = "Integer", desc = "")
	public Integer k2_mod(Integer i_number, Integer i_mod) throws Exception {
		if(i_number == null || i_mod == null)
			return null;
		return i_number % i_mod;
	}


	@category("Date")
	@out(name = "current_datetime", type = "String", desc = "")
	public String k2_currentDateTime() throws Exception {
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}


	@desc("Perform a regular expression match")
	@category("String")
	@out(name = "ret", type = "Boolean", desc = "")
	public Boolean k2_regexp_match(String pattern, String subject) throws Exception {
		if(pattern == null || subject == null)
			return false;
		return subject.matches(pattern);
	}


	@desc("A template function to be used as a root function for parser map.<BR>The function scans a folder for delimited files based on a file name pattern, parses the files and generates a stream of records.")
	@category("Parser")
	@type(RootFunction)
	@out(name = "result", type = "Map<String,Object>", desc = "")
	public void k2_FolderStreamReader(@desc("the folder to scan for files") String folderPath, @desc("the folder to scan for files in debug mode") String folderPathDebug, @desc("a Java regualr expression of file names to be parsed") String regexFilter, @desc("a character to be used as a separator between records") String recordDelimiter, @desc("a character to be used as a separator between fields") String fieldDelimiter, @desc("a character to be used to enclose fields") String enclosingChar) throws Exception {
		boolean inDebug = inDebugMode();
		if (inDebug){
			folderPath = folderPathDebug;
		}
		
		if(folderPath == null || folderPath.isEmpty()){
			throw new IllegalArgumentException("Please Populate Mandatory Parameter: Folder Path");
		}
		
		if(enclosingChar == null || enclosingChar.isEmpty()){
			throw new IllegalArgumentException("Please Populate Mandatory Parameter: Enclosing Char");
		}
		
		if(fieldDelimiter == null || fieldDelimiter.isEmpty()){
			throw new IllegalArgumentException("Please Populate Mandatory Parameter: Field Delimiter");
		}
		
		if(regexFilter == null || regexFilter.isEmpty()){
			throw new IllegalArgumentException("Please Populate Mandatory Parameter: Regex Filter");
		}
		
		if(recordDelimiter == null || recordDelimiter.isEmpty()){
			throw new IllegalArgumentException("Please Populate Mandatory Parameter: Record Delimiter");
		}
		
		FolderReader reader = new FolderReader(folderPath, regexFilter);
		InputStreamReader stream = null;
		
		while ((stream = reader.getNextStream()) != null) {	
			Object[] row = null;
			do {
				row = getStreamMap(stream, enclosingChar.charAt(0), fieldDelimiter.charAt(0), recordDelimiter);
			    yield(row);
			} while (row !=null && row[0] != null);
			
			// Drop stream only in running mode
			if(!inDebug) { // Running mode
				reader.dropStream(stream);
			}
		}
	}


	@desc("Concatenate upto 5 strings with defined delimiter")
	@category("String")
	@out(name = "o_str1", type = "String", desc = "")
	public String k2_concat5(String i_str1, String i_str2, String i_str3, String i_str4, String i_str5, String i_delimiter) throws Exception {
		if(i_delimiter == null)
			i_delimiter = "";
		
		ArrayList<String> list = new ArrayList<String>();
		if(i_str1 != null)
			list.add(i_str1);
		if(i_str2 != null)
			list.add(i_str2);
		if(i_str3 != null)
			list.add(i_str3);
		if(i_str4 != null)
			list.add(i_str4);
		if(i_str5 != null)
			list.add(i_str5);
		
		return org.apache.commons.lang3.StringUtils.join(list, i_delimiter);
		
		//
		//StringBuilder sb = new StringBuilder();
		//
		//if(i_str1 != null)
		//	sb.append(i_str1);
		//if(i_str2 != null){
		//	if(sb.length()>0) sb.append(i_delimiter);
		//	sb.append(i_str2);
		//}
		//if(i_str3 != null){
		//	if(sb.length()>0) sb.append(i_delimiter);
		//	sb.append(i_str3);
		//}
		//if(i_str4 != null){
		//	if(sb.length()>0) sb.append(i_delimiter);
		//	sb.append(i_str4);
		//}
		//if(i_str5 != null){
		//	if(sb.length()>0) sb.append(i_delimiter);
		//	sb.append(i_str5);
		//}
		//
		//return sb.toString();
	}


	@category("Utilities")
	@out(name = "ind", type = "Boolean", desc = "")
	public Boolean k2_amIMinNode_multi_dc() throws Exception {
		if(inDebugMode()){
			return true;
		}
		
		String dcName = com.k2view.cdbms.cluster.ClusterUtils.getDCName();
		List<java.net.InetAddress> nodes = new ArrayList<>(com.k2view.cdbms.cluster.ClusterUtils.getLiveMembers(dcName));
		Collections.sort(nodes, Comparator.comparing((java.net.InetAddress arr) -> arr.toString()));
		if(nodes.size()==0)
			return false;
		
		String aNodeId =  org.apache.cassandra.gms.Gossiper.instance.getHostId(nodes.get(0)).toString();
		String nodeID = com.k2view.cdbms.cluster.ClusterUtils.getNodeID().toString();
		
		if (nodeID.equals(aNodeId))
			return true;
		else
			return false;
	}


	@category("Temp")
	@out(name = "res", type = "String", desc = "")
	public String getTemp2() throws Exception {
		// test 1
		return "another data";
	}


	@category("Temp")
	@out(name = "res", type = "String", desc = "")
	public String getTemp1() throws Exception {
		// test 1
		return "another data";
	}


}
