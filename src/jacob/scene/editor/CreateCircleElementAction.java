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

public class CreateCircleElementAction extends SceneAction
{
    private Point2d center;

    private Circle2d circle = null;

    private SceneComponent selectedComp = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public CreateCircleElementAction( String args[] ) 
        throws MalformedActionException
    {
        if ( args != null )
            throw new MalformedActionException( "wrong arg count" );
    }

//------------------------------------------------------------------------------
//    Mouse Events 
//------------------------------------------------------------------------------

    public void mouseNormalPressed( SceneMouseEvent e ) 
    {
        if ( circle == null )
        {
            if ( selectedComp != null )
            {
                center = e.getSnappedLocation();
                circle = new Circle2d();
            }
        }
        else 
        {
            synchronized( getScene().getSceneLock() )
            {
                selectedComp.addChild( new Element( getScene(), circle ) );
            }
            circle = null;
            selectedComp = null;
            getScene().redraw();
        }
    }

    public void mouseMoved( SceneMouseEvent e )
    {
        if ( circle != null )
        {
            circle.set( center.x, center.y, 
                        center.distance( e.getSnappedLocation() ) );
        }
        else
        {
            selectedComp = getElementAt( e.getLocation() );
            if ( selectedComp == null )
                selectedComp = getScene().getView();
        }
        getScene().redraw(); 
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        if ( circle != null)
        {
            g.setColor( getProperties().shapeFg.getValue() );
            circle.draw( g );
        }
        if ( selectedComp != null )
            selectedComp.paintHl( g );

        paintSnappedPointer( g );
    }
}
