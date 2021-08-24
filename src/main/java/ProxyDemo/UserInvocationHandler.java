package ProxyDemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserInvocationHandler implements InvocationHandler {

    private UserService userService;
    public UserInvocationHandler(UserService userService){
        this.userService=userService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        method.invoke(userService);
        System.out.println("behind");
        return null;
    }
}
