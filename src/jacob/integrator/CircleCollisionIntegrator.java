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

public class CircleCollisionIntegrator extends ModIntegrator
{
    private double integrationStep;

    private double electricConst;

    private double magneticConst;

    private double frictionConst;

    private double collisionConst;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public CircleCollisionIntegrator() {}

//------------------------------------------------------------------------------
//    Integration Methods
//------------------------------------------------------------------------------

//FIXME
    public void integrate( Scene scene )
    {
        electricConst   = scene.getProperties().electricConst.getValue();
        magneticConst   = scene.getProperties().magneticConst.getValue();
        integrationStep = scene.getProperties().integrationStep.getValue();
        frictionConst   = scene.getProperties().frictionConst.getValue();
        collisionConst  = scene.getProperties().collisionConst.getValue();

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

		//by Dragan Simeonov 09.11.02
        ElementIterator ei1, ei2;
        ei1 = scene.createElementIterator();
        while ( !ei1.isDone() )
        {
            Element e1 = ei1.getCurrentElement();

            ei2 = scene.createElementIterator();
            while( !ei2.isDone() )
            {
                Element e2 = ei2.getCurrentElement();

                if ( e1 != e2 ) integratee( e1, e2 );
                ei2.next();
            }
            ei1.next();
        }
    }

	//by Dragan Simeonov 09.11.02
    protected void integratee( Element e1, Element e2 )
    {
		if (!((e1.getShape() instanceof Circle2d) && (e2.getShape() instanceof Circle2d)))
			return;

		double e1x = e1.getLocation().x;
		double e1y = e1.getLocation().y;
		double e2x = e2.getLocation().x;
		double e2y = e2.getLocation().y;
		double dx = e1x-e2x;
		double dy = e1y-e2y;
		double r1 = ((Circle2d) e1.getShape()).getRadius()*collisionConst;
		double r2 = ((Circle2d) e2.getShape()).getRadius()*collisionConst;
		double d = Math.sqrt(dx*dx+dy*dy);
		double dr = (r1+r2)-d;

		if (dr < 0)
			return;

		double ddxH = Math.sqrt((r1+r2)*(r1+r2)-dy*dy)*dx/Math.abs(dx)-dx;
		dx /= d;
		dy /= d;

		if (e1.getModifiers()!=null)
		{
			if (e1.getModifiers().getModifier("translator")!=null)
			{
				if (e2.getModifiers()!=null)
				{
					if (e2.getModifiers().getModifier("translator")!=null)
					{
						e1.move(new Point2d(e1x + dr*dx/2, e1y + dr*dy/2));
						e2.move(new Point2d(e2x - dr*dx/2, e2y - dr*dy/2));
					}
					else if (e2.getModifiers().getModifier("translatorH")!=null)
					{
						e1.move(new Point2d(e1x + dr*dx/2, e1y + dr*dy));
						e2.move(new Point2d(e2x - dr*dx/2, e2y));
					}
					else
						e1.move(new Point2d(e1x + dr*dx, e1y + dr*dy));
				}
				else
					e1.move(new Point2d(e1x + dr*dx, e1y + dr*dy));
			}
			else if (e1.getModifiers().getModifier("translatorH")!=null)
			{
				if (e2.getModifiers()!=null)
				{
					if (e2.getModifiers().getModifier("translator")!=null)
					{
						e1.move(new Point2d(e1x + dr*dx/2, e1y));
						e2.move(new Point2d(e2x - dr*dx/2, e2y - dr*dy));
					}
					else if (e2.getModifiers().getModifier("translatorH")!=null)
					{
						e1.move(new Point2d(e1x + ddxH/2, e1y));
						e2.move(new Point2d(e2x - ddxH/2, e2y));
					}
					else
						e1.move(new Point2d(e1x + ddxH, e1y));
				}
				else
					e1.move(new Point2d(e1x + ddxH, e1y));
			}
			else
			{
				if (e2.getModifiers()!=null)
				{
					if (e2.getModifiers().getModifier("translator")!=null)
						e2.move(new Point2d(e2x - dr*dx, e2y - dr*dy));
					else if (e2.getModifiers().getModifier("translatorH")!=null)
						e2.move(new Point2d(e2x - ddxH, e2y));
				}
			}

		}
		else
		{
			if (e2.getModifiers()!=null)
			{
				if (e2.getModifiers().getModifier("translator")!=null)
					e2.move(new Point2d(e2x - dr*dx, e2y - dr*dy));
				else if (e2.getModifiers().getModifier("translatorH")!=null)
					e2.move(new Point2d(e2x - ddxH, e2y));
			}
		}
		integratee2(e1,e2);
	}

