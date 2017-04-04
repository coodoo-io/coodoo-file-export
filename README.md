# File Export #

*Convenience util to provide POJO list exports as downloadable files in a JavaEE/Rest environment*

This library provides you a simple way to download a list of objects as an Excel, CSV or Word document.

## Getting started

1. Add the [maven dependency](http://search.maven.org/#artifactdetails%7Cio.coodoo%7Ccoodoo-file-export%7C1.0.0%7Cjar)

   ```xml
	<dependency>
	    <groupId>io.coodoo</groupId>
	    <artifactId>coodoo-file-export</artifactId>
	    <version>1.0.0</version>
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

  