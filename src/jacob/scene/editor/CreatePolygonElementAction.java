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

import jacob.scene.*;
import jacob.system.*;
import jacob.property.*;
import jacob.geometry.*;


public class CreatePolygonElementAction extends SceneAction
{
    private Polygon2d poly = null;

    private int currPoint = 0;

    private SceneComponent selectedComp = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public CreatePolygonElementAction( String args[] ) 
        throws MalformedActionException
    {
        if ( args != null )
            throw new MalformedActionException( "wrong arg count" );
    }

//------------------------------------------------------------------------------
//    Mouse Events 
//------------------------------------------------------------------------------

    public void mousePressed( SceneMouseEvent e ) 
    {
        if ( e.isMetaDown() )
        {
            if ( poly == null )
            {
                super.mousePressed( e );
                return;
            }

            if ( poly.getPointsNum() < 3 )
            {
                poly = null;
            }
            else
            {
                synchronized( getScene().getSceneLock() )
                {
                    selectedComp.addChild( new Element( getScene(), poly ) );
                }
                poly = null;
                selectedComp = null;
            }
        }
        else
        {
            if ( poly == null )
            {
                if ( selectedComp != null )
                {
                    poly = new Polygon2d();
                    poly.addPoint( e.getSnappedLocation() );
                    poly.addPoint( e.getSnappedLocation() );
                    currPoint = 1;
                }
            }
            else
            {
                poly.addPoint( e.getSnappedLocation() );
                currPoint++;
            }
        }
        getScene().redraw();
    }

    public void mouseMoved( SceneMouseEvent e )
    {
        if ( poly == null )
        {
            selectedComp = getElementAt( e.getLocation() );
            if ( selectedComp == null )
                selectedComp = getScene().getView();
        }
        else
        { 
            poly.setPoint( currPoint, e.getSnappedLocation() ); 
        }
        getScene().redraw(); 
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        if ( selectedComp != null )
            selectedComp.paintHl( g );

        if ( poly != null)
        {
            g.setColor( getProperties().shapeFg.getValue() );
            poly.draw( g );
            g.setColor( getProperties().shapeFg.getValue().brighter() );
            g.drawLine( poly.getPoint( 0 ).x, 
                        poly.getPoint( 0 ).y, 
                        poly.getPoint( currPoint ).x, 
                        poly.getPoint( currPoint ).y );
        }
        paintSnappedPointer( g );
    }
}
