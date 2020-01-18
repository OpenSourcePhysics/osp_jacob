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

package jacob.scene.editor;

import java.awt.event.KeyListener; 
import java.awt.event.MouseListener; 
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent; 
import java.awt.event.MouseEvent; 
import java.awt.Graphics; 
import java.awt.Color; 
import java.awt.PopupMenu; 

import jacob.scene.*;
import jacob.scene.modifiers.*;
import jacob.geometry.*;

public class SceneEditor implements 
    KeyListener, 
    MouseListener, 
    MouseMotionListener
{
    private Scene scene;

    private SceneListener sceneListener;

    private Point2d mousesloc = null;

    private Point2d mouseloc = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SceneEditor( Scene scene ) 
    {
        this.scene = scene;
//FIXME
        sceneListener = new NullAction();
        sceneListener.setEditor( this );
        sceneListener.added();

        scene.addMouseListener( this );
        scene.addMouseMotionListener( this );
        scene.addKeyListener( this ); 
    }

//------------------------------------------------------------------------------
//    Scene Methods
//------------------------------------------------------------------------------

    public Scene getScene()
    {
        return scene;
    }

//------------------------------------------------------------------------------
//    Scene Listener Methods
//------------------------------------------------------------------------------

    public void setAction( String saction ) 
        throws MalformedActionException, UnknownActionException
    {
        SceneAction action;

        if ( saction == null )
            action = null;
        else        
            action = ActionFactory.createAction( saction );

        setListener( action );
    }

    public void setListener( SceneListener listener )
    {
        if ( sceneListener != null )
        {
            sceneListener.removed();
            sceneListener.setEditor( null );
        }

        if ( listener == null )
            sceneListener = new NullAction();
        else
            sceneListener = listener; 

        sceneListener.setEditor( this );
        sceneListener.added();
    }

//------------------------------------------------------------------------------
//    Key Events 
//------------------------------------------------------------------------------

//FIXME
    public void keyTyped( KeyEvent e ) {} 

    public void keyPressed( KeyEvent e ) {}

    public void keyReleased( KeyEvent e ) {}

//------------------------------------------------------------------------------
//    AWT Mouse Events 
//------------------------------------------------------------------------------

    public void mouseClicked( MouseEvent e ) 
    {
        sceneListener.mouseClicked( new SceneMouseEvent( scene, e ) );
    }

    public void mousePressed( MouseEvent e ) 
    {
        sceneListener.mousePressed( new SceneMouseEvent( scene, e ) );
    }

    public void mouseReleased( MouseEvent e ) 
    {
        sceneListener.mouseReleased( new SceneMouseEvent( scene, e ) );
    }

    public void mouseEntered( MouseEvent e ) 
    {
        sceneListener.mouseEntered( new SceneMouseEvent( scene, e ) );
    }

    public void mouseExited( MouseEvent e ) 
    {
        mousesloc = mouseloc = null;
        sceneListener.mouseExited( new SceneMouseEvent( scene, e ) );
        scene.redraw();
    }

    public void mouseDragged( MouseEvent e ) 
    {
        SceneMouseEvent se = new SceneMouseEvent( scene, e );
        mousesloc = se.getSnappedLocation();
        mouseloc = se.getLocation();
        sceneListener.mouseDragged( se );
    }

    public void mouseMoved( MouseEvent e ) 
    {
        SceneMouseEvent se = new SceneMouseEvent( scene, e );
        mousesloc = se.getSnappedLocation();
        mouseloc = se.getLocation();
        sceneListener.mouseMoved( se );
    }

//------------------------------------------------------------------------------
//    Paint Methods 
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g ) 
    {
        sceneListener.paintFg( g );
    } 

    public void paintBg( SceneGraphics g ) 
    {
        sceneListener.paintBg( g );
    }

    public void paintSnappedPointer( SceneGraphics g )
    {
        Graphics gg = g.getGraphics();

        if ( mousesloc == null || 
             !scene.getProperties().snapToGrid.getValue() )
            return; 

        double scale = g.getScale();
        int size = scene.getProperties().snapMarkSize.getValue();

        gg.setColor( scene.getProperties().snapMarkFg.getValue() );

        int mx = (int)(mousesloc.x * scale);
        int my = (int)(mousesloc.y * scale);
        gg.drawLine( mx - size, my, mx + size, my ); 
        gg.drawLine( mx, my - size, mx, my + size ); 
    }

