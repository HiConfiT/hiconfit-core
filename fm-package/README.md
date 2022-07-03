# fm-package

This package provides the management functionalities for [basic feature models](https://apps.dtic.mil/sti/pdfs/ADA235785.pdf).

## Features

- Provides an object representing a feature model
- Provides a feature model factory which can create a feature model parser for the format of the given feature model
- Supports the following feature model formats:
    1. [SPLOT feature models](splot-research.org). The file extension could be “.sxfm” or “.splx.”
    2. [FeatureIDE format](https://featureide.github.io). The file extension should be “xml.”
    3. v.control format. The feature model format of the v.control tool. The file extension should be “xmi.”
    4. [Glencoe format](https://glencoe.hochschule-trier.de). The file extension should be “json.”
    5. [Descriptive format](https://github.com/manleviet/CA-CDR-V2/blob/main/fm-package/src/test/resources/bamboobike.fm4conf). Our feature model format. The file extension should be “fm4conf”.



## How to get this package

| *version* | *status* |
|---|---|
| [1.3.9-alpha-15](https://github.com/manleviet/CA-CDR-V2/packages/1408657)| latest |
| [1.3.8](https://github.com/manleviet/CA-CDR-V2/packages/1408657?version=1.3.8) | stable |

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
