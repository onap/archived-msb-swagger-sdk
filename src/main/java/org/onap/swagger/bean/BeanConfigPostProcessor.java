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

package org.onap.swagger.bean;

import io.swagger.jaxrs.config.BeanConfig;
import org.onap.swagger.util.AdapterInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * <br>
 * <p>
 * Bean post processor for setting the ip and port into swaggerConfig bean from adapter file.
 * </p>
 *
 * @author
 */
public class BeanConfigPostProcessor implements BeanPostProcessor {

    private static final String SWAGGER_BEAN_ID = "swaggerConfig";

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanConfigPostProcessor.class);

    /**
     * Post process after bean initialization.<br/>
     *
     * @param bean obj
     * @param beanName string
     * @return object
     * @throws BeansException exception
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (SWAGGER_BEAN_ID.equals(beanName)) {
            BeanConfig config = (BeanConfig) bean;
            AdapterInfoUtil util = AdapterInfoUtil.getInstance();
            if (util.getIp() != null && !"".equals(util.getIp()) && util.getPort() != null
                    && !"".equals(util.getPort())) {
                config.setHost(util.getIp() + ":" + util.getPort());
                LOGGER.info("BeanConfigPostProcessor : Host Details=" + util.getIp() + ":" + util.getPort());
                return config;
            }
            LOGGER.info("BeanConfigPostProcessor : Host Details=" + config.getHost());
        }
        return bean;
    }

    /**
     * Post processor.<br/>
     *
     * @param bean
     *            obj
     * @param beanName
     *            String
     * @return object
     * @throws BeansException
     *             exception
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
