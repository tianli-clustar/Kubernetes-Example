import bo.ClusterDataBO;
import bo.RollsiteBO;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.rabbitmq.client.*;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.util.Yaml;
import redis.clients.jedis.Jedis;
import vo.IstioVO;
import vo.ModulesVO;
import vo.PartyVO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import ai.clustar.neux.client.kubernetes.K8sUtil;

import javax.persistence.criteria.CriteriaBuilder;

class tt{
    String name;
    st namespace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public st getNamespace() {
        return namespace;
    }

    public void setNamespace(st namespace) {
        this.namespace = namespace;
    }
}

class st{
    private int s=3;
    List<String> t=new ArrayList<>();

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public List<String> getT() {
        return t;
    }

    public void setT(List<String> t) {
        this.t = t;
    }
}

public class Kubernetes {
    private static final String database_url="jdbc:mysql://172.16.0.157:30997";
    private static final String database_username="root";
    private static final String database_password="123456";
    private static java.sql.Connection conn=null;
    private static PreparedStatement preparedStatement=null;

    public static void redisDemo() throws Exception{
        Jedis jedis=new Jedis("172.16.0.157",30668);
//        jedis.set("tianli","rubbish");
//        jedis.del("tianli");
//        jedis.lpush("tianli","rubbish");
//        jedis.lpush("tianli","lowby");
//        jedis.rpush("tianli","noob");
//        System.out.println(jedis.lrange("tianli",0,2));
        Set<String> keys=jedis.keys("*");
        System.out.println(jedis.exists("tianli"));
        for(String s:keys){
            System.out.println(jedis.get("tianli"));
        }
    }

    public static void rabbitmqDemo() throws Exception{
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("neux");
        connectionFactory.setHost("172.16.0.157");
        connectionFactory.setPort(30010);
        com.rabbitmq.client.Connection connection=connectionFactory.newConnection();
        Channel channel=connection.createChannel();
        String queueName="2021062108394257521310";
        channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, "hello".getBytes());
        channel.close();
        connection.close();
    }

    private volatile AtomicInteger count=new AtomicInteger(0);
    PipedInputStream pis=new PipedInputStream();
    PipedOutputStream pos=new PipedOutputStream();
    Semaphore mutex=new Semaphore(1);
    public void produce(){
        new Thread(){
            public void run(){
                try {
                    mutex.acquire();
                    int rand=(int)(Math.random()*255);
                    pos.write(rand);
                    pos.flush();
                    mutex.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void consume(){
        new Thread() {
            public void run() {
                try {
                    mutex.acquire();
                    System.out.println(pis.read());
                    mutex.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private static String column="(CASE WHEN COLUMN_KEY='PRI'  AND EXTRA != 'auto_increment' THEN '主键约束' \n" +
            "\tWHEN COLUMN_KEY='PRI'  AND EXTRA='auto_increment' THEN '主键自增'\n" +
            "        WHEN COLUMN_KEY='UNI' THEN '唯一约束'\n" +
            "        WHEN info.`COLUMN_NAME` IN (SELECT schem.`COLUMN_NAME` \n" +
            "\t\t\t\tFROM information_schema.key_column_usage schem  \n" +
            "\t\t\t\t\n" +
            "\t\t\t\tWHERE  table_schema = 'newcrmpro' AND table_name='employee'" +
            "\t\t\t\t\n" +
            "\t\t\t\tAND  POSITION_IN_UNIQUE_CONSTRAINT IS NOT NULL) THEN '外键约束'\n" +
            "        ELSE '无' END)\n" +
            "descrip";
    private static String sql="select " + column + " from information_schema.columns info where table_schema='neux'";
    private String sst;
    public static void main(String[] args) throws Exception{
//        rabbitmqDemo();
//        redisDemo();
//        K8sUtil k8sUtil=new K8sUtil("config",false);
//        k8sUtil.deleteAllJobsOnNamespace("neux");
        InfluxDBClient influxDBClient= InfluxDBClientFactory.create("http://172.16.0.157:31086","ZbVvhmzbymZywh7F7zEqq0JaTMI_mSNWB_sCqBu8qUtLZDCt0W6qtoBpkxK6-q5MBMSHjSHVuPj6dkBEPl3lLg==".toCharArray(),"Clustar","Clustar");
        influxDBClient.getQueryApi().query("");
    }

    public static byte[] getYamlBytes() throws IOException{
        ClusterDataBO clusterDataBO=new ClusterDataBO();
        clusterDataBO.setName("tl-test").setNamespace("tl-test").setChartName("fate")
                .setChartVersion("v1.5.0").setPartyId(9991).setRegistry("").setPullPolicy("IfNotPresent")
                .setPersistence(false);
        IstioVO istioVO=new IstioVO();
        istioVO.setEnabled(false);
        ModulesVO modulesVO=new ModulesVO();
        List<String> modulesList=new ArrayList<>();
        modulesList.add("rollsite");
        modulesVO.setNoname(modulesList);
        RollsiteBO rollsiteBO=new RollsiteBO();
        rollsiteBO.setType("NodePort");
        rollsiteBO.setNodePort(30910);
        PartyVO partyVO1=new PartyVO();
        partyVO1.setPartyId(11000).setPartyIp("10.233.64.1").setPartyPort(30111);
        PartyVO partyVO2=new PartyVO();
        partyVO2.setPartyId(12000).setPartyIp("10.233.64.1").setPartyPort(30121);
        List<PartyVO> partyVOList=new ArrayList<>();
        partyVOList.add(partyVO1);
        partyVOList.add(partyVO2);
        rollsiteBO.setPartyList(partyVOList);
        clusterDataBO.setIstio(istioVO).setModules(modulesVO).setRollsite(rollsiteBO);
        System.out.println(clusterDataBO);
        return clusterDataBO.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static boolean loginByFileKey(RemoteConnect remoteConnect, File keyFile, String keyfilePass) {
        boolean flag = false;
        // 输入密钥所在路径
//         File keyfile = new File("C:\\Users\\Lenovo\\.ssh\\id_rsa");
         Connection conn=null;
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();
            // 登录认证
            flag = conn.authenticateWithPublicKey(remoteConnect.getUserName(), keyFile, keyfilePass);
            if (flag) {
                System.out.println("认证成功！");
                Session session=conn.openSession();
                session.execCommand("cd /home/centos/KubeFATE/k8s-deploy;mkdir tl-test");
                InputStream inputStream=session.getStdout();
                InputStreamReader isr=new InputStreamReader(inputStream);
                char[] data=new char[1024];
                StringBuilder stringBuilder=new StringBuilder();
                while(isr.read(data)!=-1){
                    stringBuilder.append(data);
                }
                System.out.println("data==null  "+stringBuilder.length());
                System.out.println("return message:"+stringBuilder);
                session.close();
            } else {
                System.out.println("认证失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn!=null)
                conn.close();
        }
        return flag;
    }
}
class RemoteConnect{
    private String ip;
    private String userName;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
