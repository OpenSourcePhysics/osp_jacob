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

public final class DataMgr
{

//------------------------------------------------------------------------------
//     Constructor Methods
//------------------------------------------------------------------------------

    private DataMgr() {}

//------------------------------------------------------------------------------
//     Create Element Methods
//------------------------------------------------------------------------------

    public static DataElement createXMLElement( String name )
    {
        return new XMLlikeElement( name );
    }

    public static DataElement createPDElement()
    {
        return new PDElement( "" );
    }

//------------------------------------------------------------------------------
//     Write Element Methods
//------------------------------------------------------------------------------

    public static void writeElement( OutputStream os, DataElement e )
    {
        e.write( os );
    }

//------------------------------------------------------------------------------
//     Read Element Methods
//------------------------------------------------------------------------------

    public static DataElement readXMLElement( InputStream is )
        throws IOException, DataParseException
    {
        DataElement e = createXMLElement( "" );
        e.read( is );
        return e;
    }
}
