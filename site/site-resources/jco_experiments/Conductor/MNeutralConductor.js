function createCurrentElement(condname, x, y, dx, dy)
{
//	x = 100;
//	y = 200;
//	dx = 400;
//	dy = 5;
	dtx = dy;
	dty = dy;
	dt = 2*dy;
	q  = -4;
	Ex = 80;

	p = jacob.createRectangleElement(condname, x, y, dx, dy);
	p.setClosed( false );

	vv = jacob.createRectangleElement("tp" + condname, x - dt, y, dtx, dty);
	vvname = vv.getName();
	mparticle = jacob.createParticle(vvname, q, x - dt + dtx/2, y + dty/2, 0, 0);
	mparticle.setFixed(true);

	vi = jacob.createRectangleElement("vir" + condname, x - dtx/2,  y, dtx, dty);
	viname = vi.getName();
	vi.setClosed( false ); 
	jacob.setModifier(viname, "pargenneg");

	po = jacob.createRectangleElement("ponor" + condname,x + dx - dtx/2, y, dtx, dty);
	poname = po.getName();
	po.setClosed( false );
	jacob.setModifier(poname, "parconneg");

	pp = jacob.createRectangleElement("tn" + condname,x + dx + dtx, y, dtx, dty);
	ppname = pp.getName();
	mparticle = jacob.createParticle(ppname, -q, x + dx + dtx + dtx/2, y + dty/2, 0, 0);
	mparticle.setFixed(true);

	jacob.setProperty("debugName", "false");
	jacob.setProperty("debugChildCount", "false");

	jacob.setVectorModifier(condname, "eforce", -Ex/2, 0.0, false);
	jacob.setVectorModifier(viname,   "eforce", -Ex,   0.0, false);
}



x = 100;
y = 50;
dx = 300;
dy = 10;
dd = dy/2;
ddy = 40;
np = 10;
qp = 0.5;

createCurrentElement("cp1", x, y + 2*dy, dx, dy);
createCurrentElement("cp2", x, y + 3*ddy + 2*dy, dx, dy);

p1 = jacob.createRectangleElement("C1", x, y, dx, dy);
jacob.createParticlesMeshTest("C1", qp, x + dd*2  , y + dd , dx  -  dd*2 , dy, (dx - dy)/np, true, 0, true);

p2 = jacob.createRectangleElement("C2", x, y + ddy , dx, dy);
jacob.createParticlesMeshTest("C2", qp, x + dd*2  , y + dd +   ddy , dx  -  dd*2 , dy, (dx - dy)/np, true, 0, true);

p3 = jacob.createRectangleElement("C3", x, y + 3*ddy , dx, dy);
jacob.createParticlesMeshTest("C3", qp, x + dd*2  , y + dd + 3*ddy , dx  -  dd*2 , dy, (dx - dy)/np, true, 0, true);

p4 = jacob.createRectangleElement("C4", x, y + 4*ddy, dx, dy);
jacob.createParticlesMeshTest("C4", qp, x + dd*2  , y + dd + 4*ddy , dx  -  dd*2 , dy, (dx - dy)/np, true, 0, true);
