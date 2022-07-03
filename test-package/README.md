# test-package

This package supports the core functionalities related to knolwedge base testing and debugging tasks.

## Features

- Test case and test suite management
- Test case and test suite reading
- Translation of test cases into Choco constraints

## How to get this package

| *version* | *status* |
|---|---|
| [1.3.9-alpha-15](https://github.com/manleviet/CA-CDR-V2/packages/1408658)| latest |
| [1.3.8](https://github.com/manleviet/CA-CDR-V2/packages/1408658?version=1.3.8) | stable |

Please add the below script in the *settings.xml* file to download the Maven dependencies from GitHub package repository.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <activeProfiles>
        <activeProfile>github</activeProfile>
    </activeProfiles>

    <profiles>
        <profile>
            <id>github</id>
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://repo1.maven.org/maven2</url>
                </repository>
                <repository>
                    <id>github</id>
                    <url>https://maven.pkg.github.com/manleviet/*</url>
                </repository>
            </repositories>
        </profile>
    </profiles>
    
    <servers>
        <server>
            <id>github</id>
            <username>USERNAME</username>
            <password>TOKEN</password>
        </server>
    </servers>
</settings>
```
Replacing USERNAME with your GitHub username, and TOKEN with your personal access token 
(see [Creating a personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)). Note: your token must have the ```read:packages``` scope.
