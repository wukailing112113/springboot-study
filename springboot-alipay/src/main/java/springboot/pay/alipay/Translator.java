package springboot.pay.alipay;

/**
 * Created by Sawyer on 2016/11/30.
 */
public interface Translator<T, T1> {

    T translate(T1 t1);
}
