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

public class FunctionalForce extends VectorModifier
    implements EFModifier
{

    public static final String TYPE = "functionalforce";

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public FunctionalForce() {}

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

        double s = getProperties().eforceScale.getValue();//FIXME

        double ex = vect.x / s;
        double ey = vect.y / s;

        double e = Math.sqrt( ex * ex + ey * ey );

        iterator = new ParticleIterator( getComponent().createIterator() );

        while( !iterator.isDone() )
        {
            Particle p = iterator.getCurrentParticle();
            Point2d ploc = p.getLocation();

            double x = ploc.x;
            double y = ploc.y;
            double dx = x - loc.x;
            double dy = y - loc.y;
            double d  = Math.sqrt( dx * dx + dy * dy );
            int f = 2;


			{
			  switch (f)
			  {
				   case 1:
                    p.setEx( p.getEx() + e * (loc.y - y) );
                    p.setEy( p.getEy() - e * Math.abs(loc.y) );
                   break;
                   case 2:
                   if (y < loc.y)
                   {
                     p.setEx( p.getEx() + e * 1.0) ;
                     p.setEy( p.getEy() - e * 10.0) ;
				   }
                   else
                   {
                     p.setEx( p.getEx() - e * 1.0) ;
					 p.setEy( p.getEy() - e * 10.0) ;
				   }
                   break;
                   case 3:
                    p.setEx( p.getEx() + e * 0.0 );
                    p.setEy( p.getEy() + e * Math.sqrt( x + ex ) );
                   break;
                   default:
                    p.setEx( p.getEx() + e * 0.0 );
                    p.setEy( p.getEy() + e * Math.sqrt( x + ex ) );
                   break;
			 }
		   }

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

        double s = getProperties().eforceScale.getValue();//FIXME

        double ex = vect.x / s;
        double ey = vect.y / s;

        double e = Math.sqrt( ex * ex + ey * ey );

        double dx = x - loc.x;
        double dy = y - loc.y;

        double d  = Math.sqrt( dx * dx + dy * dy );
        int f = 1;


			switch (f)
			{
				case 1:
                  ef[0] =  e * (loc.y - y);
                  ef[1] = -e * Math.abs(loc.y);
                break;
				case 2:
				if (y < loc.y)
				{
                  ef[0] =  e * 1.0;
                  ef[1] = -e * 10.0;
			    }
                else
                {
                  ef[0] = -e * 1.0;
                  ef[1] = -e * 10.0;
			    }
				break;
				case 3:
                  ef[0] = e * 0.0;
                  ef[1] = e * Math.sqrt( x + ex ) ;
				break;
				default:
                  ef[0] = e * 0.0;
                  ef[1] = e * 0.0;
				break;
			}


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

        g.setColor( getProperties().circforceFg.getValue() );//FIXME
        g.drawArrow( loc.x, loc.y, vect.x, vect.y,
                     getProperties().arrowHeadSize.getValue() );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        double s = getProperties().eforceScale.getValue();//FIXME

        Point2d vect = new Point2d();

        vect.x = data.getDoubleAttribute( "ex" ) * s;
        vect.y = data.getDoubleAttribute( "ey" ) * s;

        moveHotspotRelAt( 0, vect );
    }

    public void writeData( DataElement data )
    {
        DataElement fdata = data.newElement( TYPE );

        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().eforceScale.getValue();//FIXME

        fdata.setAttribute( "ex", vect.x / s );
        fdata.setAttribute( "ey", vect.y / s );
    }
}
