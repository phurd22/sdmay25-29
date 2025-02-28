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
// CREATED		"Wed Feb 12 23:35:18 2025"

module \4to1mux (
	s1,
	s0,
	W0,
	W1,
	W2,
	W3,
	Y
);


input wire	s1;
input wire	s0;
input wire	W0;
input wire	W1;
input wire	W2;
input wire	W3;
output wire	Y;

wire	SYNTHESIZED_WIRE_8;
wire	SYNTHESIZED_WIRE_9;
wire	SYNTHESIZED_WIRE_4;
wire	SYNTHESIZED_WIRE_5;
wire	SYNTHESIZED_WIRE_6;
wire	SYNTHESIZED_WIRE_7;




assign	SYNTHESIZED_WIRE_4 = s1 & s0 & W3;

assign	SYNTHESIZED_WIRE_7 = s1 & SYNTHESIZED_WIRE_8 & W2;

assign	SYNTHESIZED_WIRE_5 = SYNTHESIZED_WIRE_9 & s0 & W1;

assign	SYNTHESIZED_WIRE_6 = SYNTHESIZED_WIRE_9 & SYNTHESIZED_WIRE_8 & W0;

assign	SYNTHESIZED_WIRE_9 =  ~s1;

assign	SYNTHESIZED_WIRE_8 =  ~s0;

assign	Y = SYNTHESIZED_WIRE_4 | SYNTHESIZED_WIRE_5 | SYNTHESIZED_WIRE_6 | SYNTHESIZED_WIRE_7;


endmodule
