# csp2choco-package

A translator, using the ANTLR4 runtime for parsing, enables converting CSP constraints into Choco Solver commands.

## Features

- Translate simple constraints in the form of __[variable]__ __[comparative op]__ __[variable/integer]__
- Support IntVar variables (either positive or negative)
- Support comparative operators: =, !=, >, >=, <, <=
 
## How to get this package

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
(see [Creating a personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)).

## TODOs:

- Try the annotator of ANTLR4 to share information among event methods
- Catching errors
- Extending the form of constraints
- Support relative operators: !, &&, ||
- Support implication
- Take a look at https://github.com/manleviet/choco-exppar
