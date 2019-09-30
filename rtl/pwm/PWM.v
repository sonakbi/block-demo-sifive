
module PWM (PCLK, PRESETn, PENABLE, PSELPTC, PADDR, PWRITE, PWDATA, PRDATA,
       INTptc0, INTptc1, INTptc2, INTptc3, INTptc4, INTptc5,
       capt0_event, capt1_event, capt2_event, capt3_event, capt4_event, capt5_event,
       PWM_OUT0, PWM_OUT1, PWM_OUT2, PWM_OUT3, PWM_OUT4, PWM_OUT5);
 
  input         PCLK;
  input         PRESETn;
  input         PENABLE;
  input         PSELPTC;
  input   [6:0] PADDR;
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
module PTC (
	// APB common
	PCLK, PRESETn,
	// APB
	PENABLE, PSELPTC, PADDR, PWRITE, PWDATA, PRDATA, INTptc,
	// External PTC Interface
	capt_event, PWM_OUT
);

// APB common
input		PCLK;
input		PRESETn;

// APB
input		PENABLE;
input		PSELPTC;
input		[3:2] PADDR;
input		PWRITE;
input		[31:0] PWDATA;
output	[31:0] PRDATA;
output	INTptc;

// External PTC Interface
input			capt_event;				// Capture input
output		PWM_OUT;				// PWM output

//
// Internal wires & regs
//

reg  [31:0]	PTC_COUNT;		// PTC_COUNT register
reg  [31:0]	PTC_LOAD;		// PTC_LOAD register
reg  [31:0]	PTC_PERIOD;		// PTC_PERIOD register
reg  [15:0]	PTC_CON;		// PTC_CON register
wire            load_match;		// PTC_LOAD matches RPTC_CNTR
wire            period_match;		// PTC_PERIOD matches RPTC_CNTR
wire            restart;		// Restart counter when asserted
wire            stop;			// Stop counter when asserted
wire  [31:0]	PRDATA;	// Data out
reg             PWM_OUT;		// PWM output
reg             ovf;			// timer overflow
wire            PTC_EN, PTC_SINGLE;
wire            PTC_INTE, PTC_OVF, PTC_RST, PTC_CAPTE;
wire            PTC_COUNT_EN, PTC_PWMOE, PTC_NEG;
wire            set_int;
reg             capt_event_l;

always	@(posedge PCLK)	begin
	if (!PRESETn)		capt_event_l <= 1'b0;
	else capt_event_l <= capt_event;
end
wire	pos_capt_valid = capt_event & ~capt_event_l;
wire	neg_capt_valid = ~capt_event & capt_event_l;

