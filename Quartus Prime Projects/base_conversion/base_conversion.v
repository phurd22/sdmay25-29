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
// CREATED		"Thu Feb 13 17:22:37 2025"

module base_conversion(
	decimal0_4,
	decimal0_6,
	decimal0_3,
	decimal0_9,
	decimal0_7,
	decimal0_5,
	decimal1_6,
	decimal1_3,
	decimal1_9,
	decimal1_7,
	decimal1_5,
	decimal2_6,
	decimal2_3,
	decimal2_9,
	decimal2_7,
	decimal2_5,
	decimal3_6,
	decimal3_3,
	decimal3_9,
	decimal3_7,
	decimal3_5,
	decimal4_6,
	decimal4_3,
	decimal4_9,
	decimal4_7,
	decimal4_5,
	CLK,
	decimal0_8,
	decimal0_2,
	decimal1_4,
	decimal1_8,
	decimal1_2,
	decimal2_4,
	decimal2_8,
	decimal2_2,
	decimal3_4,
	decimal3_8,
	decimal3_2,
	decimal4_4,
	decimal4_8,
	decimal4_2,
	decode_slt,
	eeprom_addr,
	out0,
	out1,
	out2,
	out3,
	out4
);


input wire	decimal0_4;
input wire	decimal0_6;
input wire	decimal0_3;
input wire	decimal0_9;
input wire	decimal0_7;
input wire	decimal0_5;
input wire	decimal1_6;
input wire	decimal1_3;
input wire	decimal1_9;
input wire	decimal1_7;
input wire	decimal1_5;
input wire	decimal2_6;
input wire	decimal2_3;
input wire	decimal2_9;
input wire	decimal2_7;
input wire	decimal2_5;
input wire	decimal3_6;
input wire	decimal3_3;
input wire	decimal3_9;
input wire	decimal3_7;
input wire	decimal3_5;
input wire	decimal4_6;
input wire	decimal4_3;
input wire	decimal4_9;
input wire	decimal4_7;
input wire	decimal4_5;
input wire	CLK;
input wire	decimal0_8;
input wire	decimal0_2;
input wire	decimal1_4;
input wire	decimal1_8;
input wire	decimal1_2;
input wire	decimal2_4;
input wire	decimal2_8;
input wire	decimal2_2;
input wire	decimal3_4;
input wire	decimal3_8;
input wire	decimal3_2;
input wire	decimal4_4;
input wire	decimal4_8;
input wire	decimal4_2;
input wire	[2:0] decode_slt;
input wire	[15:0] eeprom_addr;
output wire	out0;
output wire	out1;
output wire	out2;
output wire	out3;
output wire	out4;

