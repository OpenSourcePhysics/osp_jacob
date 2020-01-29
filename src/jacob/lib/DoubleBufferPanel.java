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

package jacob.lib;
 
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

public class DoubleBufferPanel extends Panel 
{

    private Image offscreen;

    private boolean dbuff; 

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public DoubleBufferPanel()
    {
        this( true ); 
    }

    public DoubleBufferPanel( boolean dbuff )
    {
        this.dbuff = dbuff;
    }

//------------------------------------------------------------------------------
//    Attributes Methods
//------------------------------------------------------------------------------

    public void setBuffer( boolean dbuff )
    {
        this.dbuff = dbuff;
        // if ( dbuff = false ) WC: is this a syntax error?
        if ( !dbuff ) 
            offscreen = null;
    }

    public boolean isBuffered()
    {
        return dbuff;
    }

//------------------------------------------------------------------------------
//    Component Methods
//------------------------------------------------------------------------------

    public void invalidate() 
    {
        super.invalidate();
        offscreen = null;
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void update( Graphics g ) 
    {
        paint( g );
    }

    public void paint( Graphics g ) 
    {
        if ( dbuff )
        {
            if ( offscreen == null ) 
                offscreen = createImage( getSize().width, getSize().height );
 
            Graphics og = offscreen.getGraphics();
            og.setClip( 0, 0, getSize().width, getSize().height );
            super.paint( og );
            g.drawImage( offscreen, 0, 0, null );
            og.dispose();
        }
        else
        {
            super.paint( g );
        }
    }
}
