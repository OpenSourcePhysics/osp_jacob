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

import jacob.scene.*;
import jacob.system.*;
import jacob.property.*;
import jacob.geometry.*;

public class MoveAction extends SceneAction 
{
    private String compType;

    private Point2d oldloc;

    private SceneComponent selectedComp = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public MoveAction( String[] args ) 
        throws MalformedActionException
    {
        if ( args.length != 1 )
            throw new MalformedActionException( "wrong arg count" );

        compType = args[0];
    } 

//------------------------------------------------------------------------------
//    Mouse Events 
//------------------------------------------------------------------------------

    public void mouseNormalPressed( SceneMouseEvent e )
    {
        oldloc = e.getLocation();
    }

    public void mouseNormalDragged( SceneMouseEvent e ) 
    {
        if ( selectedComp != null )
        {
            synchronized( getScene().getSceneLock() )
            {
                Point2d loc = e.getLocation();
                Point2d cloc = selectedComp.getLocation();

                cloc.x += loc.x - oldloc.x; 
                cloc.y += loc.y - oldloc.y; 

                selectedComp.move( cloc );
            }
            getScene().redraw();
        }
        oldloc = e.getLocation();
    }

    public void mouseMoved( SceneMouseEvent e )
    { 
        selectedComp = getComponentAt( compType, e.getLocation() );
        getScene().redraw(); 
        oldloc = e.getLocation();
    }

//------------------------------------------------------------------------------
//    Paint Methods 
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        if ( selectedComp != null )
            selectedComp.paintHl( g );
    }
}
