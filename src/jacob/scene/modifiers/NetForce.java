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


//by Dragan Simeonov 24.01.2003
public class NetForce extends ComponentModifier
{
    public static final String TYPE = "netforce";

    private double ex = 0;

    private double ey = 0;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public NetForce() {}

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
        ex = ey = 0;
    }

//------------------------------------------------------------------------------
//    Component Modification Methods
//------------------------------------------------------------------------------

    public void preIntegrationModify( long time )
    {
		ex = ey = 0;
	}

    public void postIntegrationModify( long time )
    {
        ParticleIterator iterator;

        iterator = new ParticleIterator( getComponent().createIterator() );
        double econst = getProperties().evectorScale.getValue();
		double mconst = getProperties().mvectorScale.getValue();

        while ( !iterator.isDone() )
        {

            Particle p = iterator.getCurrentParticle();

          if ( econst > 0 )
         {
			 ex += p.getEx();
             ey += p.getEy();
		 }
		 if ( mconst > 0 )
		 {
             ex += p.getMx();
             ey += p.getMy();
	     }

            iterator.next();
        }
    }
//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintBg( SceneGraphics g )
    {
        g.setColor( getProperties().elementTranBg.getValue() );

    }


    public void paintFg( SceneGraphics g )
    {
        double econst = getProperties().evectorScale.getValue();
        g.setColor( getProperties().netforceFg.getValue() );
        Point2d loc  = getComponent().getLocation();
        g.drawArrow( loc.x, loc.y, ex*econst+loc.x, ey*econst+loc.y,
                     getProperties().arrowHeadSize.getValue() );
    }


//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException {}

    public void writeData( DataElement data )
    {
        DataElement rdata = data.newElement( TYPE );
    }
}
