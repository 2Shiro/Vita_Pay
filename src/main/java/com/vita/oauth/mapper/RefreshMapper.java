package com.vita.oauth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;
import com.vita.oauth.domain.RefreshDTO;


@Mapper
public interface RefreshMapper {

	Boolean existsByRefresh(String refresh);
	
	@Transactional
	void deleteByRefresh(String refresh);

	void save(RefreshDTO refreshDto);
}
