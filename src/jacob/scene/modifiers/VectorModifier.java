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

package jacob.scene.modifiers;

import java.awt.Graphics;

import jacob.scene.*;
import jacob.system.*;
import jacob.geometry.*;

public abstract class VectorModifier extends ComponentModifier
{
    /* relative to the location of the component */
    private Point2d vpoint;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public VectorModifier() 
    {
        vpoint = new Point2d( 0, 0 );
    }

//------------------------------------------------------------------------------
//    Visual Control Methods
//------------------------------------------------------------------------------

    public int countHotspots()
    {
        return 1;
    }

    public Point2d getHotspotRelAt( int index )
    {
        if ( index == 0 )
            return new Point2d( vpoint );
        else
            throw new InternalException( "hotspot index out of range: " + 
                                         index + ">= 1" );
    }

    public Point2d getHotspotAbsAt( int index )
    {
        Point2d loc = getComponent().getLocation();

        Point2d vect = getHotspotRelAt( index );

        vect.x += loc.x;
        vect.y += loc.y;

        return vect;
    }

    public Point2d removeHotspotAt( int index )
    {
        throw new InternalException( "illegal operation" );
    }

    public void moveHotspotRelAt( int index, Point2d p )
    {
        if ( index != 0 )
           throw new InternalException( "hotspot index out of range: " +
                                         index + ">= 1" );

        Point2d loc = getComponent().getLocation();
 
        vpoint = new Point2d( p.x, p.y );
    }

    public void moveHotspotAbsAt( int index, Point2d p )
    {
        Point2d loc = getComponent().getLocation();

        moveHotspotRelAt( index, new Point2d( p.x - loc.x, p.y - loc.y ) );
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintHl( SceneGraphics g )
    {
        Graphics gg = g.getGraphics();
        double s = g.getScale();
        int toff = getProperties().touchOffset.getValue();
        Point2d loc = getComponent().getLocation();

        loc.x += vpoint.x;
        loc.y += vpoint.y;

        gg.setColor( getProperties().modifierHotspotHl.getValue() );
        gg.drawOval( (int)(loc.x * s) - toff, (int)(loc.y * s) - toff,
                     toff * 2, toff * 2 );                    
    }
}
