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

public class CurrentGauge extends ComponentGauge
{

    public static final String TYPE = "currentgauge";

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
        double current = 0;


        for ( int i = 0; i < pdata.size(); i++ )
        {
            ParticleData p = (ParticleData)pdata.elementAt( i );
          if (p.charge > 0) //FIXME VV Works with Current Integrator Only

            current += Math.sqrt( p.vx * p.vx + p.vy * p.vy )*p.charge;

        }

        if (((Element)comp).getName() == "R1")
           current = current/3.14;

        setIndicatorValue( current );

        String svalue = Double.toString( current *
            getProperties().currentGaugeScale.getValue() );
        int dnum = svalue.indexOf( '.' ) + 2;
        if ( dnum > svalue.length() )
            dnum = svalue.length();
        if ( comp != null )
            setIndicatorLabel( "'"+((Element)comp).getName() + "'  " +
                               svalue.substring( 0, dnum ) + " A" );
        else
            setIndicatorLabel( svalue.substring( 0, dnum ) + " A" );
    }
}
