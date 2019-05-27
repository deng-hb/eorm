package ${packageName};

import com.denghb.eorm.annotation.EColumn;
import com.denghb.eorm.annotation.ETable;
<#if swagger2 >import io.swagger.annotations.ApiModelProperty;</#if>

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
<#if lombok >@lombok.Data()
@ETable(name="${tableName}", database="${databaseName}")<#else>@ETable(name="${tableName}", database="${databaseName}")</#if>
public class ${domainName} implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	<#list list as table>
	<#if swagger2 >@ApiModelProperty(value = "${table.columnComment}")<#else>/** ${table.columnComment} */</#if>
	@EColumn(name="${table.columnName}"<#if table.columnKey = "PRI">, primaryKey = true</#if>)
	private ${table.dataType} ${table.objectName};
	
    </#list>
    <#if !lombok><#list list as table>
	public ${table.dataType} get${table.methodName}() {
		return ${table.objectName};
	}

	public void set${table.methodName}(${table.dataType} ${table.objectName}) {
		this.${table.objectName} = ${table.objectName};
	}

	</#list>
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("{");
		<#list list as table>
		str.append("\"${table.objectName}\":\"");
		str.append(${table.objectName});
		str.append("\"");
		<#if (list?size - 1 > table_index) >str.append(",");</#if>
		</#list>
		return str.append("}").toString();
	}</#if>
}