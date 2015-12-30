package client.qualityChecker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 *  Quality Checker for Table & Form Entry panel
 */
public class QualityChecker {
	
	private ArrayList<String> getKnown = new ArrayList<String>();
	private TreeMap <Integer, HashSet<String>> batchDict = new  TreeMap <Integer, HashSet<String>>();
	HashSet<String> genDict=new HashSet<String>();
	HashSet<String> gen2ndDict=new HashSet<String>();	
	TreeSet<String> suggDict=new TreeSet<String>();	
		

	
	
	/**
	 * Store known data into a dictionary
	 */	
	public void downloadKnowFile(ArrayList<String> getKnown) {
		
		batchDict.clear();
		Scanner inScanner;
		
		for(int i=0; i < getKnown.size(); i++)
		{	
			if(getKnown.get(i) !=null)
			{
				HashSet<String> tempDict = new HashSet<String>();
			
				try {
	            
					URL getKnownURL = new URL(getKnown.get(i));
				
					inScanner = new Scanner(getKnownURL.openStream());
					inScanner.useDelimiter(",");
	            
					while (inScanner.hasNext())
						tempDict.add(inScanner.next().toLowerCase());
	            
					batchDict.put(i, tempDict);
	            
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			
			else
				batchDict.put(i,null);
			
          }
    }
	
	
	/**
	 * Validate user entered word with dictionary
	 */	
	public boolean findValue(int index, String word) {
		
		if(word.length() > 0)
		{
		  for(Map.Entry<Integer, HashSet<String>>  entry : batchDict.entrySet())
		  {
			if (entry.getKey().equals(index) && entry.getValue() != null)
			{
				HashSet<String> tempDict = new HashSet<String>();
				tempDict=entry.getValue();
				
					if(tempDict.contains(word.toLowerCase()))
						return true;
			}
			
			else if (entry.getKey().equals(index) && entry.getValue() == null)
				return true;
		 }
		}
		else
			return true;
		
		return false;
	}
	
	
	/**
	 * Main method for finding similar words with edit distance 1 or 2
	 * @return 
	 */	
	public TreeSet<String> sugSimWord(int index,String inputWord) {
		
		genDict.clear();
		gen2ndDict.clear();
		suggDict.clear();
		boolean sec=false;
		
		deleteWord(inputWord,sec);
		insertWord(inputWord,sec);
		alteration(inputWord,sec);
		transposition(inputWord,sec);
			
		sec = true;
		get2ndEdit(sec);
		compareDict(index);
		
		return suggDict;	
	}
	


	/**
	 * Suggest similar words with edit distance 1 or 2
	 */	
	private void compareDict(int index) {
	    
		HashSet<String> tempDict = new HashSet<String>(); 
		
		tempDict = batchDict.get(index);
		
		for(String str: tempDict)
		{
			if(genDict.contains(str))
				suggDict.add(str);
			
			if(gen2ndDict.contains(str))
				suggDict.add(str);	
		}
    }


	/**
	 * Get words with edit distance 2
	 */	
	public void get2ndEdit(boolean sec)
	{
		for(String str: genDict)
		{
			deleteWord(str,sec);
			insertWord(str,sec);
			alteration(str,sec);
			transposition(str,sec);	
		}
	}
	
	
	/**
	 * compute word with delete instance
	 */	
	public void deleteWord(String inputWord, boolean sec)
	{	
		for(int i=0; i<inputWord.length();i++)
		{
			StringBuilder str=new StringBuilder();
			str.append(inputWord);
			str.deleteCharAt(i);
			if (!sec)
				genDict.add(str.toString());
			else
				gen2ndDict.add(str.toString());
		}	
		
	}
 
 
	/**
	 * compute word with insert instance
	 */	
	public void insertWord(String inputWord, boolean sec)
	{
		char put;
 	
		for(int i=0; i<inputWord.length()+1;i++)
		{
		 for(put='a';put<='z'; put++)
		 {
			StringBuilder str=new StringBuilder();
			str.append(inputWord);
			str.insert(i, put);
			if (!sec)
				genDict.add(str.toString());
			else
				gen2ndDict.add(str.toString());
		 }	
		}
 	
	}
 
	
	/**
	 * compute word with alter instance
	 */	
	public void alteration(String inputWord, boolean sec)
	{
		char put;
 	
		for(int i=0; i<inputWord.length();i++)
		{
		 for(put='a';put<='z'; put++)
		 {
			StringBuilder str=new StringBuilder();
			str.append(inputWord);
			
			str.setCharAt(i, put);
			if (!sec)
				genDict.add(str.toString());
			else
				gen2ndDict.add(str.toString());
		 }	
		}
 	
	}
 
	
	/**
	 * compute word with transpose instance
	 */	
	public void transposition(String inputWord,boolean sec)
	{
		char temp;
		for(int i=0; i<inputWord.length()-1;i++)
		{
			StringBuilder str=new StringBuilder();
			str.append(inputWord);
			temp=inputWord.charAt(i);
			str.setCharAt(i,inputWord.charAt(i+1));
			str.setCharAt(i+1,temp);
			if (!sec)
				genDict.add(str.toString());
			else
				gen2ndDict.add(str.toString());
		}
	
	}

	
}//end of QualityChecker

