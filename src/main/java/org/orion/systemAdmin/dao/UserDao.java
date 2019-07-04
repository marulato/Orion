package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.orion.systemAdmin.entity.User;


@Mapper
public interface UserDao {

    User query(String loginId);

    void update(User user);

    void insert(User user);

    void updateAfterLogin(User user);

}