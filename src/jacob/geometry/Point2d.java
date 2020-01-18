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

public class Point2d 
{

    public double x, y;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Point2d()
    {
        this( 0, 0 );
    }

    public Point2d( Point2d p )
    {
        x = p.x;
        y = p.y;
    }

    public Point2d( double x, double y )
    {
        this.x = x;
        this.y = y;
    }

//------------------------------------------------------------------------------
//    Geometry Methods 
//------------------------------------------------------------------------------

    public double distance( Point2d p )
    {
        return distance( p.x, p.y );
    }
 
    public double distance( double px, double py )
    {
        return Math.sqrt( distance2( px, py ) );
    }

    public double distance2( Point2d p )
    {
        return distance2( p.x, p.y );
    }

    public double distance2( double px, double py )
    {
        return ((x - px) * (x - px) + (y - py) * (y - py)); 
    }

//REMIND: implement some more methods - like rotation and translation
//        and clean other classes doing this locally
}
