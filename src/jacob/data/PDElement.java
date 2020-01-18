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

import jacob.system.InternalException;

class PDElement extends XMLlikeElement
{

//------------------------------------------------------------------------------
//     Constructor Methods
//------------------------------------------------------------------------------

    public PDElement( String name )
    {
        super( name );
    }

//------------------------------------------------------------------------------//     Create Element Methods
//------------------------------------------------------------------------------ 
    public DataElement newElement( String name )
    {
        PDElement e = new PDElement( name );
        elements.addElement( e );
        return e;
    } 

//------------------------------------------------------------------------------
//     Read Methods
//------------------------------------------------------------------------------

    public void read( InputStream is ) 
        throws IOException, DataParseException
    {
        throw new InternalException( "illegal operation" );
    }

//------------------------------------------------------------------------------
//     Write Methods
//------------------------------------------------------------------------------

    public void write( OutputStream os )
    {
        PrintWriter pw = new PrintWriter( os, true );
        write( pw );
        pw.close();
    }

    void write( PrintWriter pw )
    {
        if ( getName().equals( "particle" ) )
            writeAttributes( pw );

        writeElements( pw );
    }

    private void writeAttributes( PrintWriter pw )
    {
        try
        {
            String x = getAttribute( "x" );
            String y = getAttribute( "y" );
            String q = getAttribute( "charge" );
            pw.println( x + " " + y + " " + q );
        }
        catch ( DataParseException ex )
        {
        }
    }

    private void writeElements( PrintWriter pw )
    {
        Enumeration els = getElements();
 
        while ( els.hasMoreElements() )
        {
            PDElement e = (PDElement)els.nextElement();
            e.write( pw );    
        }
    }
}
