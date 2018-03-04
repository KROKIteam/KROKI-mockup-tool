  package graphedit.strategy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import graphedit.model.components.Connector;
import graphedit.model.components.LinkNode;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;

public class RightAngledStrategy implements LinkStrategy{

	double xMinD,xMaxD,yMinD,yMaxD,xMinS,xMaxS,yMinS,yMaxS;
	Point2D sourcePosition,destinationPosition;
	@Override
	public ArrayList<LinkNode> setLinkNodes (ArrayList<LinkNode> nodes) {
		ArrayList<LinkNode> retNodes=new ArrayList<LinkNode>();
		Point2D firstPosition;
		Point2D secondPosition;
		Point2D currentNodePosition = null,previousNodePosition;
		boolean addAdditionalPoint, addNode,addNextNode=true,addCurrentNode;
		double theta,minDistance=10;
		Point2D p1,p2,p3;
		Connector destinationConnector=(Connector)nodes.get(nodes.size()-1);
		destinationPosition=(Point2D) destinationConnector.getProperty(LinkNodeProperties.POSITION);
		xMinD=destinationPosition.getX()-destinationConnector.getMinXRelative();
		xMaxD=destinationPosition.getX()+destinationConnector.getMaxXRelative();
		yMinD=destinationPosition.getY()-destinationConnector.getMinYRelative();
		yMaxD=destinationPosition.getY()+destinationConnector.getMaxYRelative();
		Connector sourceConnector=(Connector)nodes.get(0);
		sourcePosition=(Point2D) sourceConnector.getProperty(LinkNodeProperties.POSITION);
		xMinS=sourcePosition.getX()-sourceConnector.getMinXRelative();
		xMaxS=sourcePosition.getX()+sourceConnector.getMaxXRelative();
		yMinS=sourcePosition.getY()-sourceConnector.getMinYRelative();
		yMaxS=sourcePosition.getY()+sourceConnector.getMaxYRelative();

		if (nodes.size()>2){
		for (int i=0;i<nodes.size()-1;i++){
			addCurrentNode=addNextNode;
			previousNodePosition=currentNodePosition;
			if ( addNextNode){
				firstPosition=(Point2D) nodes.get(i).getProperty(LinkNodeProperties.POSITION);
				secondPosition=(Point2D) nodes.get(i+1).getProperty(LinkNodeProperties.POSITION);
			}
			else {
				firstPosition=currentNodePosition;
				secondPosition=(Point2D) nodes.get(i+1).getProperty(LinkNodeProperties.POSITION);
			}
			if (firstPosition.getX()>secondPosition.getX()){
				if (firstPosition.getY()>secondPosition.getY())
					theta=Math.atan((firstPosition.getX()-secondPosition.getX())/(firstPosition.getY()-secondPosition.getY()));	
				else if (firstPosition.getY()<secondPosition.getY())
					theta=Math.atan((firstPosition.getX()-secondPosition.getX())/(secondPosition.getY()-firstPosition.getY()));
				else 
					theta=-1; //ne treba dodati novu tacku ako su Y koordinate vec jednake
			}
			else if (firstPosition.getX()<secondPosition.getX()){
				if (firstPosition.getY()>secondPosition.getY())
					theta=Math.atan((secondPosition.getX()-firstPosition.getX())/(firstPosition.getY()-secondPosition.getY()));	
				else if (firstPosition.getY()<secondPosition.getY())
					theta=Math.atan((secondPosition.getX()-firstPosition.getX())/(secondPosition.getY()-firstPosition.getY()));
				else 
					theta=-1; //ne treba dodati novu tacku ako su Y koordinate vec jednake
			}
			else 
				theta=-1;
			
			
			if (theta>-1){
				if (theta>(Math.PI/4)){
						currentNodePosition=new Point2D.Double(secondPosition.getX(),firstPosition.getY());
						if (inDestinationElement(currentNodePosition) || inSourceElement(currentNodePosition))
							currentNodePosition.setLocation(firstPosition.getX(),secondPosition.getY());
				}
				else {
						currentNodePosition=new Point2D.Double(firstPosition.getX(),secondPosition.getY());
						if (inDestinationElement(currentNodePosition) || inSourceElement(currentNodePosition))
							currentNodePosition.setLocation(secondPosition.getX(),firstPosition.getY());
				}
				addAdditionalPoint=true;
			}
			else
				addAdditionalPoint=false;
			
			//neka vrsta optimizacije, da se ne dodaje ako su medjusobna rastojanje premala
			if (currentNodePosition!=null)// && i<nodes.size()-2)
				if (currentNodePosition.getX()-secondPosition.getX()<minDistance)
					addNextNode=false;
				else if (currentNodePosition.getY()-secondPosition.getY()<minDistance)
					addNextNode=false;
				else 
					addNextNode=true;
			else  
				addNextNode=true;
			
			if (addCurrentNode){
			addNode=true;
			if (previousNodePosition!=null && addAdditionalPoint && i>0  )
				if(currentNodePosition.getX()==previousNodePosition.getX() || currentNodePosition.getY()==previousNodePosition.getY())
					addNode=false;
			}
			else 
				addNode=false;
				
			if (addNode)
				retNodes.add(nodes.get(i));
			if (addAdditionalPoint)
				retNodes.add(new LinkNode(currentNodePosition));

			if (retNodes.size()>=3){
				p1=(Point2D) retNodes.get(retNodes.size()-1).getProperty(LinkNodeProperties.POSITION);
				p2=(Point2D) retNodes.get(retNodes.size()-2).getProperty(LinkNodeProperties.POSITION);
				p3=(Point2D) retNodes.get(retNodes.size()-3).getProperty(LinkNodeProperties.POSITION);
				if ((p1.getX()==p2.getX() && p2.getX()==p3.getX()) || (p1.getY()==p2.getY() && p2.getY()==p3.getY()))
						retNodes.remove(retNodes.size()-2);
			}	
		 }
		}
		else{
			firstPosition=(Point2D) nodes.get(0).getProperty(LinkNodeProperties.POSITION);
			secondPosition=(Point2D) nodes.get(1).getProperty(LinkNodeProperties.POSITION);
			retNodes.add(nodes.get(0));
			
			
			
			if (Math.abs(firstPosition.getX()-secondPosition.getX())<minDistance){
				destinationPosition.setLocation(sourcePosition.getX(),destinationPosition.getY());
				destinationConnector.setRelativePositions(destinationPosition);
				destinationConnector.setPercents(destinationPosition);					
			}
			else if (Math.abs(firstPosition.getY()-secondPosition.getY())<minDistance){
				destinationPosition.setLocation(destinationPosition.getX(),sourcePosition.getY());
				destinationConnector.setRelativePositions(destinationPosition);
				destinationConnector.setPercents(destinationPosition);		
			}
			else{

				if ((firstPosition.getX()<xMinD || firstPosition.getX()>xMaxD ) && (firstPosition.getY()<yMinD || firstPosition.getY()>yMaxD))
					if (secondPosition.getX()>xMinS && secondPosition.getX()<xMaxS)
						retNodes.add(new LinkNode(new Point2D.Double(firstPosition.getX(),secondPosition.getY())));
					else 
						retNodes.add(new LinkNode(new Point2D.Double(secondPosition.getX(),firstPosition.getY())));
				else {
	
					double xDistance=Math.abs(firstPosition.getX()-secondPosition.getX());
					double yDistance=Math.abs(firstPosition.getY()-secondPosition.getY());
					double xPosition,yPosition;
					if (firstPosition.getX()>secondPosition.getX())
						xPosition=xMinS - (xMinS-xMaxD)/2; 
					else 
						xPosition=xMaxS + (xMinD-xMaxS)/2; 
					if (firstPosition.getY()>secondPosition.getY())
						yPosition=yMinS - (yMinS-yMaxD)/2; 
					else 
						yPosition=yMaxS + (yMinD-yMaxS)/2; 
					
					if (xDistance>yDistance){
						retNodes.add(new LinkNode(new Point2D.Double(xPosition,firstPosition.getY())));
						retNodes.add(new LinkNode(new Point2D.Double(xPosition,secondPosition.getY())));
					}
					else {
						retNodes.add(new LinkNode(new Point2D.Double(firstPosition.getX(),yPosition)));
						retNodes.add(new LinkNode(new Point2D.Double(secondPosition.getX(),yPosition)));
					}
				}
			}
		}
		retNodes.add(nodes.get(nodes.size()-1));
		return retNodes;
		
	}
	
	private boolean inSourceElement(Point2D position){
		if ((position.getX()>xMinS && position.getX()<xMaxS) && position.getY()>yMinS && position.getY()<yMaxS)
			return true;
		return false;
	}
	private boolean inDestinationElement(Point2D position){
		if ((position.getX()>xMinD && position.getX()<xMaxD) && (position.getY()>yMinD && position.getY()<yMaxD))
			return true;
		return false;
	}

}

