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

import static org.junit.Assert.fail;

import org.junit.Test;

import io.swagger.jaxrs.config.BeanConfig;

public class SwaggerBeanTest {

     @Test
    public void test() {
         try {
         new BeanConfigPostProcessor().postProcessBeforeInitialization(new BeanConfig(), BeanConfigPostProcessor.SWAGGER_BEAN_ID);
         new BeanConfigPostProcessor().postProcessAfterInitialization(new BeanConfig(), BeanConfigPostProcessor.SWAGGER_BEAN_ID);
         } catch (Exception e) {
             fail("failed to post/pre process bean");
         }
    }
}
