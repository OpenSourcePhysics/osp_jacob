//==============================================================================
//
//  Copyright (C) 2002  Vojko Valencic <Vojko.Valencic@fe.uni-lj.si>
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

public class EnergyGauge extends ComponentGauge
{

    public static final String TYPE = "energygauge";

//------------------------------------------------------------------------------
//    Type Methods
//------------------------------------------------------------------------------

    public String getType()
    {
        return TYPE;
    }

//------------------------------------------------------------------------------
//    Gauge Methods
//------------------------------------------------------------------------------

    protected String getComponentType()
    {
        return "element";
    }

    protected void updateIndicator( SceneComponent c )
    {
        Vector pdata;
		double e = 0;

        if ( c != null )
            pdata = getParticlesData( ((Element)c) );
        else
            pdata = getParticlesData();

        double zx = getProperties().zeroX.getValue();
        double zy = getProperties().zeroY.getValue();

// System Energy


        for ( int i = 0; i < pdata.size(); i++ )
        {
            ParticleData pi = (ParticleData) pdata.elementAt( i );

            double r0 = (pi.x - zx) * (pi.x - zx) +
                        (pi.y - zy) * (pi.y - zy);
            double Vi = 0;

            for ( int j = 0; j < pdata.size(); j++ )
            {
                ParticleData pj = (ParticleData) pdata.elementAt( j );

                if ( pi != pj )
                {
                    double rij = (pi.x - pj.x) * (pi.x - pj.x) +
                                 (pi.y - pj.y) * (pi.y - pj.y);

                    Vi  +=  pj.charge * Math.log(Math.sqrt(rij ));

                }
            }
            e += pi.charge * Vi;
	    }

// Indicator Set-up
        e = -e; // FIXME
        setIndicatorValue( e );

        String svalue = Double.toString( e *
            getProperties().energyGaugeScale.getValue() );
        int dnum = svalue.indexOf( '.' ) + 2;
        if ( dnum > svalue.length() )
            dnum = svalue.length();
        if ( c != null )
            setIndicatorLabel( "'" + ((Element)c).getName() + "' " +
                               svalue.substring( 0, dnum ) + " pJ/m");
        else
            setIndicatorLabel( svalue.substring( 0, dnum ) + " pJ/m" );
    }
}
