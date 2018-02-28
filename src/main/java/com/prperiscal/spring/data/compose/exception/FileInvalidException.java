package com.prperiscal.spring.data.compose.exception;

import java.io.FileNotFoundException;

import com.prperiscal.spring.data.compose.composer.DatabaseComposer;

/**
 * Signals that an attempt to compose from the file denoted by a specified pathname
 * has failed.
 *
 * <p> This exception will be thrown by the {@link DatabaseComposer}, when a file
 * with the specified pathname is not supported.
 *
 * @since   1.0.0
 * @author Pablo Rey Periscal
 */
public class FileInvalidException extends FileNotFoundException{

    private static final String MESSAGE_WITH_FORMAT = "Spring Data Compose does not support file format '%s' within file '%s'";
    private static final String MESSAGE_EMPTY = "Spring Data Compose does not support the given file format";

    /**
     * Constructs a <code>FileInvalidException</code> with
     * <code>MESSAGE_EMPTY</code> as its error detail message.
     */
    public FileInvalidException() {
        super(MESSAGE_EMPTY);
    }


    /**
     * Constructs a <code>FileInvalidException</code> with the
     * specified detail message. The message can be
     * retrieved later by the <code>{@link Throwable#getMessage}</code>
     * method of class <code>java.lang.Throwable</code>.
     *
     * @param   fileName   the file name.
     */
    public FileInvalidException(String fileName) {
        super(String.format(MESSAGE_WITH_FORMAT, fileName.substring(fileName.lastIndexOf(".")), fileName));
    }

}