wire	[7:0] decode_out;
wire	[2:0] mux_slt_0;
wire	[2:0] mux_slt_1;
wire	[2:0] mux_slt_2;
wire	[2:0] mux_slt_3;
wire	[2:0] mux_slt_4;
wire	[7:0] nines;
wire	nines_out;
wire	[7:0] ones;
wire	ones_out;
wire	[7:0] sevens;
wire	sevens_out;
wire	[7:0] threes;
wire	threes_out;
wire	SYNTHESIZED_WIRE_104;
wire	SYNTHESIZED_WIRE_105;
wire	SYNTHESIZED_WIRE_3;
wire	SYNTHESIZED_WIRE_4;
wire	SYNTHESIZED_WIRE_5;
wire	SYNTHESIZED_WIRE_6;
wire	SYNTHESIZED_WIRE_7;
wire	SYNTHESIZED_WIRE_8;
wire	SYNTHESIZED_WIRE_9;
wire	SYNTHESIZED_WIRE_10;
wire	SYNTHESIZED_WIRE_11;
wire	SYNTHESIZED_WIRE_12;
wire	SYNTHESIZED_WIRE_13;
wire	SYNTHESIZED_WIRE_14;
wire	SYNTHESIZED_WIRE_15;
wire	SYNTHESIZED_WIRE_16;
wire	SYNTHESIZED_WIRE_17;
wire	SYNTHESIZED_WIRE_18;
wire	SYNTHESIZED_WIRE_19;
wire	SYNTHESIZED_WIRE_20;
wire	SYNTHESIZED_WIRE_21;
wire	SYNTHESIZED_WIRE_22;
wire	SYNTHESIZED_WIRE_23;
wire	SYNTHESIZED_WIRE_24;
wire	SYNTHESIZED_WIRE_25;
wire	SYNTHESIZED_WIRE_26;
wire	SYNTHESIZED_WIRE_27;
wire	SYNTHESIZED_WIRE_28;
wire	SYNTHESIZED_WIRE_29;
wire	SYNTHESIZED_WIRE_30;
wire	SYNTHESIZED_WIRE_31;
wire	SYNTHESIZED_WIRE_32;
wire	SYNTHESIZED_WIRE_33;
wire	SYNTHESIZED_WIRE_34;
wire	SYNTHESIZED_WIRE_44;
wire	SYNTHESIZED_WIRE_45;
wire	SYNTHESIZED_WIRE_106;
wire	SYNTHESIZED_WIRE_107;
wire	SYNTHESIZED_WIRE_108;
wire	SYNTHESIZED_WIRE_109;
wire	SYNTHESIZED_WIRE_50;
wire	SYNTHESIZED_WIRE_51;
wire	SYNTHESIZED_WIRE_56;
wire	SYNTHESIZED_WIRE_57;
wire	SYNTHESIZED_WIRE_62;
wire	SYNTHESIZED_WIRE_63;
wire	SYNTHESIZED_WIRE_68;
wire	SYNTHESIZED_WIRE_69;
wire	SYNTHESIZED_WIRE_110;
wire	SYNTHESIZED_WIRE_111;
wire	SYNTHESIZED_WIRE_112;
wire	SYNTHESIZED_WIRE_89;
wire	SYNTHESIZED_WIRE_90;
wire	SYNTHESIZED_WIRE_113;
wire	SYNTHESIZED_WIRE_92;
wire	SYNTHESIZED_WIRE_93;
wire	SYNTHESIZED_WIRE_95;
wire	SYNTHESIZED_WIRE_96;
wire	SYNTHESIZED_WIRE_98;
wire	SYNTHESIZED_WIRE_99;
wire	SYNTHESIZED_WIRE_101;
wire	SYNTHESIZED_WIRE_102;

assign	SYNTHESIZED_WIRE_104 = 1;
assign	SYNTHESIZED_WIRE_105 = 0;



assign	SYNTHESIZED_WIRE_7 = ~(decode_out[0] & ones[0]);


decoder_3to8	b2v_inst1(
	.in(decode_slt),
	.out(decode_out));

assign	SYNTHESIZED_WIRE_11 = ~(decode_out[2] & threes[2]);

assign	SYNTHESIZED_WIRE_69 = decimal4_2 | decimal4_8;

assign	SYNTHESIZED_WIRE_90 = decimal0_3 | decimal0_9 | decimal0_6;

assign	SYNTHESIZED_WIRE_89 = decimal0_7 | decimal0_5 | decimal0_9;

assign	SYNTHESIZED_WIRE_93 = decimal1_3 | decimal1_9 | decimal1_6;

assign	SYNTHESIZED_WIRE_92 = decimal1_7 | decimal1_5 | decimal1_9;

assign	SYNTHESIZED_WIRE_96 = decimal2_3 | decimal2_9 | decimal2_6;

assign	SYNTHESIZED_WIRE_95 = decimal2_7 | decimal2_5 | decimal2_9;

assign	SYNTHESIZED_WIRE_99 = decimal3_3 | decimal3_9 | decimal3_6;

assign	SYNTHESIZED_WIRE_98 = decimal3_7 | decimal3_5 | decimal3_9;

assign	SYNTHESIZED_WIRE_102 = decimal4_3 | decimal4_9 | decimal4_6;

assign	SYNTHESIZED_WIRE_15 = ~(decode_out[0] & threes[0]);

assign	SYNTHESIZED_WIRE_101 = decimal4_7 | decimal4_5 | decimal4_9;

assign	SYNTHESIZED_WIRE_14 = ~(decode_out[3] & threes[3]);

assign	SYNTHESIZED_WIRE_13 = ~(decode_out[4] & threes[4]);

assign	SYNTHESIZED_WIRE_17 = ~(decode_out[5] & threes[5]);

assign	SYNTHESIZED_WIRE_16 = ~(decode_out[6] & threes[6]);

assign	SYNTHESIZED_WIRE_18 = ~(decode_out[7] & threes[7]);

assign	SYNTHESIZED_WIRE_23 = ~(decode_out[0] & sevens[0]);

