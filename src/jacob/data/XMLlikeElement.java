//==============================================================================
//
//  Copyright (C) 2000  Vojko Valencic <Vojko.Valencic@fe.uni-lj.si>
//                      Savin Zlobec <savin@torina.fe.uni-lj.si>
//
//  This program is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; either version 2
//  of the License, or (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
//==============================================================================

package jacob.data;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.IOException;
import java.awt.Color;

class XMLlikeElement implements DataElement
{
    protected String name;

    protected Hashtable attributes;

//REMIND: we need the attributeOrder so that we can write the attributes
//        in some meaningful order - can we do this more elegantly?
    protected Vector attributeOrder; 

    protected Vector elements;

//------------------------------------------------------------------------------
//     Constructor Methods
//------------------------------------------------------------------------------

    public XMLlikeElement( String name )
    {
        this.name  = name.toLowerCase(); 
        attributes = new Hashtable();
        attributeOrder = new Vector(); 
        elements   = new Vector();
    }

//------------------------------------------------------------------------------
//     Info Methods
//------------------------------------------------------------------------------

    public String getName()
    {
        return name;
    }

    public int countAttributes()
    {
        return attributes.size();
    }

    public int countElements()
    {
        return elements.size();
    }

//------------------------------------------------------------------------------
//     Get Attribute Methods
//------------------------------------------------------------------------------

    public int getIntAttribute( String name )
        throws DataParseException
    {
        String val = getAttribute( name );

        try
        {
            return Integer.parseInt( val.trim() );
        }
        catch ( NumberFormatException ex ) 
        {
            throw new DataParseException( this,
                "can't parse integer attribute: " +
                "name='" + name + "' value='" + val + "'" );
        }
    }

    public int getIntAttribute( String name, int defval )
    {
        String val = getOptAttribute( name );

        if ( val == null ) return defval;

        try
        {
            return Integer.parseInt( val.trim() );
        }
        catch ( NumberFormatException ex ) 
        {
            return defval;
        }
    }

    public double getDoubleAttribute( String name )
        throws DataParseException
    {
        String val = getAttribute( name );

        try
        {
            return Double.valueOf( val.trim() ).doubleValue();
        }
        catch ( NumberFormatException ex ) 
        {
            throw new DataParseException( this,
                "can't parse double attribute: " + 
                "name='" + name + "' value='" + val + "'" );
        }
    }

    public double getDoubleAttribute( String name, double defval )
    {
        String val = getOptAttribute( name );

        if ( val == null ) return defval;

        try
        {
            return Double.valueOf( val.trim() ).doubleValue();
        }
        catch ( NumberFormatException ex ) 
        {
            return defval;
        }
    }

    public boolean getBooleanAttribute( String name )
        throws DataParseException
    {
        String val = getAttribute( name );

        if ( "true".equalsIgnoreCase( val ) )
            return true;
        if ( "false".equalsIgnoreCase( val ) )
            return false;
        else
            throw new DataParseException( this,
                "can't parse boolean attribute: " + 
                "name='" + name + "' value='" + val + "'" );
    }

    public boolean getBooleanAttribute( String name, boolean defval )
    {
        String val = getOptAttribute( name );

        if ( val == null ) return defval;

        if ( "true".equalsIgnoreCase( val ) )
            return true;
        if ( "false".equalsIgnoreCase( val ) )
            return false;
        else
            return defval;
    }

    public String getStringAttribute( String name )
        throws DataParseException
    {
        return getAttribute( name );
    }


    public String getStringAttribute( String name, String defval )
    {
        String val = getOptAttribute( name );

        if ( val == null )
            return defval;
        else
            return val;
    }

    public Color getColorAttribute( String name )
        throws DataParseException
    {
        String val = getAttribute( name );

        try
        {
            return Color.decode( val );
        } 
        catch ( NumberFormatException ex )
        {
            throw new DataParseException( this,
                "can't parse Color attribute: " + 
                "name='" + name + "' value='" + val + "'" );
        }
    }

