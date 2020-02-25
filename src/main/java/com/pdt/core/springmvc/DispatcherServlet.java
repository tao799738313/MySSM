package com.pdt.core.springmvc;

import com.alibaba.fastjson.JSON;
import com.pdt.core.mybatis.annotation.Select;
import com.pdt.core.mybatis.bean.Function;
import com.pdt.core.mybatis.bean.MapperBean;
import com.pdt.core.mybatis.utils.MapperProxy;
import com.pdt.core.mybatis.annotation.Mapper;
import com.pdt.core.spring.annotation.Autowired;
import com.pdt.core.spring.annotation.Controller;
import com.pdt.core.spring.annotation.Service;
import com.pdt.core.springmvc.annotation.RequestMapping;
import com.pdt.core.springmvc.utils.RequsetParameter;
import com.pdt.core.springmvc.utils.ReturnParameter;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: nanJunYu
 * @Description: 所有的bean使用之前得先被创建，放到mapping里面，
 * 所以启动的时候就应该创建，在init方法里做处理
 * <p>
 * 将所有的依赖关系进行扫描建立好
 * @Date: Create in  2018/8/13 11:11
 */
public class DispatcherServlet extends HttpServlet {
    //收集文件路径的List
    List<String> classList = new ArrayList();

    Map<String, Object> controllerMap = new HashMap<String, Object>();
    Map<String, Object> serviceMap = new HashMap<String, Object>();

    Map<String, Object> mapperMap = new HashMap<String, Object>();
    // key是拼接的全路径，value是方法
    Map<String, Object> handlerMap = new HashMap<String, Object>();

    //private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求路径
        String url = req.getRequestURI();
        String context = req.getContextPath();
        String path = url.replace(context, "");
        Method method= (Method) handlerMap.get(path);
        Object controller= null;
        //根据key去拿实例
        if(path.equals("/")){
            controller= controllerMap.get("/");
        }else{
            controller= controllerMap.get("/"+path.split("/")[1]);
        }
        try {
            if(controller==null || method==null){
                resp.setContentType("text/html; charset=utf-8");
                resp.getWriter().println("<h1>404</h1>");
            }else{
                Class<?> returnType = method.getReturnType();
                if(returnType == null){
                    System.out.println("Controller层不能无返回");
                }else{
                    // 参数处理
                    String returnTypeSimpleName = returnType.getSimpleName();
                    List parameList = RequsetParameter.handle(req,resp,method.getParameters());
                    // 返回处理
                    ReturnParameter.handle(returnTypeSimpleName,parameList,method,controller,resp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        // 扫描文件
        scanPackage("com.pdt.ssm");
        // 收集注解，存进对应的map里
        doInstance();
        // 生成proxy函数
        mybatisDo();
        // 把controller层的Autowired赋值上serviceMap的对应类
        controllerIoc();
        // 把service层的Autowired赋值上mapperMap的对应proxy函数
        serviceIoc();
        // 把RequestMapping拼接好作为key存进map里
        buildUrlMapping();
    }

    /**
     * @Author: nanJunYu
     * @Description:把所有的bean扫描出来 扫描所有的class文件
     * @Date: Create in  2018/8/13 15:10
     * @params:
     * @return:
     */
    private void scanPackage(String basePackage) {
        //拿到项目的路径开始扫描所有的class
        URL url = this.getClass().getClassLoader().getResource("/" + basePackage.replaceAll("\\.", "/"));
        String fileStr = url.getFile();
        File file = new File(fileStr);
        String fileArray[] = file.list();
        for (String path : fileArray) {
            File filePath = new File(fileStr + path);
            //代表是文件夹 继续递归
            if (filePath.isDirectory()) {
                scanPackage(basePackage + "." + path);
            } else {
                //代表是class文件目录 将完整的class物理文件路径地址 放入到集合里
                classList.add(basePackage + "." + filePath.getName());
            }
        }
    }

    /**
     * @Description:收集注解，存到各自的map里
     */
    private void doInstance() {
        if (classList.isEmpty()) {
            System.out.println("class扫描失败.......");
        }
        for (String className : classList) {
            //去除多余的后缀名
            String cName = className.replace(".class", "");
            try {
                Class<?> clazz = Class.forName(cName);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    //如果是控制类 反射创建控制类
                    Object instance = clazz.newInstance();
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    String value = requestMapping.value();
                    //获取到@RequestMapping上配置的地址 当key放到map容器里  将实例以value的形式存放
                    controllerMap.put(value, instance);
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    Object instance = clazz.newInstance();
                    //获取到@Service上配置的地址 当key放到map容器里  将实例以value的形式存放
                    serviceMap.put(service.value(), instance);
                } else if (clazz.isAnnotationPresent(Mapper.class)) {
                    Mapper mapper = clazz.getAnnotation(Mapper.class);
                    mapperMap.put(mapper.value(), clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }


    private void mybatisDo() {
        for (Map.Entry<String, Object> entry : mapperMap.entrySet()) {
            Class clazz = (Class) entry.getValue();
            MapperBean mapper = new MapperBean();
            List<Function> list = new ArrayList<Function>();//用来存储方法的list
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                int annsLen = method.getAnnotations().length;
                if(annsLen==0){
                    System.out.println("没有mybatis注解");
                }else{
                    Function function = new Function();  //用来存储一条方法的记录
                    if (method.isAnnotationPresent(Select.class)) {
                        function.setSqltype("select");
                        function.setSql(method.getAnnotation(Select.class).value());
                        function.setFuncName(method.getName());
                        function.setResultType(method.getReturnType().getName());
                        if(method.getGenericReturnType().getTypeName().contains("<")){
                            function.setSetGenericReturnType(method.getGenericReturnType().getTypeName().split("<")[1].split(">")[0]);
                        }
                    }else{
                        // 其他注解
                    }
                    list.add(function);
                    mapper.setInterfaceName(entry.getKey());
                    mapper.setList(list);
                }
            }
            Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz},
                    new MapperProxy(mapper));
            entry.setValue(obj);
        }
    }


    /**
     * @Description:把Service注入到控制层
     */
    private void controllerIoc() {
        for (Map.Entry<String, Object> entry : controllerMap.entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                //获得所有声明的参数 得到参数数组
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Autowired autowired = field.getAnnotation(Autowired.class);
                        String key = autowired.value();
                        //打开私有属性的权限修改
                        field.setAccessible(true);
                        try {
                            //给变量重新设值
                            field.set(instance, serviceMap.get(key));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            } else {
                continue;
            }
        }
    }


    private void serviceIoc() {
        for (Map.Entry<String, Object> entry : serviceMap.entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(Service.class)) {
                //获得所有声明的参数 得到参数数组
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Autowired autowired = field.getAnnotation(Autowired.class);
                        String key = autowired.value();
                        //打开私有属性的权限修改
                        field.setAccessible(true);
                        try {
                            //给变量重新设值
                            field.set(instance, mapperMap.get(key));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            } else {
                continue;
            }
        }
    }

    /**
     * @Description:拼接RequestMapping作为key值，value是对应的方法
     */
    private void buildUrlMapping() {
        if (controllerMap.entrySet().size() <= 0) {
            System.out.println("没有类的实例化......");
            return;
        }
        for (Map.Entry<String, Object> entry : controllerMap.entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                String classPath = requestMapping.value();
                classPath = classPath.equals("/") ? "" : classPath;
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                        String methodPath = methodMapping.value();
                        //完整的映射路径
                        handlerMap.put(classPath + methodPath, method);
                    } else {
                        continue;
                    }
                }
            } else {
                continue;
            }
        }
    }
}
