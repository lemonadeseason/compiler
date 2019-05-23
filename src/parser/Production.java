package parser;

import java.util.Vector;

//产生式的右部是一个string数组，因为文法的字符长度不止1
public class Production {
	private String left;
	private Vector<String> right;
	public Production(String left,Vector<String> right)
	{
		this.left = left;
		this.right = right;
	}
	public String getLeft()
	{
		return left;
	}
	public Vector<String> getRight()
	{
		return right;
	}
	public String toString()
	{
		return left+"->"+right;
	}
}
