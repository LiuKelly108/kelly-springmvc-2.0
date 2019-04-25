package com.kelly.study.spring.framework.beans.support;

import com.kelly.study.spring.framework.beans.config.KYBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 对配置文件进行查找、读取、解析
 */
public class KYBeanDefinitionReader {

    //存储将要注册的类的全名称
    private List<String> registBeanClasses = new ArrayList<String>();
    private Properties config = new Properties();

    private final String SCAN_PACKAGE = "scanPackage" ; //配置文件中的key

    /**
     * 构造方法
     * @param locations
     */
    public KYBeanDefinitionReader(String...locations){
        //通过URL定位找到其所定位的文件，然后转换为文件流
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(locations[0].replace("classspath",""));
        try {
            config.load(is);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    /**
     * config的get方法
     * @return
     */
    public Properties getConfig(){
        return this.config;
    }

    /**
     * 扫描scanPackage下的所有类，并将class的全路径名称注册到registBeanClasses中
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
       //转换为文件路径，将.转换为/
       URL url = this.getClass().getClassLoader().getResource("/"+SCAN_PACKAGE.replaceAll("\\.","/"));
       File classPath = new File(url.getFile());
       for(File file :classPath.listFiles()){
            if(file.isDirectory()){
                doScanner(scanPackage+"."+file.getName());
            }else {
                if(!file.getName().endsWith(".class")){
                    continue;
                }
                String className=scanPackage+"."+file.getName().replace(".class","");
                registBeanClasses.add(className);
            }
       }

    }

    /**
     * 把配置文件中扫描到的所有类信息转换为BeanDefinitions，方便后续的IOC使用
     * @return
     */
    public List<KYBeanDefinition> loadBeanDefinitions() {
        List<KYBeanDefinition> result = new ArrayList<KYBeanDefinition>();

        try{
            for(String className : registBeanClasses){
                Class<?> clazz = Class.forName(className);
                if(clazz.isInterface()){//如果类是一个接口，则跳出循环
                    continue;
                }

                KYBeanDefinition kyBeanDefinition = doCreateBeanDefinition(toLowerFirstCase(clazz.getSimpleName()), clazz.getName());
                result.add(kyBeanDefinition);
                //1、获得该类的所有的接口
                Class<?>[] interfaces = clazz.getInterfaces();
                for(Class<?> i : interfaces){
                    //配置接口的类名字，和实现类的名字
                    KYBeanDefinition i_beanDefinition = doCreateBeanDefinition(i.getName(),clazz.getName());
                    result.add(i_beanDefinition);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return  result ;
    }

    /**
     * 首字母变为小写
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        //为大小写字母的ASCII 码相差32，小写字母=大写字母+32
        chars[0]+=32;
        return String.valueOf(chars);
    }

    /**
     * 为每一个BeanDefinition设置信息
     * @param factoryBeanName 类名称（首字母小写）
     * @param beanClassName
     * @return
     */
    private KYBeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName){
        KYBeanDefinition  kyBeanDefinition = new KYBeanDefinition();
        //设置factoryBean和beanClassName
        kyBeanDefinition.setFactoryBeanName(factoryBeanName);
        kyBeanDefinition.setBeanClassName(beanClassName);
        return kyBeanDefinition ;
    }
}
