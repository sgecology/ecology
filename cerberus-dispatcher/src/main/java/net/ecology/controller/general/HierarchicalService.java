/**
 * 
 */
package net.ecology.controller.general;

import org.primefaces.extensions.component.orgchart.DefaultOrgChartNode;
import org.primefaces.extensions.component.orgchart.OrgChartNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.Connector;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import net.ecology.model.hierarchy.UIElement;

/**
 * @author ducbq
 *
 */
@Component
public class HierarchicalService {
  @Setter
  @Getter
  private DefaultDiagramModel model;

  @Setter
  @Getter
  private TreeNode root;

  /*********************************************************/
  @Setter
  @Getter
  private OrgChartNode orgChartNode;

  @Setter
  @Getter
  private String direction = "t2b";

  /*********************************************************/

  public void init() {
    initHierarchyDiagram();
    initTreeNodes();
    initOrgChartNodes();
  }

  private void initHierarchyDiagram() {
    model = new DefaultDiagramModel();
    model.setMaxConnections(-1);
    model.setConnectionsDetachable(false);

    Element ceo = new Element("CEO", "25em", "6em");
    ceo.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));
    model.addElement(ceo);

    // CFO
    Element cfo = new Element("CFO", "10em", "18em");
    cfo.addEndPoint(createEndPoint(EndPointAnchor.TOP));
    cfo.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));

    Element fin = new Element("FIN", "5em", "30em");
    fin.addEndPoint(createEndPoint(EndPointAnchor.TOP));

    Element pur = new Element("PUR", "20em", "30em");
    pur.addEndPoint(createEndPoint(EndPointAnchor.TOP));

    model.addElement(cfo);
    model.addElement(fin);
    model.addElement(pur);

    // CTO
    Element cto = new Element("CTO", "40em", "18em");
    cto.addEndPoint(createEndPoint(EndPointAnchor.TOP));
    cto.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));

    Element dev = new Element("DEV", "35em", "30em");
    dev.addEndPoint(createEndPoint(EndPointAnchor.TOP));

    Element tst = new Element("TST", "50em", "30em");
    tst.addEndPoint(createEndPoint(EndPointAnchor.TOP));

    Element quality = new Element("Quality", "55em", "30em");
    quality.addEndPoint(createEndPoint(EndPointAnchor.CONTINUOUS_TOP));

    Element svcDsk = new Element("Service Desk", "60em", "30em");
    svcDsk.addEndPoint(createEndPoint(EndPointAnchor.CONTINUOUS_TOP));

    model.addElement(cto);
    model.addElement(dev);
    model.addElement(tst);
    model.addElement(quality);
    model.addElement(svcDsk);

    // CAO
    UIElement caoEl = UIElement.builder().data("CAO").x("40em").y("18em")
        .endPointAnchors(new EndPointAnchor[] { EndPointAnchor.TOP, EndPointAnchor.BOTTOM }).build();

    // Element cao = createElement(model, caoEl, null);

    StraightConnector connector = new StraightConnector();
    // connector.setPaintStyle("{stroke:'#404a4e', strokeWidth:3}");
    connector.setHoverPaintStyle("{stroke:'#20282b'}");

    // connections
    model.connect(new Connection(ceo.getEndPoints().get(0), cfo.getEndPoints().get(0), connector));
    model.connect(new Connection(ceo.getEndPoints().get(0), cto.getEndPoints().get(0), connector));
    model.connect(new Connection(cfo.getEndPoints().get(1), fin.getEndPoints().get(0), connector));
    model.connect(new Connection(cfo.getEndPoints().get(1), pur.getEndPoints().get(0), connector));
    model.connect(new Connection(cto.getEndPoints().get(1), dev.getEndPoints().get(0), connector));
    model.connect(new Connection(cto.getEndPoints().get(1), tst.getEndPoints().get(0), connector));

    model.connect(new Connection(cto.getEndPoints().get(1), quality.getEndPoints().get(0), connector));
    model.connect(new Connection(cto.getEndPoints().get(1), svcDsk.getEndPoints().get(0), connector));

  }

  private EndPoint createEndPoint(EndPointAnchor anchor) {
    DotEndPoint endPoint = new DotEndPoint(anchor);
    endPoint.setStyle("{fill:'#404a4e'}");
    endPoint.setHoverStyle("{fill:'#20282b'}");

    return endPoint;
  }

  private Element createElement(DiagramModel diagramModel, Connector connector, UIElement mainEl, UIElement[] children) {
    Element result = new Element(mainEl.getData(), mainEl.getX(), mainEl.getY());
    result.addEndPoint(createEndPoint(EndPointAnchor.TOP));
    result.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));

    Element elChild = null;
    if (children != null) {
      for (UIElement child : children) {
        elChild = new Element(child.getData(), child.getX(), child.getY());
        for (Object endPoint : child.getEndPointAnchors()) {
          elChild.addEndPoint(createEndPoint((EndPointAnchor) endPoint));
        }
      }
    }

    Element dev = new Element("DEV", "35em", "30em");
    dev.addEndPoint(createEndPoint(EndPointAnchor.TOP));

    Element tst = new Element("TST", "50em", "30em");
    tst.addEndPoint(createEndPoint(EndPointAnchor.TOP));
    return result;
  }

  private void initTreeNodes() {
    root = new DefaultTreeNode("Files", null);
    TreeNode node0 = new DefaultTreeNode("Documents", root);
    TreeNode node1 = new DefaultTreeNode("Events", root);
    TreeNode node2 = new DefaultTreeNode("Movies", root);

    TreeNode node00 = new DefaultTreeNode("Work", node0);
    TreeNode node01 = new DefaultTreeNode("Home", node0);

    node00.getChildren().add(new DefaultTreeNode("Expenses.doc"));
    node00.getChildren().add(new DefaultTreeNode("Resume.doc"));
    node01.getChildren().add(new DefaultTreeNode("Invoices.txt"));

    TreeNode node10 = new DefaultTreeNode("Meeting", node1);
    TreeNode node11 = new DefaultTreeNode("Product Launch", node1);
    TreeNode node12 = new DefaultTreeNode("Report Review", node1);

    TreeNode node20 = new DefaultTreeNode("Al Pacino", node2);
    TreeNode node21 = new DefaultTreeNode("Robert De Niro", node2);

    node20.getChildren().add(new DefaultTreeNode("Scarface"));
    node20.getChildren().add(new DefaultTreeNode("Serpico"));

    node21.getChildren().add(new DefaultTreeNode("Goodfellas"));
    node21.getChildren().add(new DefaultTreeNode("Untouchables"));

  }

  private void initOrgChartNodes() {
    orgChartNode = new DefaultOrgChartNode("id1", "Node1", "content1");
    final OrgChartNode node2 = new DefaultOrgChartNode("id2", "Node2", "Content2</br>Content2</br>Content2");
    orgChartNode.addChild(node2);
    orgChartNode.addChild(new DefaultOrgChartNode("id3", "Node3", "Content3"));
    final OrgChartNode node = new DefaultOrgChartNode("id4", "Node4", "Content4");
    orgChartNode.addChild(node);
    node.addChild(new DefaultOrgChartNode("id5", "Node5", "Content5"));
    node.addChild(new DefaultOrgChartNode("id6", "Node6", "Content6"));
 
    final OrgChartNode node21 = new DefaultOrgChartNode("id2.1", "Node-2.1", "Content 2.1");

    node2.addChild(node21);
    node21.addChild(new DefaultOrgChartNode("id2.1.1", "Node-2.1.1", "Content 2.1.1"));
    node21.addChild(new DefaultOrgChartNode("id2.1.2", "Node-2.1.2", "Content 2.1.2"));
    node21.addChild(new DefaultOrgChartNode("id2.1.3", "Node-2.1.3", "Content 2.1.3"));
    node21.addChild(new DefaultOrgChartNode("id2.1.4", "Node-2.1.4", "Content 2.1.4"));
    node21.addChild(new DefaultOrgChartNode("id2.1.5", "Node-2.1.5", "Content 2.1.5"));
    node21.addChild(new DefaultOrgChartNode("id2.1.6", "Node-2.1.6", "Content 2.1.6"));
    node21.addChild(new DefaultOrgChartNode("id2.1.7", "Node-2.1.7", "Content 2.1.7"));
    node21.addChild(new DefaultOrgChartNode("id2.1.8", "Node-2.1.8", "Content 2.1.8"));
    node21.addChild(new DefaultOrgChartNode("id2.1.9", "Node-2.1.9", "Content 2.1.9"));

    final OrgChartNode node22 = new DefaultOrgChartNode("id2.2", "Node-2.2", "Content 2.2");
    node2.addChild(node22);
    node22.addChild(new DefaultOrgChartNode("id2.2.1", "Node-2.2.1", "Content 2.2.1"));
    node22.addChild(new DefaultOrgChartNode("id2.2.2", "Node-2.2.2", "Content 2.2.2"));
    node22.addChild(new DefaultOrgChartNode("id2.2.3", "Node-2.2.3", "Content 2.2.3"));

    OrgChartNode node224 = new DefaultOrgChartNode("id2.2.4", "Node-2.2.4", "Content 2.2.4");
    node22.addChild(node224);
    node224.addChild(new DefaultOrgChartNode("id2.2.4.1", "Node-2.2.4.1", "Content 2.2.4.1"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.2", "Node-2.2.4.2", "Content 2.2.4.2"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.3", "Node-2.2.4.3", "Content 2.2.4.3"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.4", "Node-2.2.4.4", "Content 2.2.4.4"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.5", "Node-2.2.4.5", "Content 2.2.4.5"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.6", "Node-2.2.4.6", "Content 2.2.4.6"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.7", "Node-2.2.4.7", "Content 2.2.4.7"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.8", "Node-2.2.4.8", "Content 2.2.4.8"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.9", "Node-2.2.4.9", "Content 2.2.4.9"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.10", "Node-2.2.4.10", "Content 2.2.4.10"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.1", "Node-2.2.4.1", "Content 2.2.4.1"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.2", "Node-2.2.4.2", "Content 2.2.4.2"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.3", "Node-2.2.4.3", "Content 2.2.4.3"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.4", "Node-2.2.4.4", "Content 2.2.4.4"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.5", "Node-2.2.4.5", "Content 2.2.4.5"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.6", "Node-2.2.4.6", "Content 2.2.4.6"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.7", "Node-2.2.4.7", "Content 2.2.4.7"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.8", "Node-2.2.4.8", "Content 2.2.4.8"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.9", "Node-2.2.4.9", "Content 2.2.4.9"));
    node224.addChild(new DefaultOrgChartNode("id2.2.4.10", "Node-2.2.4.10", "Content 2.2.4.10"));

    node22.addChild(new DefaultOrgChartNode("id2.2.5", "Node-2.2.5", "Content 2.2.5"));
    node22.addChild(new DefaultOrgChartNode("id2.2.6", "Node-2.2.6", "Content 2.2.6"));
    node22.addChild(new DefaultOrgChartNode("id2.2.7", "Node-2.2.7", "Content 2.2.7"));
    node22.addChild(new DefaultOrgChartNode("id2.2.8", "Node-2.2.8", "Content 2.2.8"));
    node22.addChild(new DefaultOrgChartNode("id2.2.9", "Node-2.2.9", "Content 2.2.9"));

    OrgChartNode node2210 = new DefaultOrgChartNode("id2.2.10", "Node-2.2.10", "Content 2.2.10");
    node22.addChild(node2210);

    final OrgChartNode node22101 = new DefaultOrgChartNode("id2.2.10.1", "Node-2.2.10.1", "Content 2.2.10.1");
    node2210.addChild(node22101);
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.1", "Node-2.2.1.1", "Content 2.2.10.1.1"));
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.2", "Node-2.2.1.2", "Content 2.2.10.1.2"));
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.3", "Node-2.2.1.3", "Content 2.2.10.1.3"));
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.4", "Node-2.2.1.4", "Content 2.2.10.1.4"));
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.5", "Node-2.2.1.5", "Content 2.2.10.1.5"));
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.6", "Node-2.2.1.6", "Content 2.2.10.1.6"));
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.7", "Node-2.2.1.7", "Content 2.2.10.1.7"));
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.8", "Node-2.2.1.8", "Content 2.2.10.1.8"));
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.9", "Node-2.2.1.9", "Content 2.2.10.1.9"));
    node22101.addChild(new DefaultOrgChartNode("id2.2.10.1.10", "Node-2.2.1.10", "Content 2.2.10.10"));

    final OrgChartNode node22102 = new DefaultOrgChartNode("id2.2.10.2", "Node-2.2.10.2", "Content 2.2.10.2");
    node2210.addChild(node22102);
    node22102.addChild(new DefaultOrgChartNode("id2.2.10.2.1", "Node-2.2.2.1", "Content 2.2.10.2.1"));
    node22102.addChild(new DefaultOrgChartNode("id2.2.10.2.2", "Node-2.2.2.2", "Content 2.2.10.2.2"));
    node22102.addChild(new DefaultOrgChartNode("id2.2.10.2.3", "Node-2.2.2.3", "Content 2.2.10.2.3"));
    node22102.addChild(new DefaultOrgChartNode("id2.2.10.2.4", "Node-2.2.2.4", "Content 2.2.10.2.4"));
    node22102.addChild(new DefaultOrgChartNode("id2.2.10.2.5", "Node-2.2.2.5", "Content 2.2.10.2.5"));
    node22102.addChild(new DefaultOrgChartNode("id2.2.10.2.6", "Node-2.2.2.6", "Content 2.2.10.2.6"));
    node22102.addChild(new DefaultOrgChartNode("id2.2.10.2.7", "Node-2.2.2.7", "Content 2.2.10.2.7"));
    node22102.addChild(new DefaultOrgChartNode("id2.2.10.2.8", "Node-2.2.2.8", "Content 2.2.10.2.8"));

  }
}
