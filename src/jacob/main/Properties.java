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

package jacob.main;

import jacob.property.*;


//REMIND: is jacob.main the rigth place for this class ?
public class Properties
{

//---SCENE---------------------------------------------------------------------

    public PropertyBoolean sceneRun =
        new PropertyBoolean( "sceneRun", "Scene" );

    public PropertyBoolean snapToGrid =
        new PropertyBoolean( "snapToGrid", "Scene" );

    public PropertyDouble gridSize =
        new PropertyDouble( "gridSize", "Scene" );

    public PropertyInteger touchOffset =
        new PropertyInteger( "touchOffset", "Scene" );

    public PropertyInteger animationDelay =
        new PropertyInteger( "animationDelay", "Scene" );

    public PropertyDouble sceneScale =
        new PropertyDouble( "sceneScale", "Scene", Property.EXPSPEC );

    public PropertyDouble vvectorScale =
        new PropertyDouble( "vvectorScale", "Scene", Property.EXPSPEC );

    public PropertyDouble evectorScale =
        new PropertyDouble( "evectorScale", "Scene", Property.EXPSPEC );

    public PropertyDouble mvectorScale =
        new PropertyDouble( "mvectorScale", "Scene", Property.EXPSPEC );

    public PropertyDouble eforceScale =
        new PropertyDouble( "eforceScale", "Scene", Property.EXPSPEC );

    public PropertyDouble mfluxdensityScale =
        new PropertyDouble( "mfluxdensityScale", "Scene", Property.EXPSPEC );

    public PropertyInteger tailLength =
        new PropertyInteger( "tailLength", "Scene" );

    public PropertyInteger particleSize =
        new PropertyInteger( "particleSize", "Scene" );

    public PropertyInteger arrowHeadSize =
        new PropertyInteger( "arrowHeadSize", "Scene" );

    public PropertyInteger snapMarkSize =
        new PropertyInteger( "snapMarkSize", "Scene" );

    public PropertyInteger framexSize =
        new PropertyInteger( "frameXSize", "Scene" );

    public PropertyInteger frameySize =
        new PropertyInteger( "frameYSize", "Scene" );

    public PropertyBoolean showForceArrows =
        new PropertyBoolean( "showForceArrows", "Scene" );

    public PropertyBoolean showRotatorPivot =
        new PropertyBoolean( "showRotatorPivot", "Scene" );

    public PropertyBoolean showControls =
        new PropertyBoolean( "showControls", "Scene" );

    public PropertyBoolean showGadgets =
        new PropertyBoolean( "showGadgets", "Scene" );

    public PropertyBoolean showGrid =
        new PropertyBoolean( "showGrid", "Scene" );

//---INTEGRATOR-----------------------------------------------------------------

    public PropertyString integratorType =
        new PropertyString( "integratorType", "Integrator", Property.EXPSPEC );

