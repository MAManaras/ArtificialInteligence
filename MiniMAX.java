
import java.util.*;


public class MiniMAX
{
    private final double MAX=Double.MAX_VALUE;
    private final double MIN=-Double.MAX_VALUE;
    private int maxDepth;
    private int myColor;
    public  int prunnedCounter;
    Runtime r ;

    public MiniMAX(int maxDepth,int myColor)
    {
    	this.prunnedCounter=0;
        this.maxDepth=maxDepth;
        this.setMyColor(myColor);
 
    }

    public String alpha_beta(World w)
    {
        Move m=max_Value(w,MIN,MAX,0);
        System.out.println(m.getFromX()+" "+m.getFromY()+" "+m.getX()+" "+m.getY() + " Value : "+m.getValue());
        String output = String.valueOf(m.getFromX())+String.valueOf(m.getFromY())+String.valueOf(m.getX())+String.valueOf(m.getY());
       // System.out.println("PRUNNED COUNTER : "+prunnedCounter);
        return output;
        
    }
    private Move max_Value(World w,double a,double b,int depth)
    {
    	
   
        if((w.terminal_test())||(depth==maxDepth))
        {
        	 Move lastMove=new Move(w.getLastMove().getFromX(),w.getLastMove().getFromY(),w.getLastMove().getX(),w.getLastMove().getY(),w.evaluate());
  
             return lastMove;
        }
        
        List<World> successors = new ArrayList<World>();
        successors =w.getSuccessors(myColor);
        
        
        Collections.sort(successors, new compareMaxSuccessors());
        
        
        Move maxMove=new Move(MIN);
        for(World child : successors)
        {
            Move move=min_Value(child,a,b,depth+1);
            if(move.getValue()>=maxMove.getValue())
            {
                if(move.getValue()>=b){this.prunnedCounter+=1;return move;} 
                maxMove.setFromX(child.getLastMove().getFromX());
                maxMove.setFromY(child.getLastMove().getFromY());
                maxMove.setX(child.getLastMove().getX());
                maxMove.setY(child.getLastMove().getY());
                maxMove.setValue(move.getValue());

        
            }
            a=Math.max(a,move.getValue());
        }
       
        return maxMove;
    
    }
    private Move min_Value(World w,double a,double b,int depth)
    {
    	
    	

        if((w.terminal_test())||(depth==maxDepth))
        {
   
        	 Move lastMove=new Move(w.getLastMove().getFromX(),w.getLastMove().getFromY(),w.getLastMove().getX(),w.getLastMove().getY(),w.evaluate());
       
             return lastMove;
        }
        
        
        List<World> successors = new ArrayList<World>();

        if(myColor==0)
        	successors =w.getSuccessors(1);
        else
        	successors =w.getSuccessors(0);
        
   
        
        Collections.sort(successors, new compareMinSuccessors());
        
        Move minMove=new Move(MAX);
        for(World child : successors)
        {
            Move move=max_Value(child,a,b,depth+1);
            if(move.getValue()<=minMove.getValue())
            {
                if(move.getValue()<=a) {this.prunnedCounter+=1;return move;} 
                minMove.setFromX(child.getLastMove().getFromX());
                minMove.setFromY(child.getLastMove().getFromY());
                minMove.setX(child.getLastMove().getX());
                minMove.setY(child.getLastMove().getY());
                minMove.setValue(move.getValue());

            }
            b=Math.min(b,move.getValue());
        }
     
        return minMove;

    }

	public int getColor() {
		return myColor;
	}

	public void setColor(int myColor) {
		this.myColor = myColor;
	}
	
}

class compareMaxSuccessors implements Comparator<World> {
	
	public int compare(World w1, World w2) {
	  if (w1.getLastMove().getValue() < w2.getLastMove().getValue()) return -1;
      if (w1.getLastMove().getValue() > w2.getLastMove().getValue()) return 1;
      return 0;
	}
}
class compareMinSuccessors implements Comparator<World> {

	public int compare(World w1, World w2) {
	  if (w1.getLastMove().getValue() < w2.getLastMove().getValue()) return 1;
      if (w1.getLastMove().getValue() > w2.getLastMove().getValue()) return -1;
      return 0;
	}
}
