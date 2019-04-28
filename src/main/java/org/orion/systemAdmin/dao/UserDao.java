package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.orion.systemAdmin.entity.User;

import java.util.List;


@Mapper
public interface UserDao {
    
    User selectByPrimaryKey(Long acctId);

    Integer deleteByPrimaryKey(Long acctId);

    Integer create(User user);

    Integer update(User user);

    Integer batchCreate(List<User> users);

    Integer batchUpdate(List<User> users);

    List<User> query(User userTbl);

    Long queryTotal();

    Integer deleteBatch(List<Long> list);

    /*<AUTOGEN--END>*/

}