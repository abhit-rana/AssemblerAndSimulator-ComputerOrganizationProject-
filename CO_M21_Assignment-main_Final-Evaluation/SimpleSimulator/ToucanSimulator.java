import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
// import StdIn;
///////////////////  Dought in what way the graph shoud be peinted????????????////////////////////////////
///////////////////  ask shobhit to put name of file to be "makeGraph" //////////////////////////////
public class ToucanSimulator {

	public static void main(String[] args) throws IOException 
	{
		Memory meM = new Memory();
	    execution eE = new execution();
	    Register rF = new Register();
	    Scanner sc = new Scanner(System.in);
		program_counter pc = new program_counter();
		meM.getInput(sc);
	    String inst;
	    boolean halt = false;
	    while(!halt)
	    {
	        inst = meM.fetch(pc.get_data());
	        halt = eE.execute(inst);
            pc.dump();
	        rF.dump();
			program_counter.increase();
	    }
	    meM.dump();
		// System.out.println(pc.geTstring_pc());
		// sc.nextLine();
		String temp = pc.geTstring_pc();
		printGraph(temp);
	}

	public static void printGraph(String y_axis) throws IOException{
		String arr[] = new String[3];
		arr[0] = "python3";
		arr[1] = "makeGraph.py";
		arr[2] = y_axis;
		try {
			ProcessBuilder builder = new ProcessBuilder(arr);
            Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader reader_error = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String lines = null;
			// System.out.println("in3");
			while((lines=reader.readLine())!=null){
				// System.out.println("in2");
				System.out.println(lines);
			}
			while((lines=reader_error.readLine())!=null){   //// dought don't know the reason of second while loop????//////
				// System.out.println("in error handle");
				System.out.println(lines);
			}
		} catch (Exception e) {
			//TODO: handle exception
			e.printStackTrace();
		}
	}
}

class program_counter
{   
    static int arr_pc[] = new int[256];
	private static int set_at;
	private static int ins = 0;
	private static String string_pc = "";
	public static void jump(int mem_add)
	{
		ins = mem_add;
		// Register.reset_flag();
	}
	public static void increase()
	{
		ins++;
		if((set_at!=-1)&&(ins>set_at+1))
		{
			// Register.reset_flag();
			set_at =-1 ;
		}
	}
	public static void set()
	{
		set_at = ins ;
	}
	public int get_data()
	{
		string_pc = string_pc +ins+" " ;
		return ins ;
	}
	public void dump()
	{
		String temp = Integer.toBinaryString(ins);
        System.out.print("0".repeat(8-temp.length())+temp+" ");
	}
	public String geTstring_pc(){
		return string_pc;
	}
}
class execution
{
	int flag = -1;
	public boolean execute (String mac_code)
	{
		String opcode = mac_code.substring(0,5);
		if(opcode.equals("00000"))
		{
			int r1 = Integer.parseInt(mac_code.substring(7,10),2);
			int r2_val = Register.get_data(Integer.parseInt(mac_code.substring(10,13),2));
			int r3_val = Register.get_data(Integer.parseInt(mac_code.substring(13,16),2));
			if(r2_val+r3_val>= Math.pow(2,16))
			{
				Register.set_data(r1,0);
				Register.set_flag("V");
				flag =1;
			}
			else
			{
				Register.set_data(r1,r2_val+r3_val);
			}
		}
		else if (opcode.equals("00001"))
		{
			int r1 = Integer.parseInt(mac_code.substring(7,10),2);
			int r2_val = Register.get_data(Integer.parseInt(mac_code.substring(10,13),2));
			int r3_val = Register.get_data(Integer.parseInt(mac_code.substring(13,16),2));
			if(r2_val<r3_val)
			{
				Register.set_data(r1,0);
				Register.set_flag("V");
				flag =1;
			}
			else
			{
				Register.set_data(r1,r2_val-r3_val);
			}
		}
		else if (opcode.equals("00010")) 
		{
			int r1 = Integer.parseInt(mac_code.substring(5,8),2);
			Register.set_data(r1, Integer.parseInt(mac_code.substring(8,16),2));
		}
		else if (opcode.equals("00011"))
		{
			Register.set_data(Integer.parseInt(mac_code.substring(10,13),2), Register.get_data(Integer.parseInt(mac_code.substring(13,16),2)));
		}
		else if (opcode.equals("00100"))
		{
			int reg1 = Integer.parseInt(mac_code.substring(5, 8),2);
			Memory m = new Memory();
			int temp = Integer.parseInt(m.load(Integer.parseInt(mac_code.substring(8, 16),2)));
			Register.set_data(reg1, temp);
		}
		else if (opcode.equals("00101"))
		{
			String s =Integer.toBinaryString(Register.get_data(Integer.parseInt(mac_code.substring(5, 8),2)));
			Memory m = new Memory();
			m.store("0".repeat(16-s.length())+s,Integer.parseInt(mac_code.substring(8,16),2));
		}
		else if (opcode.equals("00110"))
		{
			int r1 = Integer.parseInt(mac_code.substring(7,10),2);
			int r2_val = Register.get_data(Integer.parseInt(mac_code.substring(10,13),2));
			int r3_val = Register.get_data(Integer.parseInt(mac_code.substring(13,16),2));
			if(r2_val*r3_val>= Math.pow(2,16))
			{
				Register.set_data(r1,0);
				Register.set_flag("V");
				flag = 1;
			}
			else
			{
				Register.set_data(r1,r2_val*r3_val);
			}
		}
		else if (opcode.equals("00111"))
		{
			int r2_val = Register.get_data(Integer.parseInt(mac_code.substring(10,13),2));
			int r3_val = Register.get_data(Integer.parseInt(mac_code.substring(13,16),2));
			Register.set_data(0, r2_val/r3_val);
			Register.set_data(1,  r2_val%r3_val);
		}
		else if (opcode.equals("01000"))
		{
			int r1 = Integer.parseInt(mac_code.substring(5,8),2);
			Register.set_data(r1,Register.get_data(r1)>>Integer.parseInt(mac_code.substring(8,16),2));
		}
		else if (opcode.equals("01001"))
		{
			int r1 = Integer.parseInt(mac_code.substring(5,8),2);
			Register.set_data(r1,Register.get_data(r1)<<Integer.parseInt(mac_code.substring(8,16),2));
		}
		else if (opcode.equals("01010"))
		{
			int r1 = Integer.parseInt(mac_code.substring(7,10),2);
			int r2_val = Register.get_data(Integer.parseInt(mac_code.substring(10,13),2));
			int r3_val = Register.get_data(Integer.parseInt(mac_code.substring(13,16),2));
			Register.set_data(r1,r2_val^r3_val);
		}
		else if (opcode.equals("01011"))
		{
			int r1 = Integer.parseInt(mac_code.substring(7,10),2);
			int r2_val = Register.get_data(Integer.parseInt(mac_code.substring(10,13),2));
			int r3_val = Register.get_data(Integer.parseInt(mac_code.substring(13,16),2));
			Register.set_data(r1,r2_val|r3_val);	
		}
		else if (opcode.equals("01100"))
		{
			int r1 = Integer.parseInt(mac_code.substring(7,10),2);
			int r2_val = Register.get_data(Integer.parseInt(mac_code.substring(10,13),2));
			int r3_val = Register.get_data(Integer.parseInt(mac_code.substring(13,16),2));
			Register.set_data(r1,r2_val&r3_val);
		}
		else if (opcode.equals("01101"))
		{
			Register.set_data(Integer.parseInt(mac_code.substring(10,13),2),~Register.get_data(Integer.parseInt(mac_code.substring(7,16),2)));
			
		}
		else if (opcode.equals("01110"))
		{
			int r1_val = Register.get_data(Integer.parseInt(mac_code.substring(10,13),2));
			int r2_val = Register.get_data(Integer.parseInt(mac_code.substring(13,16),2));
			if(r1_val<r2_val)
			{
				Register.set_flag("L");
				flag =1;
			}
			else if(r1_val>r2_val)
			{
				Register.set_flag("G");
				flag =1;
			}
			else
			{
				Register.set_flag("E");
				flag =1;
			}
		}
		else if (opcode.equals("01111"))
		{
			int mem_add = Integer.parseInt(mac_code.substring(8,16));
			if(Register.flag.substring(12,13).equals("1") )
			{
				program_counter.jump(mem_add);
			}
		}
		else if (opcode.equals("10000"))
		{
			int mem_add = Integer.parseInt(mac_code.substring(8,16));
			if(Register.flag.substring(13,14).equals("1") )
			{
				program_counter.jump(mem_add);
			}
		}
		else if (opcode.equals("10001"))
		{
			int mem_add = Integer.parseInt(mac_code.substring(8,16));
			if(Register.flag.substring(14,15).equals("1") )
			{
				program_counter.jump(mem_add);
			}
		}
		else if (opcode.equals("10010"))
		{
			int mem_add = Integer.parseInt(mac_code.substring(8,16));
			if(Register.flag.substring(15,16).equals("1") )
			{
				program_counter.jump(mem_add);
			}
		}
		if(flag==0){
			Register.reset_flag();
			flag=1;
		}

		if (opcode.equals("10011"))
		{
			flag-=1;
			return true;
		}
		flag-=1;
		return false ;
		
		
	}
}

