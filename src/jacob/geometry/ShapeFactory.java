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

public final class ShapeFactory
{
    private ShapeFactory() {}

    public static Shape2d createShape( String type )
        throws UnknownShapeException
    {
         type = type.toLowerCase();

         if ( type.equals( "circle" ) )
             return new Circle2d();
         else if ( type.equals( "polygon" ) )
             return new Polygon2d();
         else if ( type.equals( "ring" ) )
             return new Ring2d();
         else
             throw new UnknownShapeException( type ); 
    }
}
