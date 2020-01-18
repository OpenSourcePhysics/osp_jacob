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

public class FieldPlotter extends Gauge
{
    public static final String TYPE = "fieldplotter";

    private double cx, cy, coldx, coldy, sw, sh, sg;
    private int    vnum;
    private double ex, ey, efx, efy, exfx, exfy, exforce, field, work, workm;
	private double vpx, vpy, vpfield;
    private Vector vdata = new Vector();

    private class VData
    {
        double x, y, ex, ey, dx, dy, vpx, vpy;

        public VData(double x,  double y,
                     double ex, double ey,
                     double dx, double dy,
                     double vpx, double vpy)
        {
            set(x, y, ex, ey, dx, dy, vpx, vpy);
        }

        public void set(double x,  double y,
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

    public void startGauge()
    {
        super.startGauge();
        cx = cy = coldx = coldy = 0;
        work = 0;
        workm = 0;
        vnum = 0;
        sw = sh = sg = 0;
        vdata.removeAllElements();
    }

    public void stopGauge()
    {
        super.stopGauge();
        vdata.removeAllElements();
    }

    protected void updateIndicator()
    {
        double x, y, dx, dy;
        Vector pdata = getParticlesData();

        double nsw = getScene().getSize().width /
                        getProperties().sceneScale.getValue();
        double nsh = getScene().getSize().height /
                        getProperties().sceneScale.getValue();
        double nsg = getProperties().gridSize.getValue();
        if (sw != nsw || sh != nsh || sg != nsg)
        {
            sw = nsw;
            sh = nsh;
            sg = nsg;
            cx = cy = coldx = coldy = sg;
            vnum = 0;
            vdata.removeAllElements();
        }

        x = cx;
        y = cy;
        dx = coldx - cx;
        dy = coldy - cy;
// Field Detection

        field = 0;
        efx = efy = 0;
        for ( int i = 0; i < pdata.size(); i++ )
        {
            ParticleData p = (ParticleData) pdata.elementAt( i );

            double pdx = x - p.x;
            double pdy = y - p.y;
            double pr2 = pdx * pdx + pdy * pdy;

            efx += p.charge * (pdx / pr2);
            efy += p.charge * (pdy / pr2);

        }

        field = Math.sqrt( efx * efx + efy * efy );

// Vector Potential Detection

        vpfield = 0;
        vpx = vpy = 0;
	boolean toClose = false;
        for ( int i = 0; i < pdata.size(); i++ )
        {
            ParticleData p = (ParticleData) pdata.elementAt( i );

            double pdx = x - p.x;
            double pdy = y - p.y;
            double pr = Math.sqrt(pdx * pdx + pdy * pdy);
            double vp = Math.sqrt(p.vx * p.vx + p.vy * p.vy);

            vpx +=  vp * p.vx / pr;
            vpy +=  vp * p.vy / pr;

	    if (pr < 10) toClose = true;
        }

        field = Math.sqrt( efx * efx + efy * efy );

// External Force Detection

        Vector edata = getElementsData(false, x, y);

        exfx = exfy = 0;
        exforce = 0;

        for (int i = 0; i < edata.size(); i++)
        {
            ElementData ed = (ElementData)edata.elementAt(i);
            exfx += ed.efx;
            exfy += ed.efy;
        }

        ex =  (efx + exfx*10);//FIXME
		ey =  (efy + exfy*10);

        exforce = Math.sqrt(ex * ex + ey * ey);

// Work Detection

		work += ex * dx + ey * dy;

// Inductance Detection

		workm += vpx * dx + vpy * dy;

// Indicator Set-up

	if (toClose)
	{
		ex = ey = vpx = vpy = 0;
	}

        if (vnum < vdata.size())
        {
            VData vd = (VData)vdata.elementAt(vnum);
            vd.set(x, y, ex, ey, dx, dy, vpx, vpy);
        }
        else
        {
            vdata.addElement(new VData(x, y, ex, ey, dx, dy, vpx, vpy));
        }
        vnum++;
/*
        setIndicatorValue( workm );

        String svalue = Double.toString( workm *
            getProperties().genericDetectorScale.getValue() );
        int dnum = svalue.indexOf( '.' ) + 2;
        if ( dnum > svalue.length() )
            dnum = svalue.length();

        setIndicatorLabel( svalue.substring( 0, dnum ) + " V" );
*/

        coldx = cx;
        coldy = cy;

        cx += sg;

        if (cx >= sw)
        {
            cx = sg;
            cy += sg;

            if (cy >= sh)
            {
                cy = sg;
                vnum = 0;
            }
        }
    }

//------------------------------------------------------------------------------
//    Scene Listener Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
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
            g.setColor(getProperties().fieldColor.getValue());
            g.drawArrow(vd.x, vd.y, vd.x + vd.ex * eascale,
                        vd.y + vd.ey * eascale, asize);
        }
    }

}
