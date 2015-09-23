package yukimura.sample.rest.jersey2.impl;

import java.util.List;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import yukimura.sample.util.mysql.bean.TableInfo;
import yukimura.sample.rest.jersey2.spec.PeepingDBDataService;
import yukimura.sample.util.mysql.DBStructExtractor;

public class PeepingDBDataServiceImpl implements PeepingDBDataService {
    @Autowired
    private DBStructExtractor dbStructExtractor;
    @Override
    public List<TableInfo> extractTableInfos() {
    	try {
        	List<TableInfo> tableInfoList = dbStructExtractor.extractTablesDescription();
        	for( TableInfo tableInfo : tableInfoList ) {
        		System.out.println(tableInfo);
        		System.out.println(dbStructExtractor);

        	}
        	return tableInfoList;

        } catch (SQLException sqle) {
            System.err.println("SQL failed.");
            sqle.printStackTrace();

        } 
        
        return null;

    }


}
