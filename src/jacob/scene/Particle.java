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

public class Particle extends SceneComponent
{
    private double vx = 0;

    private double vy = 0;

    private double ex = 0;

    private double ey = 0;

    private double mx = 0;

    private double my = 0;

    private double charge;

    private boolean fixed = false;

    private boolean onBorder = false;

    private Particle dipolPartner = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Particle( Scene scene, double charge )
    {
        super( scene, new Particle2d() );
        this.charge = charge;
    }

//------------------------------------------------------------------------------
//    Delete Methods
//------------------------------------------------------------------------------
    public void delete()
    {
        if ( dipolPartner != null )
            dipolPartner.unsetDipolPartner();
        super.delete();
    }

//------------------------------------------------------------------------------
//    Attributes Methods
//------------------------------------------------------------------------------

    public void setVx( double vx )
    {
        this.vx = vx;
    }

    public double getVx()
    {
        return vx;
    }

    public void setVy( double vy )
    {
        this.vy = vy;
    }

    public double getVy()
    {
        return vy;
    }

    public void setEx( double ex )
    {
        this.ex = ex;
    }

    public double getEx()
    {
        return ex;
    }

    public void setEy( double ey )
    {
        this.ey = ey;
    }

    public double getEy()
    {
        return ey;
    }

    public void setMx( double mx )
    {
        this.mx = mx;
    }

    public double getMx()
    {
        return mx;
    }

    public void setMy( double my )
    {
        this.my = my;
    }

    public double getMy()
    {
        return my;
    }

    public void setCharge( double charge )
    {
        this.charge = charge;
    }

    public double getCharge()
    {
        return charge;
    }

    public void setFixed( boolean fixed )
    {
        this.fixed = fixed;
        setVx( 0 );
        setVy( 0 );
    }

    public boolean isFixed()
    {
        return fixed;
    }

    public void setOnBorder( boolean onBorder )
    {
        this.onBorder = onBorder;
    }

    public boolean isOnBorder()
    {
        return onBorder;
    }

    public boolean isDipol()
    {
        if ( dipolPartner == null )
            return false;
        else
            return true;
    }

    public void setDipolPartner( Particle dipolPartner )
    {
        this.dipolPartner = dipolPartner;
    }

    public void unsetDipolPartner()
    {
        dipolPartner = null;
    }

    public Particle getDipolPartner()
    {
        return dipolPartner;
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        if ( isDipol() ) paintDipol( g );
        paintModifiersFg( g );
        if ( getScene().isRunning() &&
             getProperties().showForceArrows.getValue() )
        {
            if ( getParent() instanceof Element )
            {
                if ( ((Element)getParent()).getShowForceArrows() )
                    paintForceArrows( g );
            }
            else
            {
                paintForceArrows( g );
            }
        }

        ((Particle2d)getShape()).paintParticle( g, charge );

        if ( getProperties().debugParOnBorder.getValue() && onBorder )
            ((Particle2d)getShape()).paintParticleHl( g, charge );
    }

    public void paintBg( SceneGraphics g )
    {
        paintModifiersBg( g );
    }

    public void paintHl( SceneGraphics g )
    {
        ((Particle2d)getShape()).paintParticleHl( g, charge );
    }

    protected void paintForceArrows( SceneGraphics g )
    {
        Point2d loc = getLocation();
        double es = getProperties().evectorScale.getValue();
        double ms = getProperties().mvectorScale.getValue();
        double vs = getProperties().vvectorScale.getValue();
        int arrowsize = getProperties().arrowHeadSize.getValue();

        if ( vs > 0 )
        {
            g.setColor( getProperties().vvectorFg.getValue() );

//FIXED VV 10.3.2002
            g.drawArrow( loc.x, loc.y,
                         loc.x + vx * vs,
                         loc.y + vy * vs,
                         arrowsize );
        }
        if ( es > 0 )
        {
            if ( charge > 0 )
                g.setColor( getProperties().evectorPosFg.getValue() );
            else
                g.setColor( getProperties().evectorNegFg.getValue() );

            g.drawArrow( loc.x, loc.y,
                         loc.x + ex * es,
                         loc.y + ey * es,
                         arrowsize );
        }
        if ( ms > 0 )
        {
            g.setColor( getProperties().mvectorFg.getValue() );

            g.drawArrow( loc.x, loc.y,
                         loc.x + mx * ms,
                         loc.y + my * ms,
                         arrowsize );
        }
    }

    protected void paintDipol( SceneGraphics g )
    {
        Point2d l1 = getLocation();
        Point2d l2 = dipolPartner.getLocation();

        double x = Math.min( l1.x, l2.x );
        double y = Math.min( l1.y, l2.y );
        double w = Math.max( l1.x, l2.x ) - x;
        double h = Math.max( l1.y, l2.y ) - y;
//FIXME: make this a variable
        g.setColor( Color.black );
        g.drawOval( x, y, w, h );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        double x = data.getDoubleAttribute( "x" );
        double y = data.getDoubleAttribute( "y" );
        charge = data.getDoubleAttribute( "charge" );
        fixed  = data.getBooleanAttribute( "fixed", false );
        setShape( new Particle2d( x, y ) );
        readModifiersData( data );
    }

    public void writeData( DataElement data )
    {
        DataElement edata = data.newElement( "particle" );
        Point2d loc = getLocation();

        edata.setAttribute( "x", loc.x );
        edata.setAttribute( "y", loc.y );
        edata.setAttribute( "charge", charge );
        if ( fixed == true ) edata.setAttribute( "fixed", fixed );
        writeModifiersData( edata );
    }
}
