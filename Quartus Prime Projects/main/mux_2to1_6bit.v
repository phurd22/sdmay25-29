module mux_2to1_6bit(
	input wire [5:0] bus_a,
	input wire [5:0] bus_b,
	input wire sel,
	
	output wire [5:0] bus_out

);

assign bus_out = (sel) ? bus_b : bus_a;

endmodule