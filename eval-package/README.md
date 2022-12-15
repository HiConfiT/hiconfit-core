# eval-package

This package provides a performance evaluator, i.e., counters and timers, which could be used to measure the performance of algorithms.

## How to get this package

| *version*                                                                      | *status* |
|--------------------------------------------------------------------------------|---|
| [1.3.9-alpha-47](https://github.com/manleviet/CA-CDR-V2/packages/1408656)      | latest |
| [1.3.8](https://github.com/manleviet/CA-CDR-V2/packages/1408656?version=1.3.8) | stable |

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
