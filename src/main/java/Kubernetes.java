import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import io.kubernetes.client.Exec;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.ProtoClient;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.proto.V1;
import io.kubernetes.client.util.*;
import io.kubernetes.client.util.credentials.ClientCertificateAuthentication;
import io.kubernetes.client.util.credentials.KubeconfigAuthentication;
import io.kubernetes.client.util.credentials.UsernamePasswordAuthentication;
import okhttp3.OkHttpClient;
import org.springframework.util.ResourceUtils;
import sun.security.util.DerInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Kubernetes {
    public static void main(String[] args) throws Exception{
        RemoteConnect remoteConnect=new RemoteConnect();
        remoteConnect.setIp("172.16.0.165");
        remoteConnect.setUserName("centos");
        loginByFileKey(remoteConnect,new File("C:\\Users\\Lenovo\\.ssh\\id_rsa"),null);
    }
    public static void execute() throws Exception{
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/ssecacerts");
//        Reader kubeReader=new FileReader("src/main/resources/config");
//        KubeConfig kubeConfig=KubeConfig.loadKubeConfig(kubeReader);
//        ApiClient apiClient=new ClientBuilder().setBasePath("https://172.16.0.165:6443")
//                .setVerifyingSsl(true)
//                .setAuthentication(new KubeconfigAuthentication(kubeConfig))
//                .build();
        ApiClient apiClient=ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader("src/main/resources/config"))).build();
        Configuration.setDefaultApiClient(apiClient);
        CoreV1Api coreApi = new CoreV1Api(apiClient);
        PodLogs logs = new PodLogs();
        V1Pod pod =coreApi.listNamespacedPod("tl-test", "false", null, null, null, null, null, null, null, null, null)
                .getItems()
                .get(0);
        V1Pod podSample=coreApi.readNamespacedPod("pod-sample","tl-test","false",null,null);
        System.out.println(podSample.getKind()+" "+podSample.getMetadata()+" "+podSample.getStatus()+" "+podSample.getMetadata());
        System.out.println(coreApi.connectGetNamespacedPodExec("pod-sample","tl-test","sh","pod-sample-container",true,true,true,true));
        InputStream is = logs.streamNamespacedPodLog(pod);
        Streams.copy(is, System.out);
//        File file= ResourceUtils.getFile("classpath:pod-example.yaml");
//        V1Pod v1Pod=(V1Pod) Yaml.load(file);
//        V1Pod v1Pod1=coreApi.createNamespacedPod("tl-test",v1Pod,"false",null,null);
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

            } else {
                System.out.println("认证失败！");
            }
        } catch (Exception e) {
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
