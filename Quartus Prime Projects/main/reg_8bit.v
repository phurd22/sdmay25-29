// Copyright (C) 2024  Intel Corporation. All rights reserved.
// Your use of Intel Corporation's design tools, logic functions 
// and other software and tools, and any partner logic 
// functions, and any output files from any of the foregoing 
// (including device programming or simulation files), and any 
// associated documentation or information are expressly subject 
// to the terms and conditions of the Intel Program License 
// Subscription Agreement, the Intel Quartus Prime License Agreement,
// the Intel FPGA IP License Agreement, or other applicable license
// agreement, including, without limitation, that your use is for
// the sole purpose of programming logic devices manufactured by
// Intel and sold by Intel or its authorized distributors.  Please
// refer to the applicable agreement for further details, at
// https://fpgasoftware.intel.com/eula.

// PROGRAM		"Quartus Prime"
// VERSION		"Version 23.1std.1 Build 993 05/14/2024 SC Standard Edition"
// CREATED		"Sun Mar  2 22:00:39 2025"

module reg_8bit(
	en_n,
	clk,
	data_in,
	data_out
);


input wire	en_n;
input wire	clk;
input wire	[7:0] data_in;
output wire	[7:0] data_out;

reg	[7:0] data_out_ALTERA_SYNTHESIZED;
wire	SYNTHESIZED_WIRE_24;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_4;
wire	SYNTHESIZED_WIRE_7;
wire	SYNTHESIZED_WIRE_10;
wire	SYNTHESIZED_WIRE_13;
wire	SYNTHESIZED_WIRE_16;
wire	SYNTHESIZED_WIRE_19;
wire	SYNTHESIZED_WIRE_22;

assign	SYNTHESIZED_WIRE_24 = 1;




always@(posedge clk or negedge SYNTHESIZED_WIRE_24 or negedge SYNTHESIZED_WIRE_24)
begin
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[0] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[0] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[0] <= SYNTHESIZED_WIRE_1;
	end
end


always@(posedge clk or negedge SYNTHESIZED_WIRE_24 or negedge SYNTHESIZED_WIRE_24)
begin
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[1] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[1] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[1] <= SYNTHESIZED_WIRE_4;
	end
end


always@(posedge clk or negedge SYNTHESIZED_WIRE_24 or negedge SYNTHESIZED_WIRE_24)
begin
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[2] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[2] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[2] <= SYNTHESIZED_WIRE_7;
	end
end


mux_2_to_1	b2v_inst11(
	.S(en_n),
	.W0(data_in[2]),
	.W1(data_out_ALTERA_SYNTHESIZED[2]),
	.F(SYNTHESIZED_WIRE_7));


mux_2_to_1	b2v_inst12(
	.S(en_n),
	.W0(data_in[3]),
	.W1(data_out_ALTERA_SYNTHESIZED[3]),
	.F(SYNTHESIZED_WIRE_16));


always@(posedge clk or negedge SYNTHESIZED_WIRE_24 or negedge SYNTHESIZED_WIRE_24)
begin
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[4] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[4] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[4] <= SYNTHESIZED_WIRE_10;
	end
end


mux_2_to_1	b2v_inst14(
	.S(en_n),
	.W0(data_in[4]),
	.W1(data_out_ALTERA_SYNTHESIZED[4]),
	.F(SYNTHESIZED_WIRE_10));


mux_2_to_1	b2v_inst15(
	.S(en_n),
	.W0(data_in[5]),
	.W1(data_out_ALTERA_SYNTHESIZED[5]),
	.F(SYNTHESIZED_WIRE_19));


always@(posedge clk or negedge SYNTHESIZED_WIRE_24 or negedge SYNTHESIZED_WIRE_24)
begin
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[6] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[6] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[6] <= SYNTHESIZED_WIRE_13;
	end
end


mux_2_to_1	b2v_inst17(
	.S(en_n),
	.W0(data_in[6]),
	.W1(data_out_ALTERA_SYNTHESIZED[6]),
	.F(SYNTHESIZED_WIRE_13));


mux_2_to_1	b2v_inst18(
	.S(en_n),
	.W0(data_in[7]),
	.W1(data_out_ALTERA_SYNTHESIZED[7]),
	.F(SYNTHESIZED_WIRE_22));



always@(posedge clk or negedge SYNTHESIZED_WIRE_24 or negedge SYNTHESIZED_WIRE_24)
begin
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[3] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[3] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[3] <= SYNTHESIZED_WIRE_16;
	end
end


always@(posedge clk or negedge SYNTHESIZED_WIRE_24 or negedge SYNTHESIZED_WIRE_24)
begin
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[5] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[5] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[5] <= SYNTHESIZED_WIRE_19;
	end
end


always@(posedge clk or negedge SYNTHESIZED_WIRE_24 or negedge SYNTHESIZED_WIRE_24)
begin
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[7] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_24)
	begin
	data_out_ALTERA_SYNTHESIZED[7] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[7] <= SYNTHESIZED_WIRE_22;
	end
end


mux_2_to_1	b2v_inst8(
	.S(en_n),
	.W0(data_in[0]),
	.W1(data_out_ALTERA_SYNTHESIZED[0]),
	.F(SYNTHESIZED_WIRE_1));


mux_2_to_1	b2v_inst9(
	.S(en_n),
	.W0(data_in[1]),
	.W1(data_out_ALTERA_SYNTHESIZED[1]),
	.F(SYNTHESIZED_WIRE_4));

assign	data_out = data_out_ALTERA_SYNTHESIZED;

endmodule
