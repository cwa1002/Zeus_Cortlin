package edu.sru.thangiah.zeus.top;

//import the parent class
import edu.sru.thangiah.zeus.core.ShipmentLinkedList;
import java.io.PrintStream;

/**
 *
 * <p>Title: TOPShipmentLinkedList</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * @version 2.0
 */
public class TOPShipmentLinkedList
    extends ShipmentLinkedList implements java.io.Serializable, java.lang.Cloneable {
  public static final long serialVersionUID = 1;

  /**
   * Constructor
   */
  public TOPShipmentLinkedList() {
    setHead(null);
    setTail(null);
    setNumShipments(0);
  }

  /**
   * Returns the TOP shipment in the list which has the specified key
   * @param key int
   * @return TOPShipment
   */
  public TOPShipment findTOP(int key) {
    return (TOPShipment)find(key);
  }

  /**
   * Insert the shipment into the linked list
   * @param num int
   * @param x float
   * @param y float
   * @param q int
   */
  public void insertShipment(int num, float x, float y, int q) {
    //create an instance of the Shipment
    TOPShipment thisShip = new TOPShipment(num, x, y, q);
    //add the instance to the linked list - in this case it is added at the end of the list
    //the total number of shipments is incremented in the insert
    insertLast(thisShip);
  }

  /**
   * Returns the first shipment in the linked list
   * @return first shipment
   */
  public TOPShipment getTOPHead() {
    return (TOPShipment)getHead();
  }

  /**
   * This method will get the next shipment that is to be inserted based on the
   * type of shipment selection that has been defined in the main method
   * for the variable ProblemInfo.selectShipType
   * @param currDepot current depot being considered for the shipment
   * @param currDepotLL Depot linked list of the problem being solved
   * @param currShipmentLL shipment linked list from which the shipment to be
   * insertion is to be selected
   * @return TOPShipment the shipment to be inserted
   */
  public TOPShipment getNextInsertShipment(TOPDepotLinkedList currDepotLL, TOPDepot currDepot,
					   TOPShipmentLinkedList currShipmentLL, TOPShipment currShip) {
    TOPShipmentLinkedList selectShip = (TOPShipmentLinkedList)TOPProblemInfo.selectShipType;

    return selectShip.getSelectShipment(currDepotLL, currDepot, currShipmentLL, currShip);
  }

  /**
   * This is a stub - Leave it as it is
   * The concrere getSelectShipment will be declared by the class inheriting this
   * class and implementing the actual insertion of shipment  *
   * @param currShipmentLL shipment linked list from which the shipment to be
   * insertion is to be selected
   * @return TOPShipment the shipment to be inserted
   */
  public TOPShipment getSelectShipment(TOPDepotLinkedList currDepotLL, TOPDepot currDepot,
				       TOPShipmentLinkedList currShipmentLL, TOPShipment currShip) {
    return null;
  }

  /**
   * Writes the shipments to the print stream
   * @param out PrintStream stream to output to
   */
  public void writeTOPShipments(PrintStream out) {
    out.println(this.getNumShipments());

    TOPShipment ship = getTOPHead();
    while (ship != null) {
      out.println(ship.getIndex() + " " + ship.getTruckTypeNeeded() + " " + ship.getDemand() + " " +
                  ship.getXCoord() + " " + ship.getYCoord() + " " + ship.getExtraVariable());
      ship = ship.getTOPNext();
    }
  }

  /**
   * Returns whether or not all shipments in the list area either already assigned or unreachable
   * @return boolean
   * Added by David Crissman
   */
  public boolean isAllShipsAssignedOrUnreachable() {
    boolean retValue = true;
    TOPShipment currentShipment = getTOPHead();

    while (currentShipment != null) {
      if ( (currentShipment.getIsAssigned() == false) && (currentShipment.getUnreachable() == false)) {
	retValue = false;
      }

      currentShipment = currentShipment.getTOPNext();
    }

    return retValue;
  }

  /**
   * Returns whether or not all the shipments in the list have been assigned, are unreachable, or have already
   * been checked on the current pass
   * @return boolean
   */
  public boolean isAllShipsAssignedOrUnreachableOrChecked() {
    boolean retValue = true;
    TOPShipment currentShipment = getTOPHead();

    while (currentShipment != null) {
      if ((currentShipment.getIsAssigned() == false) && (currentShipment.getUnreachable() == false) &&
           (currentShipment.getChecked() == false)) {
        retValue = false;
      }

      currentShipment = currentShipment.getTOPNext();
    }

    return retValue;
  }

  /**
   * Returns and exact copy of the shipment linked list
   * @return Object
   */
  public Object clone() {
    TOPShipmentLinkedList clonedShipmentLinkedList = new TOPShipmentLinkedList();

    clonedShipmentLinkedList.setHead((TOPShipment)this.getTOPHead().clone());
    clonedShipmentLinkedList.getHead().setPrev(null);

    if (this.getHead() != this.getTail()) {
      TOPShipment currentShipment = clonedShipmentLinkedList.getTOPHead();
      TOPShipment nextShipment = this.getTOPHead().getTOPNext();

      while (nextShipment != null) {
        currentShipment.setNext((TOPShipment)nextShipment.clone()); //create the next shipment
        currentShipment.getTOPNext().setPrev(currentShipment); //set the next shipment's prev
        currentShipment = currentShipment.getTOPNext();
        nextShipment = nextShipment.getTOPNext();

        //once next is null, we have found the tail of the list
        if (nextShipment == null) {
          clonedShipmentLinkedList.setTail(currentShipment);
          currentShipment.setNext(null);
        }
      }
    }
    else {
      clonedShipmentLinkedList.setTail(clonedShipmentLinkedList.getHead());
    }

    return clonedShipmentLinkedList;
  }
}

