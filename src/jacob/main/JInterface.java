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

package jacob.main;

import jacob.scene.*;
import jacob.scene.modifiers.*;
import jacob.geometry.*;
import jacob.property.*;
import jacob.system.*;

public class JInterface
{
    private MainPanel mainPanel;

    private Scene scene;

    private SystemMgr systemMgr;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public JInterface( SystemMgr systemMgr, MainPanel mainPanel )
    {
        this.mainPanel = mainPanel;
        this.systemMgr = systemMgr;
        this.scene = mainPanel.getScene();
    }

//------------------------------------------------------------------------------
//    Scene Methods
//------------------------------------------------------------------------------

    public Scene getScene()
    {
        return scene;
    }

    public void readExperiment( String file )
    {
        mainPanel.readExperiment( file );
    }

    public void newScene()
    {
        scene.newScene();
    }

//------------------------------------------------------------------------------
//    Element Methods
//------------------------------------------------------------------------------

    public Element getElement( String name )
    {
        return scene.getElement( name );
    }

    public Point2d getLocation( String name )
    {
        Element e = scene.getElement( name );

        if ( e != null )
            return e.getLocation();
        else
            return null;
    }

    public double getX( String name )
    {
        Element e = scene.getElement( name );

        if ( e != null )
            return e.getLocation().x;
        else
            return 0;
    }



    public double getY( String name )
    {
        Element e = scene.getElement( name );

        if ( e != null )
            return e.getLocation().y;
        else
            return 0;
    }

//by VV 17.01.03
    public int getParticleNumber( String name )
    {
        Element e = scene.getElement( name );

        if ( e != null )
            return e.countChildren();
        else
            return 0;
    }


    public boolean setName( String name, String newname )
    {
        Element e = scene.getElement( name );

        if ( e != null )
            return e.setName( newname );
        else
            return false;
    }

    public Element createCircleElement( String name, double x, double y,
                                        double r )
    {
        return createCircleElement( name, x, y, r, null );
    }

    public Element createCircleElement( String name, double x, double y,
                                        double r, String pname )
    {
        Element e;
        Element parent = scene.getElement( pname );

        e = new Element( scene, new Circle2d( x, y, r ) );
        e.setName( name );

        synchronized( scene.getSceneLock() )
        {
            if ( parent == null )
                scene.getView().addChild( e );
            else
                parent.addChild( e );
        }
        scene.redraw();
        return e;
    }

    public Element createRingElement( String name, double x, double y,
                                      double r1, double r2 )
    {
        return createRingElement( name, x, y, r1, r2, null );
    }

    public Element createRingElement( String name, double x, double y,
                                      double r1, double r2, String pname )
    {
        Element e;
        Element parent = scene.getElement( pname );

        e = new Element( scene, new Ring2d( x, y, r1, r2 ) );
        e.setName( name );

        synchronized( scene.getSceneLock() )
        {
            if ( parent == null )
                scene.getView().addChild( e );
            else
                parent.addChild( e );
        }
        scene.redraw();
        return e;
    }

    public Element createPolygonElement( String name, double xpoints[],
                                         double ypoints[], int npoints )
    {
        return createPolygonElement( name, xpoints, ypoints, npoints, null );
    }

    public Element createPolygonElement( String name, double xpoints[],
                                         double ypoints[], int npoints,
                                         String pname )
    {
        Element e;
        Element parent = scene.getElement( pname );

        e = new Element( scene, new Polygon2d( xpoints, ypoints, npoints ) );
        e.setName( name );

        synchronized( scene.getSceneLock() )
        {
            if ( parent == null )
                scene.getView().addChild( e );
            else
                parent.addChild( e );
        }
        scene.redraw();
        return e;
    }

    public Element createRectangleElement( String name, double x, double y,
                                           double width, double height )
    {
        return createRectangleElement( name, x, y, width, height, null );
    }

    public Element createRectangleElement( String name, double x, double y,
                                           double width, double height,
                                           String pname )
    {
        double xpoints[] = { x, x + width, x + width, x };
        double ypoints[] = { y, y, y + height, y + height };

        return createPolygonElement( name, xpoints, ypoints, 4, pname );
    }

