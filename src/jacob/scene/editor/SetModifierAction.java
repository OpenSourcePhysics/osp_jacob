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

public class SetModifierAction extends SceneAction
{
    private String compType;

    private String modType;

    private SceneComponent selectedComp = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SetModifierAction( String args[] )
        throws MalformedActionException
    {
        if ( args.length != 2 )
            throw new MalformedActionException( "wrong arg count" );

        compType = args[0];
        modType  = args[1];
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
                        mods.addModifier( 
                            ModifierFactory.createModifier( modType ) );
                    }
                    catch ( UnknownModifierException ex )
                    {
//FIXME
                    }
                }
            }
            getScene().redraw();
        }
    }

    public void mouseMoved( SceneMouseEvent e )
    {
        selectedComp = getComponentAt( compType, e.getLocation() );
        getScene().redraw(); 
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        if ( selectedComp != null )
            selectedComp.paintHl( g );
    }
}
