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
// CREATED		"Fri Feb 21 00:21:24 2025"

module test(
	CLK,
	RST,
	addsub,
	keyboard
);


input wire	CLK;
input wire	RST;
input wire	addsub;
input wire	[7:0] keyboard;

reg	[7:0] carry;
reg	[7:0] counter;
wire	[14:0] counter_addr;
wire	SYNTHESIZED_WIRE_66;
wire	SYNTHESIZED_WIRE_67;
wire	SYNTHESIZED_WIRE_2;
wire	SYNTHESIZED_WIRE_6;
wire	SYNTHESIZED_WIRE_10;
wire	SYNTHESIZED_WIRE_14;
wire	SYNTHESIZED_WIRE_18;
wire	SYNTHESIZED_WIRE_22;
wire	SYNTHESIZED_WIRE_26;
wire	SYNTHESIZED_WIRE_68;
wire	SYNTHESIZED_WIRE_31;
wire	SYNTHESIZED_WIRE_35;
wire	SYNTHESIZED_WIRE_39;
wire	SYNTHESIZED_WIRE_43;
wire	SYNTHESIZED_WIRE_47;
wire	SYNTHESIZED_WIRE_51;
wire	SYNTHESIZED_WIRE_55;
wire	SYNTHESIZED_WIRE_59;
wire	SYNTHESIZED_WIRE_63;





timing_module	b2v_inst(
	.CLK(CLK),
	.RST(RST),
	.write(SYNTHESIZED_WIRE_68),
	
	
	
	.counter_addr(counter_addr[7:0])
	);


AdderSubtractor	b2v_inst10(
	.X(counter[2]),
	.Y(keyboard[2]),
	.Cin(carry[2]),
	.AddSub(addsub),
	.S(SYNTHESIZED_WIRE_55),
	.Cout(SYNTHESIZED_WIRE_22));


AdderSubtractor	b2v_inst11(
	.X(counter[1]),
	.Y(keyboard[1]),
	.Cin(carry[1]),
	.AddSub(addsub),
	.S(SYNTHESIZED_WIRE_59),
	.Cout(SYNTHESIZED_WIRE_26));


AdderSubtractor	b2v_inst12(
	.X(counter[0]),
	.Y(keyboard[0]),
	.Cin(carry[0]),
	.AddSub(addsub),
	.S(SYNTHESIZED_WIRE_63),
	.Cout(SYNTHESIZED_WIRE_31));


always@(posedge SYNTHESIZED_WIRE_67 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[7] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[7] <= 1;
	end
else
	begin
	carry[7] <= SYNTHESIZED_WIRE_2;
	end
end


always@(posedge SYNTHESIZED_WIRE_67 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[6] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[6] <= 1;
	end
else
	begin
	carry[6] <= SYNTHESIZED_WIRE_6;
	end
end


always@(posedge SYNTHESIZED_WIRE_67 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[5] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[5] <= 1;
	end
else
	begin
	carry[5] <= SYNTHESIZED_WIRE_10;
	end
end


always@(posedge SYNTHESIZED_WIRE_67 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[4] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[4] <= 1;
	end
else
	begin
	carry[4] <= SYNTHESIZED_WIRE_14;
	end
end


always@(posedge SYNTHESIZED_WIRE_67 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[3] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[3] <= 1;
	end
else
	begin
	carry[3] <= SYNTHESIZED_WIRE_18;
	end
end


always@(posedge SYNTHESIZED_WIRE_67 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[2] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[2] <= 1;
	end
else
	begin
	carry[2] <= SYNTHESIZED_WIRE_22;
	end
end


always@(posedge SYNTHESIZED_WIRE_67 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[1] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[1] <= 1;
	end
else
	begin
	carry[1] <= SYNTHESIZED_WIRE_26;
	end
end


simple_ram	b2v_inst2(
	.clk(CLK),
	.we(SYNTHESIZED_WIRE_68),
	.addr(counter_addr),
	.data_io(counter)
	);


always@(posedge SYNTHESIZED_WIRE_67 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[0] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	carry[0] <= 1;
	end
else
	begin
	carry[0] <= SYNTHESIZED_WIRE_31;
	end
end


always@(posedge SYNTHESIZED_WIRE_68 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[7] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[7] <= 1;
	end
else
	begin
	counter[7] <= SYNTHESIZED_WIRE_35;
	end
end


always@(posedge SYNTHESIZED_WIRE_68 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[6] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[6] <= 1;
	end
else
	begin
	counter[6] <= SYNTHESIZED_WIRE_39;
	end
end


always@(posedge SYNTHESIZED_WIRE_68 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[5] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[5] <= 1;
	end
else
	begin
	counter[5] <= SYNTHESIZED_WIRE_43;
	end
end


always@(posedge SYNTHESIZED_WIRE_68 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[4] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[4] <= 1;
	end
else
	begin
	counter[4] <= SYNTHESIZED_WIRE_47;
	end
end


always@(posedge SYNTHESIZED_WIRE_68 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[3] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[3] <= 1;
	end
else
	begin
	counter[3] <= SYNTHESIZED_WIRE_51;
	end
end


always@(posedge SYNTHESIZED_WIRE_68 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[2] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[2] <= 1;
	end
else
	begin
	counter[2] <= SYNTHESIZED_WIRE_55;
	end
end


always@(posedge SYNTHESIZED_WIRE_68 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[1] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[1] <= 1;
	end
else
	begin
	counter[1] <= SYNTHESIZED_WIRE_59;
	end
end


always@(posedge SYNTHESIZED_WIRE_68 or negedge SYNTHESIZED_WIRE_66 or negedge SYNTHESIZED_WIRE_66)
begin
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[0] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_66)
	begin
	counter[0] <= 1;
	end
else
	begin
	counter[0] <= SYNTHESIZED_WIRE_63;
	end
end

assign	SYNTHESIZED_WIRE_67 =  ~SYNTHESIZED_WIRE_68;

assign	SYNTHESIZED_WIRE_66 =  ~RST;


AdderSubtractor	b2v_inst5(
	.X(counter[5]),
	.Y(keyboard[5]),
	.Cin(carry[5]),
	.AddSub(addsub),
	.S(SYNTHESIZED_WIRE_43),
	.Cout(SYNTHESIZED_WIRE_10));


AdderSubtractor	b2v_inst6(
	.X(counter[4]),
	.Y(keyboard[4]),
	.Cin(carry[4]),
	.AddSub(addsub),
	.S(SYNTHESIZED_WIRE_47),
	.Cout(SYNTHESIZED_WIRE_14));


AdderSubtractor	b2v_inst7(
	.X(counter[7]),
	.Y(keyboard[7]),
	.Cin(carry[7]),
	.AddSub(addsub),
	.S(SYNTHESIZED_WIRE_35),
	.Cout(SYNTHESIZED_WIRE_2));


AdderSubtractor	b2v_inst8(
	.X(counter[6]),
	.Y(keyboard[6]),
	.Cin(carry[6]),
	.AddSub(addsub),
	.S(SYNTHESIZED_WIRE_39),
	.Cout(SYNTHESIZED_WIRE_6));


AdderSubtractor	b2v_inst9(
	.X(counter[3]),
	.Y(keyboard[3]),
	.Cin(carry[3]),
	.AddSub(addsub),
	.S(SYNTHESIZED_WIRE_51),
	.Cout(SYNTHESIZED_WIRE_18));


endmodule
