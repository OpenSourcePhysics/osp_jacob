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

public class DepxGauge extends ComponentGauge
{

    public static final String TYPE = "depxgauge";

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

    protected boolean allowNoComponent()
    {
        return false;
    }

    protected void updateIndicator( SceneComponent comp )
    {
        Vector pdata = getParticlesData( (Element)comp );
        double fx = 0;
        double fy = 0;
        double f  = 0;

        for ( int i = 0; i < pdata.size(); i++ )
        {
            ParticleData p = (ParticleData)pdata.elementAt( i );
 //           if (p.charge  > 0.0)
          fx += p.ex;
          fy += p.ey;

        }
        f = Math.sqrt(fx * fx + fy * fy);

        setIndicatorValue( f );

        String svalue = Double.toString( f *
            getProperties().depxGaugeScale.getValue() );
        int dnum = svalue.indexOf( '.' ) + 4;
        if ( dnum > svalue.length() )
            dnum = svalue.length();
        setIndicatorLabel( "'" + ((Element)comp).getName() + "' " +
                               svalue.substring( 0, dnum ) + " nN/m" );
    }
}
