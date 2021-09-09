/**
 * 
 */
package net.ecology.model.hierarchy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class UIElement {
  private String data;
  private String x;
  private String y;
  private Object[] endPointAnchors;
}