    public Color getColorAttribute( String name, Color defval )
    {
        String val = getOptAttribute( name );

        if ( val == null ) return defval;
            
        try
        {
            return Color.decode( val );
        } 
        catch ( NumberFormatException ex )
        {
            return defval;
        }
    }

    public double[][] getDoubleMatrixAttribute( String name ) 
        throws DataParseException
    {
        String val = getAttribute( name );

        try
        {
            return parseDoubleMatrix( name, val );
        }
        catch ( NumberFormatException ex )
        {
            throw new DataParseException( this,
                "can't parse Double Matrix " + 
                "attribute: name='" + name + "' value='" + val + "'" ); 
        }
    }

    public double[][] getDoubleMatrixAttribute( String name, 
                                                double defval[][] )
    {
        String val = getOptAttribute( name );

        if ( val == null ) return defval;
            
        try
        {
            return parseDoubleMatrix( name, val );
        }
        catch ( Exception ex )
        {
            return defval;
        }
    }

    public Enumeration getAttributes()
    {
        return attributes.keys();
    }

    protected String getAttribute( String name )
        throws DataParseException
    {
        String val = (String)attributes.get( name.toLowerCase() );

        if ( val == null )
            throw new DataParseException( this,
                "no such attribute: name='" + name + "'" );
        return val;
    }

    protected String getOptAttribute( String name )
    {
        return (String)attributes.get( name.toLowerCase() );
    }

    protected double[][] parseDoubleMatrix( String name, String val )
        throws DataParseException, NumberFormatException
    {
        String matrix[][] = parseStringMatrix( name, val );
        double dmatrix[][] = new double[matrix.length][matrix[0].length];

        for ( int i = 0; i < matrix.length; i++ )
            for ( int j = 0; j < matrix[i].length; j++ )
                dmatrix[i][j] = 
                       Double.valueOf( matrix[i][j].trim() ).doubleValue(); 
        return dmatrix;
    }

    protected String[][] parseStringMatrix( String name, String val )
        throws DataParseException
    {
        StringTokenizer st = new StringTokenizer( val, ";" );
        String matrix[][] = null;;
        int nrows = st.countTokens();
        int ncols = -1;

        if ( nrows < 1 )
            throw new DataParseException( this, 
                "malformed matrix: name='" + name + "'" );
                                          
        for ( int i = 0; i < nrows; i++ )
        {
            String row = st.nextToken().trim();
            StringTokenizer strow = new StringTokenizer( row, "," );

            if ( ncols == -1 )
            {
                ncols  = strow.countTokens();
                matrix = new String[nrows][ncols];
            }
            else 
            {
                if ( ncols != strow.countTokens() )
                    throw new DataParseException( this,
                        "malformed matrix: name='" +  name + "'" );
            }                                               

            for ( int j = 0; j < ncols; j++ )
            {
                matrix[i][j] = strow.nextToken().trim();
            }
        }
        return matrix;
    }

//------------------------------------------------------------------------------
//     Set Attribute Methods
//------------------------------------------------------------------------------

    public void setAttribute( String name, int val )
    {
        setAttribute0( name, Integer.toString( val ) );
    }

    public void setAttribute( String name, double val )
    {
        setAttribute0( name, Double.toString( val ) );
    }

    public void setAttribute( String name, boolean val )
    {
        setAttribute0( name, val ? "true" : "false" );
    }

    public void setAttribute( String name, String val )
    {
        setAttribute0( name, val );
    }

    public void setAttribute( String name, Color val )
    {
        setAttribute0( name, "#" + 
            Integer.toHexString( val.getRGB() & 0x00ffffff ) );
    }

    public void setAttribute( String name, double[][] val )
    {
        int i, j;
        String sval = "";

        for( i = 0; i < val.length; i++ )
        {
            for( j = 0; j < val[i].length - 1; j++ )
            {
                sval += val[i][j] + ",";
            }
            if ( i < val.length - 1 )
                sval += val[i][j] + ";";
            else
                sval += val[i][j];
        }
        setAttribute0( name, sval );
    }

