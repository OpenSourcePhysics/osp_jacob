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

import jacob.system.ClassFactory;

public final class GadgetFactory
{
    public static final String[][] GADGETS =
    {
        { PotentialDetector.TYPE,    "jacob.gadgets.PotentialDetector" },
        { FieldDetectorStrength.TYPE,"jacob.gadgets.FieldDetectorStrength" },
        { EnergyGauge.TYPE,          "jacob.gadgets.EnergyGauge" },
        { CapacitanceGauge.TYPE,     "jacob.gadgets.CapacitanceGauge" },
        { ResistanceDetector.TYPE,   "jacob.gadgets.ResistanceDetector" },
        { ResistanceGauge.TYPE,      "jacob.gadgets.ResistanceGauge" },
        { CurrentGauge.TYPE,         "jacob.gadgets.CurrentGauge" },
        { VoltageDetector.TYPE,      "jacob.gadgets.VoltageDetector" },
        { SurfacePressureGauge.TYPE, "jacob.gadgets.SurfacePressureGauge" },
        { DepxGauge.TYPE,            "jacob.gadgets.DepxGauge" },// Dielectrophoresis gauge
        { VelocityGauge.TYPE,        "jacob.gadgets.VelocityGauge" },
        { VecPotDetector.TYPE,       "jacob.gadgets.VecPotDetector" },
        { FieldPlotter.TYPE,         "jacob.gadgets.FieldPlotter" },
        { GenericGauge.TYPE,         "jacob.gadgets.GenericGauge" },
        { GenericDetector.TYPE,      "jacob.gadgets.GenericDetector" }

    };

    private GadgetFactory() {}

    public static Gadget createGadget( String type )
        throws UnknownGadgetException
    {
         Object gobj = ClassFactory.getObject( type, GADGETS );

         if ( gobj != null )
             return (Gadget) gobj;
         else
             throw new UnknownGadgetException( type );
    }
}
