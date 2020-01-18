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

public class EFDetectorOriginal extends Detector
{

    public static final String TYPE = "efdetectororiginal";

    private double x, y, ex, ey, efx, efy, exfx, exfy, exforce, field;

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

     protected void updateIndicator(double x, double y)
    {


        Vector edata = getElementsData(false, x, y);
        Vector pdata = getParticlesData();

// Field Detection

        field = 0;
        efx = efy = 0;
        for ( int i = 0; i < pdata.size(); i++ )
        {
            ParticleData p = (ParticleData) pdata.elementAt( i );

            double dx = x - p.x;
            double dy = y - p.y;
            double r2 = dx * dx + dy * dy;

            efx += p.charge * (dx / r2);
            efy += p.charge * (dy / r2);

        }

        field = Math.sqrt( efx * efx + efy * efy );

// External Force Detection

        exforce = 0;
        exfx = exfy = 0;
        for (int i = 0; i < edata.size(); i++)
        {
            ElementData ed = (ElementData)edata.elementAt(i);
            exfx += ed.efx;
            exfy += ed.efy;
        }

        this.x = x;
        this.y = y;

        ex =  (efx + exfx*10);//FIXME
        ey =  (efy + exfy*10);

        exforce = Math.sqrt(ex * ex + ey * ey);


// Indicator Set-up

        setIndicatorValue( exforce );

        String svalue = Double.toString( exforce *
            getProperties().genericGaugeScale.getValue() );
        int dnum = svalue.indexOf( '.' ) + 2;
        if ( dnum > svalue.length() )
            dnum = svalue.length();

            setIndicatorLabel( svalue.substring( 0, dnum ) + " V/m" );

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
