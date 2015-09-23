package yukimura.sample.rest.jersey2.spec;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import yukimura.sample.util.mysql.bean.TableInfo;

@Path("/sample")
public interface PeepingDBDataService {    
    // ex) http://localhost:8080/jersey2-sample/rest/sample/tableInfos
    @GET
    @Path("/tableInfos")
    @Produces("application/json")
    public List<TableInfo> extractTableInfos();

}
