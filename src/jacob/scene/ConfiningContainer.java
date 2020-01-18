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
import java.awt.Graphics;

import jacob.geometry.*;
import jacob.data.*;

public abstract class ConfiningContainer extends SceneContainer
{

     private boolean closed = true;

     private Vector neighbors = new Vector();

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public ConfiningContainer( Scene scene, Shape2d shape )
    {
        super( scene, shape );
    }

//------------------------------------------------------------------------------
//    Delete Methods
//------------------------------------------------------------------------------

    public void delete()
    {
        super.delete();
        removeAllNeighbors();
    }

//------------------------------------------------------------------------------
//    Children Methods 
//------------------------------------------------------------------------------

    public void correctChildLocation( SceneComponent child, 
                                      Point2d oldl, Point2d newl )
    {
        Point2d conl = getConfinedLocation( oldl, newl );

        if ( conl != newl ) 
        {
            SceneComponent c; 
            child.getShape().move( conl );
            c = getNeighborUnder( child );
            if ( c != null )
            {
                removeChild( child );
                c.addChild( child );
            }
        }
    }

//------------------------------------------------------------------------------
//    Geometry Methods 
//------------------------------------------------------------------------------

    public void move( Point2d p, boolean silent )
    {
        super.move( p, silent );
        updateNeighbors();
    }

    public void scale( double scale, boolean silent )
    {
        super.scale( scale, silent );
        updateNeighbors();
    }

    public void rotate( Point2d pivot, double angle, boolean silent )
    {
        super.rotate( pivot, angle, silent );
        updateNeighbors();
    }

//------------------------------------------------------------------------------
//    Neighbors Methods 
//------------------------------------------------------------------------------

    public boolean isClosed()
    {
        return closed;
    }

    public void setClosed( boolean closed )
    {
        this.closed = closed;
	updateNeighbors();
    }

    public void addNeighbor( ConfiningContainer c )
    {
        neighbors.addElement( c );
    }

    public void removeNeighbor( ConfiningContainer c )
    {
        neighbors.removeElement( c );
    }

    public int countNeighbors()
    {
        return neighbors.size();
    }

    public ConfiningContainer getNeighborAt( int index )
    {
        return (ConfiningContainer)neighbors.elementAt( index );
    }

    public void removeAllNeighbors()
    {
        for ( int i = 0; i < neighbors.size(); i++ )
        {
            ConfiningContainer c = (ConfiningContainer) 
                                       neighbors.elementAt( i );
            c.removeNeighbor( this );
        }
        neighbors.removeAllElements();
    }

    public void updateNeighbors()
    {
        removeAllNeighbors();

        if ( !isClosed() )
        {
            SceneIterator iterator = getScene().createIterator();
            while( !iterator.isDone() ) 
            {
                SceneComponent c = iterator.getCurrentComponent();
                if ( c instanceof ConfiningContainer &&
                     !c.equals( this ) )
                {
                    ConfiningContainer cc = (ConfiningContainer) c;
                    if ( !cc.isClosed() && 
                         cc.getBounds().intersects( getBounds() ) )
                    {
                        addNeighbor( cc );
                        cc.addNeighbor( this );
                    }
                }
                iterator.next();
            } 
        }
    }

    public ConfiningContainer getNeighborUnder( SceneComponent child )
    {
        Point2d cloc = child.getLocation();

        for ( int i = 0; i < neighbors.size(); i++ )
        {
             ConfiningContainer cc = (ConfiningContainer) 
                                       neighbors.elementAt( i );
             if ( child != cc && 
                  child != cc.getParent() && 
                  cc.contains( cloc ) )
             {
//FIXME!!: this is not enough - we should check the entire tree
//         (now we can move a container into its child's child container)
                 return cc;
             }
        }
        return null; 
    }

//------------------------------------------------------------------------------
//    Data R/W Methods 
//------------------------------------------------------------------------------

    protected void readConfiningContData( DataElement data )
        throws DataParseException
    {
        setClosed( data.getBooleanAttribute( "closed", true ) );
    }

    protected void writeConfiningContData( DataElement data )
    {
        data.setAttribute( "closed", closed );
    }
}
