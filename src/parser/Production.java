package parser;

import java.util.Vector;

//����ʽ���Ҳ���һ��string���飬��Ϊ�ķ����ַ����Ȳ�ֹ1
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
