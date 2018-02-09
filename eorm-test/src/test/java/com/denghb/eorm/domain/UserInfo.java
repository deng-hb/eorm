package com.denghb.eorm.domain;

import com.denghb.eorm.annotation.Ecolumn;
import com.denghb.eorm.annotation.Etable;

/**
 * 用户信息
 * DDL
 * 
 <pre>
CREATE TABLE `user_info` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar_url` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息'
 <pre>
 * @author DbHelper
 * @generateTime Fri Feb 09 15:23:39 CST 2018
 */
@Etable(name="user_info",database="test")
public class UserInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	/**  */
	@Ecolumn(name="id", primaryKey = true)
	private Long id;
	
	/**  */
	@Ecolumn(name="username")
	private String username;
	
	/**  */
	@Ecolumn(name="password")
	private String password;
	
	/**  */
	@Ecolumn(name="avatar_url")
	private String avatarUrl;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("UserInfo [");
		str.append("id=\"");
		str.append(id);
		str.append("\"");
		str.append(",");
		str.append("username=\"");
		str.append(username);
		str.append("\"");
		str.append(",");
		str.append("password=\"");
		str.append(password);
		str.append("\"");
		str.append(",");
		str.append("avatarUrl=\"");
		str.append(avatarUrl);
		str.append("\"");
		
		str.append("]");
	
		return str.toString();
	}
}