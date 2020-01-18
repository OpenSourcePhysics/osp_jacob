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

public class Bounds2d 
{

    public double x, y;

    public double width, height;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Bounds2d() 
    {
    	this( Double.MAX_VALUE, Double.MAX_VALUE, 
              Double.MIN_VALUE, Double.MIN_VALUE );
    }

    public Bounds2d( Bounds2d r ) 
    {
    	this( r.x, r.y, r.width, r.height );
    }

    public Bounds2d( double  x, double y, double width, double height )
    {
	this.x = x;
	this.y = y;
	this.width  = width;
	this.height = height;
    }

//------------------------------------------------------------------------------
//    Geometry Methods 
//------------------------------------------------------------------------------

    public boolean contains( Point2d p ) 
    {
	return contains( p.x, p.y );
    }

    public boolean contains( double x, double y )
    {
	return ( x >= this.x )  && 
               ( y >= this.y )  && 
               ( x - this.x <= this.width ) && 
               ( y - this.y <= this.height );
    }

    public boolean intersects( Bounds2d r ) 
    {
	return !( ( r.x + r.width  <= x ) ||
		  ( r.y + r.height <= y ) ||
		  ( r.x >= x + width )    ||
		  ( r.y >= y + height ) );
    }

    public void add( double nx, double ny ) 
    {
        if ( isNull() )
        {
            x = nx;
            y = ny;
            return;
        }
	double x1 = Math.min( x, nx );
	double x2 = Math.max( x + width, nx );
	double y1 = Math.min( y, ny );
	double y2 = Math.max( y + height, ny );
	x = x1;
	y = y1;
	width  = x2 - x1;
	height = y2 - y1;
    }

    public void add( Point2d pt )  
    {
	add( pt.x, pt.y );
    }

//REMIND: this is a dirty hack, but we need this in order to
//        make add work 
    public boolean isNull()
    {
        if ( x == Double.MAX_VALUE && 
             y == Double.MAX_VALUE &&
             width  == Double.MIN_VALUE && 
             height == Double.MIN_VALUE 
           )
           return true;
        else
           return false;
    }
}
