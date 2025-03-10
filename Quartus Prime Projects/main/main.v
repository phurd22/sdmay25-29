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
// CREATED		"Mon Mar 10 16:49:46 2025"

module main(
	AddSubSrc,
	Transfer,
	clk,
	rst_n,
	rst_counter_n,
	rst_carry_n,
	rst_keyboard_n,
	Shift,
	mask,
	add_sub_test,
	ASMSrc,
	base_2,
	base_conversion_sign,
	card_slt,
	sum
);


input wire	AddSubSrc;
input wire	Transfer;
input wire	clk;
input wire	rst_n;
input wire	rst_counter_n;
input wire	rst_carry_n;
input wire	rst_keyboard_n;
input wire	Shift;
input wire	mask;
input wire	[4:0] add_sub_test;
input wire	[1:0] ASMSrc;
input wire	[4:0] base_2;
input wire	[4:0] base_conversion_sign;
input wire	[1:0] card_slt;
output wire	[4:0] sum;

wire	[4:0] add_sub;
wire	[5:0] addr_1;
wire	[4:0] base_10;
wire	[19:0] card_data;
wire	[4:0] carry;
wire	[4:0] carry_in;
wire	ce_n;
wire	[4:0] counter;
wire	[5:0] counter_addr;
wire	[3:0] decade_count;
wire	[4:0] keyboard;
wire	[5:0] keyboard_addr;
wire	[4:0] keyboard_ram;
wire	[4:0] keyboard_reg;
wire	[4:0] keyboard_write;
wire	read_n;
wire	[4:0] sum_ALTERA_SYNTHESIZED;
wire	[4:0] sum_reg;
wire	write_n;
wire	[4:0] zero;
wire	zero_n;
wire	SYNTHESIZED_WIRE_0;
wire	SYNTHESIZED_WIRE_23;
wire	SYNTHESIZED_WIRE_24;
wire	SYNTHESIZED_WIRE_16;
wire	SYNTHESIZED_WIRE_25;
wire	SYNTHESIZED_WIRE_18;
wire	SYNTHESIZED_WIRE_20;
wire	SYNTHESIZED_WIRE_22;

assign	SYNTHESIZED_WIRE_23 = 0;
assign	SYNTHESIZED_WIRE_24 = 1;




AdderSubtractor	b2v_inst(
	.X(counter[4]),
	.Y(keyboard[4]),
	.Cin(carry_in[4]),
	.AddSub(add_sub[4]),
	.S(sum_ALTERA_SYNTHESIZED[4]),
	.Cout(carry[4]));


AdderSubtractor	b2v_inst1(
	.X(counter[3]),
	.Y(keyboard[3]),
	.Cin(carry_in[3]),
	.AddSub(add_sub[3]),
	.S(sum_ALTERA_SYNTHESIZED[3]),
	.Cout(carry[3]));

assign	SYNTHESIZED_WIRE_22 = rst_n & rst_keyboard_n;

assign	SYNTHESIZED_WIRE_18 = rst_n & rst_counter_n;

assign	SYNTHESIZED_WIRE_25 = clk & SYNTHESIZED_WIRE_0 & write_n;

assign	SYNTHESIZED_WIRE_16 = Shift & write_n;


mux_4to1_5bit	b2v_inst13(
	.bus_a(zero),
	.bus_b(base_10),
	.bus_c(base_2),
	.bus_d(keyboard_ram),
	.sel(ASMSrc),
	.bus_out(keyboard));



eeprom	b2v_inst15(
	.ce_n(SYNTHESIZED_WIRE_23),
	.oe_n(SYNTHESIZED_WIRE_23),
	.we_n(SYNTHESIZED_WIRE_24),
	.decade(decade_count),
	.digit(card_data[3:0]),
	.time_slot(counter_addr),
	.data(base_10[0]));


eeprom	b2v_inst16(
	.ce_n(SYNTHESIZED_WIRE_23),
	.oe_n(SYNTHESIZED_WIRE_23),
	.we_n(SYNTHESIZED_WIRE_24),
	.decade(decade_count),
	.digit(card_data[7:4]),
	.time_slot(counter_addr),
	.data(base_10[1]));


eeprom	b2v_inst17(
	.ce_n(SYNTHESIZED_WIRE_23),
	.oe_n(SYNTHESIZED_WIRE_23),
	.we_n(SYNTHESIZED_WIRE_24),
	.decade(decade_count),
	.digit(card_data[11:8]),
	.time_slot(counter_addr),
	.data(base_10[2]));


