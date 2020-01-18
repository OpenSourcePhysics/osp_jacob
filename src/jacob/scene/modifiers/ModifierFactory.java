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

import jacob.system.ClassFactory;

public final class ModifierFactory
{
    private static final String[][] modifiers =
    {
        { Tail.TYPE,              "jacob.scene.modifiers.Tail" },
        { NetForce.TYPE,          "jacob.scene.modifiers.NetForce" },
        { Rotator.TYPE,           "jacob.scene.modifiers.Rotator" },
        { Translator.TYPE,        "jacob.scene.modifiers.Translator" },
        { TranslatorH.TYPE,       "jacob.scene.modifiers.TranslatorH" },
        { TranslatorV.TYPE,       "jacob.scene.modifiers.TranslatorV" },
        { Inflator.TYPE,          "jacob.scene.modifiers.Inflator" },
        { EForce.TYPE,            "jacob.scene.modifiers.EForce" },
        { EForceFix.TYPE,         "jacob.scene.modifiers.EForceFix" },
        { RepelerForce.TYPE,      "jacob.scene.modifiers.RepelerForce" },
        { CentralForce.TYPE,      "jacob.scene.modifiers.CentralForce" },
        { CircularForce.TYPE,     "jacob.scene.modifiers.CircularForce" },
        { CircularOscForce.TYPE,  "jacob.scene.modifiers.CircularOscForce" },
        { RotatForce.TYPE,        "jacob.scene.modifiers.RotatForce" },
        { OscForce.TYPE,          "jacob.scene.modifiers.OscForce" },
        { OscForceFix.TYPE,       "jacob.scene.modifiers.OscForceFix" },
        { TransOrtoForce.TYPE,    "jacob.scene.modifiers.TransOrtoForce" },
        { TransOrtoForceFix.TYPE, "jacob.scene.modifiers.TransOrtoForceFix" },
        { PulseForce.TYPE,        "jacob.scene.modifiers.PulseForce" },
        { PulseForceFix.TYPE,     "jacob.scene.modifiers.PulseForceFix" },
        { GaussPulseForce.TYPE,   "jacob.scene.modifiers.GaussPulseForce" },
        { GaussPulseForceFix.TYPE,"jacob.scene.modifiers.GaussPulseForceFix" },
        { ParGenerator.TYPE,      "jacob.scene.modifiers.ParGenerator" },
        { ParGeneratorNeg.TYPE,   "jacob.scene.modifiers.ParGeneratorNeg" },
        { ParConsumerNeg.TYPE,    "jacob.scene.modifiers.ParConsumerNeg" },
        { ParConsumer.TYPE,       "jacob.scene.modifiers.ParConsumer" },
        { DipoleConsumer.TYPE,    "jacob.scene.modifiers.DipoleConsumer" },
        { OscCharge.TYPE, 		  "jacob.scene.modifiers.OscCharge" },
        { PulseOscCharge.TYPE, 	  "jacob.scene.modifiers.PulseOscCharge" },
        { RandomForce.TYPE,       "jacob.scene.modifiers.RandomForce" },
        { MFluxDensity.TYPE, 	  "jacob.scene.modifiers.MFluxDensity" },
        { FunctionalForce.TYPE,   "jacob.scene.modifiers.FunctionalForce" },
    };

    private ModifierFactory() {}

    public static ComponentModifier createModifier( String type )
        throws UnknownModifierException
    {
         Object mobj = ClassFactory.getObject( type, modifiers );

         if ( mobj != null )
             return (ComponentModifier) mobj;
         else
             throw new UnknownModifierException( type );
    }
}