// -------------------------------------------------------------------
// PTC Counter Register (PTC_COUNT)
// -------------------------------------------------------------------
wire	PTC_COUNT_wr = (PADDR[3:2] == 2'b00) & PSELPTC & PWRITE & PENABLE;
wire	PTC_COUNT_rd = (PADDR[3:2] == 2'b00) & PSELPTC & ~PWRITE & PENABLE;

always @( negedge (PRESETn) or posedge (PCLK) )
begin
	if ((!PRESETn)) PTC_COUNT <= 32'h0000_0000;
	else if (PTC_COUNT_wr) PTC_COUNT <= PWDATA;
	else if (restart) PTC_COUNT <= 32'h0000_0000;
	//else if (!stop && PTC_EN) 				PTC_COUNT <= PTC_COUNT + 1'b1;
	else if (!stop && PTC_EN & ~PTC_COUNT_EN) PTC_COUNT <= PTC_COUNT + 1'b1;
	else if (PTC_COUNT_EN & PTC_NEG & neg_capt_valid) PTC_COUNT <= PTC_COUNT + 1'b1; 
	else if (PTC_COUNT_EN & ~PTC_NEG & pos_capt_valid) PTC_COUNT <= PTC_COUNT + 1'b1; 
end


// -------------------------------------------------------------------
// PTC Reference/Captuere Load Register (PTC_LOAD)
// -------------------------------------------------------------------
wire	PTC_LOAD_wr = (PADDR[3:2] == 2'b01) & PSELPTC & PWRITE & PENABLE;
wire	PTC_LOAD_rd = (PADDR[3:2] == 2'b01) & PSELPTC & ~PWRITE & PENABLE;

always @( negedge (PRESETn) or posedge (PCLK) )
begin
	if ((!PRESETn))  PTC_LOAD <= 32'h0000_0000;
	else if (PTC_LOAD_wr) PTC_LOAD <= PWDATA;
	//else if (PTC_CAPTE) PTC_LOAD <= PTC_COUNT;
	else if (PTC_CAPTE & PTC_NEG & neg_capt_valid) PTC_LOAD <= PTC_COUNT;
	else if (PTC_CAPTE & ~PTC_NEG & pos_capt_valid) PTC_LOAD <= PTC_COUNT;
end


// -------------------------------------------------------------------
// PTC Period Register (PTC_PERIOD)
// -------------------------------------------------------------------
wire	PTC_PERIOD_wr = (PADDR[3:2] == 2'b10) & PSELPTC & PWRITE & PENABLE;
wire	PTC_PERIOD_rd = (PADDR[3:2] == 2'b10) & PSELPTC & ~PWRITE & PENABLE;

always @( posedge (PCLK) )
begin
	if ((!PRESETn)) PTC_PERIOD <= 32'h0000_0000;
	else if (PTC_PERIOD_wr) PTC_PERIOD <= PWDATA;
end

// -------------------------------------------------------------------
// PTC Control Register (PTC_CON)
// -------------------------------------------------------------------
wire	PTC_CON_wr = (PADDR[3:2] == 2'b11) & PSELPTC & PWRITE & PENABLE;
wire	PTC_CON_rd = (PADDR[3:2] == 2'b11) & PSELPTC & ~PWRITE & PENABLE;

always @( posedge (PCLK) )
begin
	if ((!PRESETn)) PTC_CON 		<= 16'h0000;
	else if (PTC_CON_wr) PTC_CON 		<= PWDATA[15:0];
	//else if (ovf)  PTC_CON[3] 	<= 1'b1;
	else if (set_int) PTC_CON[3] 	<= 1'b1;	// one-shot pulse
end

assign PTC_EN 			= PTC_CON[0];
assign PTC_SINGLE		= PTC_CON[1];
assign PTC_INTE			= PTC_CON[2];
assign PTC_OVF			= PTC_CON[3];
assign PTC_RST			= PTC_CON[4];
assign PTC_CAPTE		= PTC_CON[5];
assign PTC_COUNT_EN	= PTC_CON[6];
assign PTC_PWMOE		= PTC_CON[7];
assign PTC_NEG			= PTC_CON[8];


// -------------------------------------------------------------------
// Output Data Driver
// -------------------------------------------------------------------
assign PRDATA = (PTC_COUNT_rd) 	? {PTC_COUNT} :
			 (PTC_LOAD_rd)		? {PTC_LOAD} :
			 (PTC_PERIOD_rd)	? {PTC_PERIOD} :
			 (PTC_CON_rd) 		? {16'h0000, PTC_CON} : 32'h0000_0000;

// A match when RPTC_LRC is equal to RPTC_CNTR
assign load_match = PTC_EN & (PTC_COUNT == PTC_LOAD);

// A match when RPTC_LRC is equal to RPTC_CNTR
assign period_match = PTC_EN & (PTC_COUNT == PTC_PERIOD);

// Restart counter when load_match asserted and PTC_SINGLE cleared
// or when PTC_RST is set
assign restart = (load_match & ~PTC_SINGLE) | PTC_RST;

// Stop counter when load_match and RPTC_SINGLE both asserted
assign stop = load_match & PTC_SINGLE;


// PWM output
always @( posedge (PCLK) )
begin
	if (!PRESETn) PWM_OUT <= 1'b0;
	else if (period_match & PTC_PWMOE) PWM_OUT <= 1'b1;
	else if (load_match & PTC_PWMOE) PWM_OUT <= 1'b0;
	else if (~PTC_PWMOE) PWM_OUT <= 1'b0;
end

// Register interrupt request
always @( posedge (PCLK) )
begin
	if ((!PRESETn)) ovf <= 1'b0;
	else if (load_match) ovf <= 1'b1;
	else ovf <= 1'b0;
end
assign	set_int = load_match & ~ovf;

wire	INTptc = PTC_OVF & PTC_INTE;

endmodule
