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

import java.util.Vector;
import java.util.Enumeration;
import java.awt.Graphics;

import jacob.geometry.*;
import jacob.property.*;
import jacob.data.*;

public abstract class SceneContainer extends SceneComponent
{

    private Vector components = new Vector();

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SceneContainer( Scene scene, Shape2d shape )
    {
        super( scene, shape );
    }

//------------------------------------------------------------------------------
//    Delete Methods
//------------------------------------------------------------------------------

    public void delete()
    {
        super.delete();
        while( components.size() > 0 )
        {
            SceneComponent c = (SceneComponent) components.elementAt( 0 );
            c.delete();
        }
    }

//------------------------------------------------------------------------------
//    Children Methods
//------------------------------------------------------------------------------

    public void addChild( SceneComponent c )
    {
        if ( c.getParent() != null )
            c.getParent().removeChild( c );

        components.addElement( c );
        c.setParent( this );
    }

    public void removeChild( SceneComponent c )
    {
        c = (SceneComponent) c;

        components.removeElement( c );
        c.setParent( null );
    }

    public int countChildren()
    {
        return components.size();
    }

    public SceneComponent getChildAt( int index )
    {
        return (SceneComponent) components.elementAt( index );
    }

    public SceneIterator createIterator()
    {
        return new SceneContainerIterator( this );
    }

    public void correctChildLocation( SceneComponent child,
                                      Point2d oldl, Point2d newl )
    {
    }

    protected void moveChildren( double xoff, double yoff )
    {
		double integrationStep = getScene().getProperties().integrationStep.getValue();
		String inttype = getScene().getProperties().integratorType.getValue();

        for ( int i = 0; i < components.size(); i++ )
        {
            SceneComponent c = (SceneComponent) components.elementAt( i );

            Point2d cloc = c.getLocation();
            c.move( new Point2d( cloc.x + xoff, cloc.y + yoff ), true );

//FIX ME - magnetic force on object movement (works only in test integrator)
            if (c instanceof Particle && inttype.equals("test"))
            {
            	((Particle) c).setVx(((Particle) c).getVx()+xoff/integrationStep);
            	((Particle) c).setVy(((Particle) c).getVy()+yoff/integrationStep);
			}

        }
    }

    protected void scaleChildren( double scale )
    {
        Point2d loc = getLocation();
        for ( int i = 0; i < components.size(); i++ )
        {
            SceneComponent c = (SceneComponent) components.elementAt( i );
            Point2d cloc = c.getLocation();

            c.move( new Point2d( (cloc.x - loc.x) * scale + loc.x,
                                (cloc.y - loc.y) * scale + loc.y ), true );
            c.scale( scale, true );
        }
    }

    protected void rotateChildren( Point2d pivot, double angle )
    {
        for ( int i = 0; i < components.size(); i++ )
        {
            SceneComponent c = (SceneComponent) components.elementAt( i );
            c.rotate( pivot, angle, true );
        }
    }

//------------------------------------------------------------------------------
//    Container Geometry Methods
//------------------------------------------------------------------------------

    public void move( Point2d p, boolean silent )
    {
        Point2d loc, oldloc;

        oldloc = getLocation();
        super.move( p, silent );
        loc = getLocation();
        moveChildren( loc.x - oldloc.x,
                      loc.y - oldloc.y );
    }

    public void scale( double scale, boolean silent )
    {
        super.scale( scale, silent );
        scaleChildren( scale );
    }

    public void rotate( Point2d pivot, double angle, boolean silent )
    {
        super.rotate( pivot, angle, silent );
        rotateChildren( pivot, angle );
    }

//------------------------------------------------------------------------------
//    Update Property Methods
//------------------------------------------------------------------------------

    public void updateProperty( UpdatePropertyEvent pe )
    {
        super.updateProperty( pe );
        for( int i = 0; i < components.size(); i++ )
        {
            SceneComponent c = (SceneComponent)components.elementAt( i );
            c.updateProperty( pe );
        }
    }

//------------------------------------------------------------------------------
//    Paint Children Methods
//------------------------------------------------------------------------------

    protected void paintChildrenFg( SceneGraphics g )
    {
        for( int i = 0; i < components.size(); i++ )
        {
            SceneComponent c = (SceneComponent) components.elementAt( i );
            c.paintFg( g );
        }
    }

    protected void paintChildrenBg( SceneGraphics g )
    {
        for( int i = 0; i < components.size(); i++ )
        {
            SceneComponent c = (SceneComponent) components.elementAt( i );
            c.paintBg( g );
        }
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    protected void readChildrenData( DataElement data )
        throws DataParseException
    {
        DataElement csdata = data.getOptElement( "components" );

        if ( csdata == null ) return;

        Enumeration enumm  = csdata.getElements();

        while( enumm.hasMoreElements() )
        {
            DataElement cdata = (DataElement)enumm.nextElement();
            try
            {
                SceneComponent c = ComponentFactory.createComponent( getScene(),
                                       cdata.getName() );
                c.readData( cdata );
                addChild( c );
            }
            catch ( UnknownComponentException ex )
            {
                throw new DataParseException( "unknown component: " +
                                              cdata.getName() );
            }
        }
    }

    protected void writeChildrenData( DataElement data )
    {
        if ( components.size() == 0 )
            return;

        DataElement cdata = data.newElement( "components" );

        for ( int i = 0; i < components.size(); i++ )
        {
            SceneComponent c = (SceneComponent) components.elementAt( i );
            c.writeData( cdata );
        }
    }
}



//==============================================================================
//    SceneContainerIterator Class
//==============================================================================



class SceneContainerIterator implements SceneIterator
{

//------------------------------------------------------------------------------
//    Iterator Fields
//------------------------------------------------------------------------------

    private int childnum;

    private SceneComponent component;

    private SceneIterator iterator;

    private SceneContainer parent;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SceneContainerIterator( SceneContainer parent )
    {
        this.parent = parent;
        first();
    }

//------------------------------------------------------------------------------
//    Iteration Methods
//------------------------------------------------------------------------------

    public void first()
    {
        childnum  = 0;
        component = parent;
        iterator  = null;
    }

    public void next()
    {
        if ( iterator == null || iterator.isDone() )
        {
            if ( childnum >= parent.countChildren() )
            {
                component = null;
                return;
            }
            iterator  = parent.getChildAt( childnum++ ).createIterator();
        }
        component = iterator.getCurrentComponent();
        iterator.next();
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
