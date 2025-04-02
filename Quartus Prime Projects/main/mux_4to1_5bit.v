module mux_4to1_5bit(
	input wire [3:0] bus_a,
	input wire [3:0] bus_b,
	input wire [3:0] bus_c,
	input wire [3:0] bus_d,
	input wire [1:0] sel,
	
	output wire [3:0] bus_out

);

assign bus_out = (sel[1]) ? ((sel[0]) ? (bus_d) : (bus_c)) : ((sel[0]) ? (bus_b) : (bus_a));

endmodule