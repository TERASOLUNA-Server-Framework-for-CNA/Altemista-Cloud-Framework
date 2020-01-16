package org.altemista.cloudfwk.remindersmybatis.module.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.altemista.cloudfwk.remindersmybatis.module.model.RmndrsUsers;
import org.altemista.cloudfwk.remindersmybatis.module.model.RmndrsUsersExample;

public interface RmndrsUsersMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    int countByExample(RmndrsUsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    int deleteByExample(RmndrsUsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    int deleteByPrimaryKey(String username);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    int insert(RmndrsUsers record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    int insertSelective(RmndrsUsers record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    List<RmndrsUsers> selectByExampleWithRowbounds(RmndrsUsersExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    List<RmndrsUsers> selectByExample(RmndrsUsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    RmndrsUsers selectByPrimaryKey(String username);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    int updateByExampleSelective(@Param("record") RmndrsUsers record, @Param("example") RmndrsUsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    int updateByExample(@Param("record") RmndrsUsers record, @Param("example") RmndrsUsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    int updateByPrimaryKeySelective(RmndrsUsers record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USERS
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    int updateByPrimaryKey(RmndrsUsers record);
}