# ChocoKB - at.tugraz.ist.ase.choco-kb

A Maven package for Configuration Knowledge Bases in Choco Solver

Knowledge bases:

1. [PC](https://www.itu.dk/research/cla/externals/clib/)
2. [Renault](https://www.itu.dk/research/cla/externals/clib/)
3. [Feature Models](http://www.splot-research.org)

## How to use

Add the below script in your pom file:

```
<dependency>
  <groupId>at.tugraz.ist.ase</groupId>
  <artifactId>choco-kb</artifactId>
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
