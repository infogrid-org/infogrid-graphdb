//
// This file is part of InfoGrid(tm). You may not use this file except in
// compliance with the InfoGrid license. The InfoGrid license and important
// disclaimers are contained in the file LICENSE.InfoGrid.txt that you should
// have received with InfoGrid. If you have not received LICENSE.InfoGrid.txt
// or you do not consent to all aspects of the license and the disclaimers,
// no license is granted; do not use this file.
// 
// For more information about InfoGrid go to http://infogrid.org/
//
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import java.io.ObjectStreamException;
import java.text.ParseException;
import java.util.regex.Pattern;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringRepresentationParseException;
import org.infogrid.util.text.StringifierException;

/**
  * This represents a Binary Large Object DataType. It carries a MIME type, identifying its content.
  * The MIME type may be null, indicating that instances of this DataType can hold any data. If the MIME
  * type is non-null, only data of the respective MIME type can be held by instances.
  */
public final class BlobDataType
        extends
            DataType
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * The known MIME type patterns.
     */
    static final Pattern [] theKnownMimeTypePatterns;
    static {
        theKnownMimeTypePatterns = new Pattern[ BlobValue.KNOWN_MIME_TYPES.length ];
        for( int i=0 ; i<theKnownMimeTypePatterns.length ; ++i ) {
            theKnownMimeTypePatterns[i] = Pattern.compile( BlobValue.KNOWN_MIME_TYPES[i] );
        }
    }

    /**
      * This DataType allows any MIME type.
      */
    public static final BlobDataType theAnyType = create(
            "",
            BlobValue.KNOWN_MIME_TYPES,
            new Pattern[] { Pattern.compile( ".*" ) },
            null );

    /**
      * This DataType allows any known MIME type.
      */
    public static final BlobDataType theAnyKnownType = create(
            "",
            BlobValue.KNOWN_MIME_TYPES,
            theKnownMimeTypePatterns,
            null );

    /**
     * This is a text DataType of any text format, plain or formatted.
     */
    public static final BlobDataType theTextAnyType = create(
            "",
            new String[] {
                    BlobValue.TEXT_PLAIN_MIME_TYPE,
                    BlobValue.TEXT_HTML_MIME_TYPE
            },
            new Pattern[] {
                    Pattern.compile( Pattern.quote( BlobValue.TEXT_PLAIN_MIME_TYPE )),
                    Pattern.compile( Pattern.quote( BlobValue.TEXT_HTML_MIME_TYPE ))
            },
            theAnyType );

    /**
     * This is a plain text DataType.
     */
    public static final BlobDataType theTextPlainType = create(
            "",
            new String[]  { BlobValue.TEXT_PLAIN_MIME_TYPE },
            new Pattern[] { Pattern.compile( Pattern.quote( BlobValue.TEXT_PLAIN_MIME_TYPE )) },
            theTextAnyType );

    /**
     * This is an HTML text DataType.
     */
    public static final BlobDataType theTextHtmlType = create(
            "",
            new String[]  { BlobValue.TEXT_HTML_MIME_TYPE },
            new Pattern[] { Pattern.compile( Pattern.quote( BlobValue.TEXT_HTML_MIME_TYPE )) },
            theTextAnyType );

    /**
     * This is an XML text DataType.
     */
    public static final BlobDataType theTextXmlType = create(
            "<xml/>",
            new String[] {
                    BlobValue.TEXT_XML_MIME_TYPE,
                    BlobValue.APPLICATION_XML_MIME_TYPE
            },
            new Pattern[] {
                    Pattern.compile( Pattern.quote( BlobValue.TEXT_XML_MIME_TYPE )),
                    Pattern.compile( Pattern.quote( BlobValue.APPLICATION_XML_MIME_TYPE )),
                    Pattern.compile( "^application/(.+)\\+xml$" )
            },
            theTextAnyType );

    /**
     * Helper variable.
     */
    private static final String packageName;
    static{
        String className = BlobDataType.class.getName();
        packageName      = className.substring( 0, className.lastIndexOf( '.' ) ).replace( '.', '/' );
    }

    /**
     * This is an image DataType whose underlying representation is supported
     * by the JDK. Currently that is GIF, JPG and PNG.
     */
    public static final BlobDataType theJdkSupportedBitmapType = createByLoadingFrom(
            BlobDataType.class.getClassLoader(),
            packageName + "/BlobDefaultValue.gif",
            new String[] {
                    BlobValue.IMAGE_GIF_MIME_TYPE,
                    BlobValue.IMAGE_JPEG_MIME_TYPE,
                    BlobValue.IMAGE_PNG_MIME_TYPE
            },
            new Pattern[] {
                    Pattern.compile( Pattern.quote( BlobValue.IMAGE_GIF_MIME_TYPE )),
                    Pattern.compile( Pattern.quote( BlobValue.IMAGE_JPEG_MIME_TYPE )),
                    Pattern.compile( Pattern.quote( BlobValue.IMAGE_PNG_MIME_TYPE ))
            },
            theAnyType );

    /**
     * This is a GIF DataType.
     */
    public static final BlobDataType theGifType = createByLoadingFrom(
            BlobDataType.class.getClassLoader(),
            packageName + "/BlobDefaultValue.gif",
            new String[]  { BlobValue.IMAGE_GIF_MIME_TYPE },
            new Pattern[] { Pattern.compile( Pattern.quote( BlobValue.IMAGE_GIF_MIME_TYPE )) },
            theJdkSupportedBitmapType );

    /**
     * This is a JPG DataType.
     */
    public static final BlobDataType theJpgType = createByLoadingFrom(
            BlobDataType.class.getClassLoader(),
            packageName + "/BlobDefaultValue.jpg",
            new String[]  { BlobValue.IMAGE_JPEG_MIME_TYPE },
            new Pattern[] { Pattern.compile( Pattern.quote( BlobValue.IMAGE_JPEG_MIME_TYPE )) },
            theJdkSupportedBitmapType );

    /**
     * This is a PNG DataType.
     */
    public static final BlobDataType thePngType = createByLoadingFrom(
            BlobDataType.class.getClassLoader(),
            packageName + "/BlobDefaultValue.png",
            new String[]  { BlobValue.IMAGE_PNG_MIME_TYPE },
            new Pattern[] { Pattern.compile( Pattern.quote( BlobValue.IMAGE_PNG_MIME_TYPE )) },
            theJdkSupportedBitmapType );

    /**
      * This is the default instance of this class (plain text).
      */
    public static final BlobDataType theDefault = theAnyType;

    /**
     * Factory method.
     *
     * @param defaultValue default value for instances of this DataType, expressed as String. May be null.
     * @param defaultValueMimeTypes default MIME types, the first one being the MIME type of the default value
     * @param mimeTypeRegexes the allowed MIME types
     * @param superType supertype of this DataType
     * @return the created BlobDataType
     */
    public static BlobDataType create(
            String     defaultValue,
            String []  defaultValueMimeTypes,
            Pattern [] mimeTypeRegexes,
            DataType   superType )
    {
        return new BlobDataType( defaultValue, defaultValueMimeTypes, mimeTypeRegexes, superType );
    }

    /**
     * Factory method.
     *
     * @param defaultValue default value for instances of this DataType, expressed as byte array. May be null.
     * @param defaultValueMimeTypes default MIME types, the first one being the MIME type of the default value
     * @param mimeTypeRegexes the allowed MIME types
     * @param superType supertype of this DataType
     * @return the created BlobDataType
     */
    public static BlobDataType create(
            byte []    defaultValue,
            String[]   defaultValueMimeTypes,
            Pattern [] mimeTypeRegexes,
            DataType   superType )
    {
        return new BlobDataType( defaultValue, defaultValueMimeTypes, mimeTypeRegexes, superType );
    }

    /**
     * Factory method.
     *
     * @param defaultValueLoader the ClassLoader through which the default value is loaded
     * @param defaultValueLoadFrom the location relative to loader from which we load
     * @param defaultValueMimeTypes default MIME types, the first one being the MIME type of the default value
     * @param mimeTypeRegexes the allowed MIME types
     * @param superType supertype of this DataType
     * @return the created BlobDataType
     */
    public static BlobDataType createByLoadingFrom(
            ClassLoader defaultValueLoader,
            String      defaultValueLoadFrom,
            String []   defaultValueMimeTypes,
            Pattern []  mimeTypeRegexes,
            DataType    superType )
    {
        return new BlobDataType( defaultValueLoader, defaultValueLoadFrom, defaultValueMimeTypes, mimeTypeRegexes, superType );
    }

    /**
     * Private constructor, use factory methods.
     *
     * @param defaultValue default value for instances of this DataType, expressed as String. May be null.
     * @param defaultValueMimeTypes default MIME types, the first one being the MIME type of the default value
     * @param mimeTypeRegexes the allowed MIME types
     * @param superType supertype of this DataType
     */
    private BlobDataType(
            String     defaultValue,
            String []  defaultValueMimeTypes,
            Pattern [] mimeTypeRegexes,
            DataType   superType )
    {
        super( superType );

        if( mimeTypeRegexes == null || mimeTypeRegexes.length == 0 ) {
            throw new IllegalArgumentException( "Must support at least one MIME type" );
        }
        theMimeTypeRegexes  = mimeTypeRegexes;
        theDefaultValue     = createBlobValueOrNull( defaultValue, defaultValueMimeTypes[0] );
        theDefaultMimeTypes = defaultValueMimeTypes;
    }

    /**
     * Private constructor, use factory methods.
     *
     * @param defaultValue default value for instances of this DataType, expressed as a byte array. May be null.
     * @param defaultValueMimeTypes default MIME types, the first one being the MIME type of the default value
     * @param mimeTypeRegexes the allowed MIME types
     * @param superType supertype of this DataType
     */
    private BlobDataType(
            byte []    defaultValue,
            String []  defaultValueMimeTypes,
            Pattern [] mimeTypeRegexes,
            DataType   superType )
    {
        super( superType );

        if( mimeTypeRegexes == null || mimeTypeRegexes.length == 0 ) {
            throw new IllegalArgumentException( "Must support at least one MIME type" );
        }
        theMimeTypeRegexes  = mimeTypeRegexes;
        theDefaultValue     = createBlobValueOrNull( defaultValue, defaultValueMimeTypes[0] );
        theDefaultMimeTypes = defaultValueMimeTypes;
    }

    /**
     * Private constructor, use factory methods.
     *
     * @param defaultValueLoader the ClassLoader through which the default value is loaded
     * @param defaultValueLoadFrom the location relative to loader from which we load
     * @param defaultValueMimeTypes default MIME types, the first one being the MIME type of the default value
     * @param mimeTypeRegexes the allowed MIME types
     * @param superType supertype of this DataType
     */
    private BlobDataType(
            ClassLoader defaultValueLoader,
            String      defaultValueLoadFrom,
            String []   defaultValueMimeTypes,
            Pattern []  mimeTypeRegexes,
            DataType    superType )
    {
        super( superType );

        if( mimeTypeRegexes == null || mimeTypeRegexes.length == 0 ) {
            throw new IllegalArgumentException( "Must support at least one MIME type" );
        }
        theMimeTypeRegexes  = mimeTypeRegexes;
        theDefaultValue     = createBlobValueByLoadingFrom( defaultValueLoader, defaultValueLoadFrom, defaultValueMimeTypes[0] );
        theDefaultMimeTypes = defaultValueMimeTypes;
    }

    
    /**
     * Factory method to construct one with formatted text according
     * to a certain MIME type in "abc/def" format.
     *
     * @param value the formatted text
     * @param mimeType the MIME type of the text, in "abc/def" format
     * @return the created BlobValue
     */
    public BlobValue createBlobValue(
            String       value,
            String       mimeType )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        if( value == null ) {
            throw new IllegalArgumentException( "null mimeType" );
        }
        if( !isAllowedMimeType( mimeType )) {
            throw new MimeTypeNotInDomainException( this, mimeType );
        }

        return new BlobValue.StringBlob( this, value, mimeType );
    }

    /**
     * Factory method to construct one with formatted text according
     * to a certain MIME type in "abc/def" format, or to return null if the
     * value argument is null.
     *
     * @param value the formatted text
     * @param mimeType the MIME type of the text, in "abc/def" format
     * @return the created BlobValue, or null
     */
    public BlobValue createBlobValueOrNull(
            String       value,
            String       mimeType )
    {
        if( value == null ) {
            return null;
        }
        if( mimeType == null ) {
            mimeType = BlobValue.TEXT_PLAIN_MIME_TYPE;
        }
        if( !isAllowedMimeType( mimeType )) {
            throw new MimeTypeNotInDomainException( this, mimeType );
        }

        return new BlobValue.StringBlob( this, value, mimeType );
    }

    /**
      * Factory method to construct one with formatted text according
      * to a certain MIME type in "abc/def" format.
      *
      * @param value the formatted text
      * @param mimeType the MIME type of the text, in "abc/def" format
      * @return the created BlobValue
      */
    public BlobValue createBlobValue(
            byte []      value,
            String       mimeType )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        if( mimeType == null ) {
            mimeType = BlobValue.OCTET_STREAM_MIME_TYPE;
        }
        if( !isAllowedMimeType( mimeType )) {
            throw new MimeTypeNotInDomainException( this, mimeType );
        }

        return new BlobValue.ByteBlob( this, value, mimeType );
    }

    /**
     * Factory method to construct one with formatted text according
     * to a certain MIME type in "abc/def" format, or to return null
     * if the argument is null.
     *
     * @param value the formatted text
     * @param mimeType the MIME type of the text, in "abc/def" format
     * @return the created BlobValue, or null
     */
    public BlobValue createBlobValueOrNull(
            byte []      value,
            String       mimeType )
    {
        if( value == null ) {
            return null;
        }
        if( mimeType == null ) {
            mimeType = BlobValue.OCTET_STREAM_MIME_TYPE;
        }
        if( !isAllowedMimeType( mimeType )) {
            throw new MimeTypeNotInDomainException( this, mimeType );
        }

        return new BlobValue.ByteBlob( this, value, mimeType );
    }

    /**
     * Factory method to construct one with a certain MIME type by loading it from
     * a certain path relative to a given ClassLoader.
     *
     * @param loader the ClassLoader through which this is loaded
     * @param loadFrom the location relative to loader from which we load
     * @param mimeType the MIME type of the BlobValue, in "abc/def" format
     * @return the created BlobValue
     */
    public BlobValue createBlobValueByLoadingFrom(
            ClassLoader  loader,
            String       loadFrom,
            String       mimeType )
    {
        if( loadFrom == null ) {
            throw new IllegalArgumentException( "null loadFrom" );
        }
        if( mimeType == null ) {
            throw new IllegalArgumentException( "null mime type" );
        }
        if( !isAllowedMimeType( mimeType )) {
            throw new MimeTypeNotInDomainException( this, mimeType );
        }

        return new BlobValue.DelayedByteBlob( this, loader, loadFrom, mimeType );
    }

    /**
     * Factory method to construct one with a certain MIME type by loading it from
     * a certain path relative to the default ClassLoader.
     *
     * @param loadFrom the location relative to the default ClassLoader
     * @param mimeType the MIME type of the BlobValue, in "abc/def" format
     * @return the created BlobValue
     */
    public BlobValue createBlobValueByLoadingFrom(
            String       loadFrom,
            String       mimeType )
    {
        if( !isAllowedMimeType( mimeType )) {
            throw new MimeTypeNotInDomainException( this, mimeType );
        }
        return new BlobValue.DelayedByteBlob( this, null, loadFrom, mimeType );
    }

    /**
     * Helper method to create an array of BlobValues from byte arrays.
     *
     * @param raw the array of byte arrays, or null
     * @param mimeType the MIME type of the byte arrays, in the same sequence
     * @return the corresponding array of raw, or null
     */
    public BlobValue [] createMultipleBlobValues(
            byte [][]    raw,
            String       mimeType )
    {
        if( raw == null ) {
            return null;
        }
        BlobValue [] ret = new BlobValue[ raw.length ];
        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = createBlobValue( raw[i], mimeType );
        }
        return ret;
    }

    /**
     * Determine the allowed MIME type regular expressions. If this is null, all MIME types are allowed.
     *
     * @return the MIME types
     */
    public Pattern [] getMimeTypeRegexes()
    {
        return theMimeTypeRegexes;
    }

    /**
      * Test for equality.
      *
      * @param other object to test against
      * @return if true, the objects are equal
      */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof BlobDataType ) {
            BlobDataType realOther = (BlobDataType) other;

            if( !ArrayHelper.hasSameContentOutOfOrder( theMimeTypeRegexes, realOther.theMimeTypeRegexes, true )) {
                return false;
            }

            if( theDefaultValue == null ) {
                return realOther.theDefaultValue == null;
            }

            return theDefaultValue.equals( realOther.theDefaultValue );
        }
        return false;
    }

    /**
     * Determine hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        int ret = 0;
        if( theDefaultValue != null ) {
            ret ^= theDefaultValue.hashCode();
        }
        for( int i=0 ; i<theMimeTypeRegexes.length ; ++i ) {
            ret ^= theMimeTypeRegexes[i].hashCode();
        }
        return ret;
    }

    /**
      * Test whether this DataType is a superset of or equals the argument
      * DataType. This is useful to test whether an assigment of
      * values is legal prior to attempting to do it.
      *
      * @param other the DataType we test against
      * @return if true, this DataType is a superset or equals the argument DataType
      */
    @Override
    public boolean isSupersetOfOrEquals(
            DataType other )
    {
        if( other instanceof BlobDataType ) {
            BlobDataType realOther = (BlobDataType) other;

            return ArrayHelper.firstHasSecondAsSubset( theMimeTypeRegexes, realOther.theMimeTypeRegexes, true );
        }
        return false;
    }

    /**
     * Determine whether this PropertyValue conforms to the constraints of this instance of DataType.
     *
     * @param value the candidate PropertyValue
     * @return 0 if the candidate PropertyValue conforms to this type. Non-zero values depend
     *         on the DataType; generally constructed by analogy with the return value of strcmp.
     * @throws ClassCastException if this PropertyValue has the wrong type (e.g.
     *         the PropertyValue is a StringValue, and the DataType an IntegerDataType)
     */
    public int conforms(
            PropertyValue value )
        throws
            ClassCastException
    {
        BlobValue realValue = (BlobValue) value;

        String mime = realValue.getMimeType();

        for( int i=0 ; i<theMimeTypeRegexes.length ; ++i ) {
            if( theMimeTypeRegexes[i].matcher( mime ).matches() ) {
                return 0;
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Determine whether a given MIME type is allowed.
     *
     * @param mime the MIME type to test
     * @return true if it is allowed
     */
    public boolean isAllowedMimeType(
            String mime )
    {
        for( int i=0 ; i<theMimeTypeRegexes.length ; ++i ) {
            if( theMimeTypeRegexes[i].matcher( mime ).matches() ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether this BlobDataType supports at least one text MIME type.
     *
     * @return true if it supports at least one text MIME type
     */
    public boolean supportsTextMimeType()
    {
        for( int i=0 ; i<theMimeTypeRegexes.length ; ++i ) {
            if( theMimeTypeRegexes[i].toString().startsWith( "\\Qtext/" )) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether this BlobDataType supports at least non non-text MIME type.
     *
     * @return true if it supports at least one MIME type that is not text
     */
    public boolean supportsBinaryMimeType()
    {
        for( int i=0 ; i<theMimeTypeRegexes.length ; ++i ) {
            if( !theMimeTypeRegexes[i].toString().startsWith( "\\Qtext/" )) {
                return true;
            }
        }
        return false;
    }

    /**
      * Determine whether a domain check shall be performed on
      * assignments. Default to false.
      *
      * @return if true, a domain check shall be performed prior to performing assignments
      */
    @Override
    public boolean getPerformDomainCheck()
    {
        // the old plan:
        // if we have a mime type, we'd like to do a domain check
        // the new plan:
        return true;

        // Reasoning: if we have a PropertyType (eg Content) which
        // accepts any MIME type, but a subtype accepts only a subset (eg
        // HTMLSection) we'd get an IllegalValueException in the subtype, but
        // not in the supertype, and the compiler won't like that ...
    }

    /**
      * Return a boolean expression in the Java language that uses
      * varName as an argument and that evaluates whether the content
      * of variable varName is assignable to a value of this data type.
      *
      * @param varName the name of the variable containing the value
      * @return the boolean expression
      */
    @Override
    public String getJavaDomainCheckExpression(
            String varName )
    {
        StringBuilder almostRet = new StringBuilder( 100 );
        almostRet.append( "(" );
        for( int i=0 ; i<theMimeTypeRegexes.length ; ++i ) {
            if( i!=0 ) {
                almostRet.append( " || " );
            }
            almostRet.append( theMimeTypeRegexes[i] );
            almostRet.append( ".matcher( " );
            almostRet.append( varName );
            almostRet.append( " ).matches() )" );
        }
        almostRet.append( CLOSE_PARENTHESIS_STRING );
        return almostRet.toString();
    }

    /**
      * Obtain the Java class that can hold values of this data type.
      *
      * @return the Java class that can hold values of this data type
      */
    public Class getCorrespondingJavaClass()
    {
        return BlobValue.class;
    }

    /**
     * Obtain the default value of this DataType.
     *
     * @return the default value of this DataType
     */
    public BlobValue getDefaultValue()
    {
        return theDefaultValue;
    }
    
    /**
     * Obtain all default MIME typse of this BlobDataType.
     *
     * @return the default MIME types.
     */
    public String [] getDefaultMimeTypes()
    {
        return theDefaultMimeTypes;
    }

    /**
     * Obtain the default MIME type of this BlobDataType.
     *
     * @return the default MIME type.
     */
    public String getDefaultMimeType()
    {
        return theDefaultMimeTypes[0];
    }

    /**
     * Convert to string for debugging. This uses '?' instead of '*' as common in mime types, but if we don't
     * we can't put this into a JavaDoc comment!
     *
     * @return this object in string form
     */
    @Override
    public String toString()
    {
        return  "BlobDataType: with"
                + ((theDefaultValue == null ) ? "out" : "" )
                + " default, mime: "
                + ( (theMimeTypeRegexes.length == 1 )
                        ? theMimeTypeRegexes[0]
                        : ArrayHelper.arrayToString( theMimeTypeRegexes, "," ));
    }

    /**
     * Correctly deserialize a static instance.
     *
     * @return the static instance if appropriate
     * @throws ObjectStreamException thrown if reading from the stream failed
     */
    public Object readResolve()
        throws
            ObjectStreamException
    {
        if( this.equals( theDefault )) {
            return theDefault;
        } else if( this.equals( theAnyType )) {
            return theAnyType;
        } else if( this.equals( theAnyKnownType )) {
            return theAnyKnownType;
        } else if( this.equals( theGifType )) {
            return theGifType;
        } else if( this.equals( theJpgType )) {
            return theJpgType;
        } else if( this.equals( thePngType )) {
            return thePngType;
        } else if( this.equals( theJdkSupportedBitmapType )) {
            return theJdkSupportedBitmapType;
        } else if( this.equals( theTextAnyType )) {
            return theTextAnyType;
        } else if( this.equals( theTextHtmlType )) {
            return theTextHtmlType;
        } else if( this.equals( theTextPlainType )) {
            return theTextPlainType;
        } else if( this.equals( theTextXmlType )) {
            return theTextXmlType;
        } else {
            return this;
        }
    }

    /**
     * Obtain a value expression in the Java language that invokes the constructor
     * of factory method of the underlying concrete class, thereby creating or
     * reusing an instance of the underlying concrete class that is identical
     * to the instance on which this method was invoked.
     *
     * This is used primarily for code-generation purposes.
     *
     * @param classLoaderVar name of a variable containing the class loader to be used to initialize this value
     * @return the Java language expression
     */
    public String getJavaConstructorString(
            String classLoaderVar )
    {
        final String className = getClass().getName();

        if( this == theAnyType ) {
            return className + ".theAnyType";
        } else if( this == theDefault ) {
            return className + DEFAULT_STRING;
        } else if( this == theAnyKnownType ) {
            return className + ".theAnyKnownType";
        } else if( this == theGifType ) {
            return className + ".theGifType";
        } else if( this == theJpgType ) {
            return className + ".theJpgType";
        } else if( this == thePngType ) {
            return className + ".thePngType";
        } else if( this == theJdkSupportedBitmapType ) {
            return className + ".theJdkSupportedBitmapType";
        } else if( this == theTextAnyType ) {
            return className + ".theTextAnyType";
        } else if( this == theTextHtmlType ) {
            return className + ".theTextHtmlType";
        } else if( this == theTextPlainType ) {
            return className + ".theTextPlainType";
        } else if( this == theTextXmlType ) {
            return className + ".theTextXmlType";
        } else {
            StringBuilder ret = new StringBuilder( className );
            ret.append( CREATE_STRING );
            if( theDefaultValue != null ) {
                ret.append( theDefaultValue.getJavaConstructorString( classLoaderVar, null )); // null is okay here
            } else {
                ret.append( NULL_STRING );
            }
            ret.append( COMMA_STRING );

            if( theMimeTypeRegexes != null ) {
                ret.append( "new java.util.Pattern[] { " );
                for( int i=0 ; i<theMimeTypeRegexes.length ; ++i ) {
                    ret.append( QUOTE_STRING );
                    ret.append( theMimeTypeRegexes[i].toString() );
                    ret.append( QUOTE_STRING );
                    if( i<theMimeTypeRegexes.length-1 ) {
                        ret.append( COMMA_STRING );
                    }
                }
                ret.append( " }" );
            } else {
                ret.append( NULL_STRING );
            }

            if( theSupertype != null ) {
                ret.append( theSupertype.getJavaConstructorString( classLoaderVar ));
            } else {
                ret.append( NULL_STRING );
            }
            ret.append( " )" );

            return ret.toString();
        }
    }

    /**
     * Obtain a String representation of this instance that can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentation(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        return rep.formatEntry(
                BlobValue.class,
                DEFAULT_ENTRY,
                pars,
                this,
                PropertyValue.toStringRepresentationOrNull( theDefaultValue, rep, pars ), // presumably shorter, but we don't know
                theMimeTypeRegexes,
                theSupertype );
    }

    /**
     * Obtain a PropertyValue that corresponds to this PropertyType, based on the String representation
     * of the PropertyValue.
     * 
     * @param representation the StringRepresentation in which the String s is given
     * @param s the String
     * @param mimeType the MIME type of the representation, if known
     * @return the PropertyValue
     * @throws PropertyValueParsingException thrown if the String representation could not be parsed successfully
     */
    public BlobValue fromStringRepresentation(
            StringRepresentation representation,
            String               s,
            String               mimeType )
        throws
            PropertyValueParsingException
    {
        try {
            Object [] found = representation.parseEntry( BlobValue.class, "String", s, this );

            BlobValue ret;

            switch( found.length ) {
                case 4:
                    ret = createBlobValue( (String) found[3], mimeType );
                    break;

                default:
                    throw new PropertyValueParsingException( this, representation, s );
            }

            return ret;

        } catch( StringRepresentationParseException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex.getFormatString(), ex );

        } catch( ParseException ex ) {
            throw new PropertyValueParsingException( this, representation, s, null, ex );

        } catch( ClassCastException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex );
        }
    }

    /**
     * The default value that goes with this DataType.
     */
    protected BlobValue theDefaultValue;

    /**
     * The default MIME types that goes with this DataType.
     */
    protected String [] theDefaultMimeTypes;

    /**
     * The set of MIME types allowed for BlobValues. This must not be null. If any one of the regexes matches,
     * the MIME type is supported.
     */
    protected Pattern [] theMimeTypeRegexes;
}
