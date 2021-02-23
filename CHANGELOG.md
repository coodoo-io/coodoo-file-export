

<!--
### Bug Fixes
### Features
### BREAKING CHANGES
-->

<a name="1.0.4"></a>

## [1.0.4] - 2020-04-08

### Bug Fixes
 * The buffered SXSSFWorkbook is not supported with Java 9 and newer Java versions, so we switched to XSSFWorkbook. Therefore `FileExportConfig.XSL_BUFFER_LIMIT` has no function anymore and is deprecated.
 
 
<a name="1.0.3"></a>

## 1.0.3 (2018-01-25)

### Bug Fixes
 * Fixing Content-Header Issue for Microsoft Office

<a name="1.0.2"></a>

## 1.0.2 (2017-04-20)

### Features
 * Interface `ExportReadable` to provide String representation of objects or enumerations
 * Method `buildDOC` without the need for a file name
 * No EJB components anymore, just plain CDI


<a name="1.0.1"></a>

## 1.0.1 (2017-04-05)

### Bug Fixes
 * @Stateless removed
 * Configuration static loader


<a name="1.0.0"></a>

## 1.0.0 (2017-04-04)

### Features

Initial release:

* Utility to create export files
* Supported file types: XLS, CSV & DOC (rudimentary)
* Annotations to specify the result in the target POJO
* Settings via property file
