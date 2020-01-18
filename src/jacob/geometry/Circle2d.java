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

public class Circle2d extends Shape2d
{
    protected double x, y;

    protected double r;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Circle2d() {}

    public Circle2d( double x, double y, double r )
    {
        set( x, y, r );
    }

//------------------------------------------------------------------------------
//    Circle Geometry Methods
//------------------------------------------------------------------------------

    public void set( double x, double y, double r )
    {
        this.x = x;
        this.y = y;
        this.r = r;
        setBounds( x - r, y - r, 2 * r, 2 * r );
    }

    public void setRadius( double r )
    {
        this.r = r;
        setBounds( x - r, y - r, 2 * r, 2 * r );
    }

    public double getRadius()
    {
        return r;
    }

//------------------------------------------------------------------------------
//    Shape Geometry Methods
//------------------------------------------------------------------------------

    public void move( Point2d p )
    {
        double xoff = p.x - x;
        double yoff = p.y - y;

        x = p.x;
        y = p.y;
        setBounds( bounds.x + xoff, bounds.y + yoff,
                   bounds.width, bounds.height );
    }

    public void scale( double scale )
    {
        r = r * scale;
        setBounds( x - r, y - r, 2 * r, 2 * r );
    }

    public void rotate( Point2d pivot, double angle )
    {
//REMIND: the rotation should be done in some other class
//        maybe Point2d
        double cosf = Math.cos( angle );
        double sinf = Math.sin( angle );

        double x0 = x - pivot.x;
        double y0 = y - pivot.y;

        x =   x0 * cosf + y0 * sinf + pivot.x;
        y = - x0 * sinf + y0 * cosf + pivot.y;
        setBounds( x - r, y - r, 2 * r, 2 * r );
    }

    public Point2d getCenter()
    {
        return new Point2d( x, y );
    }

    public double getBorderDistance( Point2d p )
    {
        return Math.abs( p.distance( x, y ) - r );
    }

    public boolean contains( Point2d p )
    {
        if ( bounds.contains( p ) )
        {
            if ( p.distance2( x, y ) <= r*r ) return true;
        }
        return false;
    }

    public Point2d getConfinedPoint( Point2d oldp, Point2d newp )
    {
        return getConfinedPoint( newp );
    }

    public Point2d getConfinedPoint( Point2d p )
    {
        double dx = p.x - x;
        double dy = p.y - y;

        double d = Math.sqrt( dx * dx + dy * dy );

        if ( d <= r ) return p;

        double nx = x + r * (dx / d);
        double ny = y + r * (dy / d);

/* vvv it should be FIXED
        if ( d >= r ) return p;

        double nx = x - r * (dx / d);
        double ny = y - r * (dy / d);
*/
        return new Point2d( nx, ny );
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void draw( Graphics2d g )
    {
        g.drawOval( x - r, y - r, 2 * r, 2 * r );
    }

    public void fill( Graphics2d g )
    {
        g.fillOval( x - r, y - r, 2 * r, 2 * r );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        set( data.getDoubleAttribute( "x" ),
             data.getDoubleAttribute( "y" ),
             data.getDoubleAttribute( "r" ) );
    }

    public void writeData( DataElement data )
    {
        DataElement cdata = data.newElement( "circle" );
        cdata.setAttribute( "x", x );
        cdata.setAttribute( "y", y );
        cdata.setAttribute( "r", r );
    }
}