/**
 *
 * <p>Title: EllipticalTargetArea</p>
 * <p>Description: This is a selection algorithm which only chooses shipments from a within the bounds
 * of two ellipses, the first defined by the starting point and a target point, and the second defined
 * by the target point and the ending point. The shipments are selected using a secondary selection
 * algorithm.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Slippery Rock University</p>
 * @author David Crissman
 * @version 1.0
 */
class EllipticalTargetArea
    extends TOPShipmentLinkedList {
  private static TOPEllipse targetAreaA, targetAreaB;                //The two elliptical target areas
  private static TOPShipmentLinkedList secondarySelectionAlgorithm;  //Selection algorithm used to select
                                                                     //shipments from within the target ares

  /**
   * Constructor
   */
  public EllipticalTargetArea() {
    super();

    targetAreaA = new TOPEllipse(TOPProblemInfo.startXCoord, TOPProblemInfo.startYCoord,
                                     TOPProblemInfo.endXCoord, TOPProblemInfo.endYCoord,
                                     TOPProblemInfo.truckMaxTravelTime);
    targetAreaB = targetAreaA;
    secondarySelectionAlgorithm = null;
  }

  /**
   * Constructor
   * @param xCoord double
   * @param yCoord double
   * @param dist double
   * @param alg TOPShipmentLinkedList
   */
  public EllipticalTargetArea(double xCoord, double yCoord, double dist, TOPShipmentLinkedList alg) {
    targetAreaA = new TOPEllipse(TOPProblemInfo.startXCoord, TOPProblemInfo.startYCoord,
                                 xCoord, yCoord, dist);
    targetAreaB = new TOPEllipse(TOPProblemInfo.endXCoord, TOPProblemInfo.endYCoord,
                                 xCoord, yCoord, dist);
    secondarySelectionAlgorithm = alg;
  }

  /**
   * Sets the secondary selection algorithm
   * @param alg TOPShipmentLinkedList
   */
  public static void setSecondaryAlgorithm(TOPShipmentLinkedList alg) {
    secondarySelectionAlgorithm = alg;
  }

  /**
   * Chooses a shipment from within the target area, using the secondary selection algorithm
   * @param currDepotLL TOPDepotLinkedList
   * @param currDepot TOPDepot
   * @param currShipLL TOPShipmentLinkedList
   * @param currShip TOPShipment
   * @return TOPShipment
   */
  public TOPShipment getSelectShipment(TOPDepotLinkedList currDepotLL, TOPDepot currDepot,
                                       TOPShipmentLinkedList currShipLL, TOPShipment currShip) {
    TOPShipment currentShipment;
    boolean status, isDiagnostic = false;

    //Clear the 'checked' flags on all the shipments
    currentShipment = this.getTOPHead();
    while (currentShipment != null) {
      currentShipment.setChecked(false);
      currentShipment = currentShipment.getTOPNext();
    }

    //Call the secondary algorithm until it returns a shipment which is in the target area
    status = false;
    currentShipment = null;
    while (status == false) {
      currentShipment = secondarySelectionAlgorithm.getSelectShipment(currDepotLL, currDepot, currShipLL, currShip);

      if (currentShipment != null) {
	if ((targetAreaA.pointInsideEllipse(currentShipment.getXCoord(), currentShipment.getYCoord()) == true) ||
           (targetAreaB.pointInsideEllipse(currentShipment.getXCoord(), currentShipment.getYCoord()) == true)) {
          status = true;
	}
	else {
          //This shipment is not in the target area, so mark it as checked, and move on to a different one
	  currentShipment.setChecked(true);
	}
      }
      else {
        status = true;
      }
    }

    return currentShipment;
  }


  public static String WhoAmI() {
    return ("Selection Type: Selects shipments from a sweeping elliptical area");
  }

}

