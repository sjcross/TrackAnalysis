for (i=0;i<nResults();i++) {
	x = getResult("X",i)+1;
	y = getResult("Y",i)+1;
	z = getResult("Z",i)+1;
	t = getResult("T",i)/5+1;
	c = 1;

	if (t > 100) {
		Stack.setPosition(c,z,t-100);
		setPixel(x,y,65535);
	}	
}
