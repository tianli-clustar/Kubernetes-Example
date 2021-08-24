package vo;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;

public class K8sUtils{

    private static final Logger logger = LoggerFactory.getLogger(K8sUtils.class);

    private CoreV1Api coreV1Api = null;

    private AppsV1Api appsV1Api = null;

    private BatchV1Api batchV1Api = null;

    public K8sUtils(String configFilePath,boolean debug){
        try {
            //load k8s config
            ApiClient client= ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(configFilePath))).build();

            client.setDebugging(debug);

            //set default api client
            Configuration.setDefaultApiClient(client);

            //create api
            coreV1Api = new CoreV1Api();

            appsV1Api=new AppsV1Api();

            batchV1Api = new BatchV1Api();

        } catch (Exception e) {

            logger.error("K8S config client error: {}",e);

        }
    }

    /**
     * create pod
     *
     * @param nameSpace : namespace of k8s
     * @param body      ：pod
     * @return
     * @throws ApiException
     */
    public V1Pod createPod(String nameSpace, V1Pod body) throws ApiException {

        return coreV1Api.createNamespacedPod(nameSpace, body, "true", "true", null);

    }

    /**
     * list pods of specified namespace
     * @param namespace : namespace of k8s
     * @return
     * @throws ApiException
     */
    public  V1PodList listNamespacedPod(String namespace) throws ApiException{
        return coreV1Api.listNamespacedPod(namespace,null,null,null,null,null,
                null,null,null,null,null);
    }

    /**
     * list all pods
     * @return
     * @throws ApiException
     */
    public  V1PodList listAllPods() throws ApiException{
        return coreV1Api.listPodForAllNamespaces(null,null,null
        ,null,null,null,null,null,null,null);
    }

    /**
     * list jobs of specified namespace
     * @param namespace : namespace of k8s
     * @return
     * @throws ApiException
     */
    public  V1JobList listNamespacedJob(String namespace) throws ApiException{
        return batchV1Api.listNamespacedJob(namespace,null,null,null,null,null,
                null,null,null,null,null);
    }

    /**
     * delete pod
     *
     * @param nameSpace : namespace of k8s
     * @param podName   : name of pod
     * @throws Exception
     */
    public void deletePod(String nameSpace, String podName) throws Exception {
        coreV1Api.deleteNamespacedPod(podName, nameSpace, null, null, null, null, null, null);
    }

    /**
     * create service
     *
     * @param nameSpace : namespace of k8s
     * @param svc       : service
     * @throws ApiException
     */
    public  void createService(String nameSpace, V1Service svc) throws ApiException {
        coreV1Api.createNamespacedService(nameSpace, svc, "true", "true", null);
    }

    /**
     * delete service
     *
     * @param nameSpace     : namespace of k8s
     * @param serviceName   : service of k8s
     * @throws Exception
     */
    public void deleteService(String nameSpace, String serviceName) throws Exception {
        coreV1Api.deleteNamespacedService(serviceName, nameSpace, null, null, null, null, null, null);
    }

    /**
     * create deployment
     *
     * @param nameSpace : namespace of k8s
     * @param body      : deployment object
     * @throws ApiException
     */
    public void createDeployment(String nameSpace, V1Deployment body) throws ApiException {
        appsV1Api.createNamespacedDeployment(nameSpace, body, "true", "true", null);
    }

    /**
     * delete namespace
     *
     * @param nameSpace     : namespace of k8s
     * @param deployName   : deployment name
     * @throws ApiException
     */
    public void deleteDeployment(String nameSpace, String deployName) throws ApiException {
        appsV1Api.deleteNamespacedDeployment(deployName, nameSpace, "true", null, null, null, null, null);
    }

    /**
     * create job
     *
     * @param nameSpace : namespace of k8s
     * @param body      : job object
     * @throws ApiException
     */
    public void createJob(String nameSpace, V1Job body) throws ApiException {

        batchV1Api.createNamespacedJob(nameSpace, body, "true", null, null);

    }
    /**
     * delete job
     *
     * @param nameSpace : namespace of k8s
     * @param jobName   : name of job
     * @throws ApiException
     */
    public void deleteJob(String nameSpace, String jobName) throws Exception {

        try {
            batchV1Api.deleteNamespacedJob(jobName, nameSpace, null, null, null, null, "Background", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() instanceof IllegalStateException) {
                IllegalStateException ise = (IllegalStateException) e.getCause();
                if (ise.getMessage() != null && ise.getMessage().contains("Expected a string but was BEGIN_OBJECT")) {
                    logger.info("Catching exception because of issue https://github.com/kubernetes/kubernetes/issues/65121");
                } else {
                    throw new Exception(e.toString());
                }
            } else {
                throw new Exception(e.toString());
            }
        }

    }

    /**
     * delete jobs of specified namespace
     * @param namespace : namespace of k8s
     * @throws Exception
     */
    public void deleteAllJobsOnNamespace(String namespace) throws Exception{
        V1JobList v1JobList=listNamespacedJob(namespace);
        for(V1Job v1Job:v1JobList.getItems()){
            deleteJob(namespace,v1Job.getMetadata().getName());
        }
    }

    /**
     * list namespaces
     * @return
     * @throws ApiException
     */
    public  V1NamespaceList listNamespaces() throws ApiException {
        V1NamespaceList v1NamespaceList = coreV1Api.listNamespace(null, null, null, null,
                null, null, null, null, null,null);

        return v1NamespaceList;
    }

    /**
     * create namespace
     * @param body  : namespace object
     * @return
     * @throws ApiException
     */
    public  V1Namespace createNamespace(V1Namespace body) throws ApiException {

        V1Namespace v1Namespace = coreV1Api.createNamespace(body, "false", null, null);

        return v1Namespace;
    }

    /**
     * create namespace
     * @param namespace : namespace of k8s
     * @return
     * @throws ApiException
     */
    public V1Namespace createNamespace(String namespace) throws ApiException{
        V1ObjectMeta objectMeta=new V1ObjectMeta();
        objectMeta.setName(namespace);
        V1Namespace ns=new V1Namespace();
        ns.setMetadata(objectMeta);
        return createNamespace(ns);
    }

    /**
     * delete namespace
     * @param namespace : namespace of k8s
     * @throws ApiException
     */
    public void deleteNamespace(String namespace) throws ApiException{
        coreV1Api.deleteNamespace(namespace,null,null,null,null,null,null);
    }
}