/**
 *
 * <p>Title: HighestDemandInReachableArea</p>
 * <p>Description: This is a selection algorithm which chooses the shipment with the highest demand.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Slippery Rock University</p>
 *
 * @author David Crissman
 * @version 1.0
 */
class HighestDemandInReachableArea
    extends TOPShipmentLinkedList {
  /**
   * Returns the next available shipment
   * @param currDepotLL TOPDepotLinkedList
   * @param currDepot TOPDepot
   * @param currShipLL TOPShipmentLinkedList
   * @param currShip TOPShipment
   * @return TOPShipment
   */
  public TOPShipment getSelectShipment(TOPDepotLinkedList currDepotLL, TOPDepot currDepot,
				       TOPShipmentLinkedList currShipLL, TOPShipment currShip) {
    TOPShipment currentShipment, bestShipment = null;
    int highestDemand, demand;
    boolean isDiagnostic = false;

    highestDemand = -1;
    currentShipment = currShipLL.getTOPHead(); // Pete changed this because 'head' was null.
    while (currentShipment != null) {
      if (isDiagnostic) {
	System.out.print("Shipment " + currentShipment.getIndex() + " ");
      }

      if (currentShipment.getIsAssigned()) {
	if (isDiagnostic) {
	  System.out.println("has been assigned");
	}
      }
      else if (currentShipment.getUnreachable() == true) {
	if (isDiagnostic) {
	  System.out.println("is not reachable");
	}
      }
      else if (currentShipment.getChecked() == true) {
        if (isDiagnostic) {
          System.out.println("is outside the target area");
        }
      }
      else {
	demand = currentShipment.getDemand();
	if (demand > highestDemand) {
	  bestShipment = currentShipment;
	  highestDemand = currentShipment.getDemand();
	}

	if (isDiagnostic) {
	  System.out.println("  " + demand);
	}
      }

      currentShipment = currentShipment.getTOPNext();
    }

    return bestShipment;
  }

  public static String WhoAmI() {
    return ("Selection Type: Highest demand within the trucks' reachable area");
  }
}

/**
 * <p>Title: ClosestEuclidianDistToDepot</p>
 * <p>Description: Select the shipment with the shortest distance to the depot</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Slippery Rock University</p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
class ClosestEuclideanDistToDepot
    extends TOPShipmentLinkedList {
  public TOPShipment getSelectShipment(TOPDepotLinkedList currDepotLL,
				       TOPDepot currDepot,
				       TOPShipmentLinkedList currShipLL,
				       TOPShipment currShip) {
    //currDepotLL is the depot linked list of the problem
    //currDepot is the depot under consideration
    //currShipLL is the set of avaialble shipments
    boolean isDiagnostic = false;
    TOPShipment temp = currShipLL.getTOPHead(); //point to the first shipment
    TOPShipment foundShipment = null; //the shipment found with the criteria
    double distance;
    double foundDistance = 200; //initial distance
    double depotX;
	double depotY;

    //Get the X and Y coordinate of the depot
    depotX = currDepot.getXCoord();
    depotY = currDepot.getYCoord();

    while (temp != null) {
      if (isDiagnostic) {
	System.out.print("Shipment " + temp.getIndex() + " ");

	if ( ( (temp.getXCoord() - depotX) >= 0) &&
	    ( (temp.getYCoord() - depotX) >= 0)) {
	  System.out.print("Quadrant I ");
	}
	else if ( ( (temp.getXCoord() - depotX) <= 0) &&
		 ( (temp.getYCoord() - depotY) >= 0)) {
	  System.out.print("Quadrant II ");
	}
	else if ( ( (temp.getXCoord()) <= (0 - depotX)) &&
		 ( (temp.getYCoord() - depotY) <= 0)) {
	  System.out.print("Quadrant III ");
	}
	else if ( ( (temp.getXCoord() - depotX) >= 0) &&
		 ( (temp.getYCoord() - depotY) <= 0)) {
	  System.out.print("Quadrant VI ");
	}
	else {
	  System.out.print("No Quadrant");
	}
      }

      //if the shipment is assigned, skip it
      if (temp.getIsAssigned() == true) {
	if (isDiagnostic) {
	  System.out.println("has been assigned");
	}

	temp = temp.getTOPNext();

	continue;
      }
      else if (temp.getUnreachable() == true) {
	if (isDiagnostic) {
	  System.out.println("is not reachable");
	}

	temp = temp.getTOPNext();

	continue;
      }
      else if (temp.getChecked() == true) {
        if (isDiagnostic) {
          System.out.println("is outside the target area");
        }

        temp = temp.getTOPNext();

        continue;
      }

      /** @todo Associate the quadrant with the distance to get the correct shipment.
       * Set up another insertion that takes the smallest angle and the smallest distance */
      distance = calcDist(depotX, temp.getXCoord(), depotY, temp.getYCoord());

      if (isDiagnostic) {
	System.out.println("  " + distance);
      }

      //check if this shipment should be tracked
      if (foundShipment == null) { //this is the first shipment being checked
	foundShipment = temp;
	foundDistance = distance;
      }
      else {
	if (distance < foundDistance) { //found an angle that is less
	  foundShipment = temp;
	  foundDistance = distance;
	}
      }
      temp = temp.getTOPNext();
    }
    return foundShipment; //stub
  }

  //The WhoAmI methods gives the id of the assigned object
  //It is a static method so that it can be accessed without creating an object
  public static String WhoAmI() {
    return ("Selection Type: Closest euclidean distance to depot");
  }

}

