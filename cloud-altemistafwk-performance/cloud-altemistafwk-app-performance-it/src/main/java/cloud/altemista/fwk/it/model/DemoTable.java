package cloud.altemista.fwk.it.model;

/*
 * #%L
 * altemista-cloud performance module integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DemoTable implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.KEY
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Integer key;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.INTEGER_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Integer integerField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.INT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Integer intField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.SMALLINT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Short smallintField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.TINYINT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Byte tinyintField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.BIGINT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Long bigintField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.DECIMAL_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private BigDecimal decimalField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.NUMERIC_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private BigDecimal numericField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.FLOAT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Double floatField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.DOUBLE_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Double doubleField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.DATE_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Date dateField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.DATETIME_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Date datetimeField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.TIMESTAMP_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Date timestampField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.TIME_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private Date timeField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.CHAR_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private String charField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column DEMO_TABLE.VARCHAR_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private String varcharField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table DEMO_TABLE
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.KEY
     *
     * @return the value of DEMO_TABLE.KEY
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Integer getKey() {
        return key;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.KEY
     *
     * @param key the value for DEMO_TABLE.KEY
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setKey(Integer key) {
        this.key = key;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.INTEGER_FIELD
     *
     * @return the value of DEMO_TABLE.INTEGER_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Integer getIntegerField() {
        return integerField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.INTEGER_FIELD
     *
     * @param integerField the value for DEMO_TABLE.INTEGER_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setIntegerField(Integer integerField) {
        this.integerField = integerField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.INT_FIELD
     *
     * @return the value of DEMO_TABLE.INT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Integer getIntField() {
        return intField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.INT_FIELD
     *
     * @param intField the value for DEMO_TABLE.INT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setIntField(Integer intField) {
        this.intField = intField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.SMALLINT_FIELD
     *
     * @return the value of DEMO_TABLE.SMALLINT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Short getSmallintField() {
        return smallintField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.SMALLINT_FIELD
     *
     * @param smallintField the value for DEMO_TABLE.SMALLINT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setSmallintField(Short smallintField) {
        this.smallintField = smallintField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.TINYINT_FIELD
     *
     * @return the value of DEMO_TABLE.TINYINT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Byte getTinyintField() {
        return tinyintField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.TINYINT_FIELD
     *
     * @param tinyintField the value for DEMO_TABLE.TINYINT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setTinyintField(Byte tinyintField) {
        this.tinyintField = tinyintField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.BIGINT_FIELD
     *
     * @return the value of DEMO_TABLE.BIGINT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Long getBigintField() {
        return bigintField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.BIGINT_FIELD
     *
     * @param bigintField the value for DEMO_TABLE.BIGINT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setBigintField(Long bigintField) {
        this.bigintField = bigintField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.DECIMAL_FIELD
     *
     * @return the value of DEMO_TABLE.DECIMAL_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public BigDecimal getDecimalField() {
        return decimalField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.DECIMAL_FIELD
     *
     * @param decimalField the value for DEMO_TABLE.DECIMAL_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setDecimalField(BigDecimal decimalField) {
        this.decimalField = decimalField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.NUMERIC_FIELD
     *
     * @return the value of DEMO_TABLE.NUMERIC_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public BigDecimal getNumericField() {
        return numericField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.NUMERIC_FIELD
     *
     * @param numericField the value for DEMO_TABLE.NUMERIC_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setNumericField(BigDecimal numericField) {
        this.numericField = numericField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.FLOAT_FIELD
     *
     * @return the value of DEMO_TABLE.FLOAT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Double getFloatField() {
        return floatField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.FLOAT_FIELD
     *
     * @param floatField the value for DEMO_TABLE.FLOAT_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setFloatField(Double floatField) {
        this.floatField = floatField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.DOUBLE_FIELD
     *
     * @return the value of DEMO_TABLE.DOUBLE_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Double getDoubleField() {
        return doubleField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.DOUBLE_FIELD
     *
     * @param doubleField the value for DEMO_TABLE.DOUBLE_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setDoubleField(Double doubleField) {
        this.doubleField = doubleField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.DATE_FIELD
     *
     * @return the value of DEMO_TABLE.DATE_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Date getDateField() {
        return dateField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.DATE_FIELD
     *
     * @param dateField the value for DEMO_TABLE.DATE_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setDateField(Date dateField) {
        this.dateField = dateField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.DATETIME_FIELD
     *
     * @return the value of DEMO_TABLE.DATETIME_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Date getDatetimeField() {
        return datetimeField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.DATETIME_FIELD
     *
     * @param datetimeField the value for DEMO_TABLE.DATETIME_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setDatetimeField(Date datetimeField) {
        this.datetimeField = datetimeField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.TIMESTAMP_FIELD
     *
     * @return the value of DEMO_TABLE.TIMESTAMP_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Date getTimestampField() {
        return timestampField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.TIMESTAMP_FIELD
     *
     * @param timestampField the value for DEMO_TABLE.TIMESTAMP_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setTimestampField(Date timestampField) {
        this.timestampField = timestampField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.TIME_FIELD
     *
     * @return the value of DEMO_TABLE.TIME_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public Date getTimeField() {
        return timeField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.TIME_FIELD
     *
     * @param timeField the value for DEMO_TABLE.TIME_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setTimeField(Date timeField) {
        this.timeField = timeField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.CHAR_FIELD
     *
     * @return the value of DEMO_TABLE.CHAR_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public String getCharField() {
        return charField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.CHAR_FIELD
     *
     * @param charField the value for DEMO_TABLE.CHAR_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setCharField(String charField) {
        this.charField = charField == null ? null : charField.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column DEMO_TABLE.VARCHAR_FIELD
     *
     * @return the value of DEMO_TABLE.VARCHAR_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public String getVarcharField() {
        return varcharField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column DEMO_TABLE.VARCHAR_FIELD
     *
     * @param varcharField the value for DEMO_TABLE.VARCHAR_FIELD
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    public void setVarcharField(String varcharField) {
        this.varcharField = varcharField == null ? null : varcharField.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DEMO_TABLE
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
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
        DemoTable other = (DemoTable) that;
        return (this.getKey() == null ? other.getKey() == null : this.getKey().equals(other.getKey()))
            && (this.getIntegerField() == null ? other.getIntegerField() == null : this.getIntegerField().equals(other.getIntegerField()))
            && (this.getIntField() == null ? other.getIntField() == null : this.getIntField().equals(other.getIntField()))
            && (this.getSmallintField() == null ? other.getSmallintField() == null : this.getSmallintField().equals(other.getSmallintField()))
            && (this.getTinyintField() == null ? other.getTinyintField() == null : this.getTinyintField().equals(other.getTinyintField()))
            && (this.getBigintField() == null ? other.getBigintField() == null : this.getBigintField().equals(other.getBigintField()))
            && (this.getDecimalField() == null ? other.getDecimalField() == null : this.getDecimalField().equals(other.getDecimalField()))
            && (this.getNumericField() == null ? other.getNumericField() == null : this.getNumericField().equals(other.getNumericField()))
            && (this.getFloatField() == null ? other.getFloatField() == null : this.getFloatField().equals(other.getFloatField()))
            && (this.getDoubleField() == null ? other.getDoubleField() == null : this.getDoubleField().equals(other.getDoubleField()))
            && (this.getDateField() == null ? other.getDateField() == null : this.getDateField().equals(other.getDateField()))
            && (this.getDatetimeField() == null ? other.getDatetimeField() == null : this.getDatetimeField().equals(other.getDatetimeField()))
            && (this.getTimestampField() == null ? other.getTimestampField() == null : this.getTimestampField().equals(other.getTimestampField()))
            && (this.getTimeField() == null ? other.getTimeField() == null : this.getTimeField().equals(other.getTimeField()))
            && (this.getCharField() == null ? other.getCharField() == null : this.getCharField().equals(other.getCharField()))
            && (this.getVarcharField() == null ? other.getVarcharField() == null : this.getVarcharField().equals(other.getVarcharField()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DEMO_TABLE
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getKey() == null) ? 0 : getKey().hashCode());
        result = prime * result + ((getIntegerField() == null) ? 0 : getIntegerField().hashCode());
        result = prime * result + ((getIntField() == null) ? 0 : getIntField().hashCode());
        result = prime * result + ((getSmallintField() == null) ? 0 : getSmallintField().hashCode());
        result = prime * result + ((getTinyintField() == null) ? 0 : getTinyintField().hashCode());
        result = prime * result + ((getBigintField() == null) ? 0 : getBigintField().hashCode());
        result = prime * result + ((getDecimalField() == null) ? 0 : getDecimalField().hashCode());
        result = prime * result + ((getNumericField() == null) ? 0 : getNumericField().hashCode());
        result = prime * result + ((getFloatField() == null) ? 0 : getFloatField().hashCode());
        result = prime * result + ((getDoubleField() == null) ? 0 : getDoubleField().hashCode());
        result = prime * result + ((getDateField() == null) ? 0 : getDateField().hashCode());
        result = prime * result + ((getDatetimeField() == null) ? 0 : getDatetimeField().hashCode());
        result = prime * result + ((getTimestampField() == null) ? 0 : getTimestampField().hashCode());
        result = prime * result + ((getTimeField() == null) ? 0 : getTimeField().hashCode());
        result = prime * result + ((getCharField() == null) ? 0 : getCharField().hashCode());
        result = prime * result + ((getVarcharField() == null) ? 0 : getVarcharField().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DEMO_TABLE
     *
     * @mbggenerated Sat Jun 11 14:07:11 CEST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", key=").append(key);
        sb.append(", integerField=").append(integerField);
        sb.append(", intField=").append(intField);
        sb.append(", smallintField=").append(smallintField);
        sb.append(", tinyintField=").append(tinyintField);
        sb.append(", bigintField=").append(bigintField);
        sb.append(", decimalField=").append(decimalField);
        sb.append(", numericField=").append(numericField);
        sb.append(", floatField=").append(floatField);
        sb.append(", doubleField=").append(doubleField);
        sb.append(", dateField=").append(dateField);
        sb.append(", datetimeField=").append(datetimeField);
        sb.append(", timestampField=").append(timestampField);
        sb.append(", timeField=").append(timeField);
        sb.append(", charField=").append(charField);
        sb.append(", varcharField=").append(varcharField);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
