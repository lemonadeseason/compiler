//���ķ�   �Ҳ� string����
//д��ĳһ���ս��Ϊ�󲿵����в���ʽ
//�ж��Ƿ�Ϊ�ս��
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
	private ArrayList<Production> productions;     //�������ķ������в���ʽ
	private Map<String,HashSet<String>>firstSetSingle;   //���ս����Ӧ��first��,����Ӧ�ð��ս����Ҳ�ӽ�ȥ
	private HashSet<String> nTerminals;      //�ս��
	private HashSet<String> terminals;           //���ս��
	public ll1parser(String path)  
	{
		//��path�ļ��е�����Ϊ����������Ե��ķ�
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
            	//�ٶ� ��->��  �ǽ����ŵ�
            	int bound = s.indexOf("-");
                String left = s.substring(0, bound);
                left=left.trim();//  ����ʽ��
                String rightStr = s.substring(bound+2,s.length());
                //�ÿո��Ҳ��и�
                String [] arr = rightStr.split("\\s+");
                Vector<String> right = new Vector<String>();     //����ʽ���Ҳ�
                for(String element:arr)
                {
                	right.addElement(element);
                	terminals.add(element);    //��ʱȫ�������ս��set�����ȥ����
                }
                if(!nTerminals.contains(left))
                {
                	nTerminals.add(left);       
                }
                productions.add(new Production(left,right));   //��������ʽ
                
            }  
            terminals.removeAll(nTerminals);    //�Ƴ�
            terminals.remove("null");
            for(String t:nTerminals)
            	firstSetSingle.put(t,new HashSet<String>());
            for(String t:terminals)
            	firstSetSingle.put(t,new HashSet<String>());
            reader.close();  
            /*
            System.out.println("����ʽ��");
            for(Production p:productions)
            {
            	System.out.println(p);
            }
            System.out.println("�ս����\n"+terminals);
            
            System.out.println("���ս����\n"+nTerminals);
           */
            
            }
        catch(IOException e)
        {
        	e.printStackTrace();
        }
	}
	//Ѱ����BΪ�󲿵����в���ʽ
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
	
	//�ж��Ƿ�Ϊ�ս��
	boolean isTerminal(String s)
	{
		return terminals.contains(s);
	}
	public void updateFirst()
	{
			 //����ĳһ�����ս����firstʱ���ҵ�����Ϊ�󲿵����в���ʽ���ж����ǵ��Ҳ�
			 //�����null������Լ���null
			 //����Ƿ��ս����ͷ�����룬���ս��������ȫ��...
			 int oldTotal = -1,newTotal=0;
			 while(oldTotal<newTotal)
			 {
				 oldTotal = newTotal;
				 //���з��ս��
				 for(String nTerminal:nTerminals)
				 {
					 List<Production> pdc = leftBy(nTerminal);
					 for(Production production:pdc)
					 {
						 //�Ҳ���һ������
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
						 //��һ�����ս�
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
							 break;     //�������ʽ����ѭ����
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