/**
 * <p>Title: SmallestPolarAngleToDepot</p>
 * <p>Description: Select the shipment with the smallest polar coordinate angle to the depot</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Slippery Rock University</p>
 *
 * @author Sam R. Thangiah
 * @version 1.0
 */
class SmallestPolarAngleToDepot
    extends TOPShipmentLinkedList {
  public TOPShipment getSelectShipment(TOPDepotLinkedList currDepotLL,
				       TOPDepot currDepot,
				       TOPShipmentLinkedList currShipLL,
				       TOPShipment currShip) {
    //currDepotLL is the depot linked list of the problem
    //currDepot is the depot under consideration
    //currShipLL is the set of avaialble shipments
    boolean isDiagnostic = false;
    //VRPShipment temp = (TOPShipment) getHead(); //point to the first shipment
    TOPShipment temp = currShipLL.getTOPHead(); //point to the first shipment
    TOPShipment foundShipment = null; //the shipment found with the criteria
    double angle;
    double foundAngle = 360; //initial value
    double depotX;
	//double distance;
    //double foundDistance = 200; //initial distance
    double depotY;
    int type = 2;

    //Get the X and Y coordinate of the depot
    depotX = currDepot.getXCoord();
    depotY = currDepot.getYCoord();

    while (temp != null) {
      if (isDiagnostic) {
	System.out.print("Shipment " + temp.getIndex() + " ");

	if ( ( (temp.getXCoord() - depotX) >= 0) &&
	    ( (temp.getYCoord() - depotX) >= 0)) {
	  System.out.print("Quadrant I ");
	}
	else if ( ( (temp.getXCoord() - depotX) <= 0) &&
		 ( (temp.getYCoord() - depotY) >= 0)) {
	  System.out.print("Quadrant II ");
	}
	else if ( ( (temp.getXCoord()) <= (0 - depotX)) &&
		 ( (temp.getYCoord() - depotY) <= 0)) {
	  System.out.print("Quadrant III ");
	}
	else if ( ( (temp.getXCoord() - depotX) >= 0) &&
		 ( (temp.getYCoord() - depotY) <= 0)) {
	  System.out.print("Quadrant VI ");
	}
	else {
	  System.out.print("No Quadrant");
	}
      }

      //if the shipment is assigned, skip it
      if (temp.getIsAssigned()) {
	if (isDiagnostic) {
	  System.out.println("has been assigned");
	}

	temp = temp.getTOPNext();

	continue;
      }
      else if (temp.getUnreachable() == true) {
	if (isDiagnostic) {
	  System.out.println("is not reachable");
	}

	temp = temp.getTOPNext();

	continue;

      }
      else if (temp.getChecked() == true) {
        if (isDiagnostic) {
          System.out.println("is outside the target area");
        }

        temp = temp.getTOPNext();

        continue;
      }


      angle = calcPolarAngle(depotX, depotX, temp.getXCoord(),
			     temp.getYCoord());

      if (isDiagnostic) {
	System.out.println("  " + angle);
      }

      //check if this shipment should be tracked
      if (foundShipment == null) { //this is the first shipment being checked
	foundShipment = temp;
	foundAngle = angle;
      }
      else {
	if (angle < foundAngle) { //found an angle that is less
	  foundShipment = temp;
	  foundAngle = angle;
	}
      }

      temp = temp.getTOPNext();
    }
    return foundShipment; //stub
  }

  //The WhoAmI methods gives the id of the assigned object
  //It is a static method so that it can be accessed without creating an object
  public static String WhoAmI() {
    return ("Selection Type: Smallest polar angle to the depot");
  }
}

