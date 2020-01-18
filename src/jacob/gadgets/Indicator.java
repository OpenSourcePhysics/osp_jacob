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

package jacob.gadgets;

import java.awt.Panel;
import java.awt.Button;
import java.awt.Label;
import java.awt.Insets;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jacob.lib.*;

public class Indicator extends Panel 
    implements ActionListener
{

    private IndicatorDisplay display;

    private Label label;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Indicator()
    {
        this( 1 );
    } 

    public Indicator( double scale ) 
    {
        GridBagLayout      gridbag  = new GridBagLayout();
        GridBagConstraints gridbagC = new GridBagConstraints();

        setLayout( gridbag );

        gridbagC.insets     = new Insets( 0, 0, 0, 0 );
        gridbagC.fill       = GridBagConstraints.BOTH;
        gridbagC.gridwidth  = GridBagConstraints.REMAINDER;
        gridbagC.gridheight = 1;

        display = new GraphDisplay(); //FIXME
        display.setScale( scale );
        display.setValue( 0 );

        DoubleBufferPanel dDBuff = new DoubleBufferPanel();
        dDBuff.setLayout( new BorderLayout() );
        dDBuff.add( "Center", display ); 

        gridbag.setConstraints( dDBuff, gridbagC );
        add( dDBuff );

        gridbagC.fill      = GridBagConstraints.NONE;
        gridbagC.gridwidth = 1;

        SimpleButton bp = new SimpleButton( "+" );
        bp.addActionListener( this );
        gridbag.setConstraints( bp, gridbagC );
        add( bp );

        SimpleButton bm = new SimpleButton( "-" );
        bm.addActionListener( this );
        gridbag.setConstraints( bm, gridbagC );
        add( bm );

        gridbagC.fill      = GridBagConstraints.BOTH;
        gridbagC.gridwidth = GridBagConstraints.REMAINDER;

        label = new Label( "", Label.RIGHT );
        gridbag.setConstraints( label, gridbagC );
        add( label );

        setActive( false );
    }

//------------------------------------------------------------------------------
//    Display Methods
//------------------------------------------------------------------------------

    public void setScale( double scale )
    {
        display.setScale( scale );
    }

    public double getScale()
    {
        return display.getScale();
    }

    public void setValue( double value )
    {
        display.setValue( value );
        display.repaint();
    }

    public double getValue()
    {
        return display.getValue();
    }

    public double getScaledValue()
    {
        return display.getValue() * display.getScale();
    }

    public void setLabel( String text )
    {
        label.setText( text );
        label.invalidate();
    }

    public void setActive( boolean active )
    {
        if ( active )
            display.setBackground( Color.lightGray );
        else
            display.setBackground( Color.gray );
        
        display.clear(); 
    }

//------------------------------------------------------------------------------
//    ActionListener Methods
//------------------------------------------------------------------------------

    public void actionPerformed( ActionEvent e )
    {
        String command = e.getActionCommand();

        if ( command.equals( "+" ) )
            setScale( getScale() * 2.0 );
        else if ( command.equals( "-" ) )
            setScale( getScale() / 2.0 );
    }
}
