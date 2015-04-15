alfresco-agreement-filter
=========================

This extension adds a must read page for every user before starting to use Alfresco.

The default page content is set in ```alfresco-global.properties``` file and must be an Alfresco FTS path. For instance:
```sh
/app:company_home/app:dictionary/app:publishing_root/cm:Alfresco_Terms_Of_Use.pdf
```

The plugin is licensed under the [LGPL v3.0](http://www.gnu.org/licenses/lgpl-3.0.html). The current version is compatible with Alfresco **5.0** CE.

![Screencap](https://cloud.githubusercontent.com/assets/1818300/7159455/7ddf5f10-e382-11e4-8a85-2967a8a26365.png)

Downloading the ready-to-deploy-plugin
--------------------------------------
The binary distribution is made of one AMP file for repo and another AMP file for share:

* [repo AMP](https://github.com/keensoft/alfresco-agreement-filter/releases/download/1.0.0/agreement-filter-repo-1.0.0.amp)
* [share AMP](https://github.com/keensoft/alfresco-agreement-filter/releases/download/1.0.0/agreement-filter-share-1.0.0.amp)

You can install it by using standard [Alfresco deployment tools](http://docs.alfresco.com/community/tasks/dev-extensions-tutorials-simple-module-install-amp.html)

Building the artifacts
----------------------
If you are new to Alfresco and the Alfresco Maven SDK, you should start by reading [Jeff Potts' tutorial on the subject](http://ecmarchitect.com/alfresco-developer-series-tutorials/maven-sdk/tutorial/tutorial.html).

You can build the artifacts from source code using maven
```sh
$ mvn clean package
```

Configuring the content
-----------------------
Include and set the following properties at ```alfresco-global.properties``` on your Alfresco installation:
```sh
# Agreement content path
agreement.file.path=/app:company_home/app:dictionary/app:publishing_root/cm:Alfresco_Terms_Of_Use.pdf
```