/**
 * <p>Title: SmallestPolarAngleShortestDistToDepot</p>
 * <p>Description: Select the shipment with the smallest polar coordinate angle and the
 * shortest distance to the depot</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Slippery Rock University</p>
 *
 * @author Sam R. Thangiah
 * @version 1.0
 */
class SmallestPolarAngleShortestDistToDepot
    extends TOPShipmentLinkedList {
  public TOPShipment getSelectShipment(TOPDepotLinkedList currDepotLL,
				       TOPDepot currDepot,
				       TOPShipmentLinkedList currShipLL,
				       TOPShipment currShip) {
    //currDepotLL is the depot linked list of the problem
    //currDepot is the depot under consideration
    //currShipLL is the set of avaialble shipments
    boolean isDiagnostic = false;
    //VRPShipment temp = (TOPShipment) getHead(); //point to the first shipment
    TOPShipment temp = currShipLL.getTOPHead(); //point to the first shipment
    TOPShipment foundShipment = null; //the shipment found with the criteria
    double angle;
    double foundAngle = 360; //initial value
    double distance;
    double foundDistance = 200; //initial distance
    double depotX;
	double depotY;
    int type = 2;

    //Get the X and Y coordinate of the depot
    depotX = currDepot.getXCoord();
    depotY = currDepot.getYCoord();

    while (temp != null) {
      if (isDiagnostic) {
	System.out.print("Shipment " + temp.getIndex() + " ");

	if ( ( (temp.getXCoord() - depotX) >= 0) &&
	    ( (temp.getYCoord() - depotX) >= 0)) {
	  System.out.print("Quadrant I ");
	}
	else if ( ( (temp.getXCoord() - depotX) <= 0) &&
		 ( (temp.getYCoord() - depotY) >= 0)) {
	  System.out.print("Quadrant II ");
	}
	else if ( ( (temp.getXCoord()) <= (0 - depotX)) &&
		 ( (temp.getYCoord() - depotY) <= 0)) {
	  System.out.print("Quadrant III ");
	}
	else if ( ( (temp.getXCoord() - depotX) >= 0) &&
		 ( (temp.getYCoord() - depotY) <= 0)) {
	  System.out.print("Quadrant VI ");
	}
	else {
	  System.out.print("No Quadrant");
	}
      }

      //if the shipment is assigned, skip it
      if (temp.getIsAssigned()) {
	if (isDiagnostic) {
	  System.out.println("has been assigned");
	}

	temp = temp.getTOPNext();

	continue;
      }
      else if (temp.getUnreachable() == true) {
	if (isDiagnostic) {
	  System.out.println("is not reachable");
	}

	temp = temp.getTOPNext();

	continue;
      }
      else if (temp.getChecked() == true) {
        if (isDiagnostic) {
          System.out.println("is outside the target area");
        }

        temp = temp.getTOPNext();

        continue;
      }

      distance = calcDist(depotX, temp.getXCoord(), depotY, temp.getYCoord());
      angle = calcPolarAngle(depotX, depotX, temp.getXCoord(),
			     temp.getYCoord());

      if (isDiagnostic) {
	System.out.println("  " + angle);
      }

      //check if this shipment should be tracked
      if (foundShipment == null) { //this is the first shipment being checked
	foundShipment = temp;
	foundAngle = angle;
	foundDistance = distance;
      }
      else {
	//if angle and disnace are smaller than what had been found
	//if (angle <= foundAngle && distance <= foundDistance) {
	if (angle + distance <= foundAngle + foundDistance) {
	  //if ((angle*.90)+ (distance * 0.1)  <= (foundAngle*0.9) + (foundDistance*0.1)) {
	  foundShipment = temp;
	  foundAngle = angle;
	  foundDistance = distance;
	}
      }
      temp = temp.getTOPNext();
    }
    return foundShipment; //stub
  }

  //The WhoAmI methods gives the id of the assigned object
  //It is a static method so that it can be accessed without creating an object
  public static String WhoAmI() {
    return ("Selection Type: Smallest polar angle to the depot");
  }

}
