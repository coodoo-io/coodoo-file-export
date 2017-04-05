# File Export #

*Convenience util to provide POJO list exports as downloadable files in a JavaEE/Rest environment*

This library provides you a simple way to download a list of objects as an Excel, CSV or Word document.

## Getting started

1. Add the [maven dependency](http://search.maven.org/#artifactdetails%7Cio.coodoo%7Ccoodoo-file-export%7C1.0.1%7Cjar)

   ```xml
	<dependency>
	    <groupId>io.coodoo</groupId>
	    <artifactId>coodoo-file-export</artifactId>
	    <version>1.0.1</version>
	</dependency>
   ```

2. Provide a List for download via Rest

   ```java
   
    @GET
    @Path("/export")
    @Produces({FileExport.MEDIA_TYPE_XLS})
    public Response export() {

        List<Item> items = getItems();

        return FileExport.createXLSResponse(items, "Items-Export");
    }
    ```
   
3. Make it pretty

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


## Customization
Without any customization, the exported files will contain mostly what you can see in the POJO: Technical attribute names, results of plain toString() methods, etc.
Therefore you can use following annotations to adjust the outcome of the export file:

`@ExportTitle("I am a headline!")`
The resulting file will begin with the text found at this annotation (not in CSV).  

`@ExportColumn("Best Value")`
This will name the column of the annotated attribute in the export file.  

`@ExportDateTimePattern()`
To use at a `java.util.Date` or `java.time.LocalDateTime` attribut to get it formatted by this the pattern "dd.MM.yyyy HH:mm" or the pattern you put at the annotation. 

`@ExportBooleanLabels()`
Put it an a boolian attribute and this will get you a "X" when it is `true` and nothing when it is `false`.  As seen above you can define your own labels.

`@ExportIgnoreField()`
This is your way to restrict an attribute from the export.

   
## Configuration

To provide own configuration you need to add a property file named `coodoo.fileexport.properties` to your project. This file gets read on JavaEE server startup if available or manually by calling `FileExportConfig.loadConfig()`;

These are the properties to be defined on the file:

```properties
### coodoo file export settings ###

## Timestamp pattern for the usage in the export filename
coodoo.fileexport.export.timestamp.pattern = yyyyMMddHHmmss

## XSL creation: Amount of rows to keep in memory, exceeding rows will be flushed to disk
coodoo.fileexport.xls.buffer.limit = 1000

## CSV separator
coodoo.fileexport.csv.separator = ;

## CSV quotation for values that contain the designated CSV seperator
coodoo.fileexport.csv.quotes = "
```
*You can find a template [here](https://github.com/coodoo-io/coodoo-file-export/tree/master/src/main/resources/example.coodoo.fileexport.properties)*