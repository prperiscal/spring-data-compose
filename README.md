[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d4d0c3a0f6c04e0689af94007f23ea16)](https://www.codacy.com/app/prperiscal/spring-data-compose?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=prperiscal/spring-data-compose&amp;utm_campaign=Badge_Grade)

# spring-data-compose


## Overview
Spring module to compose database data dynamically for tests.

Loads data from a json resource and insert them in the available data source. A different compose file can be specified for each test.

This module does not provide any memory database such as H2 configuration neither a testcontainer with a proper database engine. This module, uses the available engine to compose data on each test method execution.


## Getting Started
* In a Maven .pom file:
```
<dependency>
  <groupId>com.prperiscal</groupId>
  <artifactId>spring-data-compose</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```
Also the repository will be necessary
```
<repositories>
  <repository>
    <id>Pablo-spring-data-compose</id>
    <url>https://packagecloud.io/Pablo/spring-data-compose/maven2</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```


* In a Gradle build.gradle file:
```
compile 'com.prperiscal:spring-data-compose:1.0.0-SNAPSHOT'
```
Also the repository will be necessary
```
repositories {
    maven {
        url "https://packagecloud.io/Pablo/spring-data-compose/maven2"
    }
}
}
```

## How to use it

* The test class must be annotated with:
```
@SpringDataCompose
```
This will inject the needed listeners to compose the data before their execution

* Each test method should provide the compose file to load by the annotation:
```
@DataComposeResource("ComposeFile.json")
```

That is all what is need to compose the data.

## Compose file format

The compose file should conforms to a defined format:

It must be a json file with two base elements:
* metadata
* entities 

```
{
  "metadata": {

  },
  "entities": {
  
  }
}
```

Inside the metadata element some basic information about the entities are expected by the following format:

```
"entiyName": {
      "_class": "FullClassName",
      "repository": "repositoryName",
      "id": "idFieldName"
    }
```

And inside the entities elements, an collection of entities with the data to insert:

```
"entiyName": [
      {
        "fieldA": "value",
        "fieldB": "value",
        "fieldC": "value",
        "fieldD": "value",
        "fieldE": "value"
      }
    ]
```

Data compose allows relations (OneToOne, OneToMany, ManyToMany) between entities. For that, the field that has the relation, has to be named with the prefix '@', and the value should be the fk value joining the name of the foreign entity plus '@'

```
"entities": {
    "entityA": [
      {
        "id": "1",
        "fieldA": "value",
        "fieldB": "value",
      },
      {
        "id": "2",
        "fieldA": "value",
        "fieldB": "value"
      }
    ],
    "entityB": [
      {
        "id": "1",
        "field": "value",
        "@fieldRelatedWithEntityA": [
          "entity1@1",
          "entity1@2"
        ]
      }
  }
```

A full example, assuming we have this two entities:

```
package com.model
public class User {

    @Id
    private long id;
    
    @Column(length = 127, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "users")
    private Set<UserGroup> userGroups = Sets.newHashSet();
}

package com.model
public class UserGroup {

    @Id
    private long id;
    
    @Column(length = 127, nullable = false)
    private String name;

    @ManyToMany
    private Set<User> users = Sets.newHashSet();
}
```

A compose file will looks like:

```
{
  "metadata": {
    "user": {
      "_class": "com.model.User",
      "repository": "userRepository",
      "id": "id"
    },
    "userGroup": {
      "_class": "com.UserGroup",
      "repository": "userGroupRepository",
      "id": "id"
    }
  },
  "entities": {
    "user": [
      {
        "id": "1",
        "name": "Pablo",
      },
      {
        "id": "2",
        "name": "Juan",
      },
      {
        "id": "2",
        "name": "Pedro",
      },
      {
        "id": "4",
        "name": "Marta",
      },
      {
        "id": "5",
        "name": "Raquel",
      }
    ],
    "userGroup": [
      {
        "id": "1",
        "name": "group1",
        "@users": [
          "user@1",
          "user@2",
          "user@3"
        ]
      },
      {
        "id": "2",
        "name": "group2",
        "@users": [
          "user@2",
          "user@3"
        ]
      },
      {
        "id": "3",
        "name": "group3",
        "@users": [
          "user@4",
          "user@1",
          "user@5"
        ]
      }
    ]
  }
}
```


## Contributing

Please read [CONTRIBUTING](https://gist.github.com/prperiscal/900729941edc5d5ddaaf9e21e5055a62) for details on our code of conduct, and the process for submitting pull requests to us.

## Workflow

Please read [WORKFLOW-BRANCHING](https://gist.github.com/prperiscal/ce8b8b5a9e0f79378475243e2d227011) for details on our workflow and branching directives. 


## Authors

* **Pablo Rey Periscal** - *Initial work* -

See also the list of [contributors]() who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
