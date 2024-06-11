package com.vita.oauth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

	private Long id;   
	private String email;
	private String name;
	private String oauth_name;
	private String password;
	private String sex;
	private String BIRTH;
	private String role;
	
}
