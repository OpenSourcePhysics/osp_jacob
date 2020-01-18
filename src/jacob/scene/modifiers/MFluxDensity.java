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

public class MFluxDensity extends VectorModifier
{
    public static final String TYPE = "mfluxdensity";

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public MFluxDensity() {}

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

//by Dragan Simeonov 08.10.02
    public void preIntegrationModify( long time )
    {
        ParticleIterator iterator;

        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().mfluxdensityScale.getValue();

        double b = vect.y / s;

        iterator = new ParticleIterator( getComponent().createIterator() );

        while( !iterator.isDone() )
        {
            Particle p = iterator.getCurrentParticle();

            p.setMx( p.getMx() +  p.getCharge() * p.getVy() * b);
            p.setMy( p.getMy() -  p.getCharge() * p.getVx() * b);
            iterator.next();
        }
    }


//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        Point2d loc  = getComponent().getLocation();
        Point2d vect = getHotspotAbsAt( 0 );
		double arr = getProperties().arrowHeadSize.getValue();


        g.setColor( getProperties().mfluxdensityFg.getValue() );
        g.drawLine( loc.x, loc.y, vect.x, vect.y );
        g.drawOval( vect.x - 2*arr, vect.y - 2*arr, 4*arr, 4*arr );
        if (getHotspotRelAt( 0 ).y > 0)
        {
			arr *= Math.sqrt(2);
        	g.drawLine(	vect.x - arr, vect.y - arr, vect.x + arr, vect.y + arr );
			g.drawLine(	vect.x - arr, vect.y + arr, vect.x + arr, vect.y - arr );
		}
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        double s = getProperties().mfluxdensityScale.getValue();

        Point2d vect = new Point2d();

        vect.x = data.getDoubleAttribute( "ex" ) * s;
        vect.y = data.getDoubleAttribute( "ey" ) * s;

        moveHotspotRelAt( 0, vect );
    }

    public void writeData( DataElement data )
    {
        DataElement fdata = data.newElement( TYPE );

        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().mfluxdensityScale.getValue();

        fdata.setAttribute( "ex", vect.x / s );
        fdata.setAttribute( "ey", vect.y / s );
    }
}
