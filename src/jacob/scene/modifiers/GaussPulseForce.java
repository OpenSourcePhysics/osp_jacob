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

public class GaussPulseForce extends VectorModifier
    implements EFModifier
{
    public static final String TYPE = "gausspulseforce";
 
    private int period = 0;

    private double tp = 0;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public GaussPulseForce() {} 

//------------------------------------------------------------------------------//    Type Methods
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

        double s = getProperties().eforceScale.getValue();

        double ex = vect.x / s;
        double ey = vect.y / s;

        double e = Math.sqrt( ex * ex + ey * ey );

        double t = 0;

        if ( period > 0 )
            tp = time / period;
        else
            tp = time / getProperties().gausspulseforcePeriod.getValue();

        iterator = new ParticleIterator( getComponent().createIterator() );

// Vojko, Gauss pulse force 14/10/00

        double cosf = Math.cos(tp-period);

        if (cosf > 0)
            t++;
        else
            t = 0;
            				
        double tc = (period/2 - t); 
    
        double gaussf = Math.exp( -tc * tc );
		 	
        double fx = ex * gaussf;
        double fy = ey * gaussf;
                    
        while( !iterator.isDone() )
        {
            Particle p = iterator.getCurrentParticle();
            Point2d ploc = p.getLocation(); 
            p.setEx( p.getEx() + fx );
            p.setEy( p.getEy() + fy );
            iterator.next();
        }
    }

//------------------------------------------------------------------------------
//    EFModifier Methods
//------------------------------------------------------------------------------

    public double[] getEAt(double x, double y)
    {
        double ef[] = new double[2];

        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().transforceConst.getValue();

        double ex = vect.x * s;
        double ey = vect.y * s;

        double e = Math.sqrt( ex * ex + ey * ey );

        double cosf = Math.cos(tp-period);

        double tc;

        if (cosf > 0)
            tc = (period/2 - 1);
        else
            tc = period/2;
            				
        double gaussf = Math.exp( -tc * tc );
		 	
        ef[0] = ex * gaussf;
        ef[1] = ey * gaussf;
 
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

        g.setColor( getProperties().gausspulseforceFg.getValue() );
        g.drawArrow( loc.x, loc.y, vect.x, vect.y, 
                     getProperties().arrowHeadSize.getValue() );
    } 

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        double s = getProperties().eforceScale.getValue();

        Point2d vect = new Point2d();

        vect.x = data.getDoubleAttribute( "ex" ) * s;
        vect.y = data.getDoubleAttribute( "ey" ) * s;

        period = data.getIntAttribute( "period", 0 );

        moveHotspotRelAt( 0, vect ); 
    }
 
    public void writeData( DataElement data )
    {
        DataElement fdata = data.newElement( TYPE );

        Point2d vect = getHotspotRelAt( 0 );

        double s = getProperties().eforceScale.getValue();

        fdata.setAttribute( "ex", vect.x / s );
        fdata.setAttribute( "ey", vect.y / s );
        if ( period > 0 ) fdata.setAttribute( "period", period );
    }
}
