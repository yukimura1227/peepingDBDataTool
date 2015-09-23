package yukimura.sample.util.mysql.bean;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnInfo {
	private String field     ; 
    private String type      ; 
    private String nullable  ; 
    private String key       ; 
    private String defaultVal; 
    private String extra     ; 
}
