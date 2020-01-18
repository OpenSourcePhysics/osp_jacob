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

package jacob.integrator;

import jacob.geometry.*;
import jacob.scene.*;

public class TestIntegrator extends ModIntegrator
{
    private double integrationStep;

    private double electricConst;

    private double magneticConst;

    private double dipolCloudSize;

    private double dipolCloudSize2;

    private double dipolChargeDiv;

    private double dipolSuscept;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public TestIntegrator() {}

//------------------------------------------------------------------------------
//    Integration Methods
//------------------------------------------------------------------------------

//FIXME
    public void integrate( Scene scene )
    {
        electricConst   = scene.getProperties().electricConst.getValue();
        magneticConst   = scene.getProperties().magneticConst.getValue();
        integrationStep = scene.getProperties().integrationStep.getValue();
        dipolCloudSize  = scene.getProperties().dipolCloudSize.getValue();
        dipolChargeDiv  = scene.getProperties().dipolChargeDiv.getValue();
        dipolSuscept    = scene.getProperties().dipolSuscept.getValue();

        dipolCloudSize2 = dipolCloudSize * dipolCloudSize;

        super.integrate( scene );
    }

    protected void integrate0( Scene scene )
    {
        ParticleIterator i1, i2;

        i1 = scene.createParticleIterator();
        while ( !i1.isDone() )
        {
            Particle p1 = i1.getCurrentParticle();

            i2 = scene.createParticleIterator();
            while( !i2.isDone() )
            {
                Particle p2 = i2.getCurrentParticle();

                if ( p1 != p2 ) integrate1( p1, p2 );
                i2.next();
            }
            integrate2( p1 );
            i1.next();
        }
    }

    protected void integrate1( Particle p1, Particle p2 )
    {
        Point2d l1 = p1.getLocation();
        Point2d l2 = p2.getLocation();
        double q1 = p1.getCharge();
        double q2 = p2.getCharge();

        double dx = l1.x - l2.x;
        double dy = l1.y - l2.y;

        double r2 = dx * dx + dy * dy;

        if ( r2 < dipolCloudSize2 )
        {
//FIXME
            if ( r2 == 0 )
            {
                dx = 2 - Math.random() * 4;
                dy = 2 - Math.random() * 4;
                r2 = dx * dx + dy * dy;
            }

            if ( q1 == -q2     &&
                 !p1.isDipol() &&
                 !p2.isDipol() &&
                 p1.getParent() == p2.getParent() )
            {
                p1.setDipolPartner( p2 );
                p2.setDipolPartner( p1 );
            }
        }
        else if ( p1.getDipolPartner() == p2 )
        {
            p1.unsetDipolPartner();
            p2.unsetDipolPartner();
        }

        if ( p2.isDipol() )
        {
            q2 /= dipolChargeDiv;
            if ( p1.getDipolPartner() == p2 )
                q2 *= dipolSuscept * r2;
        }

        p1.setEx( p1.getEx() + q2 * dx / r2 );
        p1.setEy( p1.getEy() + q2 * dy / r2 );

        if ( magneticConst > 0 )
        {
            double rl = Math.sqrt( r2 );

            double vx1 = p1.getVx();
            double vy1 = p1.getVy();
            double vx2 = p2.getVx();
            double vy2 = p2.getVy();
//FIXED VV 5.3.2002
            p1.setMx( p1.getMx() +
                      q2 * ( -vy1 * vy2 * dx + vx2 * vy1 * dy ) / rl );
            p1.setMy( p1.getMy() +
                      q2 * (  vx1 * vy2 * dx - vx1 * vx2 * dy ) / rl );
        }
    }

    protected void integrate2( Particle p )
    {
        double q = p.getCharge();

        if ( p.isDipol() )
        {
            q /= dipolChargeDiv;
            p.setVx( 0 );
            p.setVy( 0 );
        }

        p.setEx( p.getEx() * q * electricConst );
        p.setEy( p.getEy() * q * electricConst );

        p.setMx( p.getMx() * q * magneticConst );
        p.setMy( p.getMy() * q * magneticConst );
    }

//------------------------------------------------------------------------------
//    Particle Pre and Post Integration Methods
//------------------------------------------------------------------------------

    protected void particlePreIntegrate( Particle p )
    {
        p.setEx( 0 );
        p.setEy( 0 );
        p.setMx( 0 );
        p.setMy( 0 );
    }

    protected void particlePostIntegrate( Particle p )
    {
        if ( !p.isFixed() )
        {
            Point2d loc = p.getLocation();

            double vx = p.getVx();
            double vy = p.getVy();
            double ex = p.getEx();
            double ey = p.getEy();
            double mx = p.getMx();
            double my = p.getMy();



            //vx += (mx + ex) * integrationStep;
            //vy += (my + ey) * integrationStep;
//FIXED VV 10.3.2002
            vx = mx + ex;
            vy = my + ey;
//FIX ME
            vx = vx * 6.0;
            vy = vy * 6.0;

            loc.x += vx * integrationStep;
            loc.y += vy * integrationStep;


            p.setVx( vx );
            p.setVy( vy );
            p.move( loc );
        }
    }
}
