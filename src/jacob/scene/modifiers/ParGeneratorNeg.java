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

package jacob.scene.modifiers;

import jacob.scene.*;
import jacob.system.*;
import jacob.property.*;
import jacob.geometry.*;
import jacob.data.*;

public class ParGeneratorNeg extends ComponentModifier
{

    public static final String TYPE = "pargenneg";

    private double charge = -1;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public ParGeneratorNeg() {}

//------------------------------------------------------------------------------
//    Type Methods
//------------------------------------------------------------------------------

    public String getType()
    {
        return TYPE;
    }

//------------------------------------------------------------------------------
//    Component Modification Methods
//------------------------------------------------------------------------------

    public void postIntegrationModify( long time )
    {
//FIXME: nonparticle children are ok
        if ( getComponent().countChildren() == 0 )
        {
            Point2d loc = getComponent().getLocation();
            Particle p = new Particle( getScene(), charge );
            p.move( loc );
            getComponent().addChild( p );
        }
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        charge = data.getDoubleAttribute( "charge", charge );
    }

    public void writeData( DataElement data )
    {
        DataElement tdata = data.newElement( TYPE );
        tdata.setAttribute( "charge", charge );
    }
}
