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

import java.util.Hashtable;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.awt.Color;

public interface DataElement
{

//------------------------------------------------------------------------------
//     Info Methods
//------------------------------------------------------------------------------
    
    public String getName();

    public int countAttributes();

    public int countElements();

//------------------------------------------------------------------------------
//     Get Attribute Methods
//------------------------------------------------------------------------------

    public int getIntAttribute( String name )
        throws DataParseException;

    public int getIntAttribute( String name, int defval );

    public double getDoubleAttribute( String name )
        throws DataParseException;

    public double getDoubleAttribute( String name, double defval );

    public boolean getBooleanAttribute( String name )
        throws DataParseException;

    public boolean getBooleanAttribute( String name, boolean defval );

    public String getStringAttribute( String name )
        throws DataParseException;

    public String getStringAttribute( String name, String defval );

    public Color getColorAttribute( String name )
        throws DataParseException;

    public Color getColorAttribute( String name, Color defval );

    public double[][] getDoubleMatrixAttribute( String name )
        throws DataParseException;

    public double[][] getDoubleMatrixAttribute( String name, 
                                                double defval[][] );

//REMIND: implement more matrix attributes 

    public Enumeration getAttributes();

//------------------------------------------------------------------------------
//     Set Attribute Methods
//------------------------------------------------------------------------------

    public void setAttribute( String name, int val );

    public void setAttribute( String name, double val );

    public void setAttribute( String name, boolean val );

    public void setAttribute( String name, String val );

    public void setAttribute( String name, Color val );

    public void setAttribute( String name, double val[][] );

//------------------------------------------------------------------------------
//     Get Element Methods
//------------------------------------------------------------------------------

    public DataElement getElement( String name )
        throws DataParseException;

    public DataElement getElementAt( int index )
        throws DataParseException;

    public DataElement getOptElement( String name );

    public Enumeration getElements();

//------------------------------------------------------------------------------
//     Element Create Methods
//------------------------------------------------------------------------------

    public DataElement newElement( String name );

//------------------------------------------------------------------------------
//     Read Methods
//------------------------------------------------------------------------------

    public void read( InputStream is ) 
        throws IOException, DataParseException;

//------------------------------------------------------------------------------
//     Write Methods
//------------------------------------------------------------------------------

    public void write( OutputStream os );
}
