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

public class PulseOscCharge extends VectorModifier  // VV 17/02/02/ VV 17/02/02
{
    public static final String TYPE = "pulseosccharge";

    private double period = 0;
    private double initQ = 1;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public PulseOscCharge() {}

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

    public void added()
    {
	     ParticleIterator iterator;

		 iterator = new ParticleIterator( getComponent().createIterator() );

		 if ( !iterator.isDone() )
		 {
         	Particle p = iterator.getCurrentParticle();
         	initQ = p.getCharge();
	 	 }
	 	 else
	 	 {
		  	initQ = 1;
	     }
    }

    public void preIntegrationModify( long time )
    {
        ParticleIterator iterator;

        Point2d loc = getComponent().getLocation();
        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().oscchargeConst.getValue();

        double ex = vect.x * s;
        double ey = vect.y * s;

        double Q = Math.sqrt( ex * ex + ey * ey );
		double q = Q;
        double tp;
        double systime=System.currentTimeMillis();

        if ( period > 0 )
            tp = systime / period;
        else
            tp = systime / getProperties().oscchargePeriod.getValue();

        iterator = new ParticleIterator( getComponent().createIterator() );

        while( !iterator.isDone() )
        {
           Particle p = iterator.getCurrentParticle();

		    if( Math.cos( tp ) > 0)
		   		q =  initQ < 0 ?  Q : -Q;
		    else
                q =  initQ < 0 ? -Q :  Q;

            p.setCharge( q );  // VV 27/02/02

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

        g.setColor( getProperties().transforceFg.getValue() );
        g.drawArrow( loc.x, loc.y, vect.x, vect.y,
                     getProperties().arrowHeadSize.getValue() );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        double s = getProperties().transforceConst.getValue();

        Point2d vect = new Point2d();

        vect.x = data.getDoubleAttribute( "ex" ) / s;
        vect.y = data.getDoubleAttribute( "ey" ) / s;

        period = data.getIntAttribute( "period", 0 );
        initQ  = data.getDoubleAttribute( "iQ", 1);
        moveHotspotRelAt( 0, vect );
    }

    public void writeData( DataElement data )
    {
        DataElement fdata = data.newElement( TYPE );

        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().transforceConst.getValue();

        fdata.setAttribute( "ex", vect.x * s );
        fdata.setAttribute( "ey", vect.y * s );
        fdata.setAttribute( "iQ", initQ);
        if ( period > 0 ) fdata.setAttribute( "period", period );
    }
}
