import java.util.*;

public class Solution {

	private static class Node {
		Node up, down, right, left;
		int val;
		Node () {
			val = -1;
			up = null;
			down = null;
			right = null;
			left = null;
		}
	}
	
	static int rows, cols;
	static Node [][] dataCenter;
	static Node begin, end;
	static int depth;
	static HashSet<Node> setOfUntravelledNodes;
	
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    	long start = System.nanoTime();
    	takeInput();
    	if(rows == 0 && cols == 0 ) {
    		System.out.println(0);
    		return;
    	}

    	setOfUntravelledNodes.remove(begin);
    	depth = 0;
    	int soln = performRecursiveDFS(begin);
    	
    	System.out.println(soln);
    	long elapsedTime = System.nanoTime() - start;
    	System.out.println(elapsedTime/1000000000);
    }
    
    private static int performRecursiveDFS(Node current) {
    	depth++;
    	if(current == end) {
    		if(setOfUntravelledNodes.size() == 0) {
    			depth--;
    			return 1;
    		}
    		else {
    			depth--;
        		return 0;
        	}
    	}
    	
    	if(chkForIslandNodes(current)) {
			depth--;
    		return 0;
    	}
    	
    	int score = 0;
    	if(setOfUntravelledNodes.remove(current.up)) {
    		score += performRecursiveDFS(current.up);
    		setOfUntravelledNodes.add(current.up);
    	}
    	if(setOfUntravelledNodes.remove(current.down)) {
    		score += performRecursiveDFS(current.down);
    		setOfUntravelledNodes.add(current.down);
    	}
    	if(setOfUntravelledNodes.remove(current.right)) {
    		score += performRecursiveDFS(current.right);
    		setOfUntravelledNodes.add(current.right);
    	}
    	if(setOfUntravelledNodes.remove(current.left)) {
    		score += performRecursiveDFS(current.left);
    		setOfUntravelledNodes.add(current.left);
    	}
    	
		depth--;
    	return score;
    }
    
    private static boolean chkForIslandNodes(Node current) {
    	
    	if(depth%2 != 0)
    		return false;
    	
    	Node tmp = current;
    	setOfUntravelledNodes.add(current);
    	Iterator<Node> i = setOfUntravelledNodes.iterator();
    	int directionsClosed = 0;
    	boolean nearend = false;
    	
    	while(i.hasNext())
    	{
    		tmp = i.next();
    		if(tmp==end || tmp == current)
    			continue;
    		directionsClosed = 0;
    		nearend = false;
    		if(tmp.up == end) {
    			nearend = true;
	    	}
    		else if(!setOfUntravelledNodes.contains(tmp.up)) {
    			directionsClosed++;
    		}
    		
	    	if(tmp.down == end) {
    			nearend = true;
	    	}
	    	else if(!setOfUntravelledNodes.contains(tmp.down)) {
    			directionsClosed++;
    		}
	    	
	    	if(tmp.right == end) {
    			nearend = true;
    		}
	    	else if(!setOfUntravelledNodes.contains(tmp.right)) {
    			directionsClosed++;
    		}
	    	
	    	if(tmp.left == end) {
    			nearend = true;
	    	}
	    	else if(!setOfUntravelledNodes.contains(tmp.left)) {
    			directionsClosed++;
    		}
	    	
	    	if(directionsClosed>2 && !nearend) {
	    		setOfUntravelledNodes.remove(current);
	    		return true;
	    	}
    	}
    	
    	setOfUntravelledNodes.remove(current);
    	
    	if(depth%4 != 0)
    		return false;
    	
    	HashSet<Node> set1 = (HashSet<Node>)(setOfUntravelledNodes.clone());
    	Queue<Node> q = new ArrayDeque<Node>();
    	q.add(current);
    	while(!set1.isEmpty() && !q.isEmpty()) {
    		current = q.remove();
    		
	    	if(set1.remove(current.up)) {
	    		q.add(current.up);
	    	}
	    	if(set1.remove(current.down)) {
	    		q.add(current.down);
	    	}
	    	if(set1.remove(current.right)) {
	    		q.add(current.right);
    		}
	    	if(set1.remove(current.left)) {
	    		q.add(current.left);
	    	}
    	}
    	if(set1.isEmpty()) {
    		return false;
    	}
    	
    	return true;
    }
    
    private static void takeInput() {
    	Scanner in = new Scanner(System.in);
        rows = in.nextInt();
        cols = in.nextInt();
        setOfUntravelledNodes = new HashSet<Node>();
        dataCenter = new Node[rows][cols];
        int num = rows*cols;
        int row = 0;
        int col = 0;
        int i, x;
        for(i = 0; i<num; i++) {
    		x = in.nextInt();
    		if(x!=1) {
        		dataCenter[row][col] = new Node();
        		dataCenter[row][col].val = x;
        		setOfUntravelledNodes.add(dataCenter[row][col]);
        		if(dataCenter[row][col].val == 2) {
        			begin = dataCenter[row][col];
        		}
        		else if(dataCenter[row][col].val == 3) {
    				end = dataCenter[row][col];
        		}
    		}
    		col = (col+1)%cols;
    		if(col == 0)
    			row++;
        }
        
        row = 0;
        col = 0;
        for(i = 0; i<num; i++) {
    		if(dataCenter[row][col]!=null) {
        		if(row > 0) {
        			dataCenter[row][col].up = dataCenter[row - 1][col];
        		}
        		if(row < rows-1) {
        			dataCenter[row][col].down = dataCenter[row + 1][col];
        		}
        		if(col < cols-1) {
        			dataCenter[row][col].right = dataCenter[row][col+1];
        		}
        		if(col > 0) {
        			dataCenter[row][col].left = dataCenter[row][col-1];
        		}
        	}
    		col = (col+1)%cols;
    		if(col == 0)
    			row++;
        }
        
    }
}