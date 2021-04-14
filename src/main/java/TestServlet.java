import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class TestServlet{
    public static void main(String[] args) throws Exception{
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