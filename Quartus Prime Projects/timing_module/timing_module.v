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
// CREATED		"Fri Feb 14 12:30:59 2025"

module timing_module(
	CLK,
	RST,
	count
);


input wire	CLK;
input wire	RST;
output wire	[7:0] count;

wire	[7:0] count_ALTERA_SYNTHESIZED;
wire	SYNTHESIZED_WIRE_0;
wire	SYNTHESIZED_WIRE_1;





counter	b2v_inst(
	.CLK(CLK),
	.RST(SYNTHESIZED_WIRE_0),
	.count0(count_ALTERA_SYNTHESIZED[0]),
	.count1(count_ALTERA_SYNTHESIZED[1]),
	.count2(count_ALTERA_SYNTHESIZED[2]),
	.count3(count_ALTERA_SYNTHESIZED[3]),
	.count4(count_ALTERA_SYNTHESIZED[4]),
	.count5(count_ALTERA_SYNTHESIZED[5]),
	.count6(count_ALTERA_SYNTHESIZED[6]),
	.count7(count_ALTERA_SYNTHESIZED[7]));

assign	SYNTHESIZED_WIRE_1 = count_ALTERA_SYNTHESIZED[5] & count_ALTERA_SYNTHESIZED[4] & count_ALTERA_SYNTHESIZED[3] & count_ALTERA_SYNTHESIZED[2];

assign	SYNTHESIZED_WIRE_0 = SYNTHESIZED_WIRE_1 | RST;

assign	count = count_ALTERA_SYNTHESIZED;

endmodule
