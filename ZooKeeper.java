package homework1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

class Node{

	int count;
	int index_X;
	int index_Y;
	ArrayList<Integer> al;

	public Node(int count, int index_X, int index_Y, ArrayList<Integer> al){

		this.count = count;
		this.index_X = index_X;
		this.index_Y = index_Y;
		this.al = al;
	}

}

public class homework{

	char matrix[][];
	private Scanner s;
	static int lizards = 0;
	String str = "";
	static Queue<Node> nodes;
	static ArrayList<Integer> al = new ArrayList<Integer>();

	//Open file
	public void openfile(){
		try {
			s = new Scanner(new File("input.txt"));
		} 

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//Read file
	public char[][] readfile(){

		str = s.next();
		int rows = s.nextInt();
		lizards = s.nextInt();
		int columns = rows;

		char matrix[][] = new char[rows][columns];

		int row = 0;
		int column = 0;

		for(int j = 0;j<rows;j++){
			String str = s.next();
			column = 0;
			for(int i = 0;i<str.length();i++){
				matrix[row][column] = str.charAt(i);
				column++;
			}
			row++;
		}

		//		for(int i = 0;i<rows;i++){
		//			for(int j = 0;j<columns;j++){
		//				System.out.print(matrix[i][j]);
		//			}
		//			System.out.println();
		//		}
		s.close();
		return matrix;
	}

	public static void display_matrix(char[][] matrix) throws IOException {
		File fout = new File("output.txt");
		FileOutputStream fos = new FileOutputStream(fout);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write("OK");
		for(int i = 0;i<matrix.length;i++){
			bw.newLine();
			for(int j = 0;j<matrix.length;j++){
				bw.write(matrix[i][j]);
			}
		}
		bw.newLine();
		bw.close();
	}

	public static void display_NoSolution() throws IOException {
		File fout = new File("output.txt");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write("FAIL");
		bw.close();
	}

	public  boolean findSolution() {

		char[][] childMatrix;
		nodes = new LinkedList<Node>();
		Node initial = new Node(0, 0, 0, new ArrayList<Integer>());
		nodes.add(initial);

		while(!nodes.isEmpty()){
			Node current = nodes.poll();
			childMatrix = new char[matrix.length][];
			for(int i = 0;i<matrix.length;i++){
				childMatrix[i] = matrix[i].clone();
			}

			for(Integer lizards: current.al)
				childMatrix[lizards/childMatrix.length][lizards%childMatrix.length]='1';

			if(current.count==lizards){
				try {
					display_matrix(childMatrix);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
			expand(childMatrix,current);
		}
		return false;

	}

	public static void expand(char[][] childMatrix, Node current) {
		int col=current.index_Y;
		for(int row=current.index_X;row<childMatrix.length;row++){
			//Traversing through each column for a particular row
			for(;col<childMatrix.length;col++){
				if(validate(childMatrix,row,col)){
					ArrayList<Integer> temp = new ArrayList<Integer>(current.al);
					temp.add(row*childMatrix.length+col);
					Node newNode = new Node(current.count+1, row, col+1, temp);			
					nodes.add(newNode);
				}
			}
			col=0;
		}
	}

	public static boolean validate(char[][] childMatrix, int row, int col) {

		//if tree return false

		if(childMatrix[row][col]=='2')
			return false;

		//checking the same row but different columns (left side)
		for(int i = col-1; i >= 0; i--) {
			if(childMatrix[row][i]=='1'){
				return false;
			}
			if(childMatrix[row][i]=='2'){
				break;
			}
		}

		//Checking the same row but different columns (right side)
		for(int i = col+1; i <childMatrix.length; i++) {
			if(childMatrix[row][i]=='1'){
				return false;
			}
			if(childMatrix[row][i]=='2'){
				break;
			}
		}

		//Checking the same col but different rows (left side)
		for(int i = row-1; i>=0; i--) {
			if(childMatrix[i][col]=='1'){
				return false;
			}
			if(childMatrix[i][col]=='2'){
				break;
			}
		}

		//Checking the same column but different rows(right side)
		for(int i = row + 1; i <childMatrix.length; i++) {
			if(childMatrix[i][col]=='1'){
				return false;
			}
			if(childMatrix[i][col]=='2'){
				break;
			}
		}

		//checking the left diagonal
		for(int i = row-1,j=col-1;j>=0 && i>=0; j--,i--){
			if(childMatrix[i][j]=='1'){
				return false;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}

		for(int i = row+1,j=col+1;j<childMatrix.length && i<childMatrix.length; j++,i++){
			if(childMatrix[i][j]=='1'){
				return false;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}

		//checking the right diagonal
		for(int i = row-1,j=col+1;j<childMatrix.length && i>=0; j++,i--){
			if(childMatrix[i][j]=='1'){
				return false;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}

		for(int i = row+1,j=col-1;j>=0 && i<childMatrix.length; j--,i++){
			if(childMatrix[i][j]=='1'){
				return false;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}
		return true;
	}

	public static boolean dfs(char[][] childMatrix, Node current) throws IOException {

		if(current.count==lizards){
			display_matrix(childMatrix);
			return true;
		}
		int col=current.index_Y;
		for(int row=current.index_X;row<childMatrix.length;row++){
			//Traversing through each column for a particular row
			for(;col<childMatrix.length;col++){
				if(validateDFS(childMatrix,row,col)){
					childMatrix[row][col] = '1';
					al.add(row*childMatrix.length+col);
					Node newNode = new Node(current.count+1, row, col+1, al);			
					if(dfs(childMatrix,newNode))return true;
					al.remove(new Integer(row*childMatrix.length+col));
					childMatrix[row][col] = '0';
				}
			} 
			col=0;
		}
		return false;
	}

	public static boolean validateDFS(char[][] childMatrix, int row, int col) {

		//if tree return false

		if(childMatrix[row][col]=='2')
			return false;

		//checking the same row but different columns (left side)
		for(int i = col-1; i >= 0; i--) {
			if(childMatrix[row][i]=='1'){
				return false;
			}
			if(childMatrix[row][i]=='2'){
				break;
			}
		}

		//Checking the same col but different rows (left side)
		for(int i = row-1; i>=0; i--) {
			if(childMatrix[i][col]=='1'){
				return false;
			}
			if(childMatrix[i][col]=='2'){
				break;
			}
		}

		//checking the left diagonal
		for(int i = row-1,j=col-1;j>=0 && i>=0; j--,i--){
			if(childMatrix[i][j]=='1'){
				return false;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}

		//checking the right diagonal
		for(int i = row-1,j=col+1;j<childMatrix.length && i>=0; j++,i--){
			if(childMatrix[i][j]=='1'){
				return false;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}

		return true;
	}

	public static boolean findSimulated(char[][] matrix) throws IOException {

		int rows = matrix.length;
		int cols = matrix.length;
		int occupiedLizards = 0, oldCount = 0, countNewConflict = 0, numberOfOperations = 0;
		int difference = 0, countOldConflict=0;
		long time =  System.currentTimeMillis();
		double temperature = 0.0, probability = 0.0;
		ArrayList<Integer> al = new ArrayList<Integer>();

		Random rn = new Random();
		while(occupiedLizards<lizards){
			int x = rn.nextInt(rows);
			int y = rn.nextInt(cols);
			if(matrix[x][y]=='0'){
				matrix[x][y]='1';
				al.add(x*matrix.length+y);
				occupiedLizards++;
			}
			if(System.currentTimeMillis()-time>290000){
				return false;
			}
		}

		numberOfOperations = 1;

		countOldConflict = calculateConflicts(al,matrix);
		if(countOldConflict==0){
			display_matrix(matrix);
			return true;
		}

		while(countOldConflict!=0){

			if(System.currentTimeMillis()-time>290000){
				return false;
			}

			//Retrieve total conflicts
			temperature = 100/numberOfOperations;

			//Picking up random lizard
			int index = rn.nextInt(al.size());
			int placedLizard = al.get(index);
			int x = rn.nextInt(rows);
			int y = rn.nextInt(cols);

			while(matrix[x][y]!='0'){
				x = rn.nextInt(rows);
				y = rn.nextInt(cols);
				if(System.currentTimeMillis()-time>290000){
				return false;
				}
			}

			matrix[x][y]='1';
			al.add(x*matrix.length+y);
			matrix[placedLizard/matrix.length][placedLizard%matrix.length] = '0';
			al.remove(new Integer(placedLizard));
			countNewConflict = calculateConflicts(al,matrix);
			difference = countNewConflict - countOldConflict;
			probability = Math.exp(-difference/temperature);

			if(difference > 0){
				if(probability<Math.random()){
					matrix[x][y]='0';
					al.remove(new Integer(x*matrix.length+y));
					matrix[placedLizard/matrix.length][placedLizard%matrix.length] = '1';
					al.add(placedLizard);
				}
				else{
					countOldConflict=countNewConflict;
				}
			}
			else{
				countOldConflict = countNewConflict;
			}
			numberOfOperations++;	 
		}
		if(countOldConflict==0){
			display_matrix(matrix);
			return true;

		}
		return false;
	}

	public static int calculateConflicts(ArrayList<Integer> al, char[][] matrix) {
		int count = 0;
		for(int i = 0;i<al.size();i++){

			int row = al.get(i)/matrix.length;
			int col = al.get(i)%matrix.length;
			count = count + countConflicts(matrix,row,col);
		}
		return count;
	}

	public static int countConflicts(char[][] childMatrix, int row, int col) {
		int count = 0;
		//checking the same row but different columns (left side)
		for(int i = col-1; i >= 0; i--) {
			if(childMatrix[row][i]=='1'){
				count++;
			}
			if(childMatrix[row][i]=='2'){
				break;
			}
		}

		//Checking the same row but different columns (right side)
		for(int i = col+1; i <childMatrix.length; i++) {
			if(childMatrix[row][i]=='1'){
				count++;
			}
			if(childMatrix[row][i]=='2'){
				break;
			}
		}

		//Checking the same col but different rows (left side)
		for(int i = row-1; i>=0; i--) {
			if(childMatrix[i][col]=='1'){
				count++;
			}
			if(childMatrix[i][col]=='2'){
				break;
			}
		}

		//Checking the same column but different rows(right side)
		for(int i = row + 1; i <childMatrix.length; i++) {
			if(childMatrix[i][col]=='1'){
				count++;
			}
			if(childMatrix[i][col]=='2'){
				break;
			}
		}

		//checking the left diagonal
		for(int i = row-1,j=col-1;j>=0 && i>=0; j--,i--){
			if(childMatrix[i][j]=='1'){
				count++;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}

		for(int i = row+1,j=col+1;j<childMatrix.length && i<childMatrix.length; j++,i++){
			if(childMatrix[i][j]=='1'){
				count++;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}

		//checking the right diagonal
		for(int i = row-1,j=col+1;j<childMatrix.length && i>=0; j++,i--){
			if(childMatrix[i][j]=='1'){
				count++;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}

		for(int i = row+1,j=col-1;j>=0 && i<childMatrix.length; j--,i++){
			if(childMatrix[i][j]=='1'){
				count++;
			}
			if(childMatrix[i][j]=='2'){
				break;
			}
		}
		return count;
	}


	public static void main(String[] args) throws IOException {

		homework st = new homework();
		st.openfile();
		st.matrix = st.readfile();
		if(st.str.equals("BFS")){
			boolean c = st.findSolution();
			if(c==false)
				display_NoSolution();
		}
		else if(st.str.equals("DFS")){
			boolean c = dfs(st.matrix,new Node(0, 0, 0, new ArrayList<Integer>()));
			if(c==false)
				display_NoSolution();
		}
		else if(st.str.equals("SA")){
			boolean c = findSimulated(st.matrix);
			if(c==false)
				display_NoSolution();

		}
		else{
			System.out.println("Wrong File");
		}

	}

}

