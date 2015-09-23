package yukimura.sample.util.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import lombok.ToString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import yukimura.sample.util.mysql.bean.ColumnInfo;
import yukimura.sample.util.mysql.bean.IndexInfo;
import yukimura.sample.util.mysql.bean.TableInfo;

@Repository
@ToString
public class DBStructExtractor implements AutoCloseable {

	@Autowired
	DataSource dataSource;
	
	@Value("${jdbc.schema}")
	private String schema;
    /**
     * テーブル名を抽出する。
     * @return
     * @throws SQLException
     */
    private List<String> extractTableNames() throws SQLException {
    	List<String> tableNameList = new ArrayList<>();
  	    String sql = "show tables";
    	try( Statement stmt = dataSource.getConnection().createStatement();ResultSet rs = stmt.executeQuery(sql) ) {
    	    while( rs.next() ) {
                String tableName = rs.getString("Tables_in_" + schema);
                tableNameList.add(tableName);

    	    }

    	}

    	return tableNameList;

    }
    
    /**
     * 引数で渡されたテーブルのカラム情報を取得して返却する。
     * @param tableName
     * @return
     * @throws SQLException
     */
    private List<ColumnInfo> extractTableDescription(final String tableName) throws SQLException {
    	final String DESC_SQL_TEMPLATE = "desc %s";
    	List<ColumnInfo> columnInfoList = new ArrayList<>();
        try( Statement ps = dataSource.getConnection().createStatement();ResultSet rs = ps.executeQuery( String.format(DESC_SQL_TEMPLATE, tableName) ) ) {
            while( rs.next() ) {
         	    columnInfoList.add( buildColumnInfoFromResultset(rs) );

           }

        }

        return columnInfoList;

    }
    
    /**
     * ColumnInfoのインスタンス生成用のヘルパー
     * @param rs
     * @return
     * @throws SQLException
     */
    private ColumnInfo buildColumnInfoFromResultset(ResultSet rs) throws SQLException {
 	    String field      = rs.getString("Field");
 	    String type       = rs.getString("Type");
 	    String nullable   = rs.getString("Null");
 	    String key        = rs.getString("Key");
 	    String defaultVal = rs.getString("Default");
 	    String extra      = rs.getString("Extra");
 	    ColumnInfo columnInfo = new ColumnInfo(field,type,nullable,key,defaultVal,extra);
 	    return columnInfo;
    }
    

    /**
     * 指定されたテーブル群のカラム情報を取得する。
     * @param tableNameList
     * @return
     * @throws SQLException
     */
    public List<TableInfo> extractTablesDescription() throws SQLException {
    	List<String> tableNameList = this.extractTableNames();
    	List<TableInfo> tableInfoList = new ArrayList<>();
        for( String tableName : tableNameList ) {
        	List<ColumnInfo> columnInfoList = extractTableDescription(tableName);
        	List<IndexInfo> indexInfoList = extractTableIndexinfo(tableName);
        	TableInfo tableInfo = new TableInfo(tableName,columnInfoList,indexInfoList);
        	tableInfoList.add(tableInfo);

        }
        return tableInfoList;

    }

    /**
     * 
     * @param tableName
     * @return
     * @throws SQLException
     */
    private List<IndexInfo> extractTableIndexinfo(String tableName) throws SQLException {
    	final String DESC_SQL_TEMPLATE = "show index from %s";
    	List<IndexInfo> indexInfoList = new ArrayList<>();
        try( Statement ps = dataSource.getConnection().createStatement();ResultSet rs = ps.executeQuery( String.format(DESC_SQL_TEMPLATE, tableName) ) ) {
            while( rs.next() ) {
         	    indexInfoList.add( buildIndexInfoFromResultset(rs) );

           }

        }
        return indexInfoList;

    }
    

    /**
     * IndexInfoのインスタンス生成用のヘルパー
     * @param rs
     * @return
     * @throws SQLException
     */
    private static IndexInfo buildIndexInfoFromResultset(ResultSet rs) throws SQLException {
        String table        = rs.getString("Table");
        String nonUnique    = rs.getString("Non_unique");
        String keyName      = rs.getString("Key_name");
        String seqInIndex   = rs.getString("Seq_in_index");
        String columnName   = rs.getString("Column_name");
        String collation    = rs.getString("Collation");
        String cardinality  = rs.getString("Cardinality");
        String subPart      = rs.getString("Sub_part");
        String packed       = rs.getString("Packed");
        String nullAble     = rs.getString("Null");
        String indexType    = rs.getString("Index_type");
        String comment      = rs.getString("Comment");
        String indexComment = rs.getString("Index_comment");
 	    IndexInfo indexInfo 
 	        = new IndexInfo
 	        (
 	          table     ,nonUnique,keyName    ,seqInIndex,
 	          columnName,collation,cardinality,subPart,
 	          packed    ,nullAble ,indexType  ,comment,
 	         indexComment
 	        );

 	    return indexInfo;

    }

	@Override
	public void close() {
	    try {
	    	Connection conn = dataSource.getConnection();
			if( conn != null && !conn.isClosed() ) {
				conn.close();
			}
		} catch (SQLException sqle) {
            System.err.println("close failed.");
			sqle.printStackTrace();
		}
		
	}
}
