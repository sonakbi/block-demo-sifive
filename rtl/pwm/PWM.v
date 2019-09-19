
module PWM (PCLK, PRESETn, PENABLE, PSELPTC, PADDR, PWRITE, PWDATA, PRDATA,
       INTptc0, INTptc1, INTptc2, INTptc3, INTptc4, INTptc5,
       capt0_event, capt1_event, capt2_event, capt3_event, capt4_event, capt5_event,
       PWM_OUT0, PWM_OUT1, PWM_OUT2, PWM_OUT3, PWM_OUT4, PWM_OUT5);
 
  input         PCLK;
  input         PRESETn;
  input         PENABLE;
  input         PSELPTC;
  input   [6:2] PADDR;
  input         PWRITE;
  input  [31:0] PWDATA;
  output [31:0] PRDATA;

  output        INTptc0; // PTC 0 interrupt output
  output        INTptc1; // PTC 1 interrupt output
  output        INTptc2; // PTC 2 interrupt output
  output        INTptc3; // PTC 0 interrupt output
  output        INTptc4; // PTC 1 interrupt output
  output        INTptc5; // PTC 2 interrupt output

  input					capt0_event, capt1_event, capt2_event, capt3_event, capt4_event, capt5_event;	// clock for capture
  output				PWM_OUT0, PWM_OUT1, PWM_OUT2, PWM_OUT3, PWM_OUT4, PWM_OUT5;
 
//------------------------------------------------------------------------------
// Signal declarations
//------------------------------------------------------------------------------
  wire         PTC0Sel;      // PTC0 select (address decoding)
  wire         PTC1Sel;      // PTC1 select (address decoding)
  wire         PTC2Sel;      // PTC2 select (address decoding)
  wire         PTC3Sel;      // PTC0 select (address decoding)
  wire         PTC4Sel;      // PTC1 select (address decoding)
  wire         PTC5Sel;      // PTC2 select (address decoding)

  wire [31:0] PTC0Data;     // PTC0 data out
  wire [31:0] PTC1Data;     // PTC1 data out
  wire [31:0] PTC2Data;     // PTC2 data out
  wire [31:0] PTC3Data;     // PTC0 data out
  wire [31:0] PTC4Data;     // PTC1 data out
  wire [31:0] PTC5Data;     // PTC2 data out

  reg  [31:0] PRDATA;
  wire        PrdataNextEn; // PRDATAEn register input

//------------------------------------------------------------------------------
// Beginning of main code
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//  Address Decoder For PTCn Registers
//------------------------------------------------------------------------------
// Generates select lines to the 2 counters, and internal Test reg select.

	assign PTC0Sel = PSELPTC & (PADDR[6:4] == 3'b000);
	assign PTC1Sel = PSELPTC & (PADDR[6:4] == 3'b001);
	assign PTC2Sel = PSELPTC & (PADDR[6:4] == 3'b010);
	assign PTC3Sel = PSELPTC & (PADDR[6:4] == 3'b011);
	assign PTC4Sel = PSELPTC & (PADDR[6:4] == 3'b100);
	assign PTC5Sel = PSELPTC & (PADDR[6:4] == 3'b101);
 
//------------------------------------------------------------------------------
// Output data generation
//------------------------------------------------------------------------------
// Address decoding for register reads.

  assign PrdataNextEn = PSELPTC & ( (~ PWRITE) ) & ( PENABLE );

// Drive output with internal version.

  always @(PADDR or PTC0Data or PTC1Data or PTC2Data  or PTC3Data  or PTC4Data  or PTC5Data or PrdataNextEn)
  begin
    if ((PrdataNextEn))
      case (PADDR[6:4])	// synopsys parallel_case full_case
        3'b000 			: PRDATA = PTC0Data;	// PTC 0
        3'b001 			: PRDATA = PTC1Data;	// PTC 1
        3'b010 			: PRDATA = PTC2Data;	// PTC 2
        3'b011 			: PRDATA = PTC3Data;	// PTC 3
        3'b100 			: PRDATA = PTC4Data;	// PTC 4
        3'b101 			: PRDATA = PTC5Data;	// PTC 5
        default 		: PRDATA = 32'h0000_0000;
      endcase
    else
      PRDATA = 32'h0000_0000;
  end
 
  PTC  uPTC0 (
    .PCLK    (PCLK),
    .PRESETn (PRESETn),
    .PENABLE (PENABLE),
    .PSELPTC (PTC0Sel),
    .PADDR   (PADDR[3:2]),
    .PWRITE  (PWRITE),
    .PWDATA  (PWDATA),
    .PRDATA  (PTC0Data),
    .INTptc  (INTptc0),
    .capt_event (capt0_event),
    .PWM_OUT (PWM_OUT0)
    );
 
  PTC  uPTC1 (
    .PCLK    (PCLK),
    .PRESETn (PRESETn),
    .PENABLE (PENABLE),
    .PSELPTC (PTC1Sel),
    .PADDR   (PADDR[3:2]),
    .PWRITE  (PWRITE),
    .PWDATA  (PWDATA),
    .PRDATA  (PTC1Data),
    .INTptc  (INTptc1),
    .capt_event (capt1_event),
		.PWM_OUT (PWM_OUT1)
		);

  PTC  uPTC2 (
    .PCLK    (PCLK),
    .PRESETn (PRESETn),
    .PENABLE (PENABLE),
    .PSELPTC (PTC2Sel),
    .PADDR   (PADDR[3:2]),
    .PWRITE  (PWRITE),
    .PWDATA  (PWDATA),
    .PRDATA  (PTC2Data),
    .INTptc  (INTptc2),
    .capt_event (capt2_event),
    .PWM_OUT (PWM_OUT2)
    );

  PTC  uPTC3 (
    .PCLK    (PCLK),
    .PRESETn (PRESETn),
    .PENABLE (PENABLE),
    .PSELPTC (PTC3Sel),
    .PADDR   (PADDR[3:2]),
    .PWRITE  (PWRITE),
    .PWDATA  (PWDATA),
    .PRDATA  (PTC3Data),
    .INTptc  (INTptc3),
    .capt_event (capt3_event),
    .PWM_OUT (PWM_OUT3)
    );

  PTC  uPTC4 (
    .PCLK    (PCLK),
    .PRESETn (PRESETn),
    .PENABLE (PENABLE),
    .PSELPTC (PTC4Sel),
    .PADDR   (PADDR[3:2]),
    .PWRITE  (PWRITE),
    .PWDATA  (PWDATA),
    .PRDATA  (PTC4Data),
    .INTptc  (INTptc4),
    .capt_event (capt4_event),
    .PWM_OUT (PWM_OUT4)
    );

  PTC  uPTC5 (
    .PCLK    (PCLK),
    .PRESETn (PRESETn),
    .PENABLE (PENABLE),
    .PSELPTC (PTC5Sel),
    .PADDR   (PADDR[3:2]),
    .PWRITE  (PWRITE),
    .PWDATA  (PWDATA),
    .PRDATA  (PTC5Data),
    .INTptc  (INTptc5),
    .capt_event (capt5_event),
    .PWM_OUT (PWM_OUT5)
    );

endmodule

// --================================= End ===================================--
