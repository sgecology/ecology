package net.ecology.framework.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class RepoObject extends Repo {
  /**
   * 
   */
  private static final long serialVersionUID = 1917303128468837506L;

  @Setter @Getter
  @Column(name = "active")
  private Boolean active = Boolean.FALSE;

  @Setter @Getter
  @Column(name = "visible")
  private Boolean visible = Boolean.FALSE;
}