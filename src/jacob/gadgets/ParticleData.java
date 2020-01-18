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

import jacob.scene.*;

public class ParticleData
{
    public double x, y;

    public double ex, ey;

    public double mx, my;

    public double vx, vy;

    public double charge;

    public boolean fixed;

    public boolean onBorder;

    public boolean dipol;

    public ParticleData( Particle p )
    {
        x = p.getLocation().x;
        y = p.getLocation().y;
        ex = p.getEx();
        ey = p.getEy();
        mx = p.getMx();
        my = p.getMy();
        vx = p.getVx();
        vy = p.getVy();
        charge   = p.getCharge();
        fixed    = p.isFixed();
        onBorder = p.isOnBorder();
        dipol    = p.isDipol();
    }
}
