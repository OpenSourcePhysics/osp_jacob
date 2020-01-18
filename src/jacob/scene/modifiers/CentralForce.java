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

public class CentralForce extends VectorModifier
    implements EFModifier
{

    public static final String TYPE = "centralforce";

    private int period = 0;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public CentralForce() {}

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

    public void preIntegrationModify( long time )
    {
        ParticleIterator iterator;

        Point2d loc = getComponent().getLocation();
        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().centralforceConst.getValue();

        double ex = vect.x * s;
        double ey = vect.y * s;

        double e = Math.sqrt( ex * ex + ey * ey );

        iterator = new ParticleIterator( getComponent().createIterator() );

        while( !iterator.isDone() )
        {
            Particle p    = iterator.getCurrentParticle();
            Point2d  ploc = p.getLocation();

            double dx = ploc.x - loc.x;
            double dy = ploc.y - loc.y;

            double d = Math.sqrt( dx * dx + dy * dy );


			double fx = dx/d;
			double fy = dy/d;


            p.setEx( p.getEx() - e * fx );
            p.setEy( p.getEy() - e * fy );
            iterator.next();
        }
    }

//------------------------------------------------------------------------------
//    EFModifier Methods
//------------------------------------------------------------------------------

    public double[] getEAt(double x, double y)
    {
        double ef[] = new double[2];
        
        Point2d loc = getComponent().getLocation();
        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().centralforceConst.getValue();

        double ex = vect.x * s;
        double ey = vect.y * s;

        double e = Math.sqrt( ex * ex + ey * ey );

        double dx = x - loc.x;
        double dy = y - loc.y;

        double d = Math.sqrt( dx * dx + dy * dy );

		ef[0] = -e * dx/d;
        ef[1] = -e * dy/d;

        return ef;   
    }

    public double getExAt(double x, double y)
    {
        double ef[] = getEAt(x, y);

        return ef[0];
    }

    public double getEyAt(double x, double y)
    {
        double ef[] = getEAt(x, y);

        return ef[1];
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        Point2d loc  = getComponent().getLocation();
        Point2d vect = getHotspotAbsAt( 0 );

        g.setColor( getProperties().centralforceFg.getValue() );
        g.drawArrow( loc.x, loc.y, vect.x, vect.y,
                     getProperties().arrowHeadSize.getValue() );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        double s = getProperties().centralforceConst.getValue();

        Point2d vect = new Point2d();

        vect.x = data.getDoubleAttribute( "ex" ) * s;
        vect.y = data.getDoubleAttribute( "ey" ) * s;

        moveHotspotRelAt( 0, vect );
    }

    public void writeData( DataElement data )
    {
        DataElement fdata = data.newElement( TYPE );

        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().centralforceConst.getValue();

        fdata.setAttribute( "ex", vect.x / s );
        fdata.setAttribute( "ey", vect.y / s );
    }
}
