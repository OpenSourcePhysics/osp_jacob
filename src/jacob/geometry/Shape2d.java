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

package jacob.geometry;

import java.awt.Graphics;
import java.awt.Color;

import jacob.scene.*;
import jacob.data.*;

public abstract class Shape2d
    implements Saveable
{

    protected Bounds2d bounds = new Bounds2d();

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Shape2d() {}

//------------------------------------------------------------------------------
//    Shape Bounds Methods
//------------------------------------------------------------------------------

    public Bounds2d getBounds()
    {
        return new Bounds2d( bounds );
    }

    protected void setBounds( Bounds2d nb )
    {
        setBounds( nb.x, nb.y, nb.width, nb.height );
    }

    protected void setBounds( double x, double y, double width, double height )
    {
        bounds.x = x;
        bounds.y = y;
        bounds.width  = width;
        bounds.height = height;
    }

//------------------------------------------------------------------------------
//    Shape Geometry Methods
//------------------------------------------------------------------------------

    public abstract void move( Point2d p );

    public abstract void scale( double scale );

    public abstract void rotate( Point2d pivot, double angle );

    public abstract Point2d getCenter();

    public double getCenterDistance( Point2d p )
    {
        return getCenter().distance( p );
    }

    public abstract double getBorderDistance( Point2d p );

    public abstract boolean contains( Point2d p );

    public abstract Point2d getConfinedPoint( Point2d p );

    public abstract Point2d getConfinedPoint( Point2d oldp, Point2d newp );

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public abstract void draw( Graphics2d g );

    public abstract void fill( Graphics2d g );
}
