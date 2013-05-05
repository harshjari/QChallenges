import java.io.*;
import java.util.*;

public class Solution_scrabbleLadder {

	static HashMap<String, TreeSet<String>> neighborsLessScoring;
	static HashMap<String, Integer> scores;
	static HashMap<String, Integer> letterValues;
	static int maxScore=0;
	static int K;
	static int N;
	static boolean noValidWords;
	public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

		setupLetterValues(); //Setting up Letter value hashmap
		
		noValidWords = false;
		
    	setupNeighborList(); //Taking Input from cmd + setting up neighborsLessScoring hashmap
    	
    	if(noValidWords) {
    		System.out.println("0");
    		return;
    	}
    	if(scores.keySet().size() == 1) {
    		System.out.println(scores.get(((String[])(scores.keySet().toArray(new String[0])))[0]).intValue());
    		return;
    	}
    	//everything setup and edge cases handled so moving to good stuff
    	String [] keys = (String[]) (neighborsLessScoring.keySet().toArray(new String[0]));
    	int i = 0;
    	maxScore = 0;
    	while(i<keys.length) { //iterating through all words
    		String currentKey = keys[i];
    		int wordScore = scores.get(keys[i]).intValue();
    		
    		if((wordScore*wordScore)<maxScore) { //upper bound of max score is limited using 2*(n*(n+1)/2)-n. here n = score of middle word
    			i++;
    			continue;
    		}
    		
    		if(maxScore<wordScore)
    			maxScore = wordScore;
    		
    		String [] neighborsLess =(String [])(neighborsLessScoring.get(currentKey).toArray(new String[0]));
    		int j = 0;
    		int k = 0;
    		//so we iterate through all possible pairs of neighbors and do a dfs to find the max score
    		for(j = 0; j<neighborsLess.length;j++) {
    			TreeSet<String> wordLadderSet = new TreeSet<String>();
        		wordLadderSet.add(currentKey);
        		String s1 = neighborsLess[j];
    			wordLadderSet.add(s1);
    			for(k = j+1; k<neighborsLess.length;k++) {
    				String s2 = neighborsLess[k];
    				wordLadderSet.add(s2);
    				double s1Score = scores.get(s1).doubleValue();
    				double s2Score = scores.get(s2).doubleValue();
    				if((wordScore+(int)(Math.ceil(s1Score*(s1Score+1)/2 + s2Score*(s2Score+1)/2)))>maxScore) { //maxscore is limited by scores of the boundary words
    					wordLadderSet.add(s2);
    					int currentScore = wordScore+(int)s1Score+(int)s2Score;
    					if(currentScore > maxScore)
    						maxScore = currentScore;
    					recurse(wordLadderSet, currentScore, s1, s2);//found words that fulfill the minimum score reqs so we move to recurse on words to start dfs
    				}
    				wordLadderSet.remove(s2);
    			}
    		}
    		
    		i++;
    	}
    	