    protected void setAttribute0( String name, String value )
    {
        name = name.toLowerCase();

        attributes.put( name, value );
        attributeOrder.addElement( name ); 
    }

//------------------------------------------------------------------------------
//     Get Element Methods
//------------------------------------------------------------------------------

    public DataElement getElement( String name )
        throws DataParseException
    {
        name = name.toLowerCase();

        for ( int i = 0; i < elements.size(); i++ )
        {
            DataElement e = (DataElement)elements.elementAt( i );
            if ( e.getName().equals( name ) )
                return e;
        }
        throw new DataParseException( this,
            "no such element: name='" + name + "'" ); 
    }

    public DataElement getElementAt( int index )
        throws DataParseException
    {
        if ( index >= elements.size() )
            throw new DataParseException( this,
                "no such element: index='" + index + "'" );
                                           
        return (DataElement)elements.elementAt( index );
    }

    public DataElement getOptElement( String name )
    {
        name = name.toLowerCase();

        for ( int i = 0; i < elements.size(); i++ )
        {
            DataElement e = (DataElement)elements.elementAt( i );
            if ( e.getName().equals( name ) )
                return e;
        }
        return null; 
    }

    public Enumeration getElements()
    {
        return elements.elements();
    }

//------------------------------------------------------------------------------
//     Create Element Methods
//------------------------------------------------------------------------------

    public DataElement newElement( String name )
    {
        XMLlikeElement e = new XMLlikeElement( name );
        elements.addElement( e );
        return e;
    }

//------------------------------------------------------------------------------
//     Read Methods
//------------------------------------------------------------------------------

//FIXME: clean this up & implement tags like x=190 
//       (now we must quote everything - x="190")

    public void read( InputStream is ) 
        throws IOException, DataParseException
    {
        Reader br = new BufferedReader( new InputStreamReader( is ) );
        StreamTokenizer st = new StreamTokenizer( br );

        st.resetSyntax();
        st.wordChars( 'a', 'z' );
        st.wordChars( 'A', 'Z' );
        st.wordChars( '0', '9' );
        st.wordChars( 128 + 32, 255 );
        st.whitespaceChars( 0, ' ' );
        st.quoteChar('"');

        attributes.clear();
        attributeOrder.removeAllElements();
        elements.removeAllElements();

        if ( st.nextToken() != '<' )
        {
            throw new DataParseException( this, st.lineno(), 
                "expected '<' got '" + writeToken( st ) + "'" );
        }
        if ( st.nextToken() == StreamTokenizer.TT_WORD ) 
        {
            name = st.sval.toLowerCase();
        }
        else
        {
            throw new DataParseException( this, st.lineno(),
                "expected tag name got '" + writeToken( st ) + "'" );
        }
        readBody( st );
        br.close();
    }

    void readBody( StreamTokenizer st )
        throws IOException, DataParseException
    {
        /*
         *  read the attributes and if the end with '>' (return true)
         *  read the element
         */
        if ( readAttributes( st ) )
            readElements( st );
    }

    private boolean readAttributes( StreamTokenizer st )
        throws IOException, DataParseException
    {
        /*
         *  read this element attributes, if they end with: 
         *   - '/>' return false (end of this element)
         *   - '>'  return true  (may contain children)
         */
        while ( true )
        {
            int ttype = st.nextToken();

            if ( ttype == '/' )
            {
                if ( st.nextToken() != '>' )
                {
                    throw new DataParseException( this, st.lineno(), 
                        "expected '>' got '" + writeToken( st ) + "'" );
                }
                return false;
            }
            else if ( ttype == '>' )
            {
                return true;
            }
            else if ( ttype == StreamTokenizer.TT_WORD )
            {
                /*
                 *  got an attribute
                 */
                String name = st.sval.toLowerCase();

                if ( st.nextToken() != '=' )
                {
                    throw new DataParseException( this, st.lineno(),
                        "expected '=' got '" + writeToken( st ) + "'" );
                }
                if ( st.nextToken() != '"' )
                {
                    throw new DataParseException( this, st.lineno(),
                        "expected attribute value " + "got '" + 
                        writeToken( st ) + "'" );
                }
                setAttribute0( name, st.sval );
            }     
            else
            {
                throw new DataParseException( this, st.lineno(),
                    "expected tag attribute or " + "'>' or '/>' got '" +
                    writeToken( st ) + "'" );
            }
        }
    }

