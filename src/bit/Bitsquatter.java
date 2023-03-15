package bit;
import java.swing.*;
import java.util.*;
import java.io.*;
import org.apache.commons.net.whois.WhoisClient;
import  java.lang.*;

public class Bitsquatter {
	//checks availability of a domain
	public static void checkDom(ArrayList<String>doms) {
		ArrayList<String>match = new ArrayList<String>();
		match.add("NOT FOUND");
		match.add("No Match For");
		match.add("Not fo");
		match.add("has not been regi");
		match.add("No entri");
		match.add("Not AVAILABLE");
		
		Map<String,String> srvlist = new HashMap <String,String>();
		ArrayList<String> urlSrvUniq = new ArrayList <String>();
		
		for(int i_00=0;i_00<doms.size();i_00++) {
			try {
				BufferedReader br00 = new BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\whoisSrv\\ExtSrvList.txt"));
				BufferedReader br01 = new  BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\whoisSrv\\noExtWhoisList.txt"));
				
				String line00,line01;
				
				 while ((line00=br00.readLine()) !=null && (line01=br01.readLine())!=null)srvlist.put(line00,line01);
				 br00.close();
				 br01.close();
				 
				 ArrayList<String>urlSrv = new ArrayList <String>();
				 String ext ="";
				 for(int i_01=0;i_01<doms.size();i_01++)
				 {
					 for(int j_00=0;j_00<doms.get(i_01).length();j_00++)
					 {
						 if(doms.get(i_01).charAt(j_00)== '.') {
							 ext = doms.get(i_01).substring(j_00+1,doms.get(i_01).length());
							 urlSrv.add(srvlist.get(ext));
							 break;
						 }
					 }
				 }
				 urlSrv.removeAll(Collections.singleton(null));
				 Collections.sort(urlSrv);
				 
				 for(int i_02=0;i_02<urlSrv.size();i_02++) {
					 if(!urlSrvUniq.contains(urlSrv.get(i_02)))
						 urlSrvUniq.add(urlSrv.get(i_02));
				 }
			}
			catch (Exception e) {
				System.out.println("Line 59" +e);
			}
			for(int j_01=0;j_01<urlSrvUniq.size();j_01++) {
				try {
					StringBuilder res = new StringBuilder("");
					WhoisClient whois = new WhoisClient();
					whois.connect(urlSrvUniq.get(j_01));
					String whoisData = whois.query("=" +doms.get(i_00));
					
					res.append(whoisData);
					whois.disconnect();
					
					String result = res.toString();
					//System.out.println(result);
					
					boolean test = false;
					for(int i_03 = 0;i_03 < match.size();i_03++)
					{
						if(result.contains(match.get(i_03))) 
						{
							test = true;
							break;
							
						}
						
					}
					if (test==false)
						System.out.println("OUERY SERVER:"+urlSrvUniq.get(j_01)+"\n"+
					"DOMAIN:"+ doms.get(i_00)+"Might be Available."+"\n");
					
					
				}
				catch (Exception e) {
					System.out.println("Line 105" + e);
				}
				
			}
		}
		
	}
	//converts from binary to text
	public static ArrayList<String>candidatesDom(ArrayList<String>can){
		ArrayList<String> canList = new ArrayList<String>();
		for(int i=0;i<can.size();i++) {
			if(can.get(i).length()==7) {
			String reverse00= new StringBuffer(can.get(i)).reverse().toString();
			reverse00 += "0";
			String reverse01=new StringBuffer(reverse00).reverse().toString();
			can.remove(i);
			can.add(i,reverse01);
		}
		else if (can.get(i).length()==6)
		{
			String reverse00 =new StringBuffer(can.get(i)).reverse().toString();
			reverse00+="0";
			String reverse01 = new StringBuffer(reverse00).reverse().toString();
			can.remove(i);
			can.add(i,reverse01);
		}
		}
		for (int j=0;j<can.size();j++) {
			String tmpStr01="";
			String tmpStr00 =can.get(j);
			for(int i=0;i<tmpStr00.length()/8;i++) {
				int a = Integer.parseInt(tmpStr00.substring(8*i,(i+1)*8),2);
				tmpStr01+= (char)(a);
				
			}
			canList.add(tmpStr01);
			
		}
		return canList;
	}
	//corrects the bits and checks the error
	public static ArrayList<String>validDomain (ArrayList<String>binDom){
		/* illegal chars
		 * =+.,/'\;[]!@#$%^&*()<>?:"|}{}_
		 * length 3-63
		 * Cannot Start with -
		 * Cannot end with
		 * Can have - in the middle
		 * 
		 * legal Chars numbers,letters and - in the middle
		 * 
		 */
		ArrayList<String>txtDom = new ArrayList<String>(candidatesDom(binDom));
		ArrayList<String>txtDomValid = new ArrayList<String>();
		for(int i=0; i<txtDom.size();i++) {
			String stripped =txtDom.get(i).replaceAll("[^-zA-z0-9-.]","");
			String strippedLower = stripped.toLowerCase();
				if(strippedLower.charAt(0)=='-'||strippedLower.charAt(strippedLower.length()-1)=='-'||strippedLower.length() != txtDom.get(i).length())
				continue;
				else
					txtDomValid.add(strippedLower);
				
		}
	ArrayList<String>uniqList = new ArrayList<String>();
	for(int i =0;i<txtDomValid.size();i++) {
		if(uniqList.contains(txtDomValid.get(i)))
			continue;
		else uniqList.add(txtDomValid.get(i));
	}
	return uniqList;
}
	//generates a list of candidates URL
	public static ArrayList<String> candidatesUrl(String binDom){
		ArrayList<String> candidates = new ArrayList<String>();
		for (int i=0;i<binDom.length();i++) {
			if(binDom.charAt(i)== '0') {
				char[] tmpChar = binDom.toCharArray();
				tmpChar[i]= '1';
				String tmpStr = new String(tmpChar);
				candidates.add(tmpStr);
			}
			else if (binDom.charAt(i)=='1') {
				char[] tmpChar = binDom.toCharArray();
				tmpChar[i] ='0';
				String tmpStr = new String (tmpChar);
				candidates.add(tmpStr);
			}
		}
		return candidates;
		
	}
	//first method gets the bitmask of the domain
	public static String bitMaskofDomain(String Dom) {
		ArrayList<String> bitMask = new ArrayList<String>();
		String tmp = "";
		//converts the array list to individual char array
		char[] tmpChar = Dom.toCharArray();
		//converts the character to bits and concats it to 
		for(int i =0;i<tmpChar.length;i++) {
			tmp+=Integer.toBinaryString(tmpChar[i]);
			bitMask.add(tmp);
			tmp="";
		}
		//create correction of character bits to bit
		String bitMaskStr ="";
		for (int i=0;i<bitMask.size();i++) {
			if(bitMask.get(i).length()==7) {
				//takes the bits and reverses it and converts it to a string
				String reverse00 = new StringBuffer(bitMask.get(i)).reverse().toString();
				//takes the string reverse00 and concats it with 0 at the end
				reverse00+="0";
				//reverses the string again to its normal form
				String reverse01 = new StringBuffer(reverse00).reverse().toString();
				bitMask.remove(i);
				bitMask.add(i,reverse01);
				bitMaskStr += bitMask.get(i);
				
			}
			else if (bitMask.get(i).length()==6) {
				String reverse00 = new StringBuffer(bitMask.get(i)).reverse().toString();
				reverse00+="00";
				String reverse01 = new StringBuffer(reverse00).reverse().toString();
				bitMask.remove(i);
				bitMask.add(i,reverse01);
				bitMaskStr += bitMask.get(i);
			}
			else {bitMaskStr += bitMask.get(i);}
		}
		return bitMaskStr;
	}
	//
	public static void main(String[] args) {
		// TODO Auto-generated method stub
String url;
JFrame frame = new JFrame ();
JButton button = new JButton();
button.setSize(150,80);
button.setText("Submit");
button.addActionListener(new ActionListener(){
public void actionPerformed(Action e){
if(e.getSource()==button){
url=text.getText;
}

}


});

JTextField text = new JTextField();
text.setSize (100,30);
text.setText("Enter url");
frame.add(text);
frame.pack();	checkDom(validDomain(candidatesUrl(bitMaskofDomain(Url))));
		//System.out.println( validDomain(candidatesUrl(bitMaskofDomain(Url))));
				
	}
	}

