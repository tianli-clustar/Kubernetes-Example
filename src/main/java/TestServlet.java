import ch.qos.logback.core.net.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.rabbitmq.client.*;

class SerialTestClass implements Serializable{
    public int iVal;
    public String sVal;
    public SerialTestClass(int i,String s){
        iVal=i;
        sVal=s;
    }
    public String toString(){
        return "{iVal:"+iVal+",sVal:"+sVal+"}";
    }
    public void method(){
        System.out.println("hello");
    }
}


public class TestServlet{
    public static void main(String[] args) throws Exception{
        String s="0x800363666174655F617263682E636F6D707574696E672E656767726F6C6C2E5F7461626C650A5461626C650A7100298171017D710258030000005F7270710363656767726F6C6C2E726F6C6C5F706169722E726F6C6C5F706169720A526F6C6C506169720A71042981710573622E";
//        s="0x80035D7100636665646572617465646D6C2E656E73656D626C652E62617369635F616C676F726974686D732E6465636973696F6E5F747265652E747265655F636F72652E6E6F64650A4E6F64650A7101298171027D7103285802000000696471044B005808000000736974656E616D657105580500000067756573747106580300000066696471074E580300000062696471084E580600000077656967687471094B00580700000069735F6C656166710A89580800000073756D5F67726164710B4E580800000073756D5F68657373710C4E580B0000006C6566745F6E6F64656964710D4AFFFFFFFF580C00000072696768745F6E6F64656964710E4AFFFFFFFF580B0000006D697373696E675F646972710F4B01580E0000007369626C696E675F6E6F6465696471104E580D000000706172656E745F6E6F6465696471114E580A00000073616D706C655F6E756D71124B00580C00000069735F6C6566745F6E6F64657113897562612E";
        if(s.startsWith("0x")){
            s=s.substring(2);
        }
        if((s.length()&1)==1){
            throw new Exception("non pair bytes");
        }
        byte[] result=new byte[s.length()>>1];
        for(int i=0;i<s.length();i+=2){
            result[i>>1]=parseByte(s.substring(i,i+2),16);
        }
        System.out.println(new String(result));
    }

    public static byte parseByte(String str,int radix) throws Exception{
        int MAX_VALUE=127;
        int MIN_VALUE=-128;
        int iValue=Integer.parseInt(str,radix);
        if(iValue>MAX_VALUE){
            iValue-=256;
        }
        if(iValue>MAX_VALUE||iValue<MIN_VALUE){
            throw new Exception("out of range");
        }
        return (byte)iValue;
    }

    private void login() throws Exception{
        String kms_url="http://172.16.0.166:31064/kms/v1/login";
        URL ur=new URL(kms_url);
        HttpURLConnection connection=(HttpURLConnection)ur.openConnection();
        connection.setRequestMethod("POST");
//        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStream out=connection.getOutputStream();
        out.write("{\"username\":\"u2\",\"password\":234}".getBytes());
        out.flush();
        out.close();
        InputStream in=connection.getInputStream();
        byte[] data=new byte[1024];
        int len;
        StringBuilder sb=new StringBuilder();
        while((len=in.read(data))!=-1){
            sb.append(new String(data,0,len));
        }
        System.out.println(sb);
    }
}
