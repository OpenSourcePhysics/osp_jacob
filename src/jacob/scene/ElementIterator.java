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

public class ElementIterator implements SceneIterator
{

    private SceneIterator iterator;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public ElementIterator( SceneIterator iterator )
    {
        this.iterator = iterator;
        first();
    }

//------------------------------------------------------------------------------
//    Iteration Methods
//------------------------------------------------------------------------------

    public void first()
    {
        iterator.first();
        if ( !(iterator.getCurrentComponent() instanceof Element) )
            nextElement();
    }

    public void next()
    {
        nextElement();
    }

    public boolean isDone()
    {
        return iterator.isDone();
    }

    public SceneComponent getCurrentComponent()
    {
        if ( isDone() )
            return null;
        else
            return iterator.getCurrentComponent();
    }

    public Element getCurrentElement()
    {
        return (Element) getCurrentComponent();
    }

    private void nextElement()
    {
        while( !iterator.isDone() )
        {
            iterator.next();
            if ( iterator.getCurrentComponent() instanceof Element )
                return;
        }
    }
}
