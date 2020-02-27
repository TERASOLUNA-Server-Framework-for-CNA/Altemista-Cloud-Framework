/**
 * Business model of module (i.e.: the model actually exposed by the module business module).
 * This model contains the model used by the service interfaces, but can include additional classes as well.
 */
package com.mycompany.application.module.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity 
@Table(name = "T_ORM_EXAMPLE") 
public class Demo implements Serializable {

  private static final long serialVersionUID = 4679200693272076037L;

  @Id 
  @Column(name = "NAME", nullable = false, unique = true) 
  private String name;

  @Column(name = "DESCRIPTION") 
  private String description;

}