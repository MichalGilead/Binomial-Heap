import java.util.HashMap;

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{

   /**
    * public boolean empty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
	
	int nodesnum = 0;
	HeapNode first = new HeapNode(-1, null, null, null, 0);
	HeapNode min = new HeapNode(-1, null, null, null, 0);
	HashMap<Integer, HeapNode> hashMap = new HashMap<>();
	int numberOfLinks = 0;
	
	public BinomialHeap(){
	}
	
	private BinomialHeap(HeapNode first, HeapNode min, int nodesnum){
		this.first = first;
		this.min = min;
		this.nodesnum = nodesnum;
		this.hashMap.put(first.key, first);
	}
	
	
    public boolean empty()
    {
    	if (size() == 0)
    		return true;
    	return false; 
    }
		
   /**
    * public void insert(int value)
    *
    * Insert value into the heap 
    *
    */
    public void insert(int value) 
    {    
    	HeapNode node = new HeapNode(value, null, null, null, 1);
    	BinomialHeap heap = new BinomialHeap(node,node,1);
    	meld(heap);
    	return;
    }

   /**
    * public void deleteMin()
    *
    * Delete the minimum value
    *
    */
    private void rotate(HeapNode minChild, HeapNode minChildNext){
    	if (minChildNext!=null){
    		rotate(minChild.next, minChildNext.next);
    		minChildNext.next = minChild; 
    	}
    }
    
    private HeapNode findingMin(HeapNode x){ //finding the new min of a new heap, x is the heap's first node
    	HeapNode z = x;
    	HeapNode min = x;
    	while (z!=null)
    	{
    		if (z.key<min.key)
    			min=z;
    		z=z.next;
    	}
    	return min;
    }
    
    private int nodesnumSum(HeapNode x){ //returns the size of a new heap, x is the heap's first node
    	HeapNode z = x;
    	int sum = 0;
    	while (z!=null){
    		sum+=z.size;
    		z=z.next;
    	}
    	return sum;
    }
    
    public void deleteMin()
    { 	
       	hashMap.remove(min.key);
       	if (nodesnum==1)
       	{
       		nodesnum = 0;
       		first = new HeapNode(-1, null, null, null, 0);
       		min = new HeapNode(-1, null, null, null, 0);
       		return;
       	}
    	if (this.first == this.min)
    			if(this.first.next!=null)		//deleting min from heap
    				this.first = this.first.next;
    			else
    				nodesnum=0;
    	else{
        	HeapNode z = this.first;
        	while (z.next != min){
        		z = z.next;
        	}
        	z.next = z.next.next;       	
    	}
    	HeapNode x = this.min.child;
    	
    	if (x!=null){
    		x.parent = null;
    		while (x.next!=null){
    			x.next.parent = null;
    			x=x.next;
    		}
    		if (this.min.child!=null&&this.min.child.next!=null){ //creating a new heap from min's children
    			rotate(this.min.child, this.min.child.next);
    			min.child.next = null;
    		}
    		BinomialHeap subMin = new BinomialHeap(x, findingMin(x), nodesnumSum(x));
    		this.min = findingMin(this.first);  //updating the new min of the heap
    		if (nodesnum!=0)
    			this.nodesnum-=subMin.nodesnum+1;
    		meld(subMin); //creating a new heap without previous min
    	}else{
    		this.min = findingMin(this.first);  //updating the new min of the heap
    		nodesnum--;
    	}
    	return; 
     	
    }

   /**
    * public int findMin()
    *
    * Return the minimum value
    *
    */
    public int findMin()
    {
    	return this.min.key;
    } 
    
   /**
    * public void meld (BinomialHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (BinomialHeap heap2)
    {
    	if (heap2.empty())
    	{
    		return;
    	}
    	if (empty())
    	{
    		this.hashMap=heap2.hashMap;
    		this.first=heap2.first;
    		this.min=heap2.min;
    		this.nodesnum=heap2.nodesnum;
    		return;
    	}
    	//if both heaps are not empty:

    	if (this.min.key>heap2.min.key) //update min
    		this.min = heap2.min;
    	this.nodesnum += heap2.nodesnum; //update nodesnum
    	this.hashMap.putAll(heap2.hashMap); //update hashmap
    	
    	HeapNode z1 = this.first;
		HeapNode z2 = heap2.first;
		if (rank(z2)<=rank(z1))
		{
			HeapNode join = z2;
			z2 = z2.next;
			join.next = z1;
			this.first = join;
			z1 = this.first;
		}
		while (z2!=null){
			if (z1.next!=null){
				if (rank(z2)<=rank(z1.next)){	//put z2 between z1 and z1.next 
					HeapNode join = z2;
					z2 = z2.next;
					join.next = z1.next;
					z1.next = join;
				}
				z1=z1.next;
			}
			else{
				z1.next=z2;	//put z2 and his following roots after z1
				z2 = null;	//end the while loop
			}
		}
		HeapNode prev_x = null;
		HeapNode x = first;
		HeapNode next_x = first.next;
		while (next_x!=null){
			if (rank(x)!=rank(next_x)||(next_x.next!=null&&rank(next_x.next)==rank(x)))
			{
				prev_x = x;
				x = next_x;
			}
			else 
			{	
				if(x.key<next_x.key)
				{
					x.next = next_x.next;
					merge(next_x, x);
				}
				else
				{
					if (prev_x==null)//if x.key>next_x.key & x is first - make next_x first instead
					{		
						first = next_x;
					}
					else
					{	
						prev_x.next = next_x;
					}
					merge(x, next_x);
					x = next_x;
				}
			}
			next_x = x.next;
		}
    	return; 
    }
    
    private void merge(HeapNode x, HeapNode y){
    	numberOfLinks++;
    	x.parent = y;
    	x.next = y.child;
    	y.child = x;
    	y.size += x.size;
    	return;
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return this.nodesnum;
    }
    
    private int maxRank(){
    	HeapNode z = this.first;
    	while (z.next!=null){
    		z = z.next;
    	}
    	return rank(z); 
    }
    
    private int rank(HeapNode x){
    	return (int) (Math.log(x.size)/Math.log(2));//rank =log2(node.size)
    }
   /**
    * public int minTreeRank()
    *
    * Return the minimum rank of a tree in the heap.
    * 
    */
    public int minTreeRank()
    {
    	if (empty())
    		return -1;
        return rank(first); //rank =log2(first.size)
    }
	
	   /**
    * public boolean[] binaryRep()
    *
    * Return an array containing the binary representation of the heap.
    * 
    */
    public boolean[] binaryRep()
    {
    	if (empty())
    		return new boolean[0];
		boolean[] arr = new boolean[maxRank()+1];
		HeapNode z = this.first;
		while (z!=null){
			int curr = rank(z);
			arr[curr]=true;
			z = z.next;
		}
        return arr; 
    }

   /**
    * public void arrayToHeap()
    *
    * Insert the array to the heap. Delete previous elemnts in the heap.
    * 
    */
    public void arrayToHeap(int[] array)
    {
    	nodesnum = 0;
    	first = new HeapNode(-1, null, null, null, 0);
    	min = new HeapNode(-1, null, null, null, 0);
    	hashMap = new HashMap<>();
    	for (int i : array){
        	insert(i);
        }
    	return;
    }
	
   /**
    * public boolean isValid()
    *
    * Returns true if and only if the heap is valid.
    *   
    */
    public boolean isValid() 
    {
    	HeapNode x = first;
    	int treesnum = 0;
    	while (x!=null){ //make sure that every child's key is smaller than its parent's key 
    		if (!smallerchildren(x))
    			return false;
    		x = x.next;
    		treesnum++;
    	}
    	x = first;
    	int[] ranks = new int[treesnum];
    	int i =0;
    	while (x!=null){ //make sure that each child is a valid binomial tree of rank 0 - k-1 (when the tree rank is k)
    		int rank = isBinTree(x);
    		if (rank==-1)
    			return false;
    		else{
    			ranks[i] = rank;
    			i++;
    		}
    		x = x.next;
    	}
    	int maxrank=Integer.MIN_VALUE;
    	for (int j=0;j<ranks.length;j++)
    		if (ranks[j]>maxrank)
    			maxrank=ranks[j];
    	int[] diffranks = new int [maxrank+1];
    	for (int j=0;j<ranks.length;j++){ // make sure that for each i there's at most one tree with rank i
    		if (diffranks[ranks[j]]==0)
    			diffranks[ranks[j]]++;
    		else if (diffranks[ranks[j]]==1)
    			return false;
    	}
    	return true;
    }
    
    private boolean smallerchildren (HeapNode x)
    {
     	HeapNode y = x.child;
    	if (y==null)
    		return true;
    	while (y!=null)
    	{
    		if (y.key<x.key)
    			return false;
    		if (!smallerchildren(y))
    			return false;
    		y=y.next;
    	}
    	return true;
    }
    
    private int isBinTree(HeapNode x){ //return -1 if isn't binomial tree or the rank of the tree if is
    	HeapNode y = x.child;
    	if (y==null)
    		return 0;
    	if (isBinTree(y)==-1)
    		return -1;
    	int result=isBinTree(y)+1;    	
    	for(int i=result-1;i>=0;i--){
    		if (y==null || isBinTree(y)!=i || (i==0&&y.next!=null))
    			return -1;
    		y=y.next;				
    	}
    	return result;	
    }
    
    
    
   /**
    * public void delete(int value)
    *
    * Delete the element with the given value from the heap, if such an element exists. 
    * If the heap doen't contain an element with the given value, don't change the heap.
    *
    */
    public void delete(int value) 
    {    
    	decreaseKey(value, Integer.MIN_VALUE);
    	deleteMin();
    	return;
    }
    	

   /**
    * public void decreaseKey(int oldValue, int newValue)
    *
    * If the heap doen't contain an element with value oldValue, don't change the heap.
    * Otherwise decrease the value of the element whose value is oldValue to be newValue. 
    * Assume newValue <= oldValue.
    */
    public void decreaseKey(int oldValue, int newValue) 
    {    
    	if (this.hashMap.containsKey(oldValue)){
    		HeapNode dec = hashMap.get(oldValue);
    		dec.key = newValue;
    		hashMap.remove(oldValue);
    		hashMap.put(newValue, dec);
    		while (dec.parent!=null&&dec.parent.key>dec.key){ 
    			int decKey = dec.key; 
    			dec.key = dec.parent.key;
    			dec.parent.key = decKey;
    			hashMap.put(dec.key, dec);
    			hashMap.put(dec.parent.key, dec.parent);
    			dec = dec.parent;
    		}
    	}
    	return; 
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than BinomialHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{
    	int key;
    	HeapNode child;
    	HeapNode parent;
    	HeapNode next;
    	int size;
    	
    	public HeapNode(int key, HeapNode child, HeapNode parent, HeapNode next, int size){
    		this.key = key;
    		this.child = child;
    		this.parent = parent;
    		this.next = next;
    		this.size = size;
    	}
  	
    }

}