package edu.sru.thangiah.zeus.top.topgui.topcheckboxtree;

import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.tree.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * @version 2.0
 */

public class TOPCheckTreeNode
    extends DefaultMutableTreeNode {
  protected boolean selected;
  protected boolean propagate;
  private Color color = Color.white;
  private ActionListener actionListener;

  public TOPCheckTreeNode(Object data) {
    this(data, false, true);
  }

  public TOPCheckTreeNode(Object data, boolean selected) {
    this(data, selected, true);
  }

  public TOPCheckTreeNode(Object data, boolean selected, boolean propagate) {
    super(data);
    this.selected = selected;
    this.propagate = propagate;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;

    if (propagate) {
      propagateSelected(selected);
    }
  }

  public void propagateSelected(boolean selected) {
    Enumeration enum_ = children();

    while (enum_.hasMoreElements()) {
      TOPCheckTreeNode node = (TOPCheckTreeNode) enum_.nextElement();
      node.setSelected(selected);
    }
  }

  public void setUserObject(Object obj) {
    if (obj == this) {
      return;
    }

    super.setUserObject(obj);
  }

  public void setColor(Color c) {
    color = c;
  }

  public Color getColor() {
    return color;
  }

  public void setActionListener(ActionListener a) {
    actionListener = a;
  }

  public ActionListener getActionListener() {
    return actionListener;
  }
}
