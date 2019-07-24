package ${daoPackage};

import java.util.List;
import java.util.Map;

import ${entityPackage}.${entity};
import com.hhx.gps.model.Page;

public interface ${entity}Mapper {
	List<${entity}> find${entity}List(Page page);
	${entity} find${entity}One(long idLong);
	void insert${entity}(${entity} ${lowEntity});
	void delete${entity}(long idLong);
	void update${entity}(${entity} ${lowEntity});
	List<${entity}> findAll();
}
