import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class World
{
	private String[][] board = null;
	private int rows = 7;
	private int columns = 5;
	private int myColor = 0;
	private int initColor =0;
	private List<String> availableMoves;
	private Map<String,Double> movez_n_Valuez; 
	private int rookBlocks = 3;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
	private int nTurns = 0;
	private int nBranches = 0;
	private int noPrize = 9;
	private Move lastMove = null ;
	private double maxiMizerScore = 0.0;
	private double miniMizerScore = 0.0;

	private boolean whiteKingIsUp = false;
	private boolean blackKingIsUp = false;
	
	public World()
	{
		board = new String[rows][columns];
		
		/* represent the board
		
		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP
		*/
		
		// initialization of the board
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = " ";
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++)
			board[1][j] = "BP";
		
		board[0][0] = "BP";
		board[0][columns-1] = "BP";
		
		// black rooks
		board[0][1] = "BR";
		board[0][columns-2] = "BR";
		
		// black king
		board[0][columns/2] = "BK";
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++)
			board[rows-2][j] = "WP";
		
		board[rows-1][0] = "WP";
		board[rows-1][columns-1] = "WP";
		
		// white rooks
		board[rows-1][1] = "WR";
		board[rows-1][columns-2] = "WR";
		
		// white king
		board[rows-1][columns/2] = "WK";
		
		// setting the prizes
		for(int j=0; j<columns; j++)
			board[rows/2][j] = "P";
		
		availableMoves = new ArrayList<String>();
	}
	
	private void whiteMoves()
	{
		availableMoves = new ArrayList<String>();
		movez_n_Valuez= new HashMap<String,Double>();
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
		double moveValue = 0.0; //modified
		
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if there is not a white chess part in this position then keep on searching
				if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					// check if it can move towards the last row
					if(i-1 == 0 && (Character.toString(board[i-1][j].charAt(0)).equals(" ") 
							         || Character.toString(board[i-1][j].charAt(0)).equals("P")))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
						       Integer.toString(i-1) + Integer.toString(j);
						
						moveValue = 1.0; //modified
						
						if (Character.toString(board[i-1][j].charAt(0)).equals("P")) //modified
							moveValue = 1.7;
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue));
						continue;
					}
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i-1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-1) + Integer.toString(j);
						
						moveValue = 0.1; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue = 0.7;
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move),new Double(moveValue)); //modified
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j-1].charAt(0));
						
						if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
							continue;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-1) + Integer.toString(j-1);
						
						
						if(firstLetter.equals("B")){ //modified
							
							String secondLetterTemp = Character.toString(board[i-1][j-1].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j+1].charAt(0));
						
						if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
							continue;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-1) + Integer.toString(j+1);
						
						if(firstLetter.equals("B")){ //modified
							
							String secondLetterTemp = Character.toString(board[i-1][j+1].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						moveValue = 0.0; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue=0.7;
						
						if(firstLetter.equals("B")){ //modified
							
							String secondLetterTemp = Character.toString(board[i-(k+1)][j].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
							
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						moveValue = 0.0; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue=0.7;
						
						if(firstLetter.equals("B")){ //modified
							
							String secondLetterTemp = Character.toString(board[i+(k+1)][j].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						moveValue = 0.0; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue=0.7;
						
						if(firstLetter.equals("B")){ //modified
							
							String secondLetterTemp = Character.toString(board[i][j-(k+1)].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						moveValue = 0.0; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue=0.7;
						
						if(firstLetter.equals("B")){ //modified
							
							String secondLetterTemp = Character.toString(board[i][j+(k+1)].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							moveValue = 0.0;
							
							if(firstLetter.equals("P")) //modified
								moveValue=0.7;
							
							if(firstLetter.equals("B")){ //modified
								
								String secondLetterTemp = Character.toString(board[i-1][j].charAt(1));
								
								if(secondLetterTemp.equals("P"))
									moveValue = 1.0;
								else if(secondLetterTemp.equals("R"))
									moveValue = 3.0;
								else if(secondLetterTemp.equals("K")) 
									moveValue = 7.0;
							}
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);	
							movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							moveValue = 0.0;
							
							if(firstLetter.equals("P")) //modified
								moveValue=0.7;
							
							if(firstLetter.equals("B")){ //modified
								
								String secondLetterTemp = Character.toString(board[i+1][j].charAt(1));
								
								if(secondLetterTemp.equals("P"))
									moveValue = 1.0;
								else if(secondLetterTemp.equals("R"))
									moveValue = 3.0;
								else if(secondLetterTemp.equals("K")) 
									moveValue = 7.0;
							}
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);
							movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							moveValue = 0.0;
							
							if(firstLetter.equals("P")) //modified
								moveValue=0.7;
							
							if(firstLetter.equals("B")){ //modified
								
								String secondLetterTemp = Character.toString(board[i][j-1].charAt(1));
								
								if(secondLetterTemp.equals("P"))
									moveValue = 1.0;
								else if(secondLetterTemp.equals("R"))
									moveValue = 3.0;
								else if(secondLetterTemp.equals("K")) 
									moveValue = 7.0;
							}
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);	
							movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							moveValue = 0.0;
							
							if(firstLetter.equals("P")) //modified
								moveValue=0.7;
							
							if(firstLetter.equals("B")){ //modified
								
								String secondLetterTemp = Character.toString(board[i][j+1].charAt(1));
								
								if(secondLetterTemp.equals("P"))
									moveValue = 1.0;
								else if(secondLetterTemp.equals("R"))
									moveValue = 3.0;
								else if(secondLetterTemp.equals("K")) 
									moveValue = 7.0;
							}
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);	
							movez_n_Valuez.put(new String(move), new Double(moveValue)); //modified
						}
					}
				}			
			}	
		}
	}
	
	private void blackMoves()
	{
		availableMoves = new ArrayList<String>();
		movez_n_Valuez= new HashMap<String,Double>();
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
		double moveValue = 0.0; //modified
		
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a black chess part in this position then keep on searching
				if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					// check if it is at the last row
					if(i+1 == rows-1 && (Character.toString(board[i+1][j].charAt(0)).equals(" ")
										  || Character.toString(board[i+1][j].charAt(0)).equals("P")))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
						       Integer.toString(i+1) + Integer.toString(j);
						
						moveValue = 1.0; //modified
						
						if(Character.toString(board[i+1][j].charAt(0)).equals("P")) //modified
							moveValue = 1.7;
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue));
						continue;
					}
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i+1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+1) + Integer.toString(j);
						
						moveValue = 0.1; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue = 0.7;
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue));
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j-1].charAt(0));
						
						if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
							continue;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+1) + Integer.toString(j-1);
						
						if(firstLetter.equals("W")){ //modified
							
							String secondLetterTemp = Character.toString(board[i+1][j-1].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue));
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j+1].charAt(0));
						
						if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
							continue;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+1) + Integer.toString(j+1);
						
						if(firstLetter.equals("W")){ //modified
							
							String secondLetterTemp = Character.toString(board[i+1][j+1].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue));
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						moveValue = 0.0; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue=0.7;
						
						if(firstLetter.equals("W")){ //modified
							
							String secondLetterTemp = Character.toString(board[i-(k+1)][j].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue));
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						moveValue = 0.0; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue=0.7;
						
						if(firstLetter.equals("W")){ //modified
							
							String secondLetterTemp = Character.toString(board[i+(k+1)][j].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue));
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						moveValue = 0.0; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue=0.7;
						
						if(firstLetter.equals("W")){ //modified
							
							String secondLetterTemp = Character.toString(board[i][j-(k+1)].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue));
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						moveValue = 0.0; //modified
						
						if(firstLetter.equals("P")) //modified
							moveValue=0.7;
						
						if(firstLetter.equals("W")){ //modified
							
							String secondLetterTemp = Character.toString(board[i][j+(k+1)].charAt(1));
							
							if(secondLetterTemp.equals("P"))
								moveValue = 1.0;
							else if(secondLetterTemp.equals("R"))
								moveValue = 3.0;
							else if(secondLetterTemp.equals("K")) 
								moveValue = 7.0;
						}
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						movez_n_Valuez.put(new String(move), new Double(moveValue));
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							moveValue = 0.0;
							
							if(firstLetter.equals("P")) //modified
								moveValue=0.7;
							
							if(firstLetter.equals("W")){ //modified
								
								String secondLetterTemp = Character.toString(board[i-1][j].charAt(1));
								
								if(secondLetterTemp.equals("P"))
									moveValue = 1.0;
								else if(secondLetterTemp.equals("R"))
									moveValue = 3.0;
								else if(secondLetterTemp.equals("K")) 
									moveValue = 7.0;
							}
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);
							movez_n_Valuez.put(new String(move), new Double(moveValue));
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							moveValue = 0.0;
							
							if(firstLetter.equals("P")) //modified
								moveValue=0.7;
							
							if(firstLetter.equals("W")){ //modified
								
								String secondLetterTemp = Character.toString(board[i+1][j].charAt(1));
								
								if(secondLetterTemp.equals("P"))
									moveValue = 1.0;
								else if(secondLetterTemp.equals("R"))
									moveValue = 3.0;
								else if(secondLetterTemp.equals("K")) 
									moveValue = 7.0;
							}
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);
							movez_n_Valuez.put(new String(move), new Double(moveValue));
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							moveValue = 0.0;
							
							if(firstLetter.equals("P")) //modified
								moveValue=0.7;
							
							if(firstLetter.equals("W")){ //modified
								
								String secondLetterTemp = Character.toString(board[i][j-1].charAt(1));
								
								if(secondLetterTemp.equals("P"))
									moveValue = 1.0;
								else if(secondLetterTemp.equals("R"))
									moveValue = 3.0;
								else if(secondLetterTemp.equals("K")) 
									moveValue = 7.0;
							}
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);
							movez_n_Valuez.put(new String(move), new Double(moveValue));
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							moveValue = 0.0;
							
							if(firstLetter.equals("P")) //modified
								moveValue=0.7;
							
							if(firstLetter.equals("W")){ //modified
								
								String secondLetterTemp = Character.toString(board[i][j+1].charAt(1));
								
								if(secondLetterTemp.equals("P"))
									moveValue = 1.0;
								else if(secondLetterTemp.equals("R"))
									moveValue = 3.0;
								else if(secondLetterTemp.equals("K")) 
									moveValue = 7.0;
							}
							
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);
							movez_n_Valuez.put(new String(move), new Double(moveValue));
						}
					}
				}			
			}	
		}
	}
	
	
	public double getAvgBFactor()
	{
		return nBranches / (double) nTurns;
	}
	
	public void makeMove(int x1, int y1, int x2, int y2, int prizeX, int prizeY)
	{
		String chesspart = Character.toString(board[x1][y1].charAt(1));
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				board[x2][y2] = " ";	// in a case an opponent's chess part has just been captured
				board[x1][y1] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			board[x2][y2] = board[x1][y1];
			board[x1][y1] = " ";
		}
		
		// check if a prize has been added in the game
		if(prizeX != noPrize)
			board[prizeX][prizeY] = "P";
	}
	
	
	
	
	public ArrayList<World> getSuccessors(int player ){
		
		availableMoves = new ArrayList<String>();
		
		if(player == 0)		// I am the white playerf
			this.whiteMoves();
		else					// I am the black player
			this.blackMoves();

		
		// keeping track of the branch factor
		nTurns++;
		nBranches += availableMoves.size();
		
		ArrayList<World> successors = new ArrayList<World>();
	    for (String move : availableMoves) {
	

	    	World successor = new World();
	    	successor.setMyInitColor(new Integer(initColor));
	        successor.board = cloneArray(this.board);
	        successor.maxiMizerScore=new Double(this.maxiMizerScore);
        	successor.miniMizerScore=new Double(this.miniMizerScore);
        	
        	
	        successor.makeMiniMaxMove(Character.getNumericValue(move.charAt(0)),Character.getNumericValue(move.charAt(1)),Character.getNumericValue(move.charAt(2)),Character.getNumericValue(move.charAt(3)));
	        
	        
	       
	        if(initColor==0){
	    
	        	
	        	if (player == 0) {
	      
		        	successor.maxiMizerScore+=movez_n_Valuez.get(move);
	        	}
	        	else{

	        		successor.miniMizerScore+=movez_n_Valuez.get(move);
	        	}
	        }
	        else
	        {

	        	if (player == 1){ 
	
		        	successor.maxiMizerScore+=movez_n_Valuez.get(move);
	        	}
	        	else{
	  
	        		successor.miniMizerScore+=movez_n_Valuez.get(move);
	        	}
	        	
	        }
	        
	       
	        successor.setLastMove(move,movez_n_Valuez.get(move));
	        successors.add(successor);
	
	    }
	
	    return successors;
		
	}
	
	public void makeMiniMaxMove(int x1, int y1, int x2, int y2)
	{
		
		
		String chesspart = Character.toString(board[x1][y1].charAt(1));
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				board[x2][y2] = " ";	// in a case an opponent's chess part has just been captured
				board[x1][y1] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			board[x2][y2] = board[x1][y1];
			board[x1][y1] = " ";
		}
		
	}
	

	public double evaluate(){

			int wRook = 0;
			int bRook = 0;
			int whitePieces = 0;
			int blackPieces = 0;
			
			for(int i=0; i<rows; i++)
			{
				for(int j=0; j<columns; j++)
				{
					
					String first = Character.toString(board[i][j].charAt(0));
					if(first.equals("W")){
						if(Character.toString(board[i][j].charAt(1)).equals("R")){ wRook += 3; }
						whitePieces+=1;
					}
					else if(first.equals("B")){
						if(Character.toString(board[i][j].charAt(1)).equals("R")){ bRook += 3; }
						blackPieces+=1;
					}
				}
			}
			
			double value=0.0;
			
			if(initColor==0)
			{
				value = maxiMizerScore - miniMizerScore + whitePieces - blackPieces;
				if(whiteKingIsUp){value+=5;}
				if(blackKingIsUp){value-=5;}
				value += wRook -bRook;
			}
			else
			{
				value = maxiMizerScore - miniMizerScore - whitePieces  + blackPieces;
				if(whiteKingIsUp){value-=5;}
				if(blackKingIsUp){value+=5;}
				value += bRook -wRook;
			}
			return value;
		}
	
	public boolean terminal_test(){
		
		boolean termination = false;
		boolean whitePartUp = false;
		boolean blackPartUp = false;
		
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				
				String firstLetter = Character.toString(board[i][j].charAt(0));
				
				
				if(firstLetter.equals("W")){
					
					
					String secondLetter = Character.toString(board[i][j].charAt(1));
					if(secondLetter.equals("K"))
						whiteKingIsUp = true;
					else
						whitePartUp = true;
			
				}
				if (firstLetter.equals("B")){
					
					String secondLetter = Character.toString(board[i][j].charAt(1));
					if(secondLetter.equals("K"))
						blackKingIsUp = true;
					else
						blackPartUp = true;
					
				}
			}
		}
		
		if(!whiteKingIsUp){
			
			termination = true;
		}
		
		if(!blackKingIsUp){
			
	
			termination = true;
		}
		
		if((whiteKingIsUp && blackKingIsUp) && !(whitePartUp || blackPartUp)){ //draw
			

			termination = true;
		}
			
		
		return termination;
	}
	
	public static String[][] cloneArray(String[][] src) {
	    int length = src.length;
	    String[][] target = new String[length][src[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(src[i], 0, target[i], 0, src[i].length);
	    }
	    return target;
	}
	
	public void setMyInitColor(int myInitColor){
		this.initColor=myInitColor;
	}
	
	public void setLastMove(String move,double value){
		
		this.lastMove = new Move(Character.getNumericValue(move.charAt(0)),Character.getNumericValue(move.charAt(1)),Character.getNumericValue(move.charAt(2)),Character.getNumericValue(move.charAt(3)),value);
	}
	
	public Move getLastMove(){
		
		return this.lastMove;
	}
	
	public void setColor(int myColor)
	{
		this.myColor = myColor;
	}
	
	public int getColor(){
		return this.myColor;
	}
}//class
