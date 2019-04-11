package springboot.aop.aop;

import net.bytebuddy.asm.Advice;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import springboot.aop.enty.Base;
import springboot.aop.enty.User;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * aop 拦截save方法并改变拦截方法参数的对象属性值（通过反射来改变）
 */
@Aspect
@Component
public class AdviceTest {

    /**
     * execution 表达式说明：第一个* 不说明，第二个..* 是service包下所有的包及子包，然后.save代表拦截的方法，括号的参数代表任何参数
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* springboot.aop.service..*.save(..))")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        System.out.println("@Around：执行目标方法之前...");
        //访问目标方法的参数：
        Object[] args = point.getArgs();
        if (args != null && args.length > 0 && args[0].getClass() == String.class) {
            args[0] = "改变后的参数1";
        }

        // 拦截方法，并通过反射来改变方法参数的值

        Base base = (Base) args[0];
        String classType = point.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);

//        String baseName = base.getClass().getName();
//
//        Class<?> aClass = Class.forName(baseName);
//       // Field field = aClass.getField("field");
//        Field[] fields = aClass.getDeclaredFields();

        Field[] fields = base.getClass().getDeclaredFields();
        for(Field field :fields){
            if(field.getName().equals("name")){
                System.out.println(field.getDeclaringClass());
                field.setAccessible(true);
                Object filedvalue = field.get(base);
                if(Objects.nonNull(filedvalue)){
                    String afterValue = filedvalue+"-hello-wkl";
                    field.set(base,afterValue);
                }
            }

            if(field.getName().equals("startTime")&& field.getType().getCanonicalName().equals("java.util.Date")){
                System.out.println(field.getDeclaringClass());
                field.setAccessible(true);
                Object filedvalue = field.get(base);
                System.out.println(filedvalue);
                field.set(base,new Date());
            }
        }

        //Field StartTime = aClass.getField("StartTime");


//        Method method = aClass.getMethod("field");
//        if(method!=null){
//            Object defaultValue = method.getDefaultValue();
//            System.out.println("defaultValue=="+defaultValue);
//
//        }



        //用改变后的参数执行目标方法
        Object returnValue = point.proceed(args);
        System.out.println("@Around：执行目标方法之后...");
        System.out.println("@Around：被织入的目标对象为：" + point.getTarget());
        return base;
    }

}
