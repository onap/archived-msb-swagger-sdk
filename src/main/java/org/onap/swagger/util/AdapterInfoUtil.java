/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.swagger.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * Loads adapter ip and port.
 *
 */
public class AdapterInfoUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdapterInfoUtil.class);

    private static final String SWAGGER = "swagger.properties";

    private static final String SWAGGER_ADAPTER_PROP = "service-config-file-path";

    private static final String HOST_IP = "ip";

    private static final String HOST_PORT = "port";

    private static final String CATALINA_BASE = "catalina.base";

    private static AdapterInfoUtil util;

    private String ip;

    private String port;

    private AdapterInfoUtil() {
        loadJson();
    }

    /**
     * Get AdapterInfoUtil instance.
     *
     * @return AdapterInfoUtil
     */
    public static AdapterInfoUtil getInstance() {
        if (util == null) {
            util = new AdapterInfoUtil();
        }

        return util;
    }

    /**
     * Get IP.
     *
     * @return string
     */
    public String getIp() {
        if (ip != null) {
            return ip;
        }
        return "";
    }

    /**
     * Get port.
     *
     * @return string
     */
    public String getPort() {
        if (port != null) {
            return port;
        }
        return "";
    }

    /**
     * Loads the json file from given service-config-file-path, if not then from class path.
     */
    private void loadJson() {
        LOGGER.info("Loading service json...");
        InputStream is;
        String jsonTxt = null;
        try {

            String catalinaDir = getAppRoot();
            LOGGER.info("Catalina base dir = " + catalinaDir);

            FileInputStream fin = new FileInputStream(
                    AdapterInfoUtil.class.getClassLoader().getResource(SWAGGER).getFile());
            Properties properties = new Properties();
            properties.load(fin);
            String configFilePath = properties.getProperty(SWAGGER_ADAPTER_PROP);
            LOGGER.info("adapterfile = " + configFilePath);
            if (configFilePath == null || "".equals(configFilePath)) {
                LOGGER.error(SWAGGER_ADAPTER_PROP + " is not set in swagger.properties. "
                        + "Default ip and port will be considered.");
                return;
            }
            File file = new File(configFilePath);
            if (file.exists()) {
                LOGGER.info("Loading using given path...");
                is = new FileInputStream(configFilePath);
            } else {
                is = this.getClass().getResourceAsStream(configFilePath);
                if (is != null) {
                    LOGGER.info("Loading using classpath...");
                    is = new FileInputStream(
                            AdapterInfoUtil.class.getClassLoader().getResource(configFilePath).getFile());
                } else {
                    LOGGER.info("Loading using relative path...");
                    if (!configFilePath.startsWith(File.separator)) {
                        configFilePath = File.separator + configFilePath;
                    }
                    configFilePath = catalinaDir + configFilePath;
                    file = new File(configFilePath);
                    if (file.exists()) {
                        is = new FileInputStream(new File(configFilePath));
                    } else {
                        LOGGER.error(SWAGGER_ADAPTER_PROP + " is incorrect in swagger.properties. "
                                + "Default ip and port will be considered.");
                        return;
                    }
                }
            }
            jsonTxt = IOUtils.toString(is);
            LOGGER.info("jsonTxt = " + jsonTxt);

        } catch (FileNotFoundException e) {
            LOGGER.error("File Exception", e);
        } catch (IOException e) {
            LOGGER.error("File Exception", e);
        }
        if (jsonTxt != null) {
            JSONObject temp = JSONObject.fromObject(jsonTxt);
            if (temp != null) {
                Iterator it = temp.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    if (key.endsWith("nodes")) {
                        JSONArray lineItems = (JSONArray) temp.get(key);
                        for (Object o : lineItems) {
                            JSONObject jsonLineItem = (JSONObject) o;
                            String ip1 = jsonLineItem.getString(HOST_IP);
                            String port1 = jsonLineItem.getString(HOST_PORT);
                            if (ip1 != null && port1 != null) {
                                this.ip = ip1;
                                this.port = port1;
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * Read catalina base.
     *
     * @return String
     */
    public String getAppRoot() {
        String appRoot;
        appRoot = System.getProperty(CATALINA_BASE);
        if (appRoot != null) {
            appRoot = getCanonicalPath(appRoot);
        }
        return appRoot;
    }

    /**
     * Get canonical path.
     *
     * @param inPath
     *            string
     * @return string
     */
    private String getCanonicalPath(String inPath) {
        String path = null;
        try {
            if (inPath != null) {
                File file = new File(inPath);
                path = file.getCanonicalPath();
            }
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        return path;
    }
}
