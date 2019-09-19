## Start
This demo project files are copied from block-pio-sifive files at first.
and modified for similar to the ddr4 on-boarding environment.

They have 3 interfaces, 2 port AXI and 1 port APB as I knew.
We don't have ddr4 ctl and phy verilog code, so we are using pwm and pio model 
instead of DDR4 verilog files as below figure.

                                                                             
                                                                             
                                                                             
        APB inf.       AXI inf.     AXI inf.                                     
         ^               ^            ^                                          
         |               |            |                                          
  +----- | ------------- | ---------- | --------+          
  |      |               |            |         |          
  |      |               V            V         |          
  |      |          +----------+ +----------+   |          
  |      |          |  capctl  | |   pio    |   |          
  |      |          |          | |          |   |          
  |      |          +--++++++--+ +----+-----+   |          
  |      |             ||||||         |         |          
  |      V             ||||||         |         |          
  | +------------------++++++--+ +----+-----+   |          
  | |                    cap   | | loopback |   |          
  | | PWM                      | |          |   |          
  | |                          | +----------+   |          
  | |                          |                |            
  | | PTC0 PTC1 ... PTC5       |                |            
  | +--------------------------+                |          
  |                                     demo    |          
  +---------------------------------------------+          
  (demo block : made simuiler to the structure of DDR ctl&phy)
                                                                             


[verilog files]                                                                             
block-demo-sifive/rtl :
  ./rtl/loopback/loopback.sv
  ./rtl/pio/pio.sv
  ./rtl/pwm/PWM.v        : pwm verilog file
  ./rtl/pwm/PTC.v        : ptc verilog file
  ./rtl/capctl/capctl.sv : for controlling cap signal
                                                                             
                                                                             
                                                                             
                                                                             
## Question
1. What about the procedure for on-boarding of demo block 
   (including PWM, capctl, pio, loopback module)

2. Can we on-board seperate module? or do we have to make top verilog module for onboarding?


                                                                             


