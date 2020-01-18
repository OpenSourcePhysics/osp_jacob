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

public class SetZeroAction extends SceneAction 
{

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SetZeroAction( String[] args ) 
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
        Point2d loc = e.getLocation();

        getProperties().zeroX.setValue( loc.x );
        getProperties().zeroY.setValue( loc.y );

        getScene().getPropertyMgr().notifyObservers( this );
        getScene().redraw();
    }

//------------------------------------------------------------------------------
//    Paint Methods 
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        Graphics gg = g.getGraphics();
        double scale = g.getScale();
        int zx = (int)(getProperties().zeroX.getValue() * scale);
        int zy = (int)(getProperties().zeroY.getValue() * scale);

        gg.setColor( getProperties().zeroFg.getValue() );
        gg.drawOval( zx - 4, zy - 4, 8, 8 );  
        gg.drawLine( zx - 4, zy - 4, zx + 4, zy + 4 );  
        gg.drawLine( zx - 4, zy + 4, zx + 4, zy - 4 );  
    }
}
