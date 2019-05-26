package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.orion.systemAdmin.dao.provider.UserProvider;
import org.orion.systemAdmin.entity.User;

import java.util.List;


@Mapper
public interface UserDao {

    User query(String userId);

    void update(User user);

    void insert(User user);

}