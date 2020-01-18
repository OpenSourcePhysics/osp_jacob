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
import jacob.system.*;
import jacob.geometry.*;
import jacob.data.*;

public class Rotator extends VectorModifier
{

    public static final String TYPE = "rotator";

    private double torque = 0;


//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Rotator() {}

//------------------------------------------------------------------------------
//    Type Methods
//------------------------------------------------------------------------------

    public String getType()
    {
        return TYPE;
    }

//------------------------------------------------------------------------------
//    Status Methods
//------------------------------------------------------------------------------

    public void added()
    {
        torque = 0;
    }

//------------------------------------------------------------------------------
//    Component Modification Methods
//------------------------------------------------------------------------------
//by Dragan Simeonov 04.10.02
//Integrating torque rather than:
//integrating force and then multiplying by displacement of element from pivot.
    public void preIntegrationModify( long time )
    {
        Point2d pivot = getHotspotAbsAt( 0 );
        double rconst = getProperties().rotationConst.getValue();
        double n;
        //we can normalize the torque to the object size for easier use on differently sized objects
        if (this.getComponent().getBounds().height > this.getComponent().getBounds().width)
        	n = this.getComponent().getBounds().height;
        else
        	n = this.getComponent().getBounds().width;

        double fi = torque * rconst * 20 / n * getComponent().getScene().getProperties().integrationStep.getValue();

        getComponent().rotate( pivot, fi );

        /* we have to fix pivot point because of numerical errors */
        moveHotspotAbsAt( 0, pivot );

        torque = 0;
    }

//by Dragan Simeonov 04.10.02
//Integrating torque rather than:
//integrating force and then multiplying by displacement of element from pivot.
    public void postIntegrationModify( long time )
    {
        ParticleIterator iterator;

        iterator = new ParticleIterator( getComponent().createIterator() );
        double econst = getProperties().evectorScale.getValue();
        double mconst = getProperties().mvectorScale.getValue();

        Point2d pivot = getHotspotAbsAt( 0 );

        while( !iterator.isDone() )
        {
//REMIND: this could be done in Integrator (for speed reasons)
            Particle p = iterator.getCurrentParticle();
//FIXED VV 12.3.2002
//            ex += p.getEx();
//            ey += p.getEy();

         if ( econst > 0 )
         {
			 torque += p.getEx() * (p.getLocation().y - pivot.y);
             torque -= p.getEy() * (p.getLocation().x - pivot.x);
		 }
		 if ( mconst > 0 )
		 {
             torque += p.getMx() * (p.getLocation().y - pivot.y);
             torque -= p.getMy() * (p.getLocation().x - pivot.x);
	     }
//	     else
//	     {
//			 ex += p.getEx() + p.getMx();
//           ey += p.getEy() + p.getMy();
//		 }
            iterator.next();
        }
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
//FIXME: we should always draw the pivot when editing
        if ( !getProperties().showRotatorPivot.getValue() )
            return;

        Point2d loc  = getComponent().getLocation();
        Point2d pivot = getHotspotAbsAt( 0 );

        g.setColor( getProperties().pivotFg.getValue() );
        g.drawLine( loc.x, loc.y, pivot.x, pivot.y );
        g.drawLine( pivot.x - 4, pivot.y - 4, pivot.x + 4, pivot.y + 4 );
        g.drawLine( pivot.x - 4, pivot.y + 4, pivot.x + 4, pivot.y - 4 );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        Point2d pivot = new Point2d();

        pivot.x = data.getDoubleAttribute( "x" );
        pivot.y = data.getDoubleAttribute( "y" );

        moveHotspotAbsAt( 0, pivot );
    }

    public void writeData( DataElement data )
    {
        DataElement rdata = data.newElement( TYPE );

        Point2d pivot = getHotspotAbsAt( 0 );

        rdata.setAttribute( "x", pivot.x );
        rdata.setAttribute( "y", pivot.y );
    }
}
