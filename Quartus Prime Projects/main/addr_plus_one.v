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
// CREATED		"Sun Mar  2 22:36:56 2025"

module addr_plus_one(
	counter_addr,
	keyboard_addr
);


input wire	[5:0] counter_addr;
output wire	[5:0] keyboard_addr;

wire	[5:0] keyboard_addr_ALTERA_SYNTHESIZED;
wire	SYNTHESIZED_WIRE_12;
wire	SYNTHESIZED_WIRE_6;
wire	SYNTHESIZED_WIRE_10;

assign	SYNTHESIZED_WIRE_12 = 0;
assign	SYNTHESIZED_WIRE_10 = 1;




adder_4bit	b2v_inst13(
	.X3(SYNTHESIZED_WIRE_12),
	.Y3(SYNTHESIZED_WIRE_12),
	.X2(SYNTHESIZED_WIRE_12),
	.Y2(SYNTHESIZED_WIRE_12),
	.X1(counter_addr[5]),
	.Y1(SYNTHESIZED_WIRE_12),
	.X0(counter_addr[4]),
	.Y0(SYNTHESIZED_WIRE_12),
	.Ci(SYNTHESIZED_WIRE_6),
	.S0(keyboard_addr_ALTERA_SYNTHESIZED[4]),
	.S1(keyboard_addr_ALTERA_SYNTHESIZED[5])
	
	
	);




adder_4bit	b2v_inst5(
	.X3(counter_addr[3]),
	.Y3(SYNTHESIZED_WIRE_12),
	.X2(counter_addr[2]),
	.Y2(SYNTHESIZED_WIRE_12),
	.X1(counter_addr[1]),
	.Y1(SYNTHESIZED_WIRE_12),
	.X0(counter_addr[0]),
	.Y0(SYNTHESIZED_WIRE_10),
	.Ci(SYNTHESIZED_WIRE_12),
	.S0(keyboard_addr_ALTERA_SYNTHESIZED[0]),
	.S1(keyboard_addr_ALTERA_SYNTHESIZED[1]),
	.S2(keyboard_addr_ALTERA_SYNTHESIZED[2]),
	.S3(keyboard_addr_ALTERA_SYNTHESIZED[3]),
	.Co(SYNTHESIZED_WIRE_6));

assign	keyboard_addr = keyboard_addr_ALTERA_SYNTHESIZED;

endmodule
