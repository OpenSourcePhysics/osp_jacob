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
import jacob.geometry.*;
import jacob.data.*;

public class RandomForce9 extends ComponentModifier
{

    public static final String TYPE = "randomforce9";

    private Point2d cloc;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public RandomForce9() {}

//------------------------------------------------------------------------------
//    Type Methods
//------------------------------------------------------------------------------

    public String getType()
    {
        return TYPE;
    }

//------------------------------------------------------------------------------
//    Status Methods
//------------------------------------------------------------------------------


    public void added()
    {
        cloc = getComponent().getLocation();
    }


//------------------------------------------------------------------------------
//    Component Modification Methods
//------------------------------------------------------------------------------


        public void preIntegrationModify( long time )
	    {
	        double r = getProperties().randomForce.getValue();

	        double rx = (Math.random() - 0.5) * r;
	        double ry = (Math.random() - 0.5) * r;

	        Point2d loc = new Point2d( cloc );

	        loc.x += rx;
	        loc.y += ry;

	        getComponent().move( loc );
	    }


//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
    }

    public void writeData( DataElement data )
    {
        DataElement rdata = data.newElement( TYPE );
    }
}
