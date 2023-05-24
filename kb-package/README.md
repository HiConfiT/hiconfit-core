# chocokb-package

A Maven package for Configuration Knowledge Bases in Choco Solver.

This package provides classes managing CSP (Choco) representations of a knowlege base.

## Features

- Provides an abstract **KB** class managing variables, variable domains, and constraints of a knowledge base/feature model
- Provides the following Configuration Knowledge Bases:
  1. **FMKB** - an implementation for the CSP representation of feature models. The input of **FMKB** is a **FeatureModel** object (from [fm-package](https://github.com/manleviet/CA-CDR-V2/tree/main/fm-package)). So you can use **FMKB** for all feature models you have, i.e., no need to implement a specific **KB** class.
  2. **PCKB** - an implementation of [PC Configuration Knowledge Base](https://www.itu.dk/research/cla/externals/clib/).
  3. **RenaultKB** - an implementation of [Renault Configuration Knowledge Base](https://www.itu.dk/research/cla/externals/clib/).
- Provides utility functions for constraints

## How to get this package

| *version*                                                                      | *status* |
|--------------------------------------------------------------------------------|---|
| [1.3.9-alpha-47](https://github.com/manleviet/CA-CDR-V2/packages/1408660)      | latest |
| [1.3.8](https://github.com/manleviet/CA-CDR-V2/packages/1408660?version=1.3.8) | stable |

Please add the below script in the *settings.xml* file.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>github-maven-repository</id>
            <username>USERNAME</username>
            <password>TOKEN</password>
        </server>
    </servers>
</settings>
```
Replacing USERNAME with your GitHub username, and TOKEN with your personal access token 
(see [Creating a personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)).

Add also the below script in the *pom.xml* file:

```xml
<profiles>
    <profile>
        <id>github-maven-repository</id>
        <repositories>
            <repository>
                <id>github-maven-repository</id>
                <url>https://maven.pkg.github.com/manleviet/*</url>
            </repository>
        </repositories>
    </profile>
</profiles>
```
