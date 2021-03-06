/**
 * ZooKeeper REST API
 * The ZooKeeper REST API provides support for the ZooKeeperUI web application. The API provides different endpoints to access and modify ZooKeeper ZNodes.  ### Authentication  All the operations of this API are authenticated. In order to start a new player session you need to invoke the `POST /sessions` operation, providing some user credentials. This operation will include an `ACCESS_TOKEN` cookie in the returned response, containing an authentication token.  This cookie must be provided in any subsequent call to the API in order to be correctly authenticated. If you are accessing this API from a web application running in a browser, the browser will already do it for you.  The returned `ACCESS_TOKEN` cookie will expire after a configurable amount of time. In order to keep your session open you have to refresh your session via the `PUT /sessions/current` operation.  A default authentication provider is included, that checks the user credentials against the `users` configuration property, which is an array of user name and password tuples. As this is a Spring Boot application, the `users` configuration property can be provided via a configuration file, environment variables, command line arguments, and Java properties.   In addition to the default authentication provider, you can provide your own, by implementing the **TODO** interface and adding your implementation to the classpath when running the application.  In order to allow users to access the API, not only have them to be authenticated, but they also need to provide the appropriate user claims. User claims grant the user permissions to perform some certain actions. Currently the only requested claim for the whole application is `ZOOKEEPER_USER`.   If you are using the default user authentication provider (based on the configuration properties), all the authenticated users will have this permission included by default. If you are using your custom authentication provider, you will have to make sure that the users have the `ZOOKEEPER_USER` claim. 
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

import * as models from './models';

export interface ChildrenNodes {
    /**
     * The list of children node names.
     */
    children: Array<string>;

}
