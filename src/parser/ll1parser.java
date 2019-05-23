//读文法   右部 string数组
//写以某一非终结符为左部的所有产生式
//判断是否为终结符
package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ll1parser {
	private ArrayList<Production> productions;     //所分析文法的所有产生式
	private Map<String,HashSet<String>>firstSetSingle;   //非终结符对应的first集,后续应该把终结符的也加进去
	private HashSet<String> nTerminals;      //终结符
	private HashSet<String> terminals;           //非终结符
	public ll1parser(String path)  
	{
		//以path文件中的内容为程序设计语言的文法
		nTerminals = new HashSet<String>();
		terminals = new HashSet<String>();
		productions = new ArrayList<Production>();
		firstSetSingle =  new  HashMap<String,HashSet<String>>();
		File file = new File(path);  
        BufferedReader reader = null;  
        try {   
            reader = new BufferedReader(new FileReader(file));  
            String s = null;  
            while ((s = reader.readLine()) != null) {
            	//假定 “->”  是紧挨着的
            	int bound = s.indexOf("-");
                String left = s.substring(0, bound);
                left=left.trim();//  产生式左部
                String rightStr = s.substring(bound+2,s.length());
                //用空格将右部切割
                String [] arr = rightStr.split("\\s+");
                Vector<String> right = new Vector<String>();     //产生式的右部
                for(String element:arr)
                {
                	right.addElement(element);
                	terminals.add(element);    //暂时全部加入终结符set，最后去除非
                }
                if(!nTerminals.contains(left))
                {
                	nTerminals.add(left);       
                }
                productions.add(new Production(left,right));   //创建产生式
                
            }  
            terminals.removeAll(nTerminals);    //移除
            terminals.remove("null");
            for(String t:nTerminals)
            	firstSetSingle.put(t,new HashSet<String>());
            for(String t:terminals)
            	firstSetSingle.put(t,new HashSet<String>());
            reader.close();  
            /*
            System.out.println("产生式：");
            for(Production p:productions)
            {
            	System.out.println(p);
            }
            System.out.println("终结符：\n"+terminals);
            
            System.out.println("非终结符：\n"+nTerminals);
           */
            
            }
        catch(IOException e)
        {
        	e.printStackTrace();
        }
	}
	//寻找以B为左部的所有产生式
	/*
	public List<Production> findLeft(String B) {
	    List<Production> list = new ArrayList<>();
	    for (Iterator<Production> iterator = productions.iterator(); iterator.hasNext();) 
	    {
	      Production production = (Production) iterator.next();
	      if (production.getLeft().equals(B)) {
	        list.add(production);
	      }
	    }
	    return list;
	  }
	*/
	List<Production> leftBy(String nTerminal)
	{
		ArrayList<Production> pros = new ArrayList<Production>();
		for(Production p:productions)
		{
			if(p.getLeft().equals(nTerminal))
				pros.add(p);
		}
		return pros;
	}
	
	//判断是否为终结符
	boolean isTerminal(String s)
	{
		return terminals.contains(s);
	}
	public void updateFirst()
	{
			 //更新某一个非终结符的first时，找到以它为左部的所有产生式，判断它们的右部
			 //如果是null，则可以加入null
			 //如果是非终结符打头，加入，非终结符，加入全部...
			 int oldTotal = -1,newTotal=0;
			 while(oldTotal<newTotal)
			 {
				 oldTotal = newTotal;
				 //所有非终结符
				 for(String nTerminal:nTerminals)
				 {
					 List<Production> pdc = leftBy(nTerminal);
					 for(Production production:pdc)
					 {
						 //右部第一个符号
						 String firstStr = production.getRight().elementAt(0);
						 if(firstStr.equals("null"))
						 {
							 if(!firstSetSingle.get(nTerminal).contains("null"))
							 {
							 HashSet<String> old = firstSetSingle.get(nTerminal);
								 old.add("null");
							 firstSetSingle.put(nTerminal, old);
							 }
						 }
						 //第一个是终结
						 else if(isTerminal(firstStr))
						 {
							 HashSet<String> old = firstSetSingle.get(nTerminal);
							 old.add(firstStr);
							 //System.out.println(firstStr);
							 firstSetSingle.put(nTerminal, old);
						 }
						 else
						 {
							 int i;
							for(i = 0;i<production.getRight().size();i++)
							{
								String s = production.getRight().elementAt(0);
								HashSet<String> old = firstSetSingle.get(nTerminal);
								HashSet<String> toAdd = firstSetSingle.get(s);
								if(!toAdd.contains("null"))
								{
								old.addAll(toAdd);
							 firstSetSingle.put(nTerminal, old);
							 break;     //这个产生式不用循环了
								}
							 else
							 {
								toAdd.remove("null");
								old.addAll(toAdd);
								 firstSetSingle.put(nTerminal, old);
							 }
							}
							if(i==production.getRight().size())
							{
								HashSet<String> old = firstSetSingle.get(nTerminal);
								 old.add("null");
							 firstSetSingle.put(nTerminal, old);
							}
						 }
					 }
				 }
				 newTotal = 0;
				 for(String n:nTerminals)
				 {
					 newTotal += firstSetSingle.get(n).size();
				 }
			 }
	}
		void showFirst()
		{
			/*
			for(String s:nTerminals)
			{
				System.out.println(s+"    "+firstSetSingle.get(s).size()+"     "+firstSetSingle.get(s));
				//for(String h:firstSetSingle.get(s))
				//{
				//	System.out.println(h);
				//}
				//System.out.println(firstSetSingle.get(s).size());
			}*/
			System.out.println(firstSetSingle);
		}

        public static void main(String[] args)
        {
        	//ll1parser parser = new ll1parser("C:\\Users\\smashandgrab\\Desktop\\SNL-Compiler-master-edited\\grammer.txt");
        	ll1parser parser = new ll1parser("grammer.txt");
        	parser.updateFirst();
            parser.showFirst();
        }
}

