package ${packageName};

import com.denghb.eorm.annotation.Ecolumn;
import com.denghb.eorm.annotation.Etable;

/**
 * ${tableComment}
 * DDL
 * 
 <pre>
${tableDdl}
 <pre>
 * @author denghb
 * @generateTime ${generateTime}
 */
@Etable(name="${tableName}",database="${databaseName}")
public class ${domainName} implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	<#list list as table>
	/** ${table.columnComment} */
	@Ecolumn(name="${table.columnName}"<#if table.columnKey = "PRI">, primaryKey = true</#if>)
	private ${table.dataType} ${table.objectName};
	
    </#list>

	<#list list as table>
	public ${table.dataType} get${table.methodName}() {
		return ${table.objectName};
	}

	public void set${table.methodName}(${table.dataType} ${table.objectName}) {
		this.${table.objectName} = ${table.objectName};
	}

	</#list>
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("${domainName} [");
		<#list list as table>
		str.append("${table.objectName}=\"");
		str.append(${table.objectName});
		str.append("\"");
		<#if (list?size - 1 > table_index) >str.append(",");</#if>
		</#list>
		str.append("]");
	
		return str.toString();
	}
}