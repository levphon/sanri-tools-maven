package ${daoPackage};

import java.util.List;

import com.hhx.gps.model.Page;
import com.hhx.gps.web.exception.BussinessException;
import com.hhx.gps.web.exception.SystemErrorException;
import ${entityPackage}.${entity};

/**
 * 
 * 作者:sanri <br/>
 * 时间:${datetime}<br/>
 * 功能: ${entity} 数据 <br/>
 */
public interface ${entity}Dao {
	
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:${datetime}<br/>
	 * 功能:查询${entity}列表 <br/>
	 * 入参: page <br/>
	 */
	List<${entity}> find${entity}List(Page page) throws BussinessException,SystemErrorException;

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:${datetime}<br/>
	 * 功能:根据 id 查询对象 <br/>
	 */
	${entity} find${entity}One(long idLong) throws BussinessException,SystemErrorException;

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:${datetime}<br/>
	 * 功能:增加 ${entity} 数据 <br/>
	 */
	void insert${entity}(${entity} ${lowEntity}) throws BussinessException,SystemErrorException;

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:${datetime}<br/>
	 * 功能:删除 ${entity} 数据 <br/>
	 */
	void delete${entity}(long idLong)  throws BussinessException,SystemErrorException;

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:${datetime}<br/>
	 * 功能:修改 ${entity} 数据 <br/>
	 */
	void update${entity}(${entity} ${lowEntity}) throws BussinessException,SystemErrorException;

}