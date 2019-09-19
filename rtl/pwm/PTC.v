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
