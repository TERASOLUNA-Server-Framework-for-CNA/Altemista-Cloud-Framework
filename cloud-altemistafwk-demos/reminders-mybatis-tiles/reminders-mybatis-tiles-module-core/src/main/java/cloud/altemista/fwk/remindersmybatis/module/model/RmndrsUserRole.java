package cloud.altemista.fwk.remindersmybatis.module.model;

import java.io.Serializable;

public class RmndrsUserRole implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column RMNDRS_USER_ROLE.ID
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column RMNDRS_USER_ROLE.USERNAME
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    private String username;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column RMNDRS_USER_ROLE.ROLE_ID
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    private Integer roleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table RMNDRS_USER_ROLE
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column RMNDRS_USER_ROLE.ID
     *
     * @return the value of RMNDRS_USER_ROLE.ID
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column RMNDRS_USER_ROLE.ID
     *
     * @param id the value for RMNDRS_USER_ROLE.ID
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column RMNDRS_USER_ROLE.USERNAME
     *
     * @return the value of RMNDRS_USER_ROLE.USERNAME
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column RMNDRS_USER_ROLE.USERNAME
     *
     * @param username the value for RMNDRS_USER_ROLE.USERNAME
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column RMNDRS_USER_ROLE.ROLE_ID
     *
     * @return the value of RMNDRS_USER_ROLE.ROLE_ID
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column RMNDRS_USER_ROLE.ROLE_ID
     *
     * @param roleId the value for RMNDRS_USER_ROLE.ROLE_ID
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USER_ROLE
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        RmndrsUserRole other = (RmndrsUserRole) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USER_ROLE
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RMNDRS_USER_ROLE
     *
     * @mbggenerated Mon Jun 06 11:21:55 CEST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", roleId=").append(roleId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}