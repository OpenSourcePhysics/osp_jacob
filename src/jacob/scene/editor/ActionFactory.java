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

import java.util.StringTokenizer;

public final class ActionFactory
{

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    private ActionFactory() {}

//------------------------------------------------------------------------------
//    Create Action Methods
//------------------------------------------------------------------------------

    public static SceneAction createAction( String name, String args[] )
        throws UnknownActionException, MalformedActionException
    {
         if ( name.equals( "CreatePolygonEl" ) )
             return new CreatePolygonElementAction( args );
         else if ( name.equals( "CreateCircleEl" ) )
             return new CreateCircleElementAction( args );
         else if ( name.equals( "CreateRingEl" ) )
             return new CreateRingElementAction( args );
         else if ( name.equals( "CreateParticle" ) )
             return new CreateParticleAction( args );
         else if ( name.equals( "Move" ) )
             return new MoveAction( args );
         else if ( name.equals( "Scale" ) )
             return new ScaleAction( args );
         else if ( name.equals( "Rotate" ) )
             return new RotateAction( args );
         else if ( name.equals( "Delete" ) )
             return new DeleteAction( args );
         else if ( name.equals( "SetAttr" ) )
             return new SetAttributeAction( args );
         else if ( name.equals( "SetMod" ) )
             return new SetModifierAction( args );
         else if ( name.equals( "SetVecMod" ) )
             return new SetVectorModifierAction( args );
         else if ( name.equals( "SetZero" ) )
             return new SetZeroAction( args );
         else
             throw new UnknownActionException( name ); 
    }

    public static SceneAction createAction( String action )
        throws UnknownActionException, MalformedActionException
    {
         String name, args;
         int i, j;

         action = action.trim();

         i = action.indexOf( '(' );
         j = action.indexOf( ')' );

         if ( i > 0 && j > i && action.length() == j + 1 )
         {
             name = action.substring( 0, i );
             args = action.substring( i+1, j );
         }
         else 
         {
             throw new MalformedActionException( "malformed arguments" );
         }
         return createAction( name, getArgsArray( args ) );
    }

//------------------------------------------------------------------------------
//    Misc Methods
//------------------------------------------------------------------------------

    private static String[] getArgsArray( String args )
    {
        if ( args == null )
            return null;
 
        args = args.trim();
 
        if ( args.length() == 0 )
            return null;

        StringTokenizer st = new StringTokenizer( args, "," );

        String argsa[] = new String[st.countTokens()];

        for ( int i = 0; i < argsa.length; i++ )
            argsa[i] = st.nextToken().trim();

        return argsa;
    }
}
