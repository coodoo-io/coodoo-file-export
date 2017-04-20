package io.coodoo.framework.export.boundary;

/**
 * Interface to provide a String representation for the use in a file export.
 * 
 * @author coodoo GmbH (coodoo.io)
 */
public interface ExportReadable {

    /**
     * @return String representation of current object for the usage in file export
     */
    public String toExportValue();

}
