module ram_buff(
	input wire en_n,
	input wire [3:0] data_in,
	output wire [3:0] data_out
);

assign data_out = (en_n) ? 8'bz : data_in;

endmodule