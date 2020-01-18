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

package jacob.gadgets;

import java.util.Vector;
import java.awt.Color;
import java.awt.Graphics;

import jacob.main.*;
import jacob.scene.*;
import jacob.geometry.*;

public class ResistanceDetector extends Detector
{

    public static final String TYPE = "resistancedetector";

    private double x, y, ex, ey;

//------------------------------------------------------------------------------
//    Type Methods
//------------------------------------------------------------------------------

    public String getType()
    {
        return TYPE;
    }

//------------------------------------------------------------------------------
//    Detector Methods
//------------------------------------------------------------------------------

    protected void updateIndicator( double x, double y)
    {
        Vector pdataall = getParticlesData();
 //     Vector pdataelm = getParticlesData( (Element)comp );

        this.x = x;
        this.y = y;

        double zx = getProperties().zeroX.getValue();
        double zy = getProperties().zeroY.getValue();

        double rzero = Math.sqrt( (x - zx) * (x - zx) +
                                  (y - zy) * (y - zy) );
        double pot = 0;
        ex = ey = 0;
        double current = 1;

        for ( int i = 0; i < pdataall.size(); i++ )
        {
            ParticleData p = (ParticleData) pdataall.elementAt( i );

            double dx = x - p.x;
            double dy = y - p.y;
            double r2 = dx * dx + dy * dy;

            ex += p.charge * (dx / r2);
            ey += p.charge * (dy / r2);

            if ( rzero > 0 )
                pot += p.charge * Math.log( Math.sqrt( r2 ) / rzero );
        }

/*

		for ( int i = 0; i < pdataelm.size(); i++ )
		{
		            ParticleData p = (ParticleData)pdataelm.elementAt( i );
		            if (p.charge  > 0.0)
		            current += Math.sqrt( p.vx * p.vx + p.vy * p.vy )*p.charge;
		}

*/
        pot = Math.abs(pot);
        setIndicatorValue( pot );
        String svalue = Double.toString( pot *
            getProperties().resistanceDetectorScale.getValue() /
                                      (current *
            getProperties().currentGaugeScale.getValue()));
        int dnum = svalue.indexOf( '.' ) + 2;
        if ( dnum > svalue.length() )
            dnum = svalue.length();
        setIndicatorLabel( svalue.substring( 0, dnum ) + " Ohm");
    }

//------------------------------------------------------------------------------
//    Scene Listener Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        if ( isActive() )
        {
            int asize = getProperties().arrowHeadSize.getValue();
            double ascale = getProperties().evectorScale.getValue() *
                            getProperties().electricConst.getValue();
            g.setColor( Color.black ); //FIXME
            g.drawArrow( x, y, x + ex * ascale, y + ey * ascale, asize );
        }
    }
}
