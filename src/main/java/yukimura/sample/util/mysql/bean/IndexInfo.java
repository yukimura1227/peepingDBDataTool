package yukimura.sample.util.mysql.bean;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class IndexInfo {
    private String table       ;
    private String nonUnique   ;
    private String keyName     ;
    private String seqInIndex  ;
    private String columnName  ;
    private String collation   ;
    private String cardinality ;
    private String subPart     ;
    private String packed      ;
    private String nullAble    ;
    private String indexType   ;
    private String comment     ;
    private String indexComment;
}
