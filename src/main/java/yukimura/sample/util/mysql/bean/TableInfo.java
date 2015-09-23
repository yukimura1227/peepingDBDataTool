package yukimura.sample.util.mysql.bean;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableInfo {
	private String tablename ;
	private List<ColumnInfo> columnInfoList;
    private	List<IndexInfo> indexInfoList;

}