class Register
{

	private static int[] regs = new int[7];
	static String flag = "0000000000000000";
	public static void set_flag(String a)
	{
		if(a.equals("V"))
		{
			flag ="0000000000001000";
		}
		else if (a.equals("L"))
		{
			flag ="0000000000000100";
		}
		else if (a.equals("G"))
		{
			flag ="0000000000000010";
		}
		else if (a.equals("E"))
		{
			flag ="0000000000000001";
		}
		program_counter.set();
	}
	public static void reset_flag()
	{
		flag ="0000000000000000";
	}
	public static void set_data( int reg ,  int data )
	{
		regs[reg] = data ;
	}
	public static int get_data( int register )
	{
		// System.out.println(register);
		if(register==7){
			return Integer.parseInt(flag, 2);
		}else{
			return regs[register];
		}
	}
	public void dump()
	{
		for(int i : regs)
		{
			String g = Integer.toBinaryString(i);
			System.out.print("0".repeat(16-g.length())+g+" ");
		}
		System.out.print(flag);
		System.out.println();
	}
}

class Memory {
	static String mem_arr[] = new String[256];
	public void getInput(Scanner input){           //// this is used to take input from the stdin
		String temp;
		int i = 0;
        while(input.hasNextLine()){
            if(i>255){
                break;
            }
            temp = input.nextLine();
            mem_arr[i] = temp;
            i+=1;
        }
		// input.close();
	}
	 
	public  String fetch(int pc){    //// this is used to give the instruction stored in the address: pc pointed by the PC
	    String inst;
		inst = mem_arr[pc];
		return inst;
	}
		  
	public  void store(String value, int addr){   //// this is used for instruction st R4 X, it will require value to be stored in address which would be the x itself
		mem_arr[addr] = value;
	}
	public  String load(int addr){     //// return the data pointed by the send address addr. //// for inst ld R4 x
		String data;
		data = mem_arr[addr];
		return data;
	}
		 
	public  void dump(){
	/// print the entire memory
		for (String string : mem_arr) {
            if(string==null){
                System.out.println("0000000000000000");
            }else{
		        System.out.println(string);
            }
		}
	}
}
