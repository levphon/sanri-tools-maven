/**
 * 表之间关联关系的维护 
 * 太复杂了,暂时不实现 
 */
define(['util','sqlclient/meta'],function(util,metatree){
	var relationNames = [],relationMap = {};
	var relation = {};

	/**
	 * 创建关系图
	 */
	relation.create = function(){
		var connMeta = metatree.meta.connMeta[metatree.current.conn];
		if(connMeta){
			var dbMeta = null;
			for(var i=0;i<connMeta.databases.length;i++){
				if(connMeta.databases[i].instance = metatree.current.db){
					dbMeta = connMeta.databases[i];
					break;
				}
			}
			if(dbMeta){
				
			}
		}
	}
	
	return relation;
});
