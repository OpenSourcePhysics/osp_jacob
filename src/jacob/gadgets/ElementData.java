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

import jacob.scene.*;
import jacob.scene.modifiers.*;

public class ElementData
{
    public double x, y, pax, pay, efx, efy;
    public Vector pdata = null;

    public ElementData(Element e)
    {
        this(e, false);
    } 

    public ElementData(Element e, boolean getpd)
    {
        this(e, getpd, false, 0, 0);
    }

    public ElementData(Element e, boolean getpd, double px, double py)
    {
        this(e, getpd, true, px, py);
    }

    public ElementData(Element e, boolean getpd, boolean getef, double px, double py)
    {
		int pn = 0;

        x = e.getLocation().x;
        y = e.getLocation().y;

       	pdata = new Vector();
       	pax = pay = 0;
	    for ( int i = 0; i < e.countChildren(); i++ )
		{
		    SceneComponent child = e.getChildAt( i );
		    if ( child instanceof Particle )
		    {

   		        if (getpd)
		            pdata.addElement( new ParticleData( (Particle)child ) );

				pax += ((Particle)child).getLocation().x;
				pay += ((Particle)child).getLocation().y;
				pn ++;
			}
	    }
	    if (pn > 0)
	    {
	    	pax /= pn;
	    	pay /= pn;
		}

        efx = efy = 0;
        if (getef && e.hasModifiers())
        {
            Modifiers m = e.getModifiers();
            for (int i = 0; i < m.countModifiers(); i++)
            {
                ComponentModifier cm = m.getModifierAt(i);

                if (cm instanceof EFModifier)
                {
                    double ef[] = ((EFModifier)cm).getEAt(px, py);
                    efx += ef[0];
                    efy += ef[1];
                }
            }
        }
    }
}