    public void deleteElement( String name )
    {
        Element e = getElement( name );
        if ( e == null ) return;

        synchronized( scene.getSceneLock() )
        {
            e.delete();
        }
        scene.redraw();
    }

    public void moveTo( String name, double x, double y )
    {
        Element e = scene.getElement( name );
        if ( e == null ) return;

        synchronized( scene.getSceneLock() )
        {
            e.move( new Point2d( x, y ) );
        }
        scene.redraw();
    }

    public void moveBy( String name, double x, double y )
    {
        Element e = scene.getElement( name );
        if ( e == null ) return;

        Point2d loc = e.getLocation();
        synchronized( scene.getSceneLock() )
        {
            e.move( new Point2d( loc.x + x, loc.y + y ) );
        }
        scene.redraw();
    }

    public void scale( String name, double scale )
    {
        Element e = scene.getElement( name );
        if ( e == null ) return;

        synchronized( scene.getSceneLock() )
        {
            e.scale( scale );
        }
        scene.redraw();
    }

    public void rotate( String name, double angle )
    {
        Element e = scene.getElement( name );
        if ( e == null ) return;

        synchronized( scene.getSceneLock() )
        {
            e.rotate( angle * Math.PI / 180 );
        }
        scene.redraw();
    }

    public void rotate( String name, double pivotx, double pivoty,
                        double angle )
    {
        Element e = scene.getElement( name );
        if ( e == null ) return;

        synchronized( scene.getSceneLock() )
        {
            e.rotate( new Point2d( pivotx, pivoty ), angle * Math.PI / 180 );
        }
        scene.redraw();
    }

//FIXME: don't allow to create vector modifiers like this
    public void setModifier( String name, String mod )
    {
        Element e = getElement( name );

        if ( e == null ) return;

        if ( !e.hasModifiers() )
            e.createModifiers();

        synchronized( scene.getSceneLock() )
        {
            ComponentModifier cm = e.getModifiers().getModifier( mod );

            if ( cm == null )
            {
                try
                {
                    cm = ModifierFactory.createModifier( mod );
                    e.getModifiers().addModifier( cm );
                }
                catch ( UnknownModifierException ex )
                {
                    return;
                }
            }
        }
        scene.redraw();
    }

    public void setParticleModifier( String name, String mod )
	{
	        Element e = getElement( name );

	        if ( e == null ) return;

	        if ( !e.hasModifiers() )
	            e.createModifiers();

	        synchronized( scene.getSceneLock() )
	        {
	            ComponentModifier cm = e.getModifiers().getModifier( mod );

	            if ( cm == null )
	            {
	                try
	                {
	                    cm = ModifierFactory.createModifier( mod );
	                    e.getModifiers().addModifier( cm );
	                }
	                catch ( UnknownModifierException ex )
	                {
	                    return;
	                }
	            }
	        }
	        scene.redraw();
	    }


    public void setVectorModifier( String name, String mod,
                                   double hx, double hy, boolean abs )
    {
        Element e = getElement( name );

        if ( e == null ) return;

        if ( !e.hasModifiers() )
            e.createModifiers();

        synchronized( scene.getSceneLock() )
        {
            ComponentModifier cm = e.getModifiers().getModifier( mod );

            if ( cm == null )
            {
                try
                {
                    cm = ModifierFactory.createModifier( mod );
                    e.getModifiers().addModifier( cm );
                }
                catch ( UnknownModifierException ex )
                {
                    return;
                }
            }
            if ( abs )
                cm.moveHotspotAbsAt( 0, new Point2d( hx, hy ) );
            else
                cm.moveHotspotRelAt( 0, new Point2d( hx, hy ) );
        }
        scene.redraw();
    }