assign	SYNTHESIZED_WIRE_20 = ~(decode_out[1] & sevens[1]);

assign	SYNTHESIZED_WIRE_19 = ~(decode_out[2] & sevens[2]);

assign	SYNTHESIZED_WIRE_4 = ~(decode_out[1] & ones[1]);

assign	SYNTHESIZED_WIRE_22 = ~(decode_out[3] & sevens[3]);

assign	SYNTHESIZED_WIRE_21 = ~(decode_out[4] & sevens[4]);

assign	SYNTHESIZED_WIRE_25 = ~(decode_out[5] & sevens[5]);

assign	SYNTHESIZED_WIRE_24 = ~(decode_out[6] & sevens[6]);

assign	SYNTHESIZED_WIRE_26 = ~(decode_out[7] & sevens[7]);

assign	SYNTHESIZED_WIRE_31 = ~(decode_out[0] & nines[0]);

assign	SYNTHESIZED_WIRE_28 = ~(decode_out[1] & nines[1]);

assign	SYNTHESIZED_WIRE_27 = ~(decode_out[2] & nines[2]);

assign	SYNTHESIZED_WIRE_30 = ~(decode_out[3] & nines[3]);

assign	SYNTHESIZED_WIRE_29 = ~(decode_out[4] & nines[4]);

assign	SYNTHESIZED_WIRE_3 = ~(decode_out[2] & ones[2]);

assign	SYNTHESIZED_WIRE_33 = ~(decode_out[5] & nines[5]);

assign	SYNTHESIZED_WIRE_32 = ~(decode_out[6] & nines[6]);

assign	SYNTHESIZED_WIRE_34 = ~(decode_out[7] & nines[7]);


ones_eeprom	b2v_inst33(
	.clk(CLK),
	.rst_n(SYNTHESIZED_WIRE_104),
	.cs_n(SYNTHESIZED_WIRE_105),
	.oe_n(SYNTHESIZED_WIRE_105),
	.addr(eeprom_addr),
	.data(ones));

assign	ones_out = ~(SYNTHESIZED_WIRE_3 & SYNTHESIZED_WIRE_4 & SYNTHESIZED_WIRE_5 & SYNTHESIZED_WIRE_6 & SYNTHESIZED_WIRE_7 & SYNTHESIZED_WIRE_8 & SYNTHESIZED_WIRE_9 & SYNTHESIZED_WIRE_10);

assign	threes_out = ~(SYNTHESIZED_WIRE_11 & SYNTHESIZED_WIRE_12 & SYNTHESIZED_WIRE_13 & SYNTHESIZED_WIRE_14 & SYNTHESIZED_WIRE_15 & SYNTHESIZED_WIRE_16 & SYNTHESIZED_WIRE_17 & SYNTHESIZED_WIRE_18);

assign	sevens_out = ~(SYNTHESIZED_WIRE_19 & SYNTHESIZED_WIRE_20 & SYNTHESIZED_WIRE_21 & SYNTHESIZED_WIRE_22 & SYNTHESIZED_WIRE_23 & SYNTHESIZED_WIRE_24 & SYNTHESIZED_WIRE_25 & SYNTHESIZED_WIRE_26);

assign	nines_out = ~(SYNTHESIZED_WIRE_27 & SYNTHESIZED_WIRE_28 & SYNTHESIZED_WIRE_29 & SYNTHESIZED_WIRE_30 & SYNTHESIZED_WIRE_31 & SYNTHESIZED_WIRE_32 & SYNTHESIZED_WIRE_33 & SYNTHESIZED_WIRE_34);


threes_eeprom	b2v_inst38(
	.clk(CLK),
	.rst_n(SYNTHESIZED_WIRE_104),
	.cs_n(SYNTHESIZED_WIRE_105),
	.oe_n(SYNTHESIZED_WIRE_105),
	.addr(eeprom_addr),
	.data(threes));


sevens_eeprom	b2v_inst39(
	.clk(CLK),
	.rst_n(SYNTHESIZED_WIRE_104),
	.cs_n(SYNTHESIZED_WIRE_105),
	.oe_n(SYNTHESIZED_WIRE_105),
	.addr(eeprom_addr),
	.data(sevens));

assign	SYNTHESIZED_WIRE_6 = ~(decode_out[3] & ones[3]);


nines_eeprom	b2v_inst40(
	.clk(CLK),
	.rst_n(SYNTHESIZED_WIRE_104),
	.cs_n(SYNTHESIZED_WIRE_105),
	.oe_n(SYNTHESIZED_WIRE_105),
	.addr(eeprom_addr),
	.data(nines));



assign	SYNTHESIZED_WIRE_5 = ~(decode_out[4] & ones[4]);

assign	SYNTHESIZED_WIRE_9 = ~(decode_out[5] & ones[5]);


shift_reg	b2v_inst61(
	.IN(ones_out),
	.CLK(CLK),
	.Q1(SYNTHESIZED_WIRE_109),
	.Q2(SYNTHESIZED_WIRE_108),
	.Q3(SYNTHESIZED_WIRE_107),
	.Q4(SYNTHESIZED_WIRE_106));


\4to1mux 	b2v_inst62(
	.s1(SYNTHESIZED_WIRE_44),
	.s0(SYNTHESIZED_WIRE_45),
	.W3(SYNTHESIZED_WIRE_106),
	.W2(SYNTHESIZED_WIRE_107),
	.W1(SYNTHESIZED_WIRE_108),
	.W0(SYNTHESIZED_WIRE_109),
	.Y(mux_slt_0[0]));


\4to1mux 	b2v_inst63(
	.s1(SYNTHESIZED_WIRE_50),
	.s0(SYNTHESIZED_WIRE_51),
	.W3(SYNTHESIZED_WIRE_106),
	.W2(SYNTHESIZED_WIRE_107),
	.W1(SYNTHESIZED_WIRE_108),
	.W0(SYNTHESIZED_WIRE_109),
	.Y(mux_slt_1[0]));


\4to1mux 	b2v_inst64(
	.s1(SYNTHESIZED_WIRE_56),
	.s0(SYNTHESIZED_WIRE_57),
	.W3(SYNTHESIZED_WIRE_106),
	.W2(SYNTHESIZED_WIRE_107),
	.W1(SYNTHESIZED_WIRE_108),
	.W0(SYNTHESIZED_WIRE_109),
	.Y(mux_slt_2[0]));


\4to1mux 	b2v_inst65(
	.s1(SYNTHESIZED_WIRE_62),
	.s0(SYNTHESIZED_WIRE_63),
	.W3(SYNTHESIZED_WIRE_106),
	.W2(SYNTHESIZED_WIRE_107),
	.W1(SYNTHESIZED_WIRE_108),
	.W0(SYNTHESIZED_WIRE_109),
	.Y(mux_slt_3[0]));


\4to1mux 	b2v_inst66(
	.s1(SYNTHESIZED_WIRE_68),
	.s0(SYNTHESIZED_WIRE_69),
	.W3(SYNTHESIZED_WIRE_106),
	.W2(SYNTHESIZED_WIRE_107),
	.W1(SYNTHESIZED_WIRE_108),
	.W0(SYNTHESIZED_WIRE_109),
	.Y(mux_slt_4[0]));


shift_reg	b2v_inst69(
	.IN(threes_out),
	.CLK(CLK),
	.Q1(SYNTHESIZED_WIRE_110),
	.Q2(SYNTHESIZED_WIRE_111)
	
	);

assign	SYNTHESIZED_WIRE_8 = ~(decode_out[6] & ones[6]);


shift_reg	b2v_inst72(
	.IN(sevens_out),
	.CLK(CLK),
	.Q1(SYNTHESIZED_WIRE_112)
	
	
	);


shift_reg	b2v_inst74(
	.IN(nines_out),
	.CLK(CLK),
	.Q1(SYNTHESIZED_WIRE_113)
	
	
	);


mux_2_to_1	b2v_inst76(
	.S(decimal0_6),
	.W0(SYNTHESIZED_WIRE_110),
	.W1(SYNTHESIZED_WIRE_111),
	.F(mux_slt_0[1]));


mux_2_to_1	b2v_inst77(
	.S(decimal1_6),
	.W0(SYNTHESIZED_WIRE_110),
	.W1(SYNTHESIZED_WIRE_111),
	.F(mux_slt_1[1]));


mux_2_to_1	b2v_inst78(
	.S(decimal2_6),
	.W0(SYNTHESIZED_WIRE_110),
	.W1(SYNTHESIZED_WIRE_111),
	.F(mux_slt_2[1]));


