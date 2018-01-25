# File Export

> Convenience util to provide POJO list exports as downloadable files in a JavaEE/Rest environment

## Table of Contents

- [Background](#background)
- [Install](#install)
- [Usage](#usage)
- [API](#api)
- [Configuration](#configuration)
- [Changelog](#changelog)
- [Maintainers](#maintainers)
- [Contribute](#contribute)
- [License](#license)

## Background

Every application contains lists and this lists should be downloadable, often lovelessly realized by some improvised CSV text assembling.
So you need to implement this assembling for all the attributes a list item has. After that you repeat the same for the next list, over and over again.
The code may remain the same, but the attributes and requirements changes and you end up with a lot of code that needs to get maintained for every little change on the list.

This library provides you a simple way to download a list of objects as an Excel, CSV or Word document just by giving it the list.


## Install

Add the following dependency to your project ([published on Maven Central](http://search.maven.org/#artifactdetails%7Cio.coodoo%7Ccoodoo-file-export%7C1.0.3%7Cjar)):

```xml
<dependency>
    <groupId>io.coodoo</groupId>
    <artifactId>coodoo-file-export</artifactId>
    <version>1.0.3</version>
</dependency>
```

## Usage

Provide a List for download via Rest

```java
 @GET
 @Path("/export")
 @Produces({FileExport.MEDIA_TYPE_XLS})
 public Response export() {

     List<Item> items = getItems();

     return FileExport.createXLSResponse(items, "Items-Export");
 }
```

Make it pretty

```java
 @ExportTitle("Coodoo Items Example")
 public class Item {

     @ExportColumn("ID")
     private Long id;

     @ExportColumn("Name")
     private String name;

     @ExportColumn("In Stock")
     @ExportBooleanLabels(trueLabel = "Yes", falseLabel = "No")
     private boolean available;

     @ExportColumn("Produced")
     @ExportDateTimePattern("dd.MM.yyyy")
     private LocalDateTime producingDate;

     @ExportIgnoreField
     private String internalInfo;

     // ...
 }
```


## API

The class `FileExport` provides static methods and constants designed to transform a list of objects into a download as seen above.

### Customization
Without any customization, the exported files will contain mostly what you can see in the POJO: Technical attribute names, results of plain toString() methods, etc.
Therefore you can use following annotations to adjust the outcome of the export file:

| Annotation                         | Description                                                                                                                                                             |
|------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `@ExportTitle("Everything")` | The resulting file will begin with the text found at this annotation (not in CSV).                                                                                       |
| `@ExportColumn("Something")`      | This will name the column of the annotated attribute in the export file.                                                                                                 |
| `@ExportDateTimePattern()`         | To use at a `java.util.Date` or `java.time.LocalDateTime` attribute to get it formatted by this the pattern "dd.MM.yyyy HH:mm" or the pattern you put at the annotation. |
| `@ExportBooleanLabels()`           | Put it an a boolean attribute and this will get you a "X" when it is `true` and nothing when it is `false`.,As seen above you can define your own labels.                |
| `@ExportIgnoreField()`             | This is your way to restrict an attribute from the export.                                                                                                               |

### Example implementation

See the 
[example Rest resource](https://github.com/coodoo-io/coodoo-framework-showcase/blob/master/src/main/java/io/coodoo/framework/showcase/fileexport/boundary/FileExportResource.java)
that provides the POJO lists as downloadable files:
- [Raw](https://github.com/coodoo-io/coodoo-framework-showcase/blob/master/src/main/java/io/coodoo/framework/showcase/fileexport/entity/RawCar.java) *A plain POJO (entity) without makeup*
- [Custom](https://github.com/coodoo-io/coodoo-framework-showcase/blob/master/src/main/java/io/coodoo/framework/showcase/fileexport/entity/CustomCar.java) *Same POJO as Raw, but annotated to make the resulting file look pretty*
- [Price list](https://github.com/coodoo-io/coodoo-framework-showcase/blob/master/src/main/java/io/coodoo/framework/showcase/fileexport/boundary/CarPricelistPojo.java) *A POJO just to fit the needs of the wanted export file, as it says a car price list*


## Configuration

To provide own configuration you need to add a property file named `coodoo.fileexport.properties` to your project. This file gets read on JavaEE server startup if available or manually by calling `FileExportConfig.loadConfig()`;

You can find a template [here](https://github.com/coodoo-io/coodoo-file-export/tree/master/src/main/resources/example.coodoo.fileexport.properties)


## Changelog

All release changes can be viewed on our [changelog](./CHANGELOG.md).

## Maintainers

[Jan Marsh](https://github.com/JPM84)

[Arend Kühle](https://github.com/laugen)

## Contribute

Pull requests and [issues](https://github.com/coodoo-io/coodoo-file-export/issues) are welcome.

## License

[MIT © coodoo GmbH](./LICENSE)
