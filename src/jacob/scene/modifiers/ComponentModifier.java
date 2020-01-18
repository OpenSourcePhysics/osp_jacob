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

package jacob.scene.modifiers;

import jacob.scene.*;
import jacob.main.*;
import jacob.system.*;
import jacob.property.*;
import jacob.geometry.*;
import jacob.data.*;

public abstract class ComponentModifier
    implements Saveable
{
    private Modifiers parent;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public ComponentModifier() 
    {
        parent = null; 
    }

//------------------------------------------------------------------------------
//    Type Methods
//------------------------------------------------------------------------------

    public abstract String getType();

//------------------------------------------------------------------------------
//    Status Methods
//------------------------------------------------------------------------------

    public void added() {}

    public void removed() {}

//------------------------------------------------------------------------------
//    Component Modification Methods
//------------------------------------------------------------------------------

    public void preIntegrationModify( long time ) {}

    public void postIntegrationModify( long time ) {}

    public void componentDeleated() {}

    public void componentMoved( Point2d p ) {}

    public void componentScaled( double scale ) {}

    public void componentRotated( Point2d pivot, double angle ) {}

//------------------------------------------------------------------------------
//    Visual Control Methods
//------------------------------------------------------------------------------

    public int countHotspots()
    {
        return 0;
    }

    public Point2d getHotspotRelAt( int index )
    {
        throw new InternalException( "illegal operation" );
    }

    public Point2d getHotspotAbsAt( int index )
    {
        throw new InternalException( "illegal operation" );
    }

    public Point2d removeHotspotAt( int index )
    {
        throw new InternalException( "illegal operation" );
    }

    public void moveHotspotRelAt( int index, Point2d p )
    {
        throw new InternalException( "illegal operation" );
    }

    public void moveHotspotAbsAt( int index, Point2d p )
    {
        throw new InternalException( "illegal operation" );
    }

//------------------------------------------------------------------------------
//    Parent Methods
//------------------------------------------------------------------------------

    public void setParent( Modifiers parent )  
    {
        this.parent = parent;
    }

    public Modifiers getParent()  
    {
        return parent;
    }

    public SceneComponent getComponent()
    {
        return parent.getComponent();
    }
 
    public Scene getScene()
    {
        return parent.getScene();
    }
 
//------------------------------------------------------------------------------
//    Property Methods
//------------------------------------------------------------------------------
 
    public void updateProperty( UpdatePropertyEvent pe ) {}

    public Properties getProperties() 
    {
        return parent.getComponent().getProperties();
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g ) {} 
   
    public void paintBg( SceneGraphics g ) {}

    public void paintHl( SceneGraphics g ) {} 
}
