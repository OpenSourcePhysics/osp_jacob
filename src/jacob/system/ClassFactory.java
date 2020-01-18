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

package jacob.system;

public final class ClassFactory
{
    private ClassFactory() {}

    public static Class getClass( String name, String[][] list ) 
    {
        
        int i = 0;

        try
        {
            for ( i = 0; i < list.length; i++ )
            {
                if ( list[i][0].toLowerCase().equals( name.toLowerCase() ) )
                    return Class.forName( list[i][1] );
                        
            }
        }
        catch ( Exception ex )
        {
            throw new InternalException( "can't get class for name '" +
                                         list[i][1] + "' (" + ex.toString() +
                                         ")" );
        }
        return null;
    }

    public static Object getObject( String name, String[][] list )
    {
        try
        {
            return getClass( name, list ).newInstance();
        }
        catch ( Exception ex )
        {
            throw new InternalException( "can't get object for name '" +
                                         name + "' (" + ex.toString() + ")" );
        }
    }
}
