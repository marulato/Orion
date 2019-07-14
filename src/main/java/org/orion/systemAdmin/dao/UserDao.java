package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.orion.systemAdmin.entity.User;
import org.orion.systemAdmin.entity.UserLoginHistory;
import org.orion.systemAdmin.entity.UserProfile;

import java.util.Date;
import java.util.List;


@Mapper
public interface UserDao {

    User query(String loginId);

    void update(User user);

    void insert(User user);

    void updateAfterLogin(User user);

    void createHistory(UserLoginHistory loginHistory);

    void updatePassword(User user);

    List<UserLoginHistory> queryLoginAudit(User user);

    List<UserLoginHistory> queryLoginAuditPeriod(@Param("id") String loginId, @Param("from") Date from, @Param("to") Date to, @Param("ss") String isSuccess);

    void createProfile(UserProfile profile);

    UserProfile queryUserProfile(User user);

}