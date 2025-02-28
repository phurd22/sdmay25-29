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
// CREATED		"Fri Feb 21 00:16:47 2025"

module AdderSubtractor(
	X,
	Y,
	Cin,
	AddSub,
	S,
	Cout
);


input wire	X;
input wire	Y;
input wire	Cin;
input wire	AddSub;
output wire	S;
output wire	Cout;

wire	SYNTHESIZED_WIRE_31;
wire	SYNTHESIZED_WIRE_32;
wire	SYNTHESIZED_WIRE_33;
wire	SYNTHESIZED_WIRE_34;
wire	SYNTHESIZED_WIRE_4;
wire	SYNTHESIZED_WIRE_35;
wire	SYNTHESIZED_WIRE_36;
wire	SYNTHESIZED_WIRE_11;
wire	SYNTHESIZED_WIRE_12;
wire	SYNTHESIZED_WIRE_37;
wire	SYNTHESIZED_WIRE_38;
wire	SYNTHESIZED_WIRE_19;
wire	SYNTHESIZED_WIRE_20;
wire	SYNTHESIZED_WIRE_21;
wire	SYNTHESIZED_WIRE_22;
wire	SYNTHESIZED_WIRE_23;
wire	SYNTHESIZED_WIRE_29;




assign	SYNTHESIZED_WIRE_38 = ~(SYNTHESIZED_WIRE_31 & SYNTHESIZED_WIRE_32);

assign	SYNTHESIZED_WIRE_23 = ~(SYNTHESIZED_WIRE_33 & SYNTHESIZED_WIRE_34);

assign	Cout =  ~SYNTHESIZED_WIRE_4;

assign	SYNTHESIZED_WIRE_29 = ~(SYNTHESIZED_WIRE_31 | SYNTHESIZED_WIRE_32);

assign	SYNTHESIZED_WIRE_19 = ~(SYNTHESIZED_WIRE_35 | SYNTHESIZED_WIRE_36);

assign	SYNTHESIZED_WIRE_22 = ~(SYNTHESIZED_WIRE_35 | SYNTHESIZED_WIRE_33);

assign	SYNTHESIZED_WIRE_4 = ~(SYNTHESIZED_WIRE_11 | SYNTHESIZED_WIRE_12);

assign	SYNTHESIZED_WIRE_36 = ~(SYNTHESIZED_WIRE_37 & SYNTHESIZED_WIRE_38 & SYNTHESIZED_WIRE_35);

assign	SYNTHESIZED_WIRE_33 = ~(SYNTHESIZED_WIRE_34 & SYNTHESIZED_WIRE_38 & SYNTHESIZED_WIRE_35);


mux_2_to_1	b2v_inst17(
	.S(AddSub),
	.W0(SYNTHESIZED_WIRE_19),
	.W1(SYNTHESIZED_WIRE_20),
	.F(SYNTHESIZED_WIRE_12));


mux_2_to_1	b2v_inst18(
	.S(AddSub),
	.W0(SYNTHESIZED_WIRE_21),
	.W1(SYNTHESIZED_WIRE_22),
	.F(SYNTHESIZED_WIRE_11));

assign	S = ~(SYNTHESIZED_WIRE_23 & SYNTHESIZED_WIRE_36);

assign	SYNTHESIZED_WIRE_21 = ~(SYNTHESIZED_WIRE_35 & SYNTHESIZED_WIRE_33);

assign	SYNTHESIZED_WIRE_20 = ~(SYNTHESIZED_WIRE_35 & SYNTHESIZED_WIRE_36);

assign	SYNTHESIZED_WIRE_32 =  ~Y;

assign	SYNTHESIZED_WIRE_31 =  ~Cin;

assign	SYNTHESIZED_WIRE_35 =  ~SYNTHESIZED_WIRE_29;

assign	SYNTHESIZED_WIRE_34 =  ~SYNTHESIZED_WIRE_37;

assign	SYNTHESIZED_WIRE_37 =  ~X;


endmodule
