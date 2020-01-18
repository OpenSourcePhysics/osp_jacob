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

public class GenericGauge extends ComponentGauge
{

    public static final String TYPE = "genericgauge";

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
        Vector edata = getElementsData(false);
        Vector pdata = getParticlesData();

        double zx = getProperties().zeroX.getValue();
        double zy = getProperties().zeroY.getValue();


		if (edata.size() < 2)
			return;


// System Energy

		double es = 0;
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

                  Vi  += pj.charge * Math.log(Math.sqrt( rij ));// /r0 );

                }
            }
                   es  += pi.charge * Vi;

	    }


// Capacitane Calculation

		double cap = 0;

		cap = (pdata.size() * pdata.size()) / (8*Math.abs(es));


// Potential Calculation

        Vector pots = new Vector();

		for ( int i = 0; i < edata.size(); i++)
		{
			ElementData e = (ElementData) edata.elementAt( i );

			double rzero = Math.sqrt( (e.pax - zx) * (e.pax - zx) +
			                          (e.pay - zy) * (e.pay - zy) );
			double pot = 0;
            for ( int j = 0; j < pdata.size(); j++ )
            {
                ParticleData p = (ParticleData) pdata.elementAt( j );

                double dx = e.pax - p.x;
                double dy = e.pay - p.y;
                double r2 = dx * dx + dy * dy;

                if ( rzero > 0 )
                    pot += p.charge * Math.log( Math.sqrt( r2 ) ); // / rzero );
		    }
		    pots.addElement(new Double(pot));
		}

// Voltage Calculation

		Vector u = new Vector();
		for ( int i = 0; i < pots.size(); i++)
		{
			double poti = ((Double)pots.elementAt(i)).doubleValue();
			for ( int j = i+1; j < pots.size(); j++)
			{
				double potj = ((Double)pots.elementAt(j)).doubleValue();

				u.addElement(new Double(Math.abs(poti - potj)));
			}
		}

// Capacitane Calculation

//		double cap = 0;
		for ( int i = 0; i < u.size(); i++)
		{
			double uij = ((Double)u.elementAt(i)).doubleValue();
			cap += 2*Math.abs(es)/(uij*uij);
		}

//		cap = (pdata.size() * pdata.size()) / (8*Math.abs(es));

// Indicator Set-up

        setIndicatorValue( cap );

        double capmin = 10000.0;
        double capmax = 0;
        if (capmin >= cap)
           capmin = cap;
        if (capmax <= cap)
           capmax = cap;

        String svalue = Double.toString( capmin *
            getProperties().genericGaugeScale.getValue() );
        int dnummin = svalue.indexOf( '.' ) + 2;
        if ( dnummin > svalue.length() )
            dnummin = svalue.length();
        if ( c != null )
            setIndicatorLabel( "'" + ((Element)c).getName() + "' " +
                               svalue.substring( 0, dnummin ) + " unit/m");
        else
            setIndicatorLabel( svalue.substring( 0, dnummin ) + " unit/m" );
    }
}