    public void unsetModifier( String name, String mod )
    {
        Element e = getElement( name );

        if ( e == null || !e.hasModifiers() ) return;

        synchronized( scene.getSceneLock() )
        {
            ComponentModifier cm = e.getModifiers().getModifier( mod );

            if ( cm != null )
                e.getModifiers().removeModifier( cm );
        }
        scene.redraw();
    }

//------------------------------------------------------------------------------
//    Element Animation Methods
//------------------------------------------------------------------------------
//
//    public void moveBy( String name, double xstep, double ystep, int count,
//                        int sleep, boolean block )
//    {
//        class A extends Thread
//        {
//            Element e;
//            double xstep, ystep;
//            int count, sleep;
//
//            public A( Element e, double xstep, double ystep,
//                      int count, int sleep )
//            {
//                this.e = e;
//                this.xstep = xstep;
//                this.ystep = ystep;
//                this.count = count;
//                this.sleep = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                for ( int i = 0; i < count; i++ )
//                {
//                    Point2d loc = e.getLocation();
//                    synchronized( scene.getSceneLock() )
//                    {
//                        e.move( new Point2d( loc.x + xstep, loc.y + ystep ) );
//                    }
//                    scene.animationRedraw();
//                    try {sleep(sleep);} catch (InterruptedException ex) {}
//                }
//            }
//        };
//
//        Element e = scene.getElement( name );
//        if ( e == null ) return;
//
//        A anim = new A( e, xstep, ystep, count, sleep );
//        // anim.start(); // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    }
//
//    public void scale( String name, double step, int count,
//                       int sleep, boolean block )
//    {
//        class A extends Thread
//        {
//            Element e;
//            double step;
//            int count, sleep;
//
//            public A( Element e, double step, int count, int sleep )
//            {
//                this.e     = e;
//                this.step  = step;
//                this.count = count;
//                this.sleep = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                for ( int i = 0; i < count; i++ )
//                {
//                    synchronized( scene.getSceneLock() )
//                    {
//                        e.scale( step );
//                    }
//                    scene.animationRedraw();
//                    try {sleep(sleep);} catch (InterruptedException ex) {}
//                }
//            }
//        };
//
//        Element e = scene.getElement( name );
//        if ( e == null ) return;
//
//        A anim = new A( e, step, count, sleep );
//        // anim.start();  // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    }
//
//    public void rotate( String name, double step, int count,
//                        int sleep, boolean block )
//    {
//        class A extends Thread
//        {
//            Element e;
//            double step;
//            int count, sleep;
//
//            public A( Element e, double step, int count, int sleep )
//            {
//                this.e = e;
//                this.step = step;
//                this.count = count;
//                this.sleep = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                for ( int i = 0; i < count; i++ )
//                {
//                    synchronized( scene.getSceneLock() )
//                    {
//                        e.rotate( step );
//                    }
//                    scene.animationRedraw();
//                    try {sleep(sleep);} catch (InterruptedException ex) {}
//                }
//            }
//        };
//
//        Element e = scene.getElement( name );
//        if ( e == null ) return;
//
//        A anim = new A( e, step, count, sleep );
//       // anim.start(); // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    }
//
//    public void rotate( String name, double pivotx, double pivoty,
//                        double step, int count,
//                        int sleep, boolean block )
//    {
//        class A extends Thread
//        {
//            Element e;
//            Point2d pivot;
//            double step;
//            int count, sleep;
//
//            public A( Element e, Point2d pivot, double step,
//                      int count, int sleep )
//            {
//                this.e = e;
//                this.pivot = pivot;
//                this.step  = step;
//                this.count = count;
//                this.sleep = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                for ( int i = 0; i < count; i++ )
//                {
//                    synchronized( scene.getSceneLock() )
//                    {
//                        e.rotate( pivot, step );
//                    }
//                    scene.animationRedraw();
//                    try {sleep(sleep);} catch (InterruptedException ex) {}
//                }
//            }
//        };
//
//        Element e = scene.getElement( name );
//        if ( e == null ) return;
//
//        A anim = new A( e, new Point2d( pivotx, pivoty ), step, count, sleep );
//        // anim.start();  // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    }
//
////------------------------------------------------------------------------------
////    Particle Methods
////------------------------------------------------------------------------------
//
//    public Particle createParticle( String pname, double charge,
//                                    double x, double y, boolean fixed)
//    {
//        Element parent = scene.getElement( pname );
//        if ( parent == null ) return null;
//
//        Particle p;
//
//        p = new Particle( scene, charge );
//        p.move( new Point2d( x, y ) );
//        p.setFixed(fixed);
//
//        synchronized( scene.getSceneLock() )
//        {
//            parent.addChild( p );
//        }
//        scene.redraw();
//        return p;
//    }
//
//    public Particle createParticle( String pname, double charge,
//                                    double x, double y,
//                                    double xoff, double yoff, boolean fixed)
//    {
//        xoff *= (1 - Math.random() * 2);
//        yoff *= (1 - Math.random() * 2);
//
//        return createParticle( pname, charge, x + xoff, y + yoff, fixed );
//    }
//
////------------------------------------------------------------------------------
////    Particle Animation Methods
////------------------------------------------------------------------------------
//
//    public void createParticles( String pname, double charge,
//                                 double x, double y,
//                                 double xoff, double yoff, int step,
//                                 int count, int sleep, boolean block )
//    {
//		createParticles( pname, charge, x, y, xoff, yoff, step,
//                         count, false, sleep, block );
//	}
//
//    public void createParticles( String pname, double charge,
//                                 double x, double y,
//                                 double xoff, double yoff, int step,
//                                 int count, boolean fixed, int sleep, boolean block )
//    {
//        class A extends Thread
//        {
//            Element parent;
//            double charge, x, y, xoff, yoff;
//            int step, count, sleep;
//            boolean fixed;
//
//            public A( Element parent, double charge,
//                      double x, double y,
//                      double xoff, double yoff,
//                      int step, int count, boolean fixed, int sleep )
//            {
//                this.parent = parent;
//                this.charge = charge;
//                this.x = x;
//                this.y = y;
//                this.xoff  = xoff;
//                this.yoff  = yoff;
//                this.step  = step;
//                this.count = count;
//                this.fixed = fixed;
//                this.sleep = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                for ( int i = 0; i < count; i++ )
//                {
//                    double px = x + xoff * (1 - Math.random() * 2);
//                    double py = y + yoff * (1 - Math.random() * 2);
//                    synchronized( scene.getSceneLock() )
//                    {
//                        for ( int j = 0; j < step; j++ )
//                        {
//                            Particle p = new Particle( scene, charge );
//                            p.move( new Point2d( px, py ) );
//                            p.setFixed(fixed);
//                            parent.addChild( p );
//                        }
//                    }
//                    scene.animationRedraw();
//                    try {sleep(sleep);} catch (InterruptedException ex) {}
//                }
//            }
//        };
//
//        Element parent = scene.getElement( pname );
//        if ( parent == null ) return;
//
//        A anim = new A( parent, charge, x, y, xoff, yoff, step, count, fixed, sleep );
//        // anim.start();  // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    }
////---------------------------------------------------------------------------------------
//    public void createParticlesMesh( String pname, double charge,
//                                     double x, double y, int width, int height,
//                                     int step, boolean fixed, int sleep, boolean block )
//    {
//        class A extends Thread
//        {
//            Element parent;
//            double charge, x, y;
//            int width, height;
//            boolean fixed;
//            int step, sleep;
//
//            public A( Element parent, double charge,
//                      double x, double y,
//                      int width, int height,
//                      int step, boolean fixed, int sleep )
//            {
//                this.parent = parent;
//                this.charge = charge;
//                this.x = x;
//                this.y = y;
//                this.width  = width;
//                this.height = height;
//                this.step   = step;
//                this.fixed  = fixed;
//                this.sleep  = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                for ( int i = 0; i < width; i += step )
//                {
//                    for ( int j = 0; j < height; j += step)
//                    {
//                        double px = x + i;
//                        double py = y + j;
//
//                        synchronized( scene.getSceneLock() )
//                        {
//                            Particle p = new Particle( scene, charge );
//                            p.move( new Point2d( px, py ) );
//                            p.setFixed(fixed);
//                            parent.addChild( p );
//                        }
//
//                        scene.animationRedraw();
//                        try {sleep(sleep);} catch (InterruptedException ex) {}
//				    }
//                }
//            }
//        };
//
//        Element parent = scene.getElement( pname );
//        if ( parent == null ) return;
//
//        A anim = new A( parent, charge, x, y, width, height, step, fixed, sleep );
//        // anim.start(); // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    };
//
////VV 10.30.2002 ----------------------------------------------------------------------------
//    public void createParticlesMeshFunction( String pname, int typef, double charge, double fconstant,
//                                     double x, double y, int width, int height,
//                                     int step, boolean fixed, int sleep, boolean block )
//    {
//        class A extends Thread
//        {
//            Element parent;
//            int typef;
//            double charge, fconstant, x, y;
//            int width, height;
//            boolean fixed;
//            int step, sleep;
//
//            public A( Element parent, int typef, double charge, double fconstant,
//                      double x, double y,
//                      int width, int height,
//                      int step, boolean fixed, int sleep )
//            {
//                this.parent = parent;
//                this.typef   = typef;
//                this.charge = charge;
//                this.fconstant = fconstant;
//                this.x = x;
//                this.y = y;
//                this.width  = width;
//                this.height = height;
//                this.step   = step;
//                this.fixed  = fixed;
//                this.sleep  = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                int nparticle = 0;
//                for ( int i = 0; i < width; i += step )
//                {
//
//                    for ( int j = 0; j < height; j += step)
//                    {
//                        double px = x + i;
//                        double py = y + j;
//                        double chargef = charge;
//
//
//                        switch (typef)
//                        {
//                         case 1:
//                           chargef = charge * (1 + i * fconstant);
//                           break;
//                         case 2:
//                           chargef = charge * (1 + i * i * fconstant);
//                           break;
//                         case 3:
//                           chargef = charge * (1 +  Math.sin(i * fconstant / width ));
//                           break;
//                          default:
//                           chargef = charge;
//                           break;
//					    }
//
//
//                        synchronized( scene.getSceneLock() )
//                        {
//                            Particle p = new Particle( scene, chargef );
//                            p.move( new Point2d( px, py ) );
//                            p.setFixed(fixed);
//                            parent.addChild( p );
//                        }
//
//                        scene.animationRedraw();
//                        try {sleep(sleep);} catch (InterruptedException ex) {}
//                        nparticle += 1;
//				    }
//                }
//            }
//        };
//
//        Element parent = scene.getElement( pname );
//        if ( parent == null ) return;
//
//        A anim = new A( parent, typef, charge, fconstant, x, y, width, height, step, fixed, sleep );
//        // anim.start();  // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    };
//
////---------------------------------------------------------------------------------------
//
//public void createParticlesRing( String pname, double charge,
//                                     double x, double y, double r1, double r2,
//                                     int step, boolean fixed, int sleep, boolean block )
//    {
//        class A extends Thread
//        {
//            Element parent;
//            double charge, x, y;
//            double r1,r2;
//            int step;
//            boolean fixed;
//            int sleep;
//
//            public A( Element parent, double charge,
//                      double x, double y,
//                      double r1, double r2,
//                      int step, boolean fixed, int sleep )
//            {
//                this.parent = parent;
//                this.charge = charge;
//                this.x = x;
//                this.y = y;
//                this.r1  = r1;
//                this.r2  = r2;
//                this.step   = step;
//                this.fixed  = fixed;
//                this.sleep  = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                double rr = (r1 + r2)/2;
//                double pstep = 2 * Math.PI/step;
//
//                for ( double i = 0; i < 2 * Math.PI; i+= pstep )
//                {
//
//                       double px = x + rr * Math.cos(i);
//                       double py = y - rr * Math.sin(i);
//
//
//                        synchronized( scene.getSceneLock() )
//                        {
//                            Particle p = new Particle( scene, charge );
//                            p.move( new Point2d( px, py ) );
//                            p.setFixed(fixed);
//                            parent.addChild( p );
//                        }
//
//                        scene.animationRedraw();
//                        try {sleep(sleep);} catch (InterruptedException ex) {}
//                }
//            }
//        };
//
//        Element parent = scene.getElement( pname );
//        if ( parent == null ) return;
//
//        A anim = new A( parent, charge, x, y, r1, r2, step, fixed, sleep );
//        // anim.start();  // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    };
//
//
//
////VV 08.12.2002----------------------------------------------------------------------
//
//public void createParticlesWater( String pname, double charge,
//                                     double x, double y, double r, double theta,
//                                     int step, boolean fixed, int sleep, boolean block)
//    {
//        class A extends Thread
//        {
//            Element parent;
//            double charge, x, y;
//            double r;
//            double theta;
//            int step;
//            boolean fixed;
//            int sleep;
//
//            public A( Element parent, double charge,
//                      double x, double y,
//                      double r, double theta,
//                      int step, boolean fixed, int sleep )
//            {
//                this.parent = parent;
//                this.charge = charge;
//                this.x = x;
//                this.y = y;
//                this.r  = r;
//                this.theta  = theta;
//                this.step   = step;
//                this.fixed  = fixed;
//                this.sleep  = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                double rr = r - 3;
//                double pstep = 2 * Math.PI/step;
//                double chargep = charge;
//                double thetarad = theta * Math.PI / 180;
//                int iion = 1;
//
//                for ( double i = 0; i < 2 * Math.PI; i+= pstep )
//                {
//
//                       switch ( iion )
//                       {
//						   case 1: chargep = charge; rr = r - 6; thetarad = -theta * Math.PI / 180;break;
//						   case 2: chargep = -2*charge; rr = r - 12; thetarad = 0.0;break;
//						   case 3: chargep = charge; rr = r - 6; thetarad =  theta * Math.PI / 180; break;
//						   default: chargep = charge; rr = r - 6; thetarad = 0.0;
//				       }
//
//                       double px = x + rr * Math.sin(i + thetarad);
//                       double py = y - rr * Math.cos(i + thetarad);
//
//
//                        synchronized( scene.getSceneLock() )
//                       {
//                            Particle p = new Particle( scene, chargep );
//                            p.move( new Point2d( px, py ) );
//                            p.setFixed(fixed);
//                            parent.addChild( p );
//                       }
//							iion++;
//                        scene.animationRedraw();
//                        try {sleep(sleep);} catch (InterruptedException ex) {}
//                }
//            }
//        };
//
//        Element parent = scene.getElement( pname );
//        if ( parent == null ) return;
//
//        A anim = new A( parent, charge, x, y, r, theta, step, fixed, sleep );
//        // anim.start();  // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    };
//
////VV 30.10.2002----------------------------------------------------------------------
//
//public void createParticlesMultipole( String pname, double charge,
//                                     double x, double y, double r, double theta,
//                                     int step, boolean fixed, int sleep, boolean block)
//    {
//        class A extends Thread
//        {
//            Element parent;
//            double charge, x, y;
//            double r;
//            double theta;
//            int step;
//            boolean fixed;
//            int sleep;
//
//            public A( Element parent, double charge,
//                      double x, double y,
//                      double r, double theta,
//                      int step, boolean fixed, int sleep )
//            {
//                this.parent = parent;
//                this.charge = charge;
//                this.x = x;
//                this.y = y;
//                this.r  = r;
//                this.theta  = theta;
//                this.step   = step;
//                this.fixed  = fixed;
//                this.sleep  = sleep;
//            }
//
//            public void run()
//            {
//                setPriority( Thread.MIN_PRIORITY );
//                double rr = r - 3;
//                double pstep = 2 * Math.PI/step;
//                double chargep = charge;
//                double thetarad = theta * Math.PI / 180;
//                int iion = 1;
//
//                for ( double i = 0; i < 2 * Math.PI; i+= pstep )
//                {
//
//                       double px = x + rr * Math.sin(i + thetarad);
//                       double py = y - rr * Math.cos(i + thetarad);
//
//                       chargep = -chargep;
//
//
//                        synchronized( scene.getSceneLock() )
//                        {
//                            Particle p = new Particle( scene, chargep );
//                            p.move( new Point2d( px, py ) );
//                            p.setFixed(fixed);
//                            parent.addChild( p );
//                        }
//
//                        scene.animationRedraw();
//                        try {sleep(sleep);} catch (InterruptedException ex) {}
//                }
//            }
//        };
//
//        Element parent = scene.getElement( pname );
//        if ( parent == null ) return;
//
//        A anim = new A( parent, charge, x, y, r, theta, step, fixed, sleep );
//        // anim.start(); // WC disabled thread for JS;  will replace with Swing timer
//
//        if ( block )
//        {
//            while ( anim.isAlive() )
//            {
//                try { Thread.currentThread().sleep( 10 ); }
//                catch (InterruptedException ex) {}
//            }
//        }
//    };



//------------------------------------------------------------------------------
//    Property Methods
//------------------------------------------------------------------------------

    public void setProperty( String name, String value )
    {
        PropertyMgr pm = systemMgr.getPropertyMgr();

        if ( pm.isDefined( name ) )
        {
            pm.getProperty( name ).setValue( value );
        }

        pm.notifyObservers( this );
    }

//REMIND: implement 'getProperty' methods
}
