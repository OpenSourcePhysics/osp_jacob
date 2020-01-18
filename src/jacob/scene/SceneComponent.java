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

import java.awt.Graphics;

import jacob.main.*;
import jacob.system.*;
import jacob.scene.modifiers.*;
import jacob.geometry.*;
import jacob.property.*;
import jacob.data.*;

public abstract class SceneComponent
    implements Saveable
{

    private Shape2d shape;

    private Modifiers modifiers;

    private SceneContainer parent;

    private Scene scene;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SceneComponent( Scene scene, Shape2d shape )
    {
        this.scene = scene;
        setShape( shape );
        parent = null;
        modifiers = null;
    }

//------------------------------------------------------------------------------
//    Delete Methods
//------------------------------------------------------------------------------

    public void delete() 
    {
        if ( parent != null )
            parent.removeChild( this );
        if ( modifiers != null ) 
            modifiers.componentDeleated();
        scene = null;
        shape = null;
    }

//------------------------------------------------------------------------------
//    Children Methods
//------------------------------------------------------------------------------

    public void addChild( SceneComponent c )
    {
        throw new InternalException( "illegal operation" );
    }

    public void removeChild( SceneComponent c )
    {
        throw new InternalException( "illegal operation" );
    }

    public int countChildren()
    {
        return 0;
    }

    public SceneComponent getChildAt( int index )
    {
        throw new InternalException( "illegal operation" );
    }

    public SceneIterator createIterator()
    {
        return new SceneComponentIterator( this );
    }

//------------------------------------------------------------------------------
//    Component Geometry Methods
//------------------------------------------------------------------------------

    public void move( Point2d p )
    {
        move( p, false );
    }

    public void move( Point2d p, boolean silent )
    {
        Point2d oldloc = shape.getCenter();
        shape.move( p );
        if ( !silent && parent != null ) 
            parent.correctChildLocation( this, oldloc, shape.getCenter() );
        if ( modifiers != null )
            modifiers.componentMoved( p );
    }

    public void scale( double scale )
    {
        scale( scale, false );
    }

    public void scale( double scale, boolean silent )
    {
        Point2d oldloc = shape.getCenter();
        shape.scale( scale );
        if ( !silent && parent != null )
//REMIND: is there a case of component scaling where location changes?
            parent.correctChildLocation( this, oldloc, shape.getCenter() );
        if ( modifiers != null )
            modifiers.componentScaled( scale );
    }

    public void rotate( double angle )
    {
        rotate( getCenter(), angle ); 
    }

    public void rotate( Point2d pivot, double angle )
    {
        rotate( pivot, angle, false );
    }

    public void rotate( Point2d pivot, double angle, boolean silent )
    {
        Point2d oldloc = shape.getCenter();
        shape.rotate( pivot, angle );
        if ( !silent && parent != null ) 
            parent.correctChildLocation( this, oldloc, shape.getCenter() );
        if ( modifiers != null )
            modifiers.componentRotated( pivot, angle );
    }

    public Bounds2d getBounds()
    {
        return shape.getBounds();
    }

    public Point2d getCenter()
    {
        return shape.getCenter(); 
    }

    public Point2d getLocation()
    {
        return shape.getCenter();
    }

    public double getCenterDistance( Point2d p )
    {
        return shape.getCenterDistance( p );
    }

    public double getBorderDistance( Point2d p )
    {
        return shape.getBorderDistance( p );
    }

    public boolean contains( Point2d p )
    {
        return shape.contains( p );
    }

    public Point2d getConfinedLocation( Point2d loc )
    {                                          
        return shape.getConfinedPoint( loc );
    }

    public Point2d getConfinedLocation( Point2d oldl, Point2d newl )
    {                                          
        return shape.getConfinedPoint( oldl, newl );
    }

    public Shape2d getShape()
    {
        return shape;
    }

    protected void setShape( Shape2d shape )
    {
        this.shape = shape;
    }

//------------------------------------------------------------------------------
//    Scene Methods 
//------------------------------------------------------------------------------

    public Scene getScene()
    {
        return scene;
    }

    public Properties getProperties()
    {
        return scene.getProperties();
    }

//------------------------------------------------------------------------------
//    Component Container Methods 
//------------------------------------------------------------------------------

    public void setParent( SceneContainer parent )
    {
        this.parent = parent;
    }

    public SceneContainer getParent()
    {
        return parent;
    }

//------------------------------------------------------------------------------
//    Modifiers Methods 
//------------------------------------------------------------------------------

    public boolean hasModifiers()
    {
        if ( modifiers == null ) 
            return false;
        else
            return true;
    }

    public Modifiers getModifiers()
    {
        return modifiers;
    }

    public void createModifiers()
    {
        if ( modifiers == null )
            modifiers = new Modifiers( this );
    }

//------------------------------------------------------------------------------
//    Property Update Methods 
//------------------------------------------------------------------------------

    public void updateProperty( UpdatePropertyEvent pe ) 
    {
        if ( modifiers != null )
            modifiers.updateProperty( pe );
    }

//------------------------------------------------------------------------------
//    Paint Methods 
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g ) {}

    public void paintBg( SceneGraphics g ) {} 

    public void paintHl( SceneGraphics g ) {}

    protected void paintModifiersFg( SceneGraphics g )
    {
         if ( modifiers != null )
            modifiers.paintFg( g );
    }

    protected void paintModifiersBg( SceneGraphics g )
    {
         if ( modifiers != null )
            modifiers.paintFg( g );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods 
//------------------------------------------------------------------------------

    protected void readShapeData( DataElement data )
        throws DataParseException
    {
        DataElement sdata = data.getElement( "shape" ).getElementAt( 0 );

        try
        {
            Shape2d shape = ShapeFactory.createShape( sdata.getName() );
            shape.readData( sdata );
            setShape( shape );
        }
        catch ( UnknownShapeException ex )
        {
            throw new DataParseException( "unknown shape: " + 
                                          sdata.getName() );
        }
    }

    protected void writeShapeData( DataElement data )
    {
        DataElement sdata = data.newElement( "shape" );

        shape.writeData( sdata );
    }

    protected void readModifiersData( DataElement data )
        throws DataParseException
    {
        DataElement mdata = data.getOptElement( "modifiers" );

        if ( mdata != null )
        {
            modifiers = new Modifiers( this );
            modifiers.readData( mdata );
        }
    }

    protected void writeModifiersData( DataElement data )
    {
        if ( modifiers != null )
            modifiers.writeData( data );
    }
}


//==============================================================================
//    SceneComponentIterator Class
//==============================================================================


class SceneComponentIterator implements SceneIterator
{
    private SceneComponent component;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SceneComponentIterator( SceneComponent component ) 
    {
        this.component = component;
    }

//------------------------------------------------------------------------------
//    Iteration Methods
//------------------------------------------------------------------------------

    public void first() {} 

    public void next() 
    {
        component = null;
    }

    public boolean isDone()
    {
        if ( component == null )
            return true;
        else
            return false;
    }

    public SceneComponent getCurrentComponent()
    {
        return component;
    }
}