    protected void integratee2( Element e10, Element e20 )
    {

		Element e1 = e10;
		do
		{
			Element e2 = e20;
			do
			{
				double e1x = e10.getLocation().x;
				double e1y = e10.getLocation().y;
				double e2x = e20.getLocation().x;
				double e2y = e20.getLocation().y;
				double dx = e1x-e2x;
				double dy = e1y-e2y;
				double r1 = ((Circle2d) e10.getShape()).getRadius()*collisionConst;
				double r2 = ((Circle2d) e20.getShape()).getRadius()*collisionConst;
				double d = Math.sqrt(dx*dx+dy*dy);
				double dr = (r1+r2)-d;

				if (dr < 0)
				{
					e1 = null;
					e2 = null;
					return;
				}

				e1x = e1.getLocation().x;
				e1y = e1.getLocation().y;
				e2x = e2.getLocation().x;
				e2y = e2.getLocation().y;

				double ddxH = Math.sqrt((r1+r2)*(r1+r2)-dy*dy)*dx/Math.abs(dx)-dx;
				dx /= d;
				dy /= d;

				if (e1.getModifiers()!=null)
				{
					if (e1.getModifiers().getModifier("translator")!=null)
					{
						if (e2.getModifiers()!=null)
						{
							if (e2.getModifiers().getModifier("translator")!=null)
							{
								e1.move(new Point2d(e1x + dr*dx/2, e1y + dr*dy/2));
								e2.move(new Point2d(e2x - dr*dx/2, e2y - dr*dy/2));
							}
							else if (e2.getModifiers().getModifier("translatorH")!=null)
							{
								e1.move(new Point2d(e1x + dr*dx/2, e1y + dr*dy));
								e2.move(new Point2d(e2x - dr*dx/2, e2y));
							}
							else
								e1.move(new Point2d(e1x + dr*dx, e1y + dr*dy));
						}
						else
							e1.move(new Point2d(e1x + dr*dx, e1y + dr*dy));
					}
					else if (e1.getModifiers().getModifier("translatorH")!=null)
					{
						if (e2.getModifiers()!=null)
						{
							if (e2.getModifiers().getModifier("translator")!=null)
							{
								e1.move(new Point2d(e1x + dr*dx/2, e1y));
								e2.move(new Point2d(e2x - dr*dx/2, e2y - dr*dy));
							}
							else if (e2.getModifiers().getModifier("translatorH")!=null)
							{
								e1.move(new Point2d(e1x + ddxH/2, e1y));
								e2.move(new Point2d(e2x - ddxH/2, e2y));
							}
							else
								e1.move(new Point2d(e1x + ddxH, e1y));
						}
						else
							e1.move(new Point2d(e1x + ddxH, e1y));
					}
					else
					{
						if (e2.getModifiers()!=null)
						{
							if (e2.getModifiers().getModifier("translator")!=null)
								e2.move(new Point2d(e2x - dr*dx, e2y - dr*dy));
							else if (e2.getModifiers().getModifier("translatorH")!=null)
								e2.move(new Point2d(e2x - ddxH, e2y));
						}
					}

				}
				else
				{
					if (e2.getModifiers()!=null)
					{
						if (e2.getModifiers().getModifier("translator")!=null)
							e2.move(new Point2d(e2x - dr*dx, e2y - dr*dy));
						else if (e2.getModifiers().getModifier("translatorH")!=null)
							e2.move(new Point2d(e2x - ddxH, e2y));
					}
				}
				if (e2.getParent() instanceof Element)
					e2 = (Element) e2.getParent();
				else
					e2 = null;
			}
			while (e2!=null);
			if (e1.getParent() instanceof Element)
				e1 = (Element) e1.getParent();
			else
				e1 = null;
		}
		while (e1!=null);
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

        p1.setEx( p1.getEx() + q2 * dx / r2 );
        p1.setEy( p1.getEy() + q2 * dy / r2 );

        if ( magneticConst > 0 )
        {
            double rl = Math.sqrt( r2 );

            double vx1 = p1.getVx();
            double vy1 = p1.getVy();
            double vx2 = p2.getVx();
            double vy2 = p2.getVy();

            p1.setMx( p1.getMx() +
                      q2 * ( -vy1 * vy2 * dx + vx2 * vy1 * dy ) / rl );
            p1.setMy( p1.getMy() +
                      q2 * (  vx1 * vy2 * dx - vx1 * vx2 * dy ) / rl );
        }
    }

    protected void integrate2( Particle p )
    {
        double q = p.getCharge();

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

//by Dragan Simeonov 27.10.2002
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
            double Bq = 0;


            vx *= 1-frictionConst;
            vy *= 1-frictionConst;

            vx += ex * integrationStep;
            vy += ey * integrationStep;

            if (vy != 0)
            	Bq = mx/vy;
            else if (vx != 0)
            	Bq = -my/vx;

            if (Bq != 0)
            {
				double cfi = Math.cos(Bq * integrationStep);
				double sfi = Math.sin(Bq * integrationStep);
				double vx0 = vx;
				double vy0 = vy;

				vx = vx0*cfi + vy0*sfi;
				vy = vy0*cfi - vx0*sfi;
			}

            loc.x += vx * integrationStep;
            loc.y += vy * integrationStep;


            p.setVx( vx );
            p.setVy( vy );
            p.move( loc );
        }
    }
}
