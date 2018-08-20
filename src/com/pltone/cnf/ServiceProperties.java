package com.pltone.cnf;

import com.pltone.init.FilePathInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 服务配置
 *
 * @author chenlong
 * @version 1.0 2018-08-17
 */
public class ServiceProperties {
    public static final String DEFAUT_HTTP = "http";
    public static final String DEFAUT_IP = "127.0.0.1";
    public static final int DEFAUT_PORT = 80;
    public static final String DEFAUT_PATH = "Elock_Service.asmx";
    public static final boolean DEFAUT_FORWARD = false;

    private static final Logger logger = LoggerFactory.getLogger(ServiceProperties.class);
    private static final String PROP_FILE_PATH = FilePathInit.getPropFilePath();
    private static final String NAME_SERVICE_IP = "service.ip";
    private static final String NAME_SERVICE_PORT = "service.port";
    private static final String NAME_SERVICE_PATH = "service.path";
    private static final String NAME_RT_FORWARD = "ruitong.forward";
    private static final String NAME_RT_HTTP = "ruitong.http";
    private static final String NAME_RT_IP = "ruitong.ip";
    private static final String NAME_RT_PORT = "ruitong.port";
    private static final String NAME_RT_PATH = "ruitong.path";
    private static final String NAME_PLT_FORWARD = "pltone.forward";
    private static final String NAME_PLT_HTTP = "pltone.http";
    private static final String NAME_PLT_IP = "pltone.ip";
    private static final String NAME_PLT_PORT = "pltone.port";
    private static final String NAME_PLT_PATH = "pltone.path";

    private static Properties properties;

    private static String serviceIp;
    private static int servicePort;
    private static String servicePath;

    private static boolean rtForward;
    private static String rtHttp;
    private static String rtIp;
    private static int rtPort;
    private static String rtPath;

    private static boolean pltForward;
    private static String pltHttp;
    private static String pltIp;
    private static int pltPort;
    private static String pltPath;

    public static String getServiceIp() {
        return serviceIp;
    }
    public static void setServiceIp(String serviceIp) {
        ServiceProperties.serviceIp = serviceIp;
        properties.setProperty(NAME_SERVICE_IP, serviceIp);
    }
    public static int getServicePort() {
        return servicePort;
    }
    public static void setServicePort(int servicePort) {
        ServiceProperties.servicePort = servicePort;
        properties.setProperty(NAME_SERVICE_PORT, Integer.toString(servicePort));
    }
    public static String getServicePath() {
        return servicePath;
    }
    public static void setServicePath(String servicePath) {
        ServiceProperties.servicePath = servicePath;
        properties.setProperty(NAME_SERVICE_PATH, servicePath);
    }
    public static boolean isRtForward() {
        return rtForward;
    }
    public static void setRtForward(boolean rtForward) {
        ServiceProperties.rtForward = rtForward;
        properties.setProperty(NAME_RT_FORWARD, rtForward ? "1" : "0");
    }
    public static String getRtHttp() {
        return rtHttp;
    }
    public static void setRtHttp(String rtHttp) {
        ServiceProperties.rtHttp = rtHttp;
        properties.setProperty(NAME_RT_HTTP, rtHttp);
    }
    public static String getRtIp() {
        return rtIp;
    }
    public static void setRtIp(String rtIp) {
        ServiceProperties.rtIp = rtIp;
        properties.setProperty(NAME_RT_IP, rtIp);
    }
    public static int getRtPort() {
        return rtPort;
    }
    public static void setRtPort(int rtPort) {
        ServiceProperties.rtPort = rtPort;
        properties.setProperty(NAME_RT_PORT, Integer.toString(rtPort));
    }
    public static String getRtPath() {
        return rtPath;
    }
    public static void setRtPath(String rtPath) {
        ServiceProperties.rtPath = rtPath;
        properties.setProperty(NAME_RT_PATH, rtPath);
    }
    public static boolean isPltForward() {
        return pltForward;
    }
    public static void setPltForward(boolean pltForward) {
        ServiceProperties.pltForward = pltForward;
        properties.setProperty(NAME_PLT_FORWARD, pltForward ? "1" : "0");
    }
    public static String getPltHttp() {
        return pltHttp;
    }
    public static void setPltHttp(String pltHttp) {
        ServiceProperties.pltHttp = pltHttp;
        properties.setProperty(NAME_PLT_HTTP, pltHttp);
    }
    public static String getPltIp() {
        return pltIp;
    }
    public static void setPltIp(String pltIp) {
        ServiceProperties.pltIp = pltIp;
        properties.setProperty(NAME_PLT_IP, pltIp);
    }
    public static int getPltPort() {
        return pltPort;
    }
    public static void setPltPort(int pltPort) {
        ServiceProperties.pltPort = pltPort;
        properties.setProperty(NAME_PLT_PORT, Integer.toString(pltPort));
    }
    public static String getPltPath() {
        return pltPath;
    }
    public static void setPltPath(String pltPath) {
        ServiceProperties.pltPath = pltPath;
        properties.setProperty(NAME_PLT_PATH, pltPath);
    }

    public static void init() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(PROP_FILE_PATH));

            serviceIp = properties.getProperty(NAME_SERVICE_IP, DEFAUT_IP);
            String _servicePort = properties.getProperty(NAME_SERVICE_PORT);
            if (_servicePort == null || _servicePort.trim().isEmpty()) {
                servicePort = DEFAUT_PORT;
            } else {
                servicePort = Integer.parseInt(_servicePort, 10);
            }
            servicePath = properties.getProperty(NAME_SERVICE_PATH, DEFAUT_PATH);

            String _rtForward = properties.getProperty(NAME_RT_FORWARD);
            if (_rtForward == null || _rtForward.trim().isEmpty()) {
                rtForward = DEFAUT_FORWARD;
            } else {
                rtForward = _rtForward.equals("1");
            }
            rtHttp = properties.getProperty(NAME_RT_HTTP, DEFAUT_HTTP);
            rtIp = properties.getProperty(NAME_RT_IP, DEFAUT_IP);
            String _rtPort = properties.getProperty(NAME_RT_PORT);
            if (_rtPort == null || _rtPort.trim().isEmpty()) {
                rtPort = DEFAUT_PORT;
            } else {
                rtPort = Integer.parseInt(_rtPort, 10);
            }
            rtPath = properties.getProperty(NAME_RT_PATH, DEFAUT_PATH);

            String _pltForward = properties.getProperty(NAME_PLT_FORWARD);
            if (_pltForward == null || _pltForward.trim().isEmpty()) {
                pltForward = DEFAUT_FORWARD;
            } else {
                pltForward = _pltForward.equals("1");
            }
            pltHttp = properties.getProperty(NAME_PLT_HTTP, DEFAUT_HTTP);
            pltIp = properties.getProperty(NAME_PLT_IP, DEFAUT_IP);
            String _pltPort = properties.getProperty(NAME_PLT_PORT);
            if (_pltPort == null || _pltPort.trim().isEmpty()) {
                pltPort = DEFAUT_PORT;
            } else {
                pltPort = Integer.parseInt(_pltPort, 10);
            }
            pltPath = properties.getProperty(NAME_PLT_PATH, DEFAUT_PATH);

        } catch (IOException e) {
            logger.error("初始化配置异常！", e);
        }
    }

    public static void save() {
        try (OutputStream out = new FileOutputStream(PROP_FILE_PATH)) {
            properties.store(out, null);
        } catch (Exception e) {
            logger.error("更新配置文件异常！", e);
        }
    }
}
