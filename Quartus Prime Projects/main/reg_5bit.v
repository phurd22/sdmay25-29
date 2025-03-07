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
// CREATED		"Sun Mar  2 23:41:02 2025"

module reg_5bit(
	en_n,
	clk,
	rst,
	data_in,
	data_out
);


input wire	en_n;
input wire	clk;
input wire	rst;
input wire	[4:0] data_in;
output wire	[4:0] data_out;

reg	[4:0] data_out_ALTERA_SYNTHESIZED;
wire	SYNTHESIZED_WIRE_15;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_4;
wire	SYNTHESIZED_WIRE_7;
wire	SYNTHESIZED_WIRE_10;
wire	SYNTHESIZED_WIRE_13;





always@(posedge clk or negedge SYNTHESIZED_WIRE_15 or negedge SYNTHESIZED_WIRE_15)
begin
if (!SYNTHESIZED_WIRE_15)
	begin
	data_out_ALTERA_SYNTHESIZED[0] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_15)
	begin
	data_out_ALTERA_SYNTHESIZED[0] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[0] <= SYNTHESIZED_WIRE_1;
	end
end


always@(posedge clk or negedge SYNTHESIZED_WIRE_15 or negedge SYNTHESIZED_WIRE_15)
begin
if (!SYNTHESIZED_WIRE_15)
	begin
	data_out_ALTERA_SYNTHESIZED[1] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_15)
	begin
	data_out_ALTERA_SYNTHESIZED[1] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[1] <= SYNTHESIZED_WIRE_4;
	end
end


always@(posedge clk or negedge SYNTHESIZED_WIRE_15 or negedge SYNTHESIZED_WIRE_15)
begin
if (!SYNTHESIZED_WIRE_15)
	begin
	data_out_ALTERA_SYNTHESIZED[2] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_15)
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
	.F(SYNTHESIZED_WIRE_13));


always@(posedge clk or negedge SYNTHESIZED_WIRE_15 or negedge SYNTHESIZED_WIRE_15)
begin
if (!SYNTHESIZED_WIRE_15)
	begin
	data_out_ALTERA_SYNTHESIZED[4] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_15)
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


always@(posedge clk or negedge SYNTHESIZED_WIRE_15 or negedge SYNTHESIZED_WIRE_15)
begin
if (!SYNTHESIZED_WIRE_15)
	begin
	data_out_ALTERA_SYNTHESIZED[3] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_15)
	begin
	data_out_ALTERA_SYNTHESIZED[3] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[3] <= SYNTHESIZED_WIRE_13;
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

assign	SYNTHESIZED_WIRE_15 =  ~rst;

assign	data_out = data_out_ALTERA_SYNTHESIZED;

endmodule
