package com.mycompany.application.module.controller;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Example
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-08-30T15:59:03.634+02:00")

public class Example   {
  @JsonProperty("example_id")
  private String exampleId = null;

  @JsonProperty("description")
  private String description = null;

  public Example exampleId(String exampleId) {
    this.exampleId = exampleId;
    return this;
  }

   /**
   * Unique identifier.
   * @return exampleId
  **/
  @ApiModelProperty(value = "Unique identifier.")


  public String getExampleId() {
    return exampleId;
  }

  public void setExampleId(String exampleId) {
    this.exampleId = exampleId;
  }

  public Example description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description.
   * @return description
  **/
  @ApiModelProperty(value = "Description.")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Example example = (Example) o;
    return Objects.equals(this.exampleId, example.exampleId) &&
        Objects.equals(this.description, example.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(exampleId, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Example {\n");
    
    sb.append("    exampleId: ").append(toIndentedString(exampleId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

