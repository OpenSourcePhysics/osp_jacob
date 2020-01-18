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

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Color;
import java.awt.Button;
import java.awt.Label;
import java.awt.Checkbox;
import java.awt.TextField;
import java.awt.ScrollPane;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;

import jacob.system.*;
import jacob.lib.*;

public class PropertyEditor extends Frame
    implements PropertyObserver
{

    private Hashtable propertyNameComponentMap = new Hashtable();

    private Hashtable componentPropertyMap = new Hashtable();

    private Hashtable propertyListMap = new Hashtable();

    private Listener listener;

    private ScrollPane scrollPane;

    private SystemMgr systemMgr;

    private PropertyMgr propertyMgr;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public PropertyEditor( SystemMgr systemMgr )
    {
        super( systemMgr.getPropertyMgr().getLString( "PropertyEditorTitle" ) );

        this.systemMgr   = systemMgr;
        this.propertyMgr = systemMgr.getPropertyMgr();

        listener = new Listener();

        addWindowListener( listener );
            
        initPropertyLists();
        initComponents();

        propertyMgr.attachObserver( this );

        setSize( 500, 500 ); //FIXME

        setVisible( true );
    }

//------------------------------------------------------------------------------
//    Init Components Methods
//------------------------------------------------------------------------------

    private void initPropertyLists()
    {
        /*
         *  create propertyList for each property group
         */

        Enumeration penum = propertyMgr.getPropertiesEnum();

        while( penum.hasMoreElements() )
        {
            String pgroup = ((Property)penum.nextElement()).getGroup();
         
            if ( !propertyListMap.containsKey( pgroup ) )
            {
                propertyListMap.put( pgroup, new PropertyList( pgroup ) );
            }
        }
    }

    private void initComponents()
    {
        GridBagLayout      gridbag  = new GridBagLayout();
        GridBagConstraints gridbagC = new GridBagConstraints();

        setLayout( gridbag );

        gridbagC.insets     = new Insets( 10, 10, 10, 10 ); 
        gridbagC.fill       = GridBagConstraints.BOTH;
        gridbagC.weightx    = 1.0;
        gridbagC.weighty    = 1.0;
        gridbagC.gridwidth  = 2;
        gridbagC.gridheight = propertyListMap.size();

        scrollPane = new ScrollPane();
//        scrollPane.setSize( 400, 350 );

        gridbag.setConstraints( scrollPane, gridbagC );
        add( scrollPane );

        gridbagC.fill       = GridBagConstraints.HORIZONTAL;
        gridbagC.gridwidth  = GridBagConstraints.REMAINDER;            
        gridbagC.gridheight = 1;
        gridbagC.insets     = new Insets( 2, 10, 2, 10 ); 

        /*
         *  create button for each property group
         */

        Enumeration pgroups = propertyListMap.keys();
        String pgroup = null;

        while ( pgroups.hasMoreElements() )
        {
            pgroup = (String) pgroups.nextElement();
//FIXME: localize this
            Button button = new Button( pgroup );

            gridbag.setConstraints( button, gridbagC );
            add( button );
            button.addActionListener( listener );
            button.setActionCommand( "PE_SETLIST_BUTTON" );
        }

        /*
         *  put propertyList into scrollPane ( the last one )
         */

        scrollPane.add( (PropertyList) propertyListMap.get( pgroup ) );

        /*
         *  create done and save buttons
         */

        Button doneButton, saveButton;

        gridbagC.insets    = new Insets( 10, 10, 10, 10 ); 
        gridbagC.fill      = GridBagConstraints.NONE;
        gridbagC.gridwidth = 1;            

        doneButton = new Button( propertyMgr.getLString( "DoneButton" ) );
        doneButton.addActionListener( listener );
        doneButton.setActionCommand( "PE_DONE_BUTTON" );

        gridbag.setConstraints( doneButton, gridbagC );
        add( doneButton );

        if ( !systemMgr.isApplet() )
        {
            saveButton = new Button( propertyMgr.getLString( "SaveButton" ) ); 
            saveButton.addActionListener( listener );
            saveButton.setActionCommand( "PE_SAVE_BUTTON" );

            gridbag.setConstraints( saveButton, gridbagC );
            add( saveButton );
        }
        validate();
    }

//------------------------------------------------------------------------------
//    Update Property Methods 
//------------------------------------------------------------------------------

    public void updateProperty( UpdatePropertyEvent pe )
    {
        Vector cp = pe.getChangedProperties();

        for ( int i = 0; i < cp.size(); i++ )
        {
            String name = (String) cp.elementAt( i );
            updatePropertyComponent( propertyMgr.getProperty( name ) );
        }
    }

    private void updatePropertyComponent( Property p ) 
    {
        Object target = propertyNameComponentMap.get( p.getName() );

        if ( target instanceof TextField )
        {
            ((TextField) target).setText( p.toString() );
        }
        else if ( target instanceof Checkbox )
        {
            Checkbox checkbox = (Checkbox) target;
 
            checkbox.setState( ((PropertyBoolean) p).getValue() );
            checkbox.setLabel( p.toString() );
        }
        else if ( target instanceof Button )
        {
            ((Button) target).setBackground( 
                ((PropertyColor) p).getValue() );
        }
    }

    private void updateTextFieldProperties()
    {
        Enumeration keys = componentPropertyMap.keys();

        while ( keys.hasMoreElements() )
        {
            Object target = keys.nextElement();

            if ( target instanceof TextField )
            {
                Property p   = (Property) componentPropertyMap.get( target );
                TextField tf = (TextField) target;

                if ( ! tf.getText().equals( p.toString() ) )
                {
                    p.setValue( tf.getText() ); 
                    tf.setText( p.toString() );
                }
            }
        }
        propertyMgr.notifyObservers( this );
    }

//------------------------------------------------------------------------------
//    Property List Panel Class
//------------------------------------------------------------------------------

    private class PropertyList extends Panel 
    {
        private final int    TEXT_FIELD_LEN    = 10;
        private final String COLOR_BUTTON_TEXT = "           ";

        public PropertyList( String group )
        {
            GridBagLayout      gridbag  = new GridBagLayout();
            GridBagConstraints gridbagC = new GridBagConstraints();

            setLayout( gridbag );       
 
            gridbagC.fill    = GridBagConstraints.VERTICAL;
            gridbagC.weightx = 1.0;
            gridbagC.anchor  = GridBagConstraints.WEST;

            /*
             *  add properties in this group
             */

            Enumeration penum = propertyMgr.getPropertiesEnum();

            while ( penum.hasMoreElements() )
            {
                Property p = (Property)penum.nextElement();

                if ( p.getGroup().equals( group ) )
                {
                    addPropertyComponent( p, gridbag, gridbagC );
                }
            }
        }
       
        private void addPropertyComponent( Property property, 
                                           GridBagLayout gridbag,
                                           GridBagConstraints gridbagC )
        {
            gridbagC.gridwidth = 1; 
 
            /*
             *  add label with property name
             */
 
            Label l = new Label( property.getName() );
            gridbag.setConstraints( l, gridbagC );
            add( l );

            /*
             *  add component for this property
             */
           
            gridbagC.gridwidth = GridBagConstraints.REMAINDER;

            if ( property instanceof PropertyInteger )
            {
                TextField tf = new TextField( property.toString(), 
                                              TEXT_FIELD_LEN );
          
                gridbag.setConstraints( tf, gridbagC );
                add( tf );        
                tf.addActionListener( listener );
      
                propertyNameComponentMap.put( property.getName(), tf );
                componentPropertyMap.put( tf, property );
            }
            else if ( property instanceof PropertyDouble ) 
            {
                TextField tf = new TextField( property.toString(), 
                                              TEXT_FIELD_LEN );
           
                gridbag.setConstraints( tf, gridbagC );
                add( tf );        
                tf.addActionListener( listener );

                propertyNameComponentMap.put( property.getName(), tf );
                componentPropertyMap.put( tf, property );
            }
            else if ( property instanceof PropertyBoolean )
            {
                Checkbox cb = new Checkbox( property.toString() );
           
                cb.setState( ((PropertyBoolean) property).getValue() ); 

                gridbag.setConstraints( cb, gridbagC );
                add( cb );        
                cb.addItemListener( listener );

                propertyNameComponentMap.put( property.getName(), cb );
                componentPropertyMap.put( cb, property );
            }
            else if ( property instanceof PropertyString )
            {
                TextField tf = new TextField( property.toString(), 
                                              TEXT_FIELD_LEN );
           
                gridbag.setConstraints( tf, gridbagC );
                add( tf );        
                tf.addActionListener( listener );

                propertyNameComponentMap.put( property.getName(), tf );
                componentPropertyMap.put( tf, property );
            }
            else if ( property instanceof PropertyColor )
            {
                Button bu = new Button( COLOR_BUTTON_TEXT );

                bu.setBackground( ((PropertyColor) property).getValue() );

                gridbag.setConstraints( bu, gridbagC );
                add( bu );        
                bu.addActionListener( listener );
                bu.setActionCommand( "PE_CEDIT_BUTTON" );

                propertyNameComponentMap.put( property.getName(), bu );
                componentPropertyMap.put( bu, property );
            }
            else 
            {
                throw new InternalException( "unknown property type: " +
                    property.getName() );
            }
        }
    }

//------------------------------------------------------------------------------
//    Event Listener Class 
//------------------------------------------------------------------------------

    private class Listener extends AWTEventAdapter
    {
        PropertyEditor parent = PropertyEditor.this;

        public Listener() {}

        public void actionPerformed( ActionEvent e )
        {
            Object target = e.getSource();
            String action = e.getActionCommand();
           
            if ( action.equals( "PE_CEDIT_BUTTON" ) ||
                ( target instanceof TextField ) )
            {
                Property p = (Property) componentPropertyMap.get( target );

                if ( p == null ) 
                {
                    throw new InternalException( "can't get target's " +
                        "property: " + target );
                }

                if ( target instanceof TextField )
                {
                    TextField tf = (TextField) target;

                    if ( p.setValue( tf.getText() ) )
                    {
                        propertyMgr.notifyObservers( parent );
                    }
                    else
                    {
                        tf.setText( p.toString() );
                    }
                }
                else if ( target instanceof Button )
                {
                    new ColorEditor( systemMgr, (PropertyColor) p );
                }
            }
            else if ( action.equals( "PE_DONE_BUTTON" ) )
            {
                updateTextFieldProperties();
                parent.setVisible( false );
            }
            else if ( action.equals( "PE_SAVE_BUTTON" ) )
            {    
                updateTextFieldProperties();        
                systemMgr.saveProperties();  
            }
            else if ( action.equals( "PE_SETLIST_BUTTON" ) )
            {
                String group = ((Button) target).getLabel();
                PropertyList list  = 
                    (PropertyList) propertyListMap.get( group );

                if ( list == null )
                {
                    throw new InternalException( "can't get propety list: " +
                        group );
                }
              
                scrollPane.add( list ); 
                scrollPane.validate();
            }
            else
            {
                throw new InternalException( "unknown action: " + action );
            }
        }

        public void itemStateChanged( ItemEvent e )
        {
            Object target = e.getSource();

            Property p = (Property) componentPropertyMap.get( target );

            if ( p == null ) 
            {
                throw new InternalException( "can't get target's property: " +
                    target );
            }

            if ( target instanceof Checkbox ) 
            {
                Checkbox cb = (Checkbox) target;
                PropertyBoolean pb = (PropertyBoolean) p;

                if ( pb.getValue() != cb.getState() )
                {
                    pb.setValue( cb.getState() );
                    cb.setLabel( pb.toString() );
                    propertyMgr.notifyObservers( parent );
                }
            }
            else
            {
                throw new InternalException( "unexpected target: " +
                    target );
            }
        }
        
        public void windowClosing( WindowEvent e )
        {
             parent.setVisible( false );
        } 
    }
}
