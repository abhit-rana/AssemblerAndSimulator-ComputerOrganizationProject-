import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Toucan
{
	static int count_arr;
	static String arr_binary[] = new String[256];
	public static void main(String[] args)throws IOException 
	{
		Scanner sc = new Scanner(System.in);
		String[] inp_arr = new String[256];
		int t_inp = 0 ;
		while(sc.hasNextLine())
		{
			String inp = sc.nextLine();
			while(!inp.equals("") && inp.substring(0,1).equals(" "))
			{		
				inp=inp.substring(1,inp.length());
			}
			while(!inp.equals("") && inp.substring(inp.length()-1,inp.length()).equals(" "))
			{
				inp = inp.substring(0,inp.length()-1);
			}
			inp_arr[t_inp] = inp;
			t_inp++;
		}
		sc.close();
		// System.out.println();
		boolean hltatlast = false;
		boolean errored = false ;
		boolean mul_hlt = false ;
		boolean flag2 = false;
		java.util.LinkedList<Integer> hl = new java.util.LinkedList<Integer>();
		if(inp_arr[t_inp-1].equals("hlt") || inp_arr[t_inp-1].substring(inp_arr[t_inp-1].length()-3,inp_arr[t_inp-1].length()).equals("hlt"))
		{
			hltatlast = true ;
			flag2 = true;
		}
		Read r = new Read();
		int countNoOfHlt = 0;
		boolean flag = true;
		for(int a = 0;a<t_inp;a++)
		{
			
			if(inp_arr[a].equals("hlt"))
			{
				if(flag2 && flag){
					countNoOfHlt += 1;
				}
				if(countNoOfHlt>1){
					mul_hlt = true;
					flag = false;					
				}
				hl.add(a+1);
				countNoOfHlt +=1;
				if(a!=t_inp-1){
					continue;
				}
			}
			if(inp_arr[a].equals("")){
				continue;
			}
			String r_out = r.readF(inp_arr[a],a+1,t_inp);
			if(r_out.equals("")){
				continue;
			}if(r_out.substring(0,1).equals("E"))
			{
				System.out.println(r_out);
				errored = true ;
				break;
			}
			else if(r_out.substring(0,1).equals("0") || r_out.substring(0,1).equals("1") && flag)
			{
				insertInBArr(r_out);
			}else if(flag){
				inp_arr[a] = r_out;
				a-=1;
				continue;
			}
		}
		if(!errored)
		{
			if(mul_hlt){
				Object[] ob = hl.toArray();
				System.out.print("Error: multiple halt commands at lines ");
				for(Object o : ob)
				{
					if(o==ob[ob.length-1]){
						System.out.print("and "+o);
					}else{
						System.out.print(o+", ");
					}
				}
				System.out.println();
			}else if(!hltatlast){
				System.out.println("Error at Line No "+ t_inp +": last command should be hlt");
			}else{
				showBin();
			}
		}
	}
	
    public static String[] insertInBArr(String string)  
    {  					
    	String arr[] = arr_binary ;
	    if(string.length()>16)
	    {
			String tarr1[] = string.split("-");
	        arr[count_arr] = tarr1[0];
	        for(int i=1;i<tarr1.length;i++)
	        {
	            String tarr2[] = tarr1[i].split(" ");
	            arr[Integer.parseInt(tarr2[0])-Integer.parseInt(tarr2[2])] += tarr2[1];
	        }
	    }
	    else
	    {
            arr[count_arr] = string;
        }
		count_arr+=1;
		return arr;
	}
	public static void showBin()
	{
		String arr[] = arr_binary ;
		for (int i = 0; i<arr.length;i++) {
			if(arr[i]=="" || arr[i] ==null){
				break;
			}else{
				System.out.println(arr[i]);
			}
		}
	}

}
//////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////
class Read
{
	CheckAP ck = new CheckAP();
public String readF(String s, int lineno, int total_ln)
{
	String to_ret = "Error: line "+(lineno)+" not executed";
	while(s.substring(0,1).equals(" "))
	{
		s=s.substring(1,s.length());
	}
	String[] splitted = s.split("\\s+");
	int strlength = splitted.length;
	if((splitted[0].equals("add")||splitted[0].equals("sub")||splitted[0].equals("mul")||splitted[0].equals("xor")||splitted[0].equals("and")||splitted[0].equals("or"))&&(strlength==4))
	{
		String[] mc = {"error","error","error","error","error"};
		type_A a1 = new type_A();
		mc[0] = a1.opcode(splitted) ;
		mc[1] = a1.unused(splitted) ;
		mc[2] = a1.reg1(splitted) ;
		mc[3] = a1.reg2(splitted) ;
		mc[4] = a1.reg3(splitted) ;
		int i =0;
		for(String str: mc)
		{	i+=1;
			if(str.equals("error: invalid instruction name")||str.equals("error: invalid resistor name"))
			{
				to_ret = str+" in line "+lineno ;
			}
			else if(i==5)
			{
				to_ret=( mc[0]+mc[1]+mc[2]+mc[3]+mc[4]) ;
			}
		}
	}
	else if ((splitted[0].equals("mov")||splitted[0].equals("rs")||splitted[0].equals("ls"))&&(strlength==3)&&(splitted[2].substring(0, 1).equals("$")))
	{
		String[] mc = {"error","error","error","error"};
		type_B b1 = new type_B();
		mc[0] = b1.opcode(splitted);
		mc[1] = b1.reg(splitted);
		mc[2] = b1.immval(splitted);
		int j=0;
		for(String str: mc)
		{ 
			j+=1;
			if(str.equals("error: invalid instruction name")||str.equals("error: invalid resistor name")||str.equals("error: invalid immval")||str.equals("error: can not have immval >255 & <0"))
			{
				to_ret=(str+" in line "+lineno);
				j-=1;
			}else if(j==4){
				to_ret = mc[0] + mc[1] + mc[2];	
			}
		}
	}
	else if(((splitted[0].equals("mov")&&(  (splitted[2].substring(0,1).equals("R"))||(splitted[2].substring(0, 1).equals("F"))   )||(splitted[0].equals("div"))||(splitted[0].equals("not"))||(splitted[0].equals("cmp"))))&&(strlength==3))
	{
		String[] mc = {"error","error","error","error"};
		type_C c1 = new type_C();
		mc[0] = c1.opcode(splitted);
		mc[1] = c1.unused(splitted);
		mc[2] = c1.reg1(splitted);
		mc[3] = c1.reg2(splitted);
		int i =0;
		for(String str: mc)
		{
			i+=1;
			if(str.equals("error: invalid resistor name")||str.equals("error: invalid instruction name"))
			{
				to_ret = (str+" in line "+lineno);
			}
			else if(i==4)
			{
				to_ret = mc[0]+mc[1]+mc[2]+mc[3] ;
			}	
		}
	}
	else if(splitted[0].equals("hlt")&&(strlength==1))
	{
		to_ret ="1001100000000000" ;
	}
	else 
	{
		to_ret = ck.check(splitted,lineno, total_ln);
	}
	if(lineno ==total_ln && !to_ret.equals("") && ((to_ret.substring(0,1).equals("0")) || (to_ret.substring(0,1).equals("1")))){
		to_ret = ck.checkLabelIntersect(to_ret, lineno);
	}
	return to_ret;
}
}

