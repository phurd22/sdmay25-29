module mux_2_to_1(S,W0,W1,F);
	
	input S, W0, W1;
	output F;
	
	assign F = S ? W1:W0;

	
endmodule