eeprom	b2v_inst18(
	.ce_n(SYNTHESIZED_WIRE_23),
	.oe_n(SYNTHESIZED_WIRE_23),
	.we_n(SYNTHESIZED_WIRE_24),
	.decade(decade_count),
	.digit(card_data[15:12]),
	.time_slot(counter_addr),
	.data(base_10[3]));



AdderSubtractor	b2v_inst2(
	.X(counter[2]),
	.Y(keyboard[2]),
	.Cin(carry_in[2]),
	.AddSub(add_sub[2]),
	.S(sum_ALTERA_SYNTHESIZED[2]),
	.Cout(carry[2]));


timing_module	b2v_inst20(
	.CLK(clk),
	.rst_n(rst_n),
	.write_n(write_n),
	.control_n(SYNTHESIZED_WIRE_0),
	
	
	.zero_n(zero_n),
	.counter_addr(counter_addr));


eeprom	b2v_inst21(
	.ce_n(SYNTHESIZED_WIRE_23),
	.oe_n(SYNTHESIZED_WIRE_23),
	.we_n(SYNTHESIZED_WIRE_24),
	.decade(decade_count),
	.digit(card_data[19:16]),
	.time_slot(counter_addr),
	.data(base_10[4]));



mux_2to1_6bit	b2v_inst23(
	.sel(SYNTHESIZED_WIRE_16),
	.bus_a(counter_addr),
	.bus_b(addr_1),
	.bus_out(keyboard_addr));



mux_2to1_5bit	b2v_inst25(
	.sel(Transfer),
	.bus_a(keyboard_reg),
	.bus_b(sum_ALTERA_SYNTHESIZED),
	.bus_out(keyboard_write));


reg_5bit	b2v_inst26(
	.clk(SYNTHESIZED_WIRE_25),
	.rst_n(SYNTHESIZED_WIRE_18),
	.data_in(sum_ALTERA_SYNTHESIZED),
	.data_out(sum_reg));


reg_5bit	b2v_inst27(
	.clk(SYNTHESIZED_WIRE_25),
	.rst_n(SYNTHESIZED_WIRE_20),
	.data_in(carry),
	.data_out(carry_in));


reg_5bit	b2v_inst28(
	.clk(SYNTHESIZED_WIRE_25),
	.rst_n(SYNTHESIZED_WIRE_22),
	.data_in(keyboard_ram),
	.data_out(keyboard_reg));


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
	.S(sum_ALTERA_SYNTHESIZED[1]),
	.Cout(carry[1]));


counter_4bit_down	b2v_inst30(
	.en_n(zero_n),
	.rst_n(rst_n),
	.count_n(decade_count));


punch_cards	b2v_inst31(
	.mask(mask),
	.card_addr(decade_count),
	.card_slt(card_slt),
	.card_out(card_data));


AdderSubtractor	b2v_inst4(
	.X(counter[0]),
	.Y(keyboard[0]),
	.Cin(carry_in[0]),
	.AddSub(add_sub[0]),
	.S(sum_ALTERA_SYNTHESIZED[0]),
	.Cout(carry[0]));


ram	b2v_inst5(
	.ce_n(ce_n),
	.oe_n(read_n),
	.we_n(write_n),
	.addr(counter_addr),
	.data(counter)
	);


ram	b2v_inst6(
	.ce_n(ce_n),
	.oe_n(read_n),
	.we_n(write_n),
	.addr(keyboard_addr),
	.data(keyboard_ram)
	);


ram_buff	b2v_inst7(
	.en_n(write_n),
	.data_in(sum_reg),
	.data_out(counter));


ram_buff	b2v_inst8(
	.en_n(write_n),
	.data_in(keyboard_write),
	.data_out(keyboard_ram));

assign	SYNTHESIZED_WIRE_20 = rst_n & rst_carry_n;


addr_plus_one	b2v_inst90(
	.counter_addr(counter_addr),
	.keyboard_addr(addr_1));

assign	read_n =  ~write_n;

assign	sum = sum_ALTERA_SYNTHESIZED;
assign	ce_n = 0;
assign	zero[3] = 0;
assign	zero[2] = 0;
assign	zero[1] = 0;
assign	zero[0] = 0;
assign	zero[4] = 0;

endmodule
