package com.salk.lib.demo.genericity;

/**
 * 泛型接口
 * @author salkli
 * @since 2022/7/26
 **/
public interface IHandler<T> {
    /**
     * 执行方法
     * @param t
     */
    void doHandler(T t);

    /**
     * 泛型方法
     * @param <T>
     * @return
     */
    <T> T result();
}

class HandlerImpl<T> implements IHandler<T> {

    private T t;
    @Override
    public void doHandler(T t) {
        this.t=t;
    }

    @Override
    public <T> T result() {
        return (T) t;
    }



    public static void main(String[] args) {
        IHandler<String> handler=new HandlerImpl<String>();
        handler.doHandler("123");
        Integer result = handler.result();
        System.out.println(result);

    }
}
class HandlerImplStr implements IHandler<String>{

    @Override
    public void doHandler(String s) {

    }

    @Override
    public <T> T result() {
        return null;
    }
}