    	System.out.println(maxScore);
    	return;
    }
	
	static void recurse(TreeSet<String> wordLadderSet, int scoreSoFar, String top, String bottom) {
		String [] neighborsLessThanTop =(String [])(neighborsLessScoring.get(top).toArray(new String[0]));
		String [] neighborsLessThanBot =(String [])(neighborsLessScoring.get(bottom).toArray(new String[0]));
		int j = 0;
		int k = 0;
		for(j = 0; j<neighborsLessThanTop.length;j++) {//look at all words that can be above the current 'top' word
    		String s1 = neighborsLessThanTop[j];
    		if(wordLadderSet.contains(s1))
    			continue;
			wordLadderSet.add(s1);
			for(k = 0; k<neighborsLessThanBot.length;k++) {//look at all words that can be below the current 'bottom' word
				String s2 = neighborsLessThanBot[k];
				if(s1.equals(s2)||wordLadderSet.contains(s2))
					continue;
				wordLadderSet.add(s2);
				double s1Score = scores.get(s1).doubleValue();
				double s2Score = scores.get(s2).doubleValue();
				if((scoreSoFar+(int)(Math.ceil(s1Score*(s1Score+1)/2 + s2Score*(s2Score+1)/2)))>maxScore) {
					wordLadderSet.add(s2);
					int currentScore = scoreSoFar+(int)s1Score+(int)s2Score;
					if(currentScore > maxScore) {
						maxScore = currentScore;
					}
					recurse(wordLadderSet, currentScore, s1, s2);//repeat till end
				}
				wordLadderSet.remove(s2);//dfs search for this bottom word at this combination is complete so removing it
			}
			wordLadderSet.remove(s1);//dfs search for this top word at this combination is complete so removing it
		}
	}
	
	static void setupLetterValues() {
		letterValues = new HashMap<String, Integer>();
		letterValues.put("A", Integer.valueOf(1));
		letterValues.put("E", Integer.valueOf(1));
		letterValues.put("I", Integer.valueOf(1));
		letterValues.put("L", Integer.valueOf(1));
		letterValues.put("N", Integer.valueOf(1));
		letterValues.put("O", Integer.valueOf(1));
		letterValues.put("R", Integer.valueOf(1));
		letterValues.put("S", Integer.valueOf(1));
		letterValues.put("T", Integer.valueOf(1));
		letterValues.put("U", Integer.valueOf(1));

		letterValues.put("D", Integer.valueOf(2));
		letterValues.put("G", Integer.valueOf(2));

		letterValues.put("B", Integer.valueOf(3));
		letterValues.put("C", Integer.valueOf(3));
		letterValues.put("M", Integer.valueOf(3));
		letterValues.put("P", Integer.valueOf(3));
		
		letterValues.put("F", Integer.valueOf(4));
		letterValues.put("H", Integer.valueOf(4));
		letterValues.put("V", Integer.valueOf(4));
		letterValues.put("W", Integer.valueOf(4));
		letterValues.put("Y", Integer.valueOf(4));

		letterValues.put("K", Integer.valueOf(5));

		letterValues.put("J", Integer.valueOf(8));
		letterValues.put("X", Integer.valueOf(8));

		letterValues.put("Q", Integer.valueOf(10));
		letterValues.put("Z", Integer.valueOf(10));

	}
	
    static void setupNeighborList() {
    	TreeSet<String> words = new TreeSet<String>();
    	try{
    		BufferedReader br = 
    				new BufferedReader(new InputStreamReader(System.in));
     
    		String inputLine = br.readLine();
    		K = Integer.parseInt(inputLine);
    		inputLine = br.readLine();
    		N = Integer.parseInt(inputLine);
    		
    		if(K==0 || N==0) {//taking edge case of k, n into account.
    			noValidWords = true;
    			return;
    		}
    		
    		int i = 0;
    		while(i<N){
    			inputLine = br.readLine();
    			if(inputLine.length()==K) {//ignore words not k length in size
    				words.add(inputLine.toUpperCase());//adding words to set in order to remove dupes
    			}
    			i++;
    		}
    		if(words.size() == 0) {//edge case if no valid k length words were given
    			noValidWords = true;
    			return;
    		}
    	}catch(IOException io){
    		io.printStackTrace();
    	}
    	
    	String [] wordsArray = (String[]) (words.toArray(new String[0]));
    	neighborsLessScoring = new HashMap<String, TreeSet<String>>();
    	scores = new HashMap<String, Integer>();
    	int i;
    	int j;
    	int charIndex;
    	int diffNum;
    	neighborsLessScoring.put(wordsArray[0], new TreeSet<String>());
    	for(i = 0; i<wordsArray.length;i++) {//loop sets up score values of each word to speedup score lookups
	    	int score = 0;
			charIndex = 0;
			while(charIndex<K) {
				score += letterValues.get(String.valueOf(wordsArray[i].charAt(charIndex)).toUpperCase()).intValue();
				charIndex++;
			}
			scores.put(wordsArray[i], Integer.valueOf(score));
    	}
    	
    	for(i = 0; i<wordsArray.length;i++) {//finding all the neighbors that score less than this. Since the algorithm doesnt need high/equal scoring neighbors
    		
    		for(j = i+1; j<wordsArray.length;j++) {
    			
    			charIndex = 0;
    			diffNum = 0;
    			
    			while(charIndex<K && diffNum<2) {
    				if(wordsArray[i].charAt(charIndex) != wordsArray[j].charAt(charIndex)) {
    					diffNum++;
    				}
    				charIndex++;
    			}
    			
    			TreeSet<String> tmpa1 = neighborsLessScoring.get(wordsArray[i]);
    			TreeSet<String> tmpb1 = neighborsLessScoring.get(wordsArray[j]);
				if(tmpa1 == null) {
					tmpa1 = new TreeSet<String>();
				}
				if(tmpb1 == null) {
					tmpb1 = new TreeSet<String>();
				}
    			
    			if(diffNum == 1) {
    				if(scores.get(wordsArray[i]).intValue() > scores.get(wordsArray[j]).intValue()) { //word at index i scores higher
    					tmpa1.add(wordsArray[j]);
    				}
    				else if(scores.get(wordsArray[i]).intValue() < scores.get(wordsArray[j]).intValue()) {//word at index j scores higher
    					tmpb1.add(wordsArray[i]);
    				}
    			}
    			
    			neighborsLessScoring.put(wordsArray[i], tmpa1);
    			neighborsLessScoring.put(wordsArray[j], tmpb1);
    		}
    		
    	}
    	
    }
}