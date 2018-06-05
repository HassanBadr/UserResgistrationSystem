package com.peace.reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peace.reg.dto.UserInfo;

public interface UserInfoJpaRepository extends JpaRepository<UserInfo, Long> {
	public UserInfo findByUserName(String username);

}
