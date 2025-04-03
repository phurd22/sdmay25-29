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
// CREATED		"Tue Apr  1 16:08:33 2025"

module reg_4bit(
	clk,
	rst_n,
	data_in,
	data_out
);


input wire	clk;
input wire	rst_n;
input wire	[3:0] data_in;
output wire	[3:0] data_out;

reg	[3:0] data_out_ALTERA_SYNTHESIZED;





always@(posedge clk or negedge rst_n or negedge rst_n)
begin
if (!rst_n)
	begin
	data_out_ALTERA_SYNTHESIZED[0] <= 0;
	end
else
if (!rst_n)
	begin
	data_out_ALTERA_SYNTHESIZED[0] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[0] <= data_in[0];
	end
end


always@(posedge clk or negedge rst_n or negedge rst_n)
begin
if (!rst_n)
	begin
	data_out_ALTERA_SYNTHESIZED[1] <= 0;
	end
else
if (!rst_n)
	begin
	data_out_ALTERA_SYNTHESIZED[1] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[1] <= data_in[1];
	end
end


always@(posedge clk or negedge rst_n or negedge rst_n)
begin
if (!rst_n)
	begin
	data_out_ALTERA_SYNTHESIZED[2] <= 0;
	end
else
if (!rst_n)
	begin
	data_out_ALTERA_SYNTHESIZED[2] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[2] <= data_in[2];
	end
end


always@(posedge clk or negedge rst_n or negedge rst_n)
begin
if (!rst_n)
	begin
	data_out_ALTERA_SYNTHESIZED[3] <= 0;
	end
else
if (!rst_n)
	begin
	data_out_ALTERA_SYNTHESIZED[3] <= 1;
	end
else
	begin
	data_out_ALTERA_SYNTHESIZED[3] <= data_in[3];
	end
end

assign	data_out = data_out_ALTERA_SYNTHESIZED;

endmodule
