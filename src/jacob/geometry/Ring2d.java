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

public class Ring2d extends Shape2d
{
    protected double x, y;
 
    protected double r1, r2;

    /* 
     *  polygon representing the ring -
     *  this is a hack, but we need it (in JDK1.1)
     *  in order to draw the filled ring
     */

    private double xpoints[];
  
    private double ypoints[];  

    private int npoints = 0;

    private double arcStep = 0; 
    
//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Ring2d() 
    {
//REMIND: make arc step a variable
        initRingPoly( 10 );
    }

    public Ring2d( double x, double y, double r1, double r2 )
    {
        initRingPoly( 10 );
        set( x, y, r1, r2 );
    }

//------------------------------------------------------------------------------
//    Ring Geometry Methods
//------------------------------------------------------------------------------

    public void set( double x, double y, double r1, double r2 )
    {
        this.x = x;
        this.y = y;
        this.r1 = Math.max( r1, r2 );
        this.r2 = Math.min( r1, r2 );
        setBounds( x - this.r1, y - this.r1, 2 * this.r1, 2 * this.r1 );
        constructRingPoly();
    }

    public void setRadius1( double r )
    {
        if ( r <= r2 ) return;
        r1 = r;
        setBounds( x - r1, y - r1, 2 * r1, 2 * r1 );
        constructRingPoly();
    }

    public void setRadius2( double r )
    {
        if ( r >= r1 ) return;
        r2 = r;
        constructRingPoly();
    }

    public double getRadius1()
    {
        return r1;
    }

    public double getRadius2()
    {
        return r2;
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

        /* 
         * we have to construct (instead of moveing)
         * the polygon every time a move (scale or rotate) is
         * caled, because of numerical errors
         */  
        constructRingPoly();
    }

    public void scale( double scale )
    {
        r1 = r1 * scale;
        r2 = r2 * scale;
        setBounds( x - r1, y - r1, 2 * r1, 2 * r1 );
        constructRingPoly();
    }

    public void rotate( Point2d pivot, double angle )
    {
        double cosf = Math.cos( angle );
        double sinf = Math.sin( angle );

        double x0 = x - pivot.x;
        double y0 = y - pivot.y;

        double oldx = x;
        double oldy = y;

        x =   x0 * cosf + y0 * sinf + pivot.x;
        y = - x0 * sinf + y0 * cosf + pivot.y;
        setBounds( x - r1, y - r1, 2 * r1, 2 * r1 );
        constructRingPoly();
    }

    public Point2d getCenter()
    {
        return new Point2d( x, y );
    }

    public double getBorderDistance( Point2d p )
    {
        double d =  p.distance( x, y );
        return Math.min( Math.abs( d - r1 ), Math.abs( d - r2 ) );
    }

    public boolean contains( Point2d p )
    {
        if ( bounds.contains( p ) )
        {
            double d = p.distance2( x, y );

            if ( d <= r1*r1 && d >= r2 * r2 ) return true;
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

        if ( d <= r1 && d >= r2 ) return p;

        double nx; 
        double ny;

        if ( d > r2 ) 
        {
            nx = x + r1 * (dx / d);
            ny = y + r1 * (dy / d);
        }
        else
        {
            nx = x + r2 * (dx / d);
            ny = y + r2 * (dy / d);
        }
        return new Point2d( nx, ny ); 
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void draw( Graphics2d g )
    {
        g.drawOval( x - r1, y - r1, 2 * r1, 2 * r1 ); 
        g.drawOval( x - r2, y - r2, 2 * r2, 2 * r2 ); 
    }

    public void fill( Graphics2d g )
    {
        g.fillPolygon( xpoints, ypoints, npoints ); 
    }

//------------------------------------------------------------------------------
//    Data R/W Methods 
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        set( data.getDoubleAttribute( "x" ),
             data.getDoubleAttribute( "y" ),
             data.getDoubleAttribute( "r1" ),
             data.getDoubleAttribute( "r2" ) ); 
    }

    public void writeData( DataElement data )
    {
        DataElement cdata = data.newElement( "ring" );
        cdata.setAttribute( "x", x );
        cdata.setAttribute( "y", y );
        cdata.setAttribute( "r1", r1 );
        cdata.setAttribute( "r2", r2 );
    }

//------------------------------------------------------------------------------
//    Ring Polygon Construction Methods 
//------------------------------------------------------------------------------

    protected void initRingPoly( int step )
    {
        arcStep = step * Math.PI / 180.0;
        npoints = (360 / step + 1) * 2 + 1;
        xpoints = new double[ npoints ];
        ypoints = new double[ npoints ];
    }

    protected void constructRingPoly()
    {
        int i = 0;

        for ( double n = 0; n <= 2 * Math.PI; n += arcStep )
        {
            xpoints[i] = x + r1 * Math.cos( n );
            ypoints[i] = y + r1 * Math.sin( n );
            i++;
        }
                         
        xpoints[i] = x + r2;
        ypoints[i] = y;
        i++;
 
        for ( double n = 0; n <= 2 * Math.PI; n += arcStep )
        {
            xpoints[i] = x + r2 * Math.cos( n );
            ypoints[i] = y + r2 * Math.sin( n ); 
            i++;
        }
    }
}
