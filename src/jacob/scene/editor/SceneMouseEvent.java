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

import java.awt.event.MouseEvent;

import jacob.scene.*;
import jacob.geometry.*;

public class SceneMouseEvent
{
    protected MouseEvent mouseEvent;

    protected Scene scene;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SceneMouseEvent( Scene scene, MouseEvent e )
    {
        this.scene = scene;
        mouseEvent = e;
    }

//------------------------------------------------------------------------------
//    Scene Methods
//------------------------------------------------------------------------------

    public double getScale()
    {
        return scene.getProperties().sceneScale.getValue();
    }

    public double getGridSize()
    {
        return scene.getProperties().gridSize.getValue();
    }

    public boolean snapToGrid()
    {
        return scene.getProperties().snapToGrid.getValue();
    }

//------------------------------------------------------------------------------
//    Mouse Event Methods
//------------------------------------------------------------------------------

    public MouseEvent getMouseEvent()
    {
        return mouseEvent;
    }    

    public boolean isMetaDown()
    {
        return mouseEvent.isMetaDown();
    }

    public Point2d getLocation()
    {
        double scale = getScale();
        double x = mouseEvent.getX() / scale;
        double y = mouseEvent.getY() / scale;

        return new Point2d( x, y );
    }

    public Point2d getSnappedLocation()
    {
        double scale = getScale();
        double x = mouseEvent.getX() / scale;
        double y = mouseEvent.getY() / scale;

        if ( snapToGrid() )
        {
            double gsize = getGridSize();
            x = Math.round( x / gsize ) * gsize;
            y = Math.round( y / gsize ) * gsize;
        } 

        return new Point2d( x, y );
    }
}
