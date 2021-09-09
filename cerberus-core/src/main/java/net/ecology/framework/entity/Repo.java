package net.ecology.framework.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class Repo implements Entity {
  /**
   * 
   */
  private static final long serialVersionUID = 813467866118213095L;

  @Setter @Getter
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private java.lang.Long id;

  /**
   * Determines whether another object is equal to this business object.  The result is 
   * <code>true</code> if and only if the argument is not null and is a business object that 
   * has the same id field values as this object.
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the argument;
   * <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (getClass() != obj.getClass())
      return false;

    Repo other = (Repo) obj;
    if (id == null) {
      if (other.getId() != null)
        return false;
    } else if (!id.equals(other.getId())) {
      return false;
    }

    return true;
  }

  public int compareTo(Object obj) {
    if (obj.hashCode() > hashCode())
      return 1;

    if (obj.hashCode() < hashCode())
      return -1;

    return 0;
  }

  public String toString() {
    return new StringBuilder("Id: ").append(this.id).toString();
  }
}