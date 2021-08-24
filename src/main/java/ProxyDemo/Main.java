package ProxyDemo;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args){
        UserService userService =new UserServiceImpl();
        UserService proxy=(UserService) Proxy.newProxyInstance(Main.class.getClassLoader(),
                new Class[]{UserService.class},new UserInvocationHandler(userService));
        proxy.query();
    }
}
