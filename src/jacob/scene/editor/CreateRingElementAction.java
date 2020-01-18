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

public class CreateRingElementAction extends SceneAction
{
    private Point2d center;

    private Ring2d ring = null;

    private int stage = 0;

    private SceneComponent selectedComp = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public CreateRingElementAction( String args[] ) 
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
        if ( ring == null )
        {
            if ( selectedComp != null )
            {
                center = e.getSnappedLocation();
                ring = new Ring2d( center.x, center.y, 0, 0 );
                stage++;
            }
        }
        else if ( stage == 1 )
        {
            stage++;
        }
        else if ( stage == 2 )
        {
            synchronized( getScene().getSceneLock() )
            {
                selectedComp.addChild( new Element( getScene(), ring ) );
            }
            ring = null;
            stage = 0;
            selectedComp = null;
            getScene().redraw();
        }
    }

    public void mouseMoved( SceneMouseEvent e )
    {
        if ( ring != null )
        {
            if ( stage == 1 ) 
                ring.setRadius1( center.distance( e.getSnappedLocation() ) );
            else if ( stage == 2 )
                ring.setRadius2( center.distance( e.getSnappedLocation() ) );
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
        if ( ring != null )
        {
            g.setColor( getProperties().shapeFg.getValue() );
            ring.draw( g );
        }
        if ( selectedComp != null )
            selectedComp.paintHl( g );

        paintSnappedPointer( g );
    }
}