    public PropertyDouble electricConst =
        new PropertyDouble( "electricConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble magneticConst =
        new PropertyDouble( "magneticConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble integrationStep =
        new PropertyDouble( "integrationStep", "Integrator", Property.EXPSPEC );

    public PropertyDouble frictionConst =
        new PropertyDouble( "frictionConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble elasticityConst =
        new PropertyDouble( "elasticityConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble inertiaConst =
        new PropertyDouble( "inertiaConst ", "Integrator", Property.EXPSPEC );

    public PropertyDouble dipolCloudSize =
        new PropertyDouble( "dipolCloudSize", "Integrator" );

    public PropertyDouble dipolChargeDiv =
        new PropertyDouble( "dipolChargeDiv", "Integrator" );

    public PropertyDouble dipolSuscept =
        new PropertyDouble( "dipolSuscept", "Integrator", Property.EXPSPEC );

    public PropertyInteger oscforcePeriod =
        new PropertyInteger( "oscforcePeriod", "Integrator", Property.EXPSPEC );

    public PropertyDouble oscforceConst =
        new PropertyDouble( "oscforceConst", "Integrator", Property.EXPSPEC );

    public PropertyInteger transforcePeriod =
        new PropertyInteger( "transforcePeriod", "Integrator", Property.EXPSPEC );

    public PropertyDouble transforceConst =
        new PropertyDouble( "transforceConst", "Integrator", Property.EXPSPEC );

    public PropertyInteger circforcePeriod =
        new PropertyInteger( "circforcePeriod", "Integrator", Property.EXPSPEC );

    public PropertyDouble circforceConst =
        new PropertyDouble( "circforceConst", "Integrator", Property.EXPSPEC );

    public PropertyInteger circoscforcePeriod =
        new PropertyInteger( "circoscforcePeriod", "Integrator", Property.EXPSPEC );

    public PropertyDouble circoscforceConst =
        new PropertyDouble( "circoscforceConst", "Integrator", Property.EXPSPEC );

    public PropertyInteger rotatforcePeriod =
        new PropertyInteger( "rotatforcePeriod", "Integrator", Property.EXPSPEC );

    public PropertyDouble rotatforceConst =
        new PropertyDouble( "rotatforceConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble repelerforceConst =
        new PropertyDouble( "repelerforceConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble centralforceConst =
        new PropertyDouble( "centralforceConst", "Integrator", Property.EXPSPEC );

    public PropertyInteger pulseforcePeriod =
        new PropertyInteger( "pulseforcePeriod", "Integrator", Property.EXPSPEC );

    public PropertyInteger pulseforceConst  =
        new PropertyInteger( "pulseforceConst", "Integrator", Property.EXPSPEC );

    public PropertyInteger gausspulseforcePeriod =
        new PropertyInteger( "gausspulseforcePeriod", "Integrator", Property.EXPSPEC );

    public PropertyInteger gausspulseforceConst =
        new PropertyInteger( "gausspulseforceConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble oscchargePeriod =
        new PropertyDouble( "oscchargePeriod", "Integrator", Property.EXPSPEC );

    public PropertyDouble oscchargeConst =
        new PropertyDouble( "oscchargeConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble randomForce =
        new PropertyDouble( "randomForce", "Integrator" , Property.EXPSPEC);

    public PropertyDouble borderSpeedDiv =
        new PropertyDouble( "borderSpeedDiv", "Integrator" );

    public PropertyDouble translationConst =
        new PropertyDouble( "translationConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble rotationConst =
        new PropertyDouble( "rotationConst", "Integrator", Property.EXPSPEC );

	public PropertyDouble collisionConst =
        new PropertyDouble( "collisionConst", "Integrator", Property.EXPSPEC );

    public PropertyDouble inflatorLimitPressure =
        new PropertyDouble( "inflatorLimitPressure", "Integrator" );

    public PropertyDouble inflatorScaleLimit =
        new PropertyDouble( "inflatorScaleLimit", "Integrator" );

    public PropertyDouble inflatorScaleInc =
        new PropertyDouble( "inflatorScaleInc", "Integrator" );

    public PropertyDouble inflatorScaleDec =
        new PropertyDouble( "inflatorScaleDec", "Integrator" );

    public PropertyDouble inflatorIncDecTrsh =
        new PropertyDouble( "inflatorIncDecTrsh", "Integrator" );

    public PropertyDouble zeroX =
        new PropertyDouble( "zeroX", "Integrator",  Property.EXPSPEC );

    public PropertyDouble zeroY =
        new PropertyDouble( "zeroY", "Integrator",  Property.EXPSPEC );

//---SYSTEM---------------------------------------------------------------------

    public PropertyBoolean doubleBuff =
        new PropertyBoolean( "doubleBuff", "System" );

    public PropertyBoolean lockedRepaint =
        new PropertyBoolean( "lockedRepaint", "System" );

    public PropertyInteger minThreadSleepTime =
        new PropertyInteger( "minThreadSleepTime", "System" );

    public PropertyInteger minTimeBetweenRedraw =
        new PropertyInteger( "minTimeBetweenRedraw", "System" );

//---GADGETS--------------------------------------------------------------------

    public PropertyInteger gaugeSleepTime =
        new PropertyInteger( "gaugeSleepTime", "Gadgets" );

    public PropertyDouble potentialDetectorScale =
        new PropertyDouble( "potentialDetectorScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble voltageDetectorScale =
        new PropertyDouble( "voltageDetectorScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble genericDetectorScale =
        new PropertyDouble( "genericDetectorScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble fieldDetectorStrengthScale =
        new PropertyDouble( "fieldDetectorStrengthScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble energyGaugeScale =
        new PropertyDouble( "energyGaugeScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble surfacepressureGaugeScale =
        new PropertyDouble( "surfacepressureGaugeScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble velocityGaugeScale =
        new PropertyDouble( "velocityGaugeScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble depxGaugeScale =
        new PropertyDouble( "depxGaugeScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble currentGaugeScale =
        new PropertyDouble( "currentGaugeScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble resistanceDetectorScale =
        new PropertyDouble( "resistanceDetectorScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble resistanceGaugeScale =
        new PropertyDouble( "resistanceGaugeScale", "Gadgets", Property.EXPSPEC );

    public PropertyDouble capacitanceGaugeScale =
        new PropertyDouble( "capacitanceGaugeScale", "Gadgets", Property.EXPSPEC );

	public PropertyDouble genericGaugeScale =
        new PropertyDouble( "genericGaugeScale", "Gadgets", Property.EXPSPEC );

	public PropertyInteger indicatorResolution =
        new PropertyInteger( "indicatorResolution", "Gadgets", Property.EXPSPEC );


//---COLORS---------------------------------------------------------------------

    public PropertyColor snapMarkFg =
        new PropertyColor( "snapMarkFg", "Colors" );

    public PropertyColor sceneBg =
        new PropertyColor( "sceneBg", "Colors" );

    public PropertyColor shapeFg =
        new PropertyColor( "shapeFg", "Colors" );

    public PropertyColor elementOpenFg =
        new PropertyColor( "elementOpenFg", "Colors" );

    public PropertyColor elementClosedFg =
        new PropertyColor( "elementClosedFg", "Colors" );

    public PropertyColor elementBg =
        new PropertyColor( "elementBg", "Colors" );

    public PropertyColor elementHl =
        new PropertyColor( "elementHl", "Colors" );

    public PropertyColor elementTranBg =
        new PropertyColor( "elementTranBg", "Colors" );

    public PropertyColor particlePosFg =
        new PropertyColor( "particlePosFg", "Colors" );

    public PropertyColor particlePosBg =
        new PropertyColor( "particlePosBg", "Colors" );

    public PropertyColor particleNegFg =
        new PropertyColor( "particleNegFg", "Colors" );

    public PropertyColor particleNegBg =
        new PropertyColor( "particleNegBg", "Colors" );

    public PropertyColor particleHl =
        new PropertyColor( "particleHl", "Colors" );

    public PropertyColor modifierHotspotHl =
        new PropertyColor( "modifierHotspotHl", "Colors" );

    public PropertyColor tailFg =
        new PropertyColor( "tailFg", "Colors" );

    public PropertyColor vvectorFg =
        new PropertyColor( "vvectorFg", "Colors" );

    public PropertyColor evectorPosFg =
        new PropertyColor( "evectorPosFg", "Colors" );

    public PropertyColor evectorNegFg =
        new PropertyColor( "evectorNegFg", "Colors" );

    public PropertyColor mvectorFg =
        new PropertyColor( "mvectorFg", "Colors" );

    public PropertyColor eforceFg =
        new PropertyColor( "eforceFg", "Colors" );

    public PropertyColor mfluxdensityFg =
        new PropertyColor( "mfluxdensityFg", "Colors" );

    public PropertyColor netforceFg =
        new PropertyColor( "netforceFg", "Colors" );

    public PropertyColor circforceFg =
        new PropertyColor( "circforceFg", "Colors" );

    public PropertyColor circoscforceFg =
        new PropertyColor( "circoscforceFg", "Colors" );

    public PropertyColor rotatforceFg =
        new PropertyColor( "rotatforceFg", "Colors" );

    public PropertyColor repelerforceFg =
        new PropertyColor( "repelerforceFg", "Colors" );

    public PropertyColor centralforceFg =
        new PropertyColor( "centralforceFg", "Colors" );

    public PropertyColor oscforceFg =
        new PropertyColor( "oscforceFg", "Colors" );

    public PropertyColor gausspulseforceFg =
        new PropertyColor( "gausspulseforceFg", "Colors" );

    public PropertyColor pulseforceFg =
        new PropertyColor( "pulseforceFg", "Colors" );

    public PropertyColor transforceFg =
        new PropertyColor( "transforceFg", "Colors" );

    public PropertyColor pivotFg =
        new PropertyColor( "pivotFg", "Colors" );

    public PropertyColor zeroFg =
        new PropertyColor( "zeroFg", "Colors" );

    public PropertyColor gridFg =
        new PropertyColor( "gridFg", "Colors" );

    public PropertyColor fieldColor =
        new PropertyColor( "fieldColor", "Colors" );

//---DEBUG----------------------------------------------------------------------

//---DEBUG START---

    public PropertyBoolean debugBounds =
        new PropertyBoolean( "debugBounds", "Debug" );

    public PropertyBoolean debugParent =
        new PropertyBoolean( "debugParent", "Debug" );

    public PropertyBoolean debugNgbr =
        new PropertyBoolean( "debugNgbr", "Debug" );

    public PropertyBoolean debugName =
        new PropertyBoolean( "debugName", "Debug" );

    public PropertyBoolean debugChildCount =
        new PropertyBoolean( "debugChildCount", "Debug" );

    public PropertyBoolean debugParOnBorder =
        new PropertyBoolean( "debugParOnBorder", "Debug" );

//---DEBUG END---

    Properties() {}
};
