package com.denghb.eorm.domain;

import com.denghb.eorm.annotation.Ecolumn;
import com.denghb.eorm.annotation.Etable;

/**
 * 
 * DDL
 * 
 <pre>
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮件',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8
 <pre>
 * @author DbHelper
 * @generateTime Fri Feb 09 15:23:39 CST 2018
 */
@Etable(name="user",database="test")
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	/**  */
	@Ecolumn(name="id", primaryKey = true)
	private Integer id;
	
	/** 年龄 */
	@Ecolumn(name="age")
	private Integer age;
	
	/** 姓名 */
	@Ecolumn(name="name")
	private String name;
	
	/** 邮件 */
	@Ecolumn(name="email")
	private String email;
	
	/** 手机号 */
	@Ecolumn(name="mobile")
	private String mobile;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("User [");
		str.append("id=\"");
		str.append(id);
		str.append("\"");
		str.append(",");
		str.append("age=\"");
		str.append(age);
		str.append("\"");
		str.append(",");
		str.append("name=\"");
		str.append(name);
		str.append("\"");
		str.append(",");
		str.append("email=\"");
		str.append(email);
		str.append("\"");
		str.append(",");
		str.append("mobile=\"");
		str.append(mobile);
		str.append("\"");
		
		str.append("]");
	
		return str.toString();
	}
}