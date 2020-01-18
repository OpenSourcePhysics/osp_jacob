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

package jacob.property;

public abstract class Property
{
//---- flags -------------------------------------------------------------------

    /* experiment specific property */
    public static final int EXPSPEC = 0x1;

//------------------------------------------------------------------------------

    protected String name;

    protected String group;

    protected int flags;

    protected PropertyMgr mgr = null;


//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Property( String name, String group, int flags )
    {
        this.name  = name;
        this.group = group;
        this.flags = flags;
    }

//------------------------------------------------------------------------------
//    Attributes Methods
//------------------------------------------------------------------------------

    public String getName()
    {
        return name;
    }

    public String getGroup()
    {
        return group;
    }

    public int getFlags()
    {
        return flags;
    }

    public boolean isFlagSet( int flag )
    {
        return ((flags & flag) != 0 );
    }

//------------------------------------------------------------------------------
//    Property Manager Methods
//------------------------------------------------------------------------------

    public void setMgr( PropertyMgr mgr )
    {
        this.mgr = mgr;
    }

    public PropertyMgr getMgr()
    {
        return mgr;
    }

    public void setChanged()
    {
        if ( mgr != null ) 
            mgr.setChanged( name );
    }

    public boolean hasChanged()
    {
        if ( mgr != null )
            return mgr.hasChanged( name );
        else
            return false;
    }

//------------------------------------------------------------------------------
//    Property Value Methods
//------------------------------------------------------------------------------

    public abstract boolean setValue( String value );

    public abstract String toString();
}
