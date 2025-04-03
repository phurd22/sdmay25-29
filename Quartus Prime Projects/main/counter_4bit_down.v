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
// CREATED		"Tue Apr  1 16:55:01 2025"

module counter_4bit_down(
	rst_n,
	reset_carriage_n,
	clk,
	lastRead,
	count_n
);


input wire	rst_n;
input wire	reset_carriage_n;
input wire	clk;
input wire	lastRead;
output wire	[3:0] count_n;

wire	[3:0] count;
wire	[3:0] count_n_ALTERA_SYNTHESIZED;
wire	[3:0] preset;






four_bit_counter	b2v_inst1(
	.rst_n(rst_n),
	.clk(clk),
	.spe_n(reset_carriage_n),
	.te(lastRead),
	.P(preset),
	.Q0(count[0]),
	.Q1(count[1]),
	.Q2(count[2]),
	.Q3(count[3])
	);


assign	count_n_ALTERA_SYNTHESIZED[0] =  ~count[0];

assign	count_n_ALTERA_SYNTHESIZED[1] =  ~count[1];

assign	count_n_ALTERA_SYNTHESIZED[2] =  ~count[2];

assign	count_n_ALTERA_SYNTHESIZED[3] =  ~count[3];

assign	count_n = count_n_ALTERA_SYNTHESIZED;
assign	preset[1] = 0;
assign	preset[2] = 0;
assign	preset[3] = 0;
assign	preset[0] = 1;

endmodule