//------------------------------------------------------------------------------
//    Get Component Methods
//------------------------------------------------------------------------------

    public SceneComponent getComponentAt( String type, Point2d p )
    {
//FIXME
        if ( type.equals( "element" ) )
            return getElementAt( p );
        else if ( type.equals( "particle" ) )
            return getParticleAt( p );
        else if ( type.equals( "elementorparticle" ) )
            return getElementOrParticleAt( p );
        else
            return null;
    }

    public SceneComponent getElementAt( Point2d p )
    {
        SceneIterator iterator;
        SceneComponent closest = null;
        double mind = Double.MAX_VALUE;

        iterator = new ElementIterator( getScene().createIterator() );

        while ( !iterator.isDone() )
        {
            SceneComponent c = iterator.getCurrentComponent();

            if ( c.contains( p ) )
            {
                double d = c.getBorderDistance( p );

//System.out.println( "contains d = " + d );
                if ( d < mind )
                {
                    mind = d;
                    closest = c;
                }
            }
            iterator.next();
        }
        return closest;
    }

    public SceneComponent getParticleAt( Point2d p )
    {
        SceneIterator iterator;
        SceneComponent closest = null;
        double mind = getScene().getProperties().touchOffset.getValue();

        iterator = new ParticleIterator( getScene().createIterator() );

        while ( !iterator.isDone() )
        {
            SceneComponent c = iterator.getCurrentComponent();

            double d = c.getCenterDistance( p );

            if ( d < mind )
            {
                mind = d;
                closest = c;
            }
            iterator.next();
        }
        return closest;
    }

    public SceneComponent getElementOrParticleAt( Point2d p )
    {
        SceneIterator iterator;
        SceneComponent closest = null;
        double toff = getScene().getProperties().touchOffset.getValue();
        double mind = Double.MAX_VALUE;

        iterator = getScene().createIterator();

        while ( !iterator.isDone() )
        {
            SceneComponent c = iterator.getCurrentComponent();

            if ( c instanceof Element )
            {
                if ( c.contains( p ) )
                {
                    double d = c.getBorderDistance( p );

                    if ( d < mind )
                    {
                        mind = d;
                        closest = c;
                    }
                }
            }
            else if ( c instanceof Particle )
            {
                double d = c.getCenterDistance( p );

                if ( d < toff && d < mind )
                {
                    mind = d;
                    closest = c;
                }
            }
            iterator.next();
        }
        return closest;
    }

//FIXME
    public Object getElementOrModifierAt( Point2d p, Class modtype )
    {
        ElementIterator iterator;
        Element  closest = null;
        ComponentModifier closestmod = null;
        int closesthspt = -1;
        double mind = Double.MAX_VALUE;
        double minhsptd = getScene().getProperties().touchOffset.getValue();

        iterator = new ElementIterator( getScene().createIterator() );

        while ( !iterator.isDone() )
        {
            Element e = iterator.getCurrentElement();

            if ( e.contains( p ) )
            {
                double d = e.getBorderDistance( p );

                if ( d < mind )
                {
                    mind = d;
                    closest = e;
                }
            }
            if ( e.hasModifiers() )
            {
                ComponentModifier m = e.getModifiers().getModifier( modtype );

                if ( m != null )
                {
                    Point2d loc = e.getLocation();
                    for ( int i = 0; i < m.countHotspots(); i++ )
                    {
                        Point2d hotspot = m.getHotspotAbsAt( i );
                        double d = hotspot.distance( p );

                        if ( d < minhsptd )
                        {
                            minhsptd = d;
                            closesthspt = i;
                            closestmod = m;
                        }
                    }
                }
            }
            iterator.next();
        }
        if ( closestmod != null )
//FIXME return hotspot data
            return closestmod;
        else
            return closest;
    }
}
