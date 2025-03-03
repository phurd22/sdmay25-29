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
// CREATED		"Mon Mar  3 00:09:09 2025"

module main(
	AddSubSrc,
	ASMSrc,
	Transfer,
	clk,
	rst,
	add_sub_test,
	base_conversion_data,
	base_conversion_sign
);


input wire	AddSubSrc;
input wire	ASMSrc;
input wire	Transfer;
input wire	clk;
input wire	rst;
input wire	[4:0] add_sub_test;
input wire	[4:0] base_conversion_data;
input wire	[4:0] base_conversion_sign;

wire	[4:0] add_sub;
wire	[5:0] addr_1;
wire	[4:0] carry;
wire	[4:0] carry_in;
wire	[4:0] counter;
wire	[5:0] counter_addr;
wire	[4:0] gdfx_temp0;
wire	[4:0] keyboard;
wire	[5:0] keyboard_addr;
wire	[4:0] sum;
wire	write;
wire	SYNTHESIZED_WIRE_11;
wire	[4:0] SYNTHESIZED_WIRE_1;
wire	[4:0] SYNTHESIZED_WIRE_12;
wire	SYNTHESIZED_WIRE_13;
wire	[4:0] SYNTHESIZED_WIRE_10;

assign	SYNTHESIZED_WIRE_13 = 0;




AdderSubtractor	b2v_inst(
	.X(counter[4]),
	.Y(keyboard[4]),
	.Cin(carry_in[4]),
	.AddSub(add_sub[4]),
	.S(sum[4]),
	.Cout(carry[4]));


AdderSubtractor	b2v_inst1(
	.X(counter[3]),
	.Y(keyboard[3]),
	.Cin(carry_in[3]),
	.AddSub(add_sub[3]),
	.S(sum[3]),
	.Cout(carry[3]));



AdderSubtractor	b2v_inst2(
	.X(counter[2]),
	.Y(keyboard[2]),
	.Cin(carry_in[2]),
	.AddSub(add_sub[2]),
	.S(sum[2]),
	.Cout(carry[2]));


timing_module	b2v_inst20(
	.CLK(clk),
	.RST(rst),
	.write(write),
	
	
	
	.counter_addr(counter_addr));


mux_2to1_6bit	b2v_inst23(
	.sel(SYNTHESIZED_WIRE_11),
	.bus_a(counter_addr),
	.bus_b(addr_1),
	.bus_out(keyboard_addr));


mux_2to1_5bit	b2v_inst25(
	.sel(Transfer),
	.bus_a(SYNTHESIZED_WIRE_1),
	.bus_b(SYNTHESIZED_WIRE_12),
	.bus_out(SYNTHESIZED_WIRE_10));


reg_5bit	b2v_inst26(
	.en_n(write),
	.clk(clk),
	.rst(rst),
	.data_in(sum),
	.data_out(SYNTHESIZED_WIRE_12));


reg_5bit	b2v_inst27(
	.en_n(write),
	.clk(clk),
	.rst(rst),
	.data_in(carry),
	.data_out(carry_in));


reg_5bit	b2v_inst28(
	.en_n(write),
	.clk(clk),
	.rst(rst),
	.data_in(gdfx_temp0),
	.data_out(SYNTHESIZED_WIRE_1));


mux_2to1_5bit	b2v_inst29(
	.sel(AddSubSrc),
	.bus_a(base_conversion_sign),
	.bus_b(add_sub_test),
	.bus_out(add_sub));


AdderSubtractor	b2v_inst3(
	.X(counter[1]),
	.Y(keyboard[1]),
	.Cin(carry_in[1]),
	.AddSub(add_sub[1]),
	.S(sum[1]),
	.Cout(carry[1]));


mux_2to1_5bit	b2v_inst30(
	.sel(ASMSrc),
	.bus_a(base_conversion_data),
	.bus_b(gdfx_temp0),
	.bus_out(keyboard));


AdderSubtractor	b2v_inst4(
	.X(counter[0]),
	.Y(keyboard[0]),
	.Cin(carry_in[0]),
	.AddSub(add_sub[0]),
	.S(sum[0]),
	.Cout(carry[0]));


ram	b2v_inst5(
	.clk(clk),
	.ce_n(SYNTHESIZED_WIRE_13),
	.oe_n(write),
	.we_n(SYNTHESIZED_WIRE_11),
	.addr(counter_addr),
	.data(counter)
	);


ram	b2v_inst6(
	.clk(clk),
	.ce_n(SYNTHESIZED_WIRE_13),
	.oe_n(write),
	.we_n(SYNTHESIZED_WIRE_11),
	.addr(keyboard_addr),
	.data(gdfx_temp0)
	);


ram_buff	b2v_inst7(
	.en_n(SYNTHESIZED_WIRE_11),
	.data_in(SYNTHESIZED_WIRE_12),
	.data_out(counter));


ram_buff	b2v_inst8(
	.en_n(SYNTHESIZED_WIRE_11),
	.data_in(SYNTHESIZED_WIRE_10),
	.data_out(gdfx_temp0));


addr_plus_one	b2v_inst90(
	.counter_addr(counter_addr),
	.keyboard_addr(addr_1));

assign	SYNTHESIZED_WIRE_11 =  ~write;


endmodule