//////////////////////////////////////////////END MAIN CLASS//////////////////////////////////////////////


////////////////////////////////////////////CheckAP class/////////////////////////////////////////
class CheckAP{
	Helper hlp = new Helper();
	LinkedList headVar = new LinkedList();      
	LinkedList headLabel = new LinkedList();    
	LinkedList headReq = new LinkedList();      
	boolean timeHasPassesdForVar = false;
	public int count_v = 0;    
	boolean flag;
	String s;
	String a;
	public String st_label = " ";  

	//////////////////////////CHECK WHETHER THE NAME IS ALPHANUMERIC OR NOT/////////////////////
	public static boolean checkName(String s) {
		char ch[] = s.toCharArray();
		for (char c : ch) {
			int t = (int)c;
			if ((64<t && t<91) || (96<t && t<123) || t==95 || (47<t && t<58)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
	///////////////////////////////////////////////////////////////////////////////////////

	///////////////////////to check the intersection of required and availaible labels///I THINK CALLING IT FROM READER WOULD BE BETTER WHEN THE LINE NO IS TOTAL-1 AND FORM THE HLT SIDE
		public String checkLabelIntersect(String str, int line_no_of_inst){
			boolean f;
			int count = 0;
			if(headReq.startM==null){
				}else{
					NodeM temp1 = headReq.startM;
					while(temp1!=null){
						f= false;
						NodeM temp2 = headLabel.startM;
						if(checkName(temp1.getData().substring(0,temp1.getData().length()-1))) {
							while(temp2!= null){
								if(temp1.getData().equals(temp2.getData())){
									String string = Integer.toBinaryString(temp2.getIndex()-1);
									str += "-" + temp1.getIndex() + " " +String.format("%8s", string).replaceAll(" ", "0")+" "+count_v;
									f = true;
								}
								temp2=temp2.getNext();
							}
						}else {
							str = "Error at Line no. "+(temp1.getIndex()+1)+st_label+": Invalid Declaration of Label name.";
							return str;
						}
						count+=1;
						if(!f){         
							str = "Error at Line no. "+(temp1.getIndex()+1)+st_label+": No such label declared";
							return str;
						}
						temp1=temp1.getNext();
						headReq.deleteAtPosM(count);
					}  
				}
			return str;
		}

	//////////////////////////FUNCTION WHICH CHECKS THE ERROR AND CONVERTS INTO BINARY CODE//////////////////////////
	public String check(String arr[], int line_no_of_inst, int total_input_no) {  
		flag = true;
		int count = 0;
		if(arr[0].equals("var")) {
			if(arr.length == 2) { 
				if(checkName(arr[1])) {
					headVar.insert(arr[1]);  
					count_v += 1;
					s="";
				}else{
					s = "Error at Line no. "+line_no_of_inst+st_label+": Invalid Declaration of variable name.";
				}
			}else{
				s = "Error at Line no. "+line_no_of_inst+st_label+": Invalid no of operands given for instruction type: Declaration of Variable.";
			}
			if(timeHasPassesdForVar){
				s = "Error at Line no. "+line_no_of_inst+st_label+": Variable Declared at wrong position.";
			}
		}else if(arr[0].equals("ld") || arr[0].equals("st")) {  //// this instruction loads the values in the registers
			count = 0;
			timeHasPassesdForVar = true;
			if(arr.length ==3){				
				if(hlp.checkRegisterName(arr[1])) { 
					Node temp = headVar.start;timeHasPassesdForVar = true;
					while(temp!=null) {
						if(arr[2].equals(temp.getData())){
							s = hlp.getOpcode(arr[0]);   
							a = Integer.toBinaryString(Character.getNumericValue(arr[1].charAt(arr[0].length()-1))); 
							s += String.format("%3s", a).replaceAll(" ", "0");  
							a = Integer.toBinaryString(count+total_input_no-count_v); 
							s += String.format("%8s", a).replaceAll(" ", "0");  
							flag = false;
							break;
						}
						temp = temp.getNext();
						count+=1;
					}
					if(flag) {
						s = "Error at Line no. "+line_no_of_inst+st_label+": No such Variable Declared.";
					}
				}else {
					s = "Error at Line no. "+line_no_of_inst+st_label+": No such Register defined.";
				}
			}else {
				s = "Error at Line no. "+line_no_of_inst+st_label+": Invalid no of operands given for instruction Type: D";
			}
		}else if (arr[0].equals("jmp") || arr[0].equals("jlt") || arr[0].equals("jgt") || arr[0].equals("je")) {
			count = 0;
			timeHasPassesdForVar = true;
			if(arr.length==2) {
				headReq.insertM(arr[1], line_no_of_inst-1);
				s = hlp.getOpcode(arr[0]);
				s += "000";
			}else {
				s = "Error at Line no. "+line_no_of_inst+st_label+": Invalid no of operands given for instruction Type: E.";
			}
		}else if (arr[0].substring(arr[0].length()-1).equals(":")) {    
			timeHasPassesdForVar = true;
			if(checkName(arr[0].substring(0,arr[0].length()-1))) { 
				if(1<arr.length && arr.length<6) {  
					String tstring="";
					headLabel.insertM(arr[0].substring(0,arr[0].length()-1), line_no_of_inst-1);
					for (int i = 1; i < arr.length; i++) {
						tstring += arr[i]+" ";
					}
					st_label = " in the Instruction iside the Declared Label";
					return tstring;
				}else {
					s = "Error at Line no. "+line_no_of_inst+st_label+": Invalid no of operands for instruction type: Declaration of Label.";
				}
			}else {
				s = "Error at Line no. "+line_no_of_inst+st_label+": Invalid Declaration of Label name.";
			}
		}else if(arr[0].equals("hlt")){
			timeHasPassesdForVar = true;
			s = "1001100000000000";
		}else{
			s = "Error at Line no. "+line_no_of_inst+st_label+": Invalid Instruction type for the ISA.";
		}
		return s;
	}
}
//////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////HELPER CLASS WHICH IMPLEMENTS - CHECKING REGISTER NAMES AND RETURNS OPCODE/////////////////////
class Helper{
public boolean checkRegisterName(String s){
	if(s.equals("R0") || s.equals("R1") || s.equals("R2") || s.equals("R3") || s.equals("R4") || s.equals("R5") || s.equals("R6") || s.equals("FLAGS")){
		return true;
	}
	return false;
}
public String getOpcode(String s){
	if(s.equals("ld")){
		return "00100";
	}
	if(s.equals("st")){
		return "00101";
	}
	if(s.equals("jmp")){
		return "01111";
	}
	if(s.equals("jlt")){
		return "10000";
	}
	if(s.equals("jgt")){
		return "10001";
	}
	if(s.equals("je")){
		return "10010";
	}
	return "0";												
}
}

//////////////////////////////////////////FORMATION OF LINKED LIST/////////////////////////////	
class LinkedList{
public NodeM startM;
public Node start;
public int size;
public LinkedList() {
	size = 0;
	start = null;
	startM = null;
}
public boolean isEmpty() {
	return (size == 0);
}
		
public void insert(String val) {
	Node ptr = new Node(val);
	Node temp = start;
	if (isEmpty()) {
		start = ptr;
	} else {
		while (temp != null) {
			if (temp.getNext() == null) {
				temp.setNext(ptr);
				break;
			}
			temp = temp.getNext();
		}
	}
	size += 1;
}
public void insertM(String val, int indexInmem) {
	NodeM ptrM = new NodeM(val);
	NodeM tempM = startM;
	ptrM.setIndex(indexInmem);
	if (isEmpty()) {
		startM = ptrM;
	} else {
		while (tempM != null) {
			if (tempM.getNext() == null) {
				tempM.setNext(ptrM);
				break;
			}
			tempM = tempM.getNext();
		}
	}
	size += 1;
}

public void deleteAtPosM(int ival) {         // POSITION IS CONSIDERED AS NATURAL NUMBER WHILE THE INDEX IS STARTING FROM 0 TO N-1
	int i = 0;
	NodeM temp = startM;
	while (i < size) {
		if (i + 1 == ival - 1) {
			temp.setNext(temp.getNext().getNext());
			break;
		}
		temp = temp.getNext();
		i += 1;
	}
	size -= 1;
}	
}
/////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////// class for making of linked list for storing label /////////////////////////
class NodeM{
private String data;
private NodeM next;
private int indInM;
public NodeM(String val) {
	data = val;
	next = null;
	indInM = -1;
}
public NodeM (String d, int k) {
	data = d;
	next = null;
	indInM = k;
}
public void setNext(NodeM n) {
	next = n;
}

public void setData(String d) {
	data = d;
}
public void setIndex(int k) {
	indInM = k;
}

public NodeM getNext() {
	return next;
}
public int getIndex() {
	return indInM;
}

public String getData() {
	return data;
}	
}
////////////////////////////////////////////////////////////////////
 
////////////////////////////////////////////////////////////////////
class Node {
private String data;
private Node next;
public Node() {
	data = "";
	next = null;
}

public Node(String d, Node n) {
	next = n;
	data = d;
}
public Node(String d) {
	data = d;
}

public void setNext(Node n) {
	next = n;
}

public Node getNext() {
	return next;
}

public String getData() {
	return data;
}
}	
////////////////////////////////////////////////////////////////////////////////////

///////////////////////// CLASS helping to TAKe THE INPUT from user////////////////////////////
class Reader {
static BufferedReader reader;
static StringTokenizer tokenizer;

/** call this method to initialize reader for InputStream */
static void init(InputStream input) {
	reader = new BufferedReader(
	    new InputStreamReader(input) );
	tokenizer = new StringTokenizer("");
}

/** get next word */
static String next() throws IOException {
	while ( ! tokenizer.hasMoreTokens() ) {
		tokenizer = new StringTokenizer(
		    reader.readLine() );
	}
	return tokenizer.nextToken();
}
static String nextLine() throws IOException {
	return reader.readLine();
}

static int nextInt() throws IOException {
	return Integer.parseInt( next() );
}

static double nextDouble() throws IOException {
	return Double.parseDouble( next() );
}
}

class type_A
{
public String opcode(String[] a)
	{
		String opcode = "error: invalid instruction name";
		if(a[0].equals("add"))
		{
			opcode = "00000";
		}
		if(a[0].equals("sub"))
		{
			opcode = "00001";
		}
		if(a[0].equals("mul"))
		{
			opcode = "00110";
		}
		if(a[0].equals("xor"))
		{
			opcode = "01010";
		}
		if(a[0].equals("or"))
		{
			opcode = "01011";
		}
		if(a[0].equals("and"))
		{
			opcode = "01100";
		}
		return opcode ;
		
	}
	
public String reg1(String[] a)
	{
		String reg1 = "error" ;
		regs r1 = new regs();
		reg1 = r1.convert(a[1]);
		return (reg1);
	}
	
public String reg2(String[] a)
	{
		String reg2 = "error" ;
		regs r2 =new regs();
		reg2 = r2.convert(a[2]);
		return (reg2);
	}
 	
public String reg3(String[] a)
	{
		String reg3 = "error" ;
		regs r3 = new regs();
		reg3 = r3.convert(a[3]);
		return (reg3);
	}
	
public String unused(String[] a)
	{
		String unused = "00";
		return unused ;
	}
}

class type_B
{
public String reg(String[] a)
{
	regs r2 = new regs();
	String reg2 = r2.convert(a[1]);
	return (reg2);
}

public String immval(String[] a)
{
	
	String[] st = a[2].split("");	
	String[] ints = {"0","1","2","3","4","5","6","7","8","9"};
	if((st[0].equals("$"))&& (st.length>1))
	{
		st[0]="0";
		for(String s : st)
		{
			if(!(Arrays.asList(ints).contains(s)))
			{
				return ("error: invalid immval");
			}
		}
		int con = Integer.parseInt(a[2].substring(1));
		if(con>255||con<0)
		{
			return ("error: can not have immval >255 & <0");
		}
		String str = Integer.toBinaryString(con);
		String immval = ("0".repeat(8-str.length())+str);
		return immval;
	}
	else
	{
		return ("error: invalid immval");
	}
}

public String opcode(String[]a)
{
	String opcode = "error: invalid instruction name" ;
	if(a[0].equals("mov"))
	{
		opcode = "00010";
	}
	else if(a[0].equals("rs"))
	{
		opcode = "01000";
	}
	else if(a[0].equals("ls"))
	{
		opcode = "01001";	
	}
	return (opcode) ;
}
}

class type_C
{
public String opcode(String[] a)
{
	String opcode = "error: invalid instruction name";
	if(a[0].equals("mov"))
	{
		opcode = "00011";
	}
	if(a[0].equals("div"))
	{
		opcode = "00111";
	}
	if(a[0].equals("not"))
	{
		opcode = "01101";
	}
	if(a[0].equals("cmp"))
	{
		opcode = "01110";
	}
	return opcode ;
}

public String unused(String[] a)
{
	String unused ="00000";
	return unused;
}

public String reg1(String[] a)
{
	regs r1 = new regs();
	String reg1 = r1.convert(a[1]);
	return (reg1);
}

public String reg2(String[] a)
{
	regs r2 = new regs();
	String reg2 = r2.convert(a[2]);
	return (reg2);
}
}

class regs
{
public String convert(String a)
	{
		String reg = "error: invalid resistor name";
		if(a.equals("R0"))
		{
			reg="000";
		}
		else if(a.equals("R1"))
		{
			reg="001";
			
		}
		else if(a.equals("R2"))
		{
			reg="010";
		}
		else if(a.equals("R3"))
		{
			reg="011";
		}
		else if(a.equals("R4"))
		{
			reg="100";
		}
		else if(a.equals("R5"))
		{
			reg="101";
		}
		else if(a.equals("R6"))
		{
			reg="110";
		}
		else if(a.equals("FLAGS"))
		{
			reg="111";
		}
		return (reg);
	}
}


//////////////////////////////////////END OF PROGRAM////////////////////////////
