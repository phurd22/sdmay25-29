module hold_shift_module(KA0,KA1,SHIFT,OUT);

	input KA0, KA1, SHIFT;
	output OUT;
	
	assign OUT = SHIFT ? KA1:KA0;

	
endmodule
