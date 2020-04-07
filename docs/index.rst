.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2017 Huawei Technologies Co., Ltd.
.. _master_index:

.. _swagger_sdk:

Swagger SDK
===========

Swagger
--------
Swagger helps on following aspects:

* Define REST API based on OAS standard, as swagger.json
* Auto generate the REST service code, using swagger.json with swagger annotations.
* Auto generate the client sdk code, using swagger.json in different language
* Provides readable REST API document using swagger.json

And for implementing mirco-services, swagger is being an default technology.

Problems in mirco-services development
--------------------------------------

Following problem occurs in micro-service based products as there is no process exist to govern them:

* Most of the time, developers manually authors the swagger REST API document, and it leads to inconsistent between the actual REST API implementation and API defined the document.
* For mirco-services, developer starts to write the client sdk manually and which will also brings inconsistent between actual REST API provided by mirco-service and the client SDK implementation.
* Client SDK in different languages become an huge effort involved and most of time, teams do not provide the client SDK in required languages of different micro-service, dependenting on another micro-service.
* When depending services (B) changes the REST API, its only detected at system testing and integration testing phase of dependent service (A). Because client SDK of B is implemented by service A team, because service A does not provide the client SDK in required language as mentioned above.

To address these problems, Swagger-SDK is brought into ONAP, as ONAP is mirco-service based architecture.

What does Swagger-SDK provide:
------------------------------
It provides feature in build-time and run-time.

Build-time feature
------------------

* Generate the java client SDK for those services, which provides swagger.json already and deploy it in nexus in following package ::

     [ Project Maven Group-id] ::[ Project maven artifact-id]-java-sdk :: [project maven version]

* Generate the swagger.json for those services, which is already annotated with swagger java annotations. Also it deploy it in nexus in following package ::

     [ Project Maven Group-id] ::[ Project maven artifact-id]-swagger-schema :: [project maven version]

MSB is already supported with this feature.

Run-time feature
----------------

* Provides an RESP API controller for reporting swagger.json at pre-defined URI. And it helps to maintain the same URI across different servicse in ONAP, by making use of it. Also it helps to generate swagger.json at run-time if a given service is already annotated with swagger annotations.

* It does support only for java based swagger enabled RESTful services.