    private void readElements( StreamTokenizer st )
        throws IOException, DataParseException
    {
        while ( true )
        {
            if ( st.nextToken() == '<' )
            {
                int ttype = st.nextToken();

                if ( ttype == '/' )
                {
                    if ( st.nextToken() == StreamTokenizer.TT_WORD ) 
                    {
                        /*
                         *  element end tag - check if the names
                         *  match and return 
                         */
                        if ( !st.sval.toLowerCase().equals( getName() ) )
                        {
                            throw new DataParseException( this, st.lineno(),
                                "expected '</" + getName() + "' got '</" +
                                st.sval.toLowerCase() + "'" );
                        }
                        if ( st.nextToken() != '>' )
                        {
                            throw new DataParseException( this, st.lineno(),
                                "expected '</" + getName() + ">' got '</"
                                + getName() + writeToken( st ) + "'" );
                        }
                        return;
                    }
                    else
                    {
                        throw new DataParseException( this, st.lineno(),
                            "expected '</" + getName() + "' got '</"
                            + writeToken( st ) + "'" );
                    }     
                }
                else if ( ttype == StreamTokenizer.TT_WORD )
                {
                    /*
                     *  new element tag - create a new
                     *  element and let it read its body
                     */
                    String name = st.sval.toLowerCase();
                    XMLlikeElement e = (XMLlikeElement)newElement( name );
                    e.readBody( st );
                }
                else
                {
                    throw new DataParseException( this, st.lineno(),
                        "expected '<TAG' or '</" + " got '" +
                        writeToken( st ) +  "'" );
                }
            }
            else
            {
                throw new DataParseException( this, st.lineno(),
                    "expected '<' got '" + writeToken( st ) +  "'" );
            }
        }
    }

    private String writeToken( StreamTokenizer st )
    {
        switch ( st.ttype ) 
        {
        case StreamTokenizer.TT_EOF:
            return "EOF";
        case StreamTokenizer.TT_EOL:
            return "EOL";
        case StreamTokenizer.TT_WORD:
            return st.sval;
        case StreamTokenizer.TT_NUMBER:
            return Double.toString( st.nval );
        default:
        {
            char s[] = new char[1];
            s[0] = (char) st.ttype;
            return new String( s );
        }
        }
    }

//------------------------------------------------------------------------------
//     Write Methods
//------------------------------------------------------------------------------

    public void write( OutputStream os )
    {
        PrintWriter pw = new PrintWriter( os, true );
        write( pw, 0 );
        pw.close();
    }

    void write( PrintWriter pw, int tab )
    {
        writeTab( pw, tab );
        pw.print( "<" + getName() );
        writeAttributes( pw );

        if ( elements.size() == 0 )
        {
            pw.println( "/>" );
        }
        else
        {
            pw.println( ">" );
            writeElements( pw, tab + 1 );
            writeTab( pw, tab );
            pw.println( "</" + getName() + ">" );
        }
    }

    private void writeAttributes( PrintWriter pw )
    {
        /*
         *  write the attributes in the same order they were set
         */
        for ( int i = 0; i < attributeOrder.size(); i++ )
        {
            String aname = (String)attributeOrder.elementAt( i );
            String aval  = (String)attributes.get( aname );
            pw.print( " " + aname + "=" + "\"" + aval + "\"" );
        }
    }

    private void writeElements( PrintWriter pw, int tab )
    {
        Enumeration els = elements.elements();
 
        while ( els.hasMoreElements() )
        {
            XMLlikeElement e = (XMLlikeElement)els.nextElement();
            e.write( pw, tab );    
        }
    }

    private void writeTab( PrintWriter pw, int tab )
    {
        for ( int i = 0; i < tab; i++ )
            pw.print( " " );
    }
}
