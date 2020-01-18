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

public class SimpleIntegrator extends ModIntegrator
{
    private double integrationStep;

    private double electricConst;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SimpleIntegrator() {} 

//------------------------------------------------------------------------------
//    Integration Methods
//------------------------------------------------------------------------------

//FIXME
    public void integrate( Scene scene )
    {
        electricConst   = scene.getProperties().electricConst.getValue();
        integrationStep = scene.getProperties().integrationStep.getValue();

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

        if ( r2 == 0 )
        {
            dx = 2 - Math.random() * 4;
            dy = 2 - Math.random() * 4;
            r2 = dx * dx + dy * dy;
        }
//------------------------------------------------------------------------------
//    Particle p1 force set
//------------------------------------------------------------------------------                

        p1.setEx( p1.getEx() + q2 * dx / r2 );
        p1.setEy( p1.getEy() + q2 * dy / r2 );
    }

    protected void integrate2( Particle p )
    {
        double q = p.getCharge();

        p.setEx( p.getEx() * q * electricConst );
        p.setEy( p.getEy() * q * electricConst );
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

            loc.x += (vx + ex/2 * integrationStep) * integrationStep;
            loc.y += (vy + ey/2 * integrationStep) * integrationStep;

            vx += ex * integrationStep;
            vy += ey * integrationStep;

            p.setVx( vx );
            p.setVy( vy );
            p.move( loc );
        }
    }  
}
