package bit;

import java.util.*;
import java.io.*;
import org.apache.commons.net.whois.WhoisClient;
import  java.lang.*;
import java.awt.*;

public class Bitsquatter {
    //checks availability of a domain
    public static String checkDom(ArrayList<String>doms) {
        ArrayList<String>match = new ArrayList<>();
        match.add("NOT FOUND");
        match.add("No Match For");
        match.add("Not fo");
        match.add("has not been regi");
        match.add("No entry");
        match.add("Not AVAILABLE");

        Map<String,String> srvlist = new HashMap<>();
        ArrayList<String> urlSrvUniq = new ArrayList<>();

        for(int i_00=0;i_00<doms.size();i_00++) {
            try {
                BufferedReader br00 = new BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\whoisSrv\\ExtSrvList.txt"));
                BufferedReader br01 = new  BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\whoisSrv\\noExtWhoisList.txt"));

                String line00,line01;

                while ((line00=br00.readLine()) !=null && (line01=br01.readLine())!=null)srvlist.put(line00,line01);
                br00.close();
                br01.close();

                ArrayList<String>urlSrv = new ArrayList<>();
                String ext;
                for (String dom : doms) {
                    for (int j_00 = 0; j_00 < dom.length(); j_00++) {
                        if (dom.charAt(j_00) == '.') {
                            ext = dom.substring(j_00 + 1);
                            urlSrv.add(srvlist.get(ext));
                            break;
                        }
                    }
                }
                urlSrv.removeAll(Collections.singleton(null));
                Collections.sort(urlSrv);

                for (String s : urlSrv) {
                    if (!urlSrvUniq.contains(s))
                        urlSrvUniq.add(s);
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
            for (String s : urlSrvUniq) {
                try {
                    StringBuilder res = new StringBuilder();
                    WhoisClient whois = new WhoisClient();
                    whois.connect(s);
                    String whoisData = whois.query("=" + doms.get(i_00));

                    res.append(whoisData);
                    whois.disconnect();

                    String result = res.toString();
                    boolean test = false;

                    for (String value : match) {
                        if (result.contains(value)) {
                            test = true;
                            break;

                        }

                    }
                    if (!test) {
                        return ("OUERY SERVER:" + s + "\n" + "DOMAIN:" + doms.get(i_00) + "Might be Available.");
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }

            }

        }
        return  null;


    }
    //converts from binary to text
    public static ArrayList<String>candidatesDom(ArrayList<String>can){
        ArrayList<String> canList = new ArrayList<>();
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
        for (String s : can) {
            StringBuilder tmpStr01 = new StringBuilder();
            String tmpStr00 = s;
            for (int i = 0; i < tmpStr00.length() / 8; i++) {
                int a = Integer.parseInt(tmpStr00.substring(8 * i, (i + 1) * 8), 2);
                tmpStr01.append((char) (a));

            }
            canList.add(tmpStr01.toString());

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
        for (String s : txtDom) {
            String stripped = s.replaceAll("[^-zA-z0-9-.]", "");
            String strippedLower = stripped.toLowerCase();
            if (strippedLower.charAt(0) == '-' || strippedLower.charAt(strippedLower.length() - 1) == '-' || strippedLower.length() != s.length()) {
            }
            else
                txtDomValid.add(strippedLower);

        }
        ArrayList<String>uniqList = new ArrayList<String>();
        for (String s : txtDomValid) {
            if (uniqList.contains(s)) {
            }
            else uniqList.add(s);
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
        for (char c : tmpChar) {
            tmp += Integer.toBinaryString(c);
            bitMask.add(tmp);
            tmp = "";
        }
        //create correction of character bits to bit
        StringBuilder bitMaskStr = new StringBuilder();
        for (int i=0;i<bitMask.size();i++) {
            if(bitMask.get(i).length()==7) {
                //takes the bits and reverses it and converts it to a string
                String reverse00 = new StringBuffer(bitMask.get(i)).reverse().toString();
                //takes the string reverse00 and concat it with 0 at the end
                reverse00+="0";
                //reverses the string again to its normal form
                String reverse01 = new StringBuffer(reverse00).reverse().toString();
                bitMask.remove(i);
                bitMask.add(i,reverse01);
                bitMaskStr.append(bitMask.get(i));

            }
            else if (bitMask.get(i).length()==6) {
                String reverse00 = new StringBuffer(bitMask.get(i)).reverse().toString();
                reverse00+="00";
                String reverse01 = new StringBuffer(reverse00).reverse().toString();
                bitMask.remove(i);
                bitMask.add(i,reverse01);
                bitMaskStr.append(bitMask.get(i));
            }
            else {
                bitMaskStr.append(bitMask.get(i));}
        }
        return bitMaskStr.toString();
    }
}

