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

public class OscCharge extends VectorModifier  // VV 17/02/02
{
    public static final String TYPE = "osccharge";

    private double period = 0;
//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public OscCharge() {}

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

        double s = getProperties().oscchargeConst.getValue();

        double ex = vect.x * s;
        double ey = vect.y * s;

		double fi;

		if (ex == 0)
			if (ey >= 0)
				fi = -Math.PI/2;
			else
				fi = Math.PI/2;
		else
		{
			fi = Math.atan(-ey/ex);
			if (ex < 0)
				fi += Math.PI;
		}


        double Q  = Math.sqrt( ex * ex + ey * ey );
        double q;
        double tp;
        double costp = 0;
        double systime=System.currentTimeMillis();

        if ( period > 0 )
            tp = systime / period;
        else
            tp = systime / getProperties().oscchargePeriod.getValue();

		tp += fi; //by Dragan Simeonov
        iterator = new ParticleIterator( getComponent().createIterator() );

        while( !iterator.isDone() )
        {
            Particle p = iterator.getCurrentParticle();
            costp = Math.cos( tp );

			q = Q * costp;

            p.setCharge( q );  // VV 19/02/02

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

        g.setColor( getProperties().oscforceFg.getValue() );
        g.drawArrow( loc.x, loc.y, vect.x, vect.y,
                     getProperties().arrowHeadSize.getValue() );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        double s = getProperties().oscforceConst.getValue();

        Point2d vect = new Point2d();

        vect.x = data.getDoubleAttribute( "ex" ) / s;
        vect.y = data.getDoubleAttribute( "ey" ) / s;

        period = data.getIntAttribute( "period", 0 );

        moveHotspotRelAt( 0, vect );
    }

    public void writeData( DataElement data )
    {
        DataElement fdata = data.newElement( TYPE );

        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().oscforceConst.getValue();

        fdata.setAttribute( "ex", vect.x * s );
        fdata.setAttribute( "ey", vect.y * s );
        if ( period > 0 ) fdata.setAttribute( "period", period );
    }
}
