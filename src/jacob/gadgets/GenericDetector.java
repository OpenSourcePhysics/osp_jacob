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

public class GenericDetector extends Detector
{
    public static final String TYPE = "genericdetector";

    private double x, y, oldx, oldy,  oldx0, oldy0;
    private double ex, ey, efx, efy, exfx, exfy, exforce, field, work, workm;
	private double vpx, vpy, vpfield;
    private Vector vdata = new Vector();
    private Vector vdataold = new Vector();

    private class VData
    {
        double x, y, ex, ey, dx, dy, vpx, vpy;

        public VData(double x,  double y,
                     double ex, double ey,
                     double dx, double dy,
                     double vpx, double vpy)
        {
            this.x = x;
            this.y = y;
            this.ex = ex;
            this.ey = ey;
            this.dx = dx;
            this.dy = dy;
            this.vpx = vpx;
            this.vpy = vpy;
        }
    }

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

    protected void startDetector(double x, double y)
    {
        oldx = x;
        oldy = y;
        work = 0;
        workm = 0;
        oldx0 =0;
        oldy0 =0;
        vdataold.removeAllElements();

    }

    protected void stopDetector(double x, double y,  SceneGraphics g )
    {
/*
            int asize = getProperties().arrowHeadSize.getValue();
            double mascale = getProperties().mvectorScale.getValue() *
                            getProperties().magneticConst.getValue()*1000;
            double eascale = getProperties().evectorScale.getValue() *
                            getProperties().electricConst.getValue();

            for (int i = 0; i < vdata.size(); i++) //FIXME VV
            {

             VData vd = (VData)vdata.elementAt(i);
		     vdataold.addElement(new VData(vd.x, vd.y, vd.ex, vd.ey, vd.dx, vd.dy, vd.vpx, vd.vpy));

            }

            for (int i = 0; i < vdataold.size(); i++) //FIXME VV
            {

                VData vd = (VData)vdataold.elementAt(i);
                g.setColor(Color.blue);
                g.drawArrow(vd.x, vd.y, vd.x + vd.vpx * mascale,
                            vd.y + vd.vpy * mascale, asize);
                g.setColor(Color.blue);
                g.drawArrow(vd.x, vd.y, vd.x + vd.ex * eascale,
                            vd.y + vd.ey * eascale, asize);

     		}
*/
         vdata.removeAllElements();

    }

    protected void updateIndicator(double x, double y)
    {
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

// Vector Potential Detection

        vpfield = 0;
        vpx = vpy = 0;
        for ( int i = 0; i < pdata.size(); i++ )
        {
            ParticleData p = (ParticleData) pdata.elementAt( i );

            double dx = x - p.x;
            double dy = y - p.y;
            double r = Math.sqrt(dx * dx + dy * dy);
            double vp = Math.sqrt(p.vx * p.vx + p.vy * p.vy);

            vpx +=  vp * p.vx / r;
            vpy +=  vp * p.vy / r;

        }

        field = Math.sqrt( efx * efx + efy * efy );

// External Force Detection

        double dx, dy, r0;

        dx = x - oldx;
        dy = y - oldy;

        r0 = Math.sqrt((x - oldx0)  *  (x - oldx0) + (y - oldy0) * (y - oldy0));

        oldx = x;
        oldy = y;

        Vector edata = getElementsData(false, x, y);

        exfx = exfy = 0;
        exforce = 0;

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

// Work Detection

		work += ex * dx + ey * dy;


     if (r0 > 12) //FIXME
      {
		vdata.addElement(new VData(x, y, ex, ey, dx, dy, vpx, vpy));
		oldx0 = x;
		oldy0 = y;
      }

// Inductance Detection

		workm += vpx * dx + vpy * dy;


     if (r0 > 12) //FIXME
      {
		vdata.addElement(new VData(x, y, ex, ey, dx, dy, vpx, vpy));
		oldx0 = x;
		oldy0 = y;
      }

// Indicator Set-up

        setIndicatorValue( workm );

        String svalue = Double.toString( workm *
            getProperties().genericDetectorScale.getValue() );
        int dnum = svalue.indexOf( '.' ) + 2;
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
            double mascale = getProperties().mvectorScale.getValue() *
                            getProperties().magneticConst.getValue()*1000;
            double eascale = getProperties().evectorScale.getValue() *
                            getProperties().electricConst.getValue();
            g.setColor(getProperties().fieldColor.getValue()); //FIXME
            for (int i = 0; i < vdata.size(); i++)
            {
                VData vd = (VData)vdata.elementAt(i);
                g.setColor(getProperties().fieldColor.getValue()); //FIXME
                g.drawArrow(vd.x, vd.y, vd.x + vd.vpx * mascale,
                            vd.y + vd.vpy * mascale, asize);
                g.setColor(Color.blue); //FIXME
                g.drawArrow(vd.x, vd.y, vd.x + vd.ex * eascale,
                            vd.y + vd.ey * eascale, asize);

            }
        }
    }

}
