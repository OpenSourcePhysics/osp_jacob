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

package jacob.scene.editor;

import java.awt.event.KeyListener; 
import java.awt.event.MouseListener; 
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent; 
import java.awt.event.MouseEvent; 
import java.awt.Graphics; 
import java.awt.Color; 

import jacob.scene.*;
import jacob.scene.modifiers.*;
import jacob.system.*;
import jacob.property.*;
import jacob.geometry.*;

public class SetVectorModifierAction extends SceneAction
{
    private String compType;

//FIXME
    private Class modType;

    private SceneComponent selectedComp = null;

    private VectorModifier selectedMod = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SetVectorModifierAction( String args[] )
        throws MalformedActionException
    {
        if ( args.length != 2 )
            throw new MalformedActionException( "wrong arg count" );

        compType = args[0];

//FIXME
        Object tmp;

        try
        {
            tmp = ModifierFactory.createModifier( args[1] );
        } 
        catch ( UnknownModifierException ex )
        {
            throw new MalformedActionException( "unknown modifier" );
        }  

        if ( !( tmp instanceof VectorModifier) )
            throw new MalformedActionException( "modifier not a vector modifier" );

        modType = tmp.getClass();
    }

//------------------------------------------------------------------------------
//    Mouse Events 
//------------------------------------------------------------------------------

    public void mouseNormalPressed( SceneMouseEvent e ) 
    {
        if ( selectedComp != null )
        {
            synchronized( getScene().getSceneLock() )
            { 
                if ( !selectedComp.hasModifiers() )
                     selectedComp.createModifiers();

                Modifiers mods = selectedComp.getModifiers();

                ComponentModifier pmod = mods.getModifier( modType );

                if ( pmod != null )
                {
                    mods.removeModifier( pmod );
                }
                else
                {
                    try
                    {
                        selectedMod = (VectorModifier)modType.newInstance();
                        selectedComp = null;
                        mods.addModifier( selectedMod );
                        selectedMod.moveHotspotAbsAt( 0, e.getLocation() );
                    }
                    catch ( Exception ex )
                    {
                        throw new InternalException( "can't create modifier: "+
                                                     ex.toString() );
                    }
                }
            }
            getScene().redraw();
        }
    }

    public void mouseNormalDragged( SceneMouseEvent e )
    {
        if ( selectedMod != null )
        {
            synchronized( getScene().getSceneLock() )
            {
                selectedMod.moveHotspotAbsAt( 0, e.getLocation() ); 
            }
        }
        getScene().redraw();
    }

    public void mouseMoved( SceneMouseEvent e )
    {
//FIXME: compType

        Object sel = getElementOrModifierAt( e.getLocation(), modType );

        if ( sel != null )
        {
            if ( sel instanceof Element )
            {
                selectedComp = (SceneComponent)sel;
                selectedMod = null;
            }
            else if ( sel instanceof ComponentModifier )
            {
                selectedMod = (VectorModifier)sel;
                selectedComp = null;
            }
        }
        else
        {
            selectedMod  = null;
            selectedComp = null;
        }
        getScene().redraw(); 
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        if ( selectedComp != null )
            selectedComp.paintHl( g );
        else if ( selectedMod != null )
            selectedMod.paintHl( g );
    }
}