mux_2_to_1	b2v_inst79(
	.S(decimal3_6),
	.W0(SYNTHESIZED_WIRE_110),
	.W1(SYNTHESIZED_WIRE_111),
	.F(mux_slt_3[1]));

assign	SYNTHESIZED_WIRE_10 = ~(decode_out[7] & ones[7]);


mux_2_to_1	b2v_inst80(
	.S(decimal4_6),
	.W0(SYNTHESIZED_WIRE_110),
	.W1(SYNTHESIZED_WIRE_111),
	.F(mux_slt_4[1]));


mux_2_to_1	b2v_inst81(
	.S(decimal0_7),
	.W0(ones_out),
	.W1(SYNTHESIZED_WIRE_112),
	.F(mux_slt_0[2]));


mux_2_to_1	b2v_inst82(
	.S(decimal1_7),
	.W0(ones_out),
	.W1(SYNTHESIZED_WIRE_112),
	.F(mux_slt_1[2]));


mux_2_to_1	b2v_inst83(
	.S(decimal2_7),
	.W0(ones_out),
	.W1(SYNTHESIZED_WIRE_112),
	.F(mux_slt_2[2]));


mux_2_to_1	b2v_inst84(
	.S(decimal3_7),
	.W0(ones_out),
	.W1(SYNTHESIZED_WIRE_112),
	.F(mux_slt_3[2]));


mux_2_to_1	b2v_inst85(
	.S(decimal4_7),
	.W0(ones_out),
	.W1(SYNTHESIZED_WIRE_112),
	.F(mux_slt_4[2]));


\4to1mux 	b2v_inst86(
	.s1(SYNTHESIZED_WIRE_89),
	.s0(SYNTHESIZED_WIRE_90),
	.W3(SYNTHESIZED_WIRE_113),
	.W2(mux_slt_0[2]),
	.W1(mux_slt_0[1]),
	.W0(mux_slt_0[0]),
	.Y(out0));


\4to1mux 	b2v_inst87(
	.s1(SYNTHESIZED_WIRE_92),
	.s0(SYNTHESIZED_WIRE_93),
	.W3(SYNTHESIZED_WIRE_113),
	.W2(mux_slt_1[2]),
	.W1(mux_slt_1[1]),
	.W0(mux_slt_1[0]),
	.Y(out1));


\4to1mux 	b2v_inst88(
	.s1(SYNTHESIZED_WIRE_95),
	.s0(SYNTHESIZED_WIRE_96),
	.W3(SYNTHESIZED_WIRE_113),
	.W2(mux_slt_2[2]),
	.W1(mux_slt_2[1]),
	.W0(mux_slt_2[0]),
	.Y(out2));


\4to1mux 	b2v_inst89(
	.s1(SYNTHESIZED_WIRE_98),
	.s0(SYNTHESIZED_WIRE_99),
	.W3(SYNTHESIZED_WIRE_113),
	.W2(mux_slt_3[2]),
	.W1(mux_slt_3[1]),
	.W0(mux_slt_3[0]),
	.Y(out3));

assign	SYNTHESIZED_WIRE_12 = ~(decode_out[1] & threes[1]);


\4to1mux 	b2v_inst90(
	.s1(SYNTHESIZED_WIRE_101),
	.s0(SYNTHESIZED_WIRE_102),
	.W3(SYNTHESIZED_WIRE_113),
	.W2(mux_slt_4[2]),
	.W1(mux_slt_4[1]),
	.W0(mux_slt_4[0]),
	.Y(out4));

assign	SYNTHESIZED_WIRE_44 = decimal0_8 | decimal0_4;

assign	SYNTHESIZED_WIRE_45 = decimal0_2 | decimal0_8;

assign	SYNTHESIZED_WIRE_50 = decimal1_8 | decimal1_4;

assign	SYNTHESIZED_WIRE_51 = decimal1_2 | decimal1_8;

assign	SYNTHESIZED_WIRE_56 = decimal2_8 | decimal2_4;

assign	SYNTHESIZED_WIRE_57 = decimal2_2 | decimal2_8;

assign	SYNTHESIZED_WIRE_62 = decimal3_8 | decimal3_4;

assign	SYNTHESIZED_WIRE_63 = decimal3_2 | decimal3_8;

assign	SYNTHESIZED_WIRE_68 = decimal4_8 | decimal4_4;


endmodule
