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
import java.awt.Color;

import jacob.system.*;
import jacob.property.*;
import jacob.geometry.*;
import jacob.data.*;

public class Element extends ConfiningContainer
{
    private String name;

    private Color fgcolor = null;

    private Color bgcolor = null;

    private boolean showForceArrows = true;

    private boolean showOpenBorder = true;

    private boolean showClosedBorder = true;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Element( Scene scene, Shape2d shape )
    {
        super( scene, shape );
        setName( scene.getFreeElementName() );
    }

//------------------------------------------------------------------------------
//    Attributes Methods
//------------------------------------------------------------------------------

    public boolean setName( String name )
    {
        if ( name == null || name.length() == 0 )
            return false;

        if ( getScene().getElement( name ) == null )
        {
            this.name = name;
            return true;
        }
        return false;
    }

    public String getName()
    {
        return name;
    }

    public void setForeground( Color color )
    {
        fgcolor = color;
    }

    public Color getForeground()
    {
        return fgcolor;
    }

    public void setBackground( Color color )
    {
        bgcolor = color;
    }

    public Color getBackground()
    {
        return bgcolor;
    }

    public void setShowForceArrows( boolean sfa )
    {
        showForceArrows = sfa;
    }

    public boolean getShowForceArrows()
    {
        return showForceArrows;
    }

    public void setShowOpenBorder( boolean sb )
    {
        showOpenBorder = sb;
    }

    public boolean getShowOpenBorder()
    {
        return showOpenBorder;
    }

    public void setShowClosedBorder( boolean sb )
    {
        showClosedBorder = sb;
    }

    public boolean getShowClosedBorder()
    {
        return showClosedBorder;
    }

//------------------------------------------------------------------------------
//    Childern Methods
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
            else
            {
                if ( child instanceof Particle )
                {
                    ((Particle)child).setOnBorder( true );
                    correctParticleSpeed( (Particle)child, oldl, conl );
                }
            }
        }
        else
        {
            if ( child instanceof Particle )
                ((Particle)child).setOnBorder( false );
        }
    }

    private void correctParticleSpeed( Particle p, Point2d oldl, Point2d newl )
    {
        double div = getProperties().borderSpeedDiv.getValue();

        if ( div == 0 )
            div = 1;

        p.setVx( (newl.x - oldl.x) / div );
        p.setVy( (newl.y - oldl.y) / div );
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        paintModifiersFg( g );

//---DEBUG START---

        paintDebug( g );

//---DEBUG END---

        if ( fgcolor == null )
            if ( isClosed() )
                g.setColor( getProperties().elementClosedFg.getValue() );
            else
                g.setColor( getProperties().elementOpenFg.getValue() );
        else
            g.setColor( fgcolor );

        if (  isClosed() && showClosedBorder ||
             !isClosed() && showOpenBorder ) getShape().draw( g );
        paintChildrenFg( g );
    }

    public void paintBg( SceneGraphics g )
    {
        paintModifiersBg( g );

        if ( bgcolor == null )
                g.setColor( getProperties().elementBg.getValue() );
        else
            g.setColor( bgcolor );

        getShape().fill( g );
        paintChildrenBg( g );
    }

    public void paintHl( SceneGraphics g )
    {
        g.setColor( getProperties().elementHl.getValue() );
        getShape().draw( g );
    }

//---DEBUG START---

    protected void paintDebug( SceneGraphics g )
    {
        Point2d loc = getLocation();
        String dmsg = "";

        if ( getProperties().debugBounds.getValue() )
        {
            Bounds2d bounds = getBounds();
            g.setColor( Color.black );
            g.drawRect( bounds.x, bounds.y, bounds.width, bounds.height );
        }
        if ( getProperties().debugParent.getValue() )
        {
            Point2d ploc = getParent().getLocation();

            g.setColor( Color.blue );
            g.drawLine( loc.x, loc.y, ploc.x, ploc.y );
        }
        if ( getProperties().debugNgbr.getValue() )
        {
            for ( int i = 0; i < countNeighbors(); i++ )
            {
                ConfiningContainer n;
                Point2d nloc;

                n = getNeighborAt( i );
                nloc = n.getLocation();
                g.setColor( Color.red );
                g.drawLine( loc.x, loc.y, nloc.x, nloc.y );
            }
        }
        if ( getProperties().debugName.getValue() )
        {
            dmsg += name;
        }
        if ( getProperties().debugChildCount.getValue() )
        {
            dmsg += "(" + countChildren() + ")";
        }

        if ( dmsg.length() > 0 )
        {
            g.setColor( Color.black );
            g.drawString( dmsg, loc.x, loc.y );
        }
    }

//---DEBUG END---

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
//FIXME!!: getFreeElementName in this case won't work if a child and its parent
//         being read have the same name - because the components are first
//         read (with their children) and then added to the scene -
//         getFreeElementName searches only the scene elements.

        if ( !setName( data.getStringAttribute( "name", null ) ) )
            setName( getScene().getFreeElementName() );

        fgcolor = data.getColorAttribute( "fgcolor", null );
        bgcolor = data.getColorAttribute( "bgcolor", null );
        showForceArrows  = data.getBooleanAttribute( "farrows", true );
        showOpenBorder   = data.getBooleanAttribute( "oborder", true );
        showClosedBorder = data.getBooleanAttribute( "cborder", true );

        readShapeData( data );
        readConfiningContData( data );
        readModifiersData( data );
        readChildrenData( data );
    }

    public void writeData( DataElement data )
    {
        DataElement edata = data.newElement( "element" );

        edata.setAttribute( "name", name );
        if ( fgcolor != null ) edata.setAttribute( "fgcolor", fgcolor );
        if ( bgcolor != null ) edata.setAttribute( "bgcolor", bgcolor );
        if ( showForceArrows != true )
            edata.setAttribute( "farrows", showForceArrows );
        if ( showOpenBorder != true )
            edata.setAttribute( "oborder", showOpenBorder );
        if ( showClosedBorder != true )
            edata.setAttribute( "cborder", showClosedBorder );

        writeShapeData( edata );
        writeConfiningContData( edata );
        writeModifiersData( edata );
        writeChildrenData( edata );
    }
}
