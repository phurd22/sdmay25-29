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
// CREATED		"Mon Mar 10 16:48:26 2025"

module counter_4bit_down(
	rst_n,
	en_n,
	count_n
);


input wire	rst_n;
input wire	en_n;
output wire	[3:0] count_n;

wire	[3:0] count;
wire	[3:0] count_n_ALTERA_SYNTHESIZED;
wire	SYNTHESIZED_WIRE_6;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_2;
wire	[3:0] SYNTHESIZED_WIRE_4;
wire	SYNTHESIZED_WIRE_5;

assign	SYNTHESIZED_WIRE_6 = 1;




four_bit_counter	b2v_inst(
	.En(SYNTHESIZED_WIRE_6),
	.CLK(SYNTHESIZED_WIRE_1),
	.CLRN(SYNTHESIZED_WIRE_2),
	.Q(SYNTHESIZED_WIRE_4));

assign	SYNTHESIZED_WIRE_1 = ~(SYNTHESIZED_WIRE_6 & en_n);


addr_plus_one_4bit	b2v_inst11(
	.decade_addr(SYNTHESIZED_WIRE_4),
	.rst_n(SYNTHESIZED_WIRE_5),
	.decade_1_addr(count));


assign	SYNTHESIZED_WIRE_2 = rst_n & SYNTHESIZED_WIRE_5;

assign	count_n_ALTERA_SYNTHESIZED[0] =  ~count[0];

assign	count_n_ALTERA_SYNTHESIZED[1] =  ~count[1];

assign	count_n_ALTERA_SYNTHESIZED[2] =  ~count[2];

assign	count_n_ALTERA_SYNTHESIZED[3] =  ~count[3];

assign	count_n = count_n_ALTERA_SYNTHESIZED;

endmodule
