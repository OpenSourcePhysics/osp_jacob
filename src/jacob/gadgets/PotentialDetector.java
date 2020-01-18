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

public class PotentialDetector extends Detector
{

    public static final String TYPE = "potentialdetector";

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

    protected void updateIndicator( double x, double y  )
    {
        Vector edata = getElementsData(false);
        Vector pdata = getParticlesData();

        this.x = x;
        this.y = y;

        double zx = getProperties().zeroX.getValue();
        double zy = getProperties().zeroY.getValue();

        double rzero = Math.sqrt( (x - zx) * (x - zx) +
                                  (y - zy) * (y - zy) );
        double pot = 0;
        ex = ey = 0;

        for ( int i = 0; i < pdata.size(); i++ )
        {
            ParticleData p = (ParticleData) pdata.elementAt( i );

            double dx = x - p.x;
            double dy = y - p.y;
            double r2 = dx * dx + dy * dy;

            ex += p.charge * (dx / r2);
            ey += p.charge * (dy / r2);

            if ( rzero > 0 )
            {
			 if (edata.size() == 1)
			  {
                pot += p.charge * Math.log( Math.sqrt( r2 ) / rzero );
		      }
             else
                pot += p.charge * Math.log( Math.sqrt( r2 ));
			}
        }
        setIndicatorValue( -pot );

        String svalue = Double.toString( -pot *
            getProperties().potentialDetectorScale.getValue() );
        int  dnum = svalue.indexOf( '.' ) + 2;
        if ( dnum > svalue.length() )
             dnum = svalue.length();

            setIndicatorLabel( svalue.substring( 0, dnum ) + " V" );
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
            g.setColor(getProperties().fieldColor.getValue()); //FIXME
            g.drawArrow( x, y, x + ex * ascale, y + ey * ascale, asize );
        }
    }
}
