jacob.readExperiment("_stvarFXF.jco");

/////////////////////////////////////////////////////////////////////////////////////
//Magnetna sila ravna osamljene vodnike toka.        -Dragan Simeonov    30.06.2002//
/////////////////////////////////////////////////////////////////////////////////////


	//E5 levi clen
	//E1 desni clen <+rotator>

	//rotate( String name, double pivotx, double pivoty, double step(Rad), 
	//	int count, int sleep, boolean block )
	//jacob.moveBy(String name , double xstep, double ystep, int count,
	//	int sleep, boolean block );



//delay 4s in zasuk 60° v levo / 3s//
jacob.moveBy("E5", 0, 0, 1, 4000, true);
jacob.rotate("E5", 24.5*20, 18.5*20, Math.PI/3/75, 75, 40, true);//75*40=3000


//delay 4s in zasuk 60°+45° v desno / .24s//
jacob.moveBy("E5", 0, 0, 1, 4000, true);
jacob.rotate("E5", 24.5*20, 18.5*20, -Math.PI*(1/3+1/4)/6, 6, 40, true);//6*40=240


//delay 1s in zasuk 45°+720° v levo / 2s//
jacob.moveBy("E5", 0, 0, 1, 1000, true);
jacob.rotate("E5", 24.5*20, 18.5*20, Math.PI*(4+1/4)/50, 50, 40, true);//50*40=2000