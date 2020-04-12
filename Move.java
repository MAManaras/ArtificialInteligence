public class Move
{
	private int fromX;
	private int fromY;
	private int x;
	private int y;
	private double value;
	
	public Move()
	{
		x = -1;
		y = -1;
		value = 0;
	}
	public Move(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.value = -1;
	}
	
	public Move(double value)
	{
		this.x = -1;
		this.y = -1;
		this.value = value;
	}
	
	public Move(int x, int y, int value)
	{
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public Move(int fromX , int fromY ,int x, int y, double value){
		
		this.fromX = fromX;
		this.fromY = fromY;
		this.x = x;
		this.y = y;
		this.value = value;
		
	}
	public Move(int fromX , int fromY ,int x, int y){
		
		this.fromX = fromX;
		this.fromY = fromY;
		this.x = x;
		this.y = y;
		
	}
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void setValue(double value)
	{
		this.value = value;
	}
	public int getFromX() {
		return fromX;
	}
	public void setFromX(int fromX) {
		this.fromX = fromX;
	}
	public int getFromY() {
		return fromY;
	}
	public void setFromY(int fromY) {
		this.fromY = fromY;
	}
}