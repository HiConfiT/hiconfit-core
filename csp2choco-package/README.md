# CSP2ChocoTranslator

A translator, using the ANTLR4 runtime for parsing, enables converting CSP constraints into Choco Solver commands.

## Features

- Translate simple constraints in the form of __[variable]__ __[comparative op]__ __[variable/integer]__
- Support IntVar variables (either positive or negative)
- Support comparative operators: =, !=, >, >=, <, <=
 
## How to use

Add the below script in your pom file:

```
<dependency>
  <groupId>at.tugraz.ist.ase</groupId>
  <artifactId>csp2choco</artifactId>
  <version>1.2</version>
</dependency>
```
And the below script in the settings.xml file:

```
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
</settings>
```

## TODOs:

- Try the annotator of ANTLR4 to share information among event methods
- Catching errors
- Extending the form of constraints
- Support relative operators: !, &&, ||
- Support implication
- Take a look at https://github.com/manleviet/choco-exppar
