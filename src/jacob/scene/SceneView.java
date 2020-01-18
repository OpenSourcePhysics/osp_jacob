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

package jacob.scene;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

import jacob.system.*;
import jacob.property.*;
import jacob.geometry.*;
import jacob.data.*;

public class SceneView extends SceneContainer
{

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SceneView( Scene scene )
    {
        super( scene, new ViewShape() );
        updateShape();
    }

//------------------------------------------------------------------------------
//    Geometry Methods 
//------------------------------------------------------------------------------

    public void move( Point2d p, boolean force )
    {
        throw new InternalException( "illegal operation" );
    }

    public void scale( double scale, boolean force )
    {
        throw new InternalException( "illegal operation" );
    }

    public void rotate( Point2d pivot, double angle, boolean force )
    {
        throw new InternalException( "illegal operation" );
    }
   
    public void setShape( Shape2d shape )
    {
        if ( !(shape instanceof ViewShape) )
            throw new InternalException( "illegal shape" );
        else
            super.setShape( shape );
    }

    public void updateShape()
    {
        double scale = getProperties().sceneScale.getValue();
        ((ViewShape)getShape()).set( 0, 0, getScene().getSize().width / scale, 
                                     getScene().getSize().height / scale );
    }

//------------------------------------------------------------------------------
//    Update Property Methods 
//------------------------------------------------------------------------------

    public void updateProperty( UpdatePropertyEvent pe ) 
    {
        if ( pe.hasChanged( "sceneScale" ) )
            updateShape(); 
        
        super.updateProperty( pe );
    }

//------------------------------------------------------------------------------
//    Paint Methods 
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        paintChildrenFg( g );
        g.setColor( getProperties().sceneBg.getValue() );
        getShape().draw( g );
    }

    public void paintBg( SceneGraphics g )
    {
        g.setColor( getProperties().sceneBg.getValue() );
        getShape().fill( g );
        paintChildrenBg( g );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods 
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        readChildrenData( data );
    }

    public void writeData( DataElement data )
    {
        DataElement vdata = data.newElement( "view" );
        writeChildrenData( vdata );
    }
}


//==============================================================================
//    ViewShape Class
//==============================================================================


class ViewShape extends Shape2d
{
    private double x, y, width, height;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public ViewShape() {}

//------------------------------------------------------------------------------
//    View Shape Methods
//------------------------------------------------------------------------------

    public void set( double x, double y, double width, double height )
    {
        this.x = x;
        this.y = y;
        this.width  = width;
        this.height = height;
    }

//------------------------------------------------------------------------------
//    Shape2d Methods
//------------------------------------------------------------------------------

    public void move( Point2d p ) {}

    public void scale( double scale ) {}

    public void rotate( Point2d pivot, double angle ) {}

    public Point2d getCenter()
    {
        return new Point2d( x + width / 2, y + height / 2 ); 
    }

    public double getBorderDistance( Point2d p )
    {
        return Double.MAX_VALUE;
    }

    public boolean contains( Point2d p )
    {
        return true;
    }

    public Point2d getConfinedPoint( Point2d p )
    {
        return p;
    }

    public Point2d getConfinedPoint( Point2d oldp, Point2d newp )
    {
        return newp;
    }

    public void draw( Graphics2d g ) 
    {
        Graphics gg = g.getGraphics();
        double s = g.getScale();

        int h = (int)(height * s) - 1;
        int w = (int)(width * s)  - 1;

        Color top    = g.getColor().brighter();
        Color bottom = g.getColor().darker();

        for ( int i = 0; i < 2; i++ )
        {
            gg.setColor( top );
            gg.drawLine( i, i, w - i, i );
            gg.drawLine( i, i, i, h - i );
            gg.setColor( bottom );
            gg.drawLine( i, h - i, w - i, h - i );
            gg.drawLine( w - i, h - i, w - i, i );
        }
    }

    public void fill( Graphics2d g ) 
    {
        g.fillRect( x, y, width, height );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException {};

    public void writeData( DataElement data ) {}
}
