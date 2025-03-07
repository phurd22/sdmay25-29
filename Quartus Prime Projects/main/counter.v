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
// CREATED		"Sun Mar  2 23:12:52 2025"

module counter(
	CLK,
	RST,
	count
);


input wire	CLK;
input wire	RST;
output wire	[5:0] count;

wire	[5:0] count_ALTERA_SYNTHESIZED;
wire	SYNTHESIZED_WIRE_9;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_10;
wire	SYNTHESIZED_WIRE_4;
wire	SYNTHESIZED_WIRE_7;
wire	SYNTHESIZED_WIRE_8;

assign	SYNTHESIZED_WIRE_9 = 1;




four_bit_counter	b2v_inst(
	.En(SYNTHESIZED_WIRE_9),
	.CLK(SYNTHESIZED_WIRE_1),
	.CLRN(SYNTHESIZED_WIRE_10),
	.Q(count_ALTERA_SYNTHESIZED[3:0]));

assign	SYNTHESIZED_WIRE_10 =  ~RST;



two_bit_counter	b2v_inst2(
	.En(SYNTHESIZED_WIRE_9),
	.CLK(SYNTHESIZED_WIRE_4),
	.CLRN(SYNTHESIZED_WIRE_10),
	.Q(count_ALTERA_SYNTHESIZED[5:4]));

assign	SYNTHESIZED_WIRE_7 =  ~CLK;

assign	SYNTHESIZED_WIRE_1 = ~(SYNTHESIZED_WIRE_9 & SYNTHESIZED_WIRE_7);

assign	SYNTHESIZED_WIRE_8 =  ~RST;

assign	SYNTHESIZED_WIRE_4 = ~(count_ALTERA_SYNTHESIZED[3] & SYNTHESIZED_WIRE_8);

assign	count = count_ALTERA_SYNTHESIZED;

endmodule
